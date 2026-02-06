package org.example.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.entity.SysPost;
import java.util.List;
/**
 * 岗位 Mapper 接口
 */
@Mapper
public interface PostMapper extends BaseMapper<SysPost> {
    /**
     * 根据部门ID查询岗位列表
     */
    @Select("SELECT * FROM sys_post WHERE department_id = #{departmentId}")
    List<SysPost> selectByDepartmentId(@Param("departmentId") Long departmentId);
    /**
     * 根据用户ID查询岗位列表
     */
    @Select("SELECT p.* FROM sys_post p INNER JOIN sys_user_post up ON p.post_id = up.post_id WHERE up.user_id = #{userId}")
    List<SysPost> selectByUserId(@Param("userId") String userId);
}
