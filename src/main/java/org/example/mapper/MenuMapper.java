package org.example.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.entity.SysMenu;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<SysMenu> {

    default List<SysMenu> selectByParentId(Long parentId) {
        return selectList(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, parentId)
                .orderByAsc(SysMenu::getSortOrder));
    }

    default List<SysMenu> selectRootMenus() {
        return selectList(new LambdaQueryWrapper<SysMenu>()
                .isNull(SysMenu::getParentId)
                .orderByAsc(SysMenu::getSortOrder));
    }

    default SysMenu selectByMenuCode(String menuCode) {
        return selectOne(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getMenuCode, menuCode));
    }

    default List<SysMenu> selectVisibleMenus() {
        return selectList(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getIsVisible, true)
                .orderByAsc(SysMenu::getSortOrder));
    }

    default List<SysMenu> selectByMenuType(Integer menuType) {
        return selectList(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getMenuType, menuType)
                .orderByAsc(SysMenu::getSortOrder));
    }

    default long countByParentId(Long parentId) {
        return selectCount(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getParentId, parentId));
    }

    default long countByMenuCode(String menuCode) {
        return selectCount(new LambdaQueryWrapper<SysMenu>()
                .eq(SysMenu::getMenuCode, menuCode));
    }

    default List<SysMenu> selectByMenuIds(List<Long> menuIds) {
        if (menuIds == null || menuIds.isEmpty()) {
            return List.of();
        }
        return selectList(new LambdaQueryWrapper<SysMenu>()
                .in(SysMenu::getMenuId, menuIds)
                .orderByAsc(SysMenu::getSortOrder));
    }
}
