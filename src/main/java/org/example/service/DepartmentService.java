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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 部门服务类
 * dep_tree规则: root(company_id)_predecessors(id)_children(id)
 * - root节点和pre节点冲突时只展示root
 * - 例如: C001_null_2,3 表示公司C001下的根部门，无父部门，子部门为2和3
 */
@Service
public class DepartmentService {

    private static final Logger log = LoggerFactory.getLogger(DepartmentService.class);

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    /**
     * 创建部门
     * @param request 创建部门请求
     * @return 创建的部门响应
     */
    @Transactional
    public DepartmentResponse createDepartment(CreateDepartmentRequest request) {
        log.info("创建部门: name={}, companyId={}, parentId={}",
                request.getDepartmentName(), request.getCompanyId(), request.getParentId());

        // 1. 创建部门实体
        SysDepartment department = new SysDepartment();
        department.setDepartmentName(request.getDepartmentName());
        department.setCompanyId(request.getCompanyId());

        // 2. 计算部门层级
        int level = 1;
        if (request.getParentId() != null) {
            SysDepartment parent = departmentRepository.findById(request.getParentId());
            if (parent != null) {
                level = parent.getLevel() + 1;
            }
        }
        department.setLevel(level);

        // 3. 先插入部门获取ID
        department.setDepTree(""); // 临时占位
        departmentRepository.save(department);
        Long newDeptId = department.getDepartmentId();

        // 4. 构建dep_tree
        String depTree = buildDepTree(request.getCompanyId(), request.getParentId());
        department.setDepTree(depTree);
        departmentRepository.updateDepTree(newDeptId, depTree);

        // 5. 如果有父部门，更新父部门的children
        if (request.getParentId() != null) {
            updateParentChildren(request.getParentId(), newDeptId);
        }

        log.info("部门创建成功: departmentId={}, depTree={}", newDeptId, depTree);

        return convertToResponse(department);
    }

    /**
     * 构建dep_tree字符串
     * 规则: root(company_id)_predecessors(id)_children(id)
     */
    private String buildDepTree(String companyId, Long parentId) {
        StringBuilder depTree = new StringBuilder();

        // 1. root节点 (company_id)
        depTree.append(companyId);
        depTree.append("_");

        // 2. predecessors (父部门ID链)
        if (parentId == null) {
            // 根部门，无父部门
            depTree.append("null");
        } else {
            // 获取父部门的predecessor链
            SysDepartment parent = departmentRepository.findById(parentId);
            if (parent != null) {
                String parentPredecessors = getPredecessorsFromDepTree(parent.getDepTree());
                if ("null".equals(parentPredecessors)) {
                    // 父部门是根部门
                    depTree.append(parentId);
                } else {
                    // 父部门有自己的父部门，拼接链
                    depTree.append(parentPredecessors).append(",").append(parentId);
                }
            } else {
                depTree.append(parentId);
            }
        }

        // 3. children (子部门ID，新建时为null)
        depTree.append("_");
        depTree.append("null");

        return depTree.toString();
    }

    /**
     * 从dep_tree中提取predecessors部分
     */
    private String getPredecessorsFromDepTree(String depTree) {
        if (depTree == null || depTree.isEmpty()) {
            return "null";
        }
        String[] parts = depTree.split("_");
        if (parts.length >= 2) {
            return parts[1];
        }
        return "null";
    }

    /**
     * 从dep_tree中提取children部分
     */
    private String getChildrenFromDepTree(String depTree) {
        if (depTree == null || depTree.isEmpty()) {
            return "null";
        }
        String[] parts = depTree.split("_");
        if (parts.length >= 3) {
            return parts[2];
        }
        return "null";
    }

