package org.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.SysUser;

/**
 * 用户 Mapper 接口
 * 继承 MyBatis-Plus 的 BaseMapper，自动提供基础 CRUD 方法
 * 这是最底层的数据库访问接口
 */
@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
    // MyBatis-Plus 自动提供以下方法：
    // - insert(T entity)
    // - deleteById(Serializable id)
    // - updateById(T entity)
    // - selectById(Serializable id)
    // - selectOne(Wrapper<T> queryWrapper)
    // - selectList(Wrapper<T> queryWrapper)
    // - selectCount(Wrapper<T> queryWrapper)
    // - selectPage(IPage<T> page, Wrapper<T> queryWrapper)
}
