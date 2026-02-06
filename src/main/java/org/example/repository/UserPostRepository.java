package org.example.repository;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.entity.SysUserPost;
import java.util.List;
/**
 * 用户岗位关联 Repository 接口
 */
public interface UserPostRepository extends IService<SysUserPost> {
    /**
     * 根据用户ID查询关联
     */
    List<SysUserPost> findByUserId(String userId);
    /**
     * 根据岗位ID查询关联
     */
    List<SysUserPost> findByPostId(Long postId);
    /**
     * 删除用户的所有岗位关联
     */
    int removeByUserId(String userId);
    /**
     * 统计岗位关联的用户数量
     */
    int countByPostId(Long postId);
    /**
     * 检查用户是否存在
     */
    boolean userExists(String userId);
    /**
     * 批量为用户分配岗位
     */
    void assignPostsToUser(String userId, List<Long> postIds);
}
