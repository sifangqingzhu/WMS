package org.example.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.SysPostMenu;

import java.util.List;

@Mapper
public interface PostMenuMapper extends BaseMapper<SysPostMenu> {

    default List<SysPostMenu> selectByPostId(Long postId) {
        return selectList(new LambdaQueryWrapper<SysPostMenu>()
                .eq(SysPostMenu::getPostId, postId));
    }

    default List<SysPostMenu> selectByMenuId(Long menuId) {
        return selectList(new LambdaQueryWrapper<SysPostMenu>()
                .eq(SysPostMenu::getMenuId, menuId));
    }

    default SysPostMenu selectByPostIdAndMenuId(Long postId, Long menuId) {
        return selectOne(new LambdaQueryWrapper<SysPostMenu>()
                .eq(SysPostMenu::getPostId, postId)
                .eq(SysPostMenu::getMenuId, menuId));
    }

    default int deleteByPostId(Long postId) {
        return delete(new LambdaQueryWrapper<SysPostMenu>()
                .eq(SysPostMenu::getPostId, postId));
    }

    default int deleteByMenuId(Long menuId) {
        return delete(new LambdaQueryWrapper<SysPostMenu>()
                .eq(SysPostMenu::getMenuId, menuId));
    }
}