    /**
     * 更新父部门的children字段，添加新的子部门ID
     */
    private void updateParentChildren(Long parentId, Long childId) {
        SysDepartment parent = departmentRepository.findById(parentId);
        if (parent == null) {
            return;
        }

        String depTree = parent.getDepTree();
        String[] parts = depTree.split("_");

        String root = parts.length > 0 ? parts[0] : parent.getCompanyId();
        String predecessors = parts.length > 1 ? parts[1] : "null";
        String children = parts.length > 2 ? parts[2] : "null";

        // 更新children
        if ("null".equals(children)) {
            children = String.valueOf(childId);
        } else {
            children = children + "," + childId;
        }

        String newDepTree = root + "_" + predecessors + "_" + children;
        departmentRepository.updateDepTree(parentId, newDepTree);

        log.info("更新父部门children: parentId={}, newDepTree={}", parentId, newDepTree);
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
    public List<DepartmentResponse> getDepartmentsByCompanyId(String companyId) {
        List<SysDepartment> departments = departmentRepository.findByCompanyId(companyId);
        return departments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
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
        String children = getChildrenFromDepTree(department.getDepTree());
        if (!"null".equals(children) && !children.isEmpty()) {
            log.warn("部门有子部门，无法删除: departmentId={}, children={}", departmentId, children);
            return false;
        }

        // 从父部门的children中移除
        String predecessors = getPredecessorsFromDepTree(department.getDepTree());
        if (!"null".equals(predecessors)) {
            String[] predIds = predecessors.split(",");
            if (predIds.length > 0) {
                Long directParentId = Long.parseLong(predIds[predIds.length - 1].trim());
                removeChildFromParent(directParentId, departmentId);
            }
        }

        // 删除部门
        int result = departmentRepository.deleteById(departmentId);
        log.info("部门删除结果: departmentId={}, result={}", departmentId, result > 0);
        return result > 0;
    }

    /**
     * 从父部门的children中移除指定子部门
     */
    private void removeChildFromParent(Long parentId, Long childId) {
        SysDepartment parent = departmentRepository.findById(parentId);
        if (parent == null) {
            return;
        }

        String depTree = parent.getDepTree();
        String[] parts = depTree.split("_");

        String root = parts.length > 0 ? parts[0] : parent.getCompanyId();
        String predecessors = parts.length > 1 ? parts[1] : "null";
        String children = parts.length > 2 ? parts[2] : "null";

        if ("null".equals(children)) {
            return;
        }

        // 移除指定的childId
        List<String> childList = new ArrayList<>(Arrays.asList(children.split(",")));
        childList.removeIf(c -> c.trim().equals(String.valueOf(childId)));

        String newChildren = childList.isEmpty() ? "null" : String.join(",", childList);
        String newDepTree = root + "_" + predecessors + "_" + newChildren;

        departmentRepository.updateDepTree(parentId, newDepTree);
        log.info("从父部门移除子部门: parentId={}, childId={}, newDepTree={}", parentId, childId, newDepTree);
    }

    /**
     * 转换为响应DTO
     */
    private DepartmentResponse convertToResponse(SysDepartment department) {
        DepartmentResponse response = new DepartmentResponse();
        response.setDepartmentId(department.getDepartmentId());
        response.setDepartmentName(department.getDepartmentName());
        response.setLevel(department.getLevel());
        response.setDepTree(department.getDepTree());
        response.setCompanyId(department.getCompanyId());
        response.setCreatedAt(department.getCreatedAt());
        response.setUpdatedAt(department.getUpdatedAt());

        // 解析dep_tree
        if (department.getDepTree() != null && !department.getDepTree().isEmpty()) {
            String[] parts = department.getDepTree().split("_");

            // 解析父部门ID列表
            if (parts.length > 1 && !"null".equals(parts[1])) {
                response.setParentIds(
                        Arrays.stream(parts[1].split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .map(Long::parseLong)
                                .collect(Collectors.toList())
                );
            } else {
                response.setParentIds(new ArrayList<>());
            }

            // 解析子部门ID列表
            if (parts.length > 2 && !"null".equals(parts[2])) {
                response.setChildrenIds(
                        Arrays.stream(parts[2].split(","))
                                .map(String::trim)
                                .filter(s -> !s.isEmpty())
                                .map(Long::parseLong)
                                .collect(Collectors.toList())
                );
            } else {
                response.setChildrenIds(new ArrayList<>());
            }
        }

        return response;
    }
}

