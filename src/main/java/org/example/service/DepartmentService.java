package org.example.service;

import org.example.dto.CreateDepartmentRequest;
import org.example.dto.DepartmentResponse;
import org.example.entity.SysDepartment;
import org.example.repository.DepartmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 部门服务类（邻接表设计）
 * 使用 parent_id 字段表达层级关系
 */
@Service
public class DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    /**
     * 创建部门（邻接表设计，只需设置parent_id）
     */
    @Transactional
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        log.info("创建部门: name={}, companyId={}, parentId={}",
                request.getDepartmentName(), request.getCompanyId(), request.getParentId());

        // 1. 验证父部门是否存在
        int level = 1;
        if (request.getParentId() != null) {
            SysDepartment parent = departmentRepository.findById(request.getParentId());
            if (parent == null) {
                throw new RuntimeException("父部门不存在: " + request.getParentId());
            }
            // 验证父部门属于同一公司
            if (!Objects.equals(parent.getCompanyId(), request.getCompanyId())) {
                throw new RuntimeException("父部门不属于该公司");
            }
            level = parent.getLevel() + 1;
        }

        // 2. 创建部门实体
        SysDepartment department = new SysDepartment();
        department.setDepartmentName(request.getDepartmentName());
        department.setCompanyId(request.getCompanyId());
        department.setParentId(request.getParentId());
        department.setLevel(level);

        // 3. 保存
        departmentRepository.save(department);
        log.info("部门创建成功: departmentId={}", department.getDepartmentId());

        return convertToResponse(department);
    }

    /**
     * 根据ID获取部门
     */
    public DepartmentResponse getDepartmentById(Long departmentId) {
        SysDepartment department = departmentRepository.findById(departmentId);
        if (department == null) {
            return null;
        }
        return convertToResponse(department);
    }

    /**
     * 根据公司ID获取所有部门
     */
    public List<DepartmentResponse> getDepartmentsByCompanyId(Long companyId) {
        List<SysDepartment> departments = departmentRepository.findByCompanyId(companyId);
        return departments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 获取部门树（根据公司ID）
     */
    public List<DepartmentResponse> getDepartmentTree(Long companyId) {
        List<SysDepartment> allDepartments = departmentRepository.findByCompanyId(companyId);
        return buildTree(allDepartments, null);
    }

    /**
     * 获取所有部门
     */
    public List<DepartmentResponse> getAllDepartments() {
        List<SysDepartment> departments = departmentRepository.findAll();
        return departments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 获取子部门列表
     */
    public List<DepartmentResponse> getChildDepartments(Long parentId) {
        List<SysDepartment> children = departmentRepository.findByParentId(parentId);
        return children.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 获取父部门
     */
    public DepartmentResponse getParentDepartment(Long departmentId) {
        SysDepartment department = departmentRepository.findById(departmentId);
        if (department == null || department.getParentId() == null) {
            return null;
        }
        SysDepartment parent = departmentRepository.findById(department.getParentId());
        return parent != null ? convertToResponse(parent) : null;
    }

    /**
     * 获取兄弟部门
     */
    public List<DepartmentResponse> getSiblingDepartments(Long departmentId) {
        SysDepartment department = departmentRepository.findById(departmentId);
        if (department == null) {
            return new ArrayList<>();
        }
        List<SysDepartment> siblings = departmentRepository.findByParentId(department.getParentId());
        return siblings.stream()
                .filter(d -> !Objects.equals(d.getDepartmentId(), departmentId))
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * 移动部门到新的父部门下
     */
    @Transactional
    public boolean moveDepartment(Long departmentId, Long newParentId) {
        log.info("移动部门: departmentId={}, newParentId={}", departmentId, newParentId);

        SysDepartment department = departmentRepository.findById(departmentId);
        if (department == null) {
            throw new RuntimeException("部门不存在: " + departmentId);
        }

        // 验证新父部门
        int newLevel = 1;
        if (newParentId != null) {
            SysDepartment newParent = departmentRepository.findById(newParentId);
            if (newParent == null) {
                throw new RuntimeException("新父部门不存在: " + newParentId);
            }
            // 不能移动到自己的子孙节点下
            if (isDescendant(departmentId, newParentId)) {
                throw new RuntimeException("不能将部门移动到其子孙部门下");
            }
            newLevel = newParent.getLevel() + 1;
        }

        // 更新parent_id和level
        department.setParentId(newParentId);
        department.setLevel(newLevel);
        departmentRepository.update(department);

        // 递归更新所有子孙部门的level
        updateDescendantsLevel(departmentId, newLevel);

        log.info("部门移动成功: departmentId={}, newParentId={}", departmentId, newParentId);
        return true;
    }

    /**
     * 检查targetId是否是sourceId的子孙
     */
    private boolean isDescendant(Long sourceId, Long targetId) {
        SysDepartment target = departmentRepository.findById(targetId);
        while (target != null && target.getParentId() != null) {
            if (Objects.equals(target.getParentId(), sourceId)) {
                return true;
            }
            target = departmentRepository.findById(target.getParentId());
        }
        return false;
    }

    /**
     * 递归更新子孙部门的level
     */
    private void updateDescendantsLevel(Long parentId, int parentLevel) {
        List<SysDepartment> children = departmentRepository.findByParentId(parentId);
        for (SysDepartment child : children) {
            child.setLevel(parentLevel + 1);
            departmentRepository.update(child);
            updateDescendantsLevel(child.getDepartmentId(), child.getLevel());
        }
    }

    /**
     * 删除部门
     */
    @Transactional
    public boolean deleteDepartment(Long departmentId) {
        log.info("删除部门: departmentId={}", departmentId);

        SysDepartment department = departmentRepository.findById(departmentId);
        if (department == null) {
            log.warn("部门不存在: departmentId={}", departmentId);
            return false;
        }

        // 检查是否有子部门
        List<SysDepartment> children = departmentRepository.findByParentId(departmentId);
        if (!children.isEmpty()) {
            throw new RuntimeException("部门有子部门，无法删除");
        }

        // 删除部门
        int result = departmentRepository.deleteById(departmentId);
        log.info("部门删除结果: departmentId={}, result={}", departmentId, result > 0);
        return result > 0;
    }

    /**
     * 构建树形结构
     */
    private List<DepartmentResponse> buildTree(List<SysDepartment> departments, Long parentId) {
        return departments.stream()
                .filter(d -> Objects.equals(d.getParentId(), parentId))
                .map(d -> {
                    DepartmentResponse response = convertToResponse(d);
                    response.setChildren(buildTree(departments, d.getDepartmentId()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    /**
     * 转换为响应DTO
     */
    private DepartmentResponse convertToResponse(SysDepartment department) {
        DepartmentResponse response = new DepartmentResponse();
        response.setDepartmentId(department.getDepartmentId());
        response.setDepartmentName(department.getDepartmentName());
        response.setLevel(department.getLevel());
        response.setParentId(department.getParentId());
        response.setCompanyId(department.getCompanyId());
        response.setCreatedAt(department.getCreatedAt());
        response.setUpdatedAt(department.getUpdatedAt());

        return response;
    }
}

