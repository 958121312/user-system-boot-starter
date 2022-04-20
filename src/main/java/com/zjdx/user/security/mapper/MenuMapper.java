package com.zjdx.user.security.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjdx.user.security.core.menu.vo.MenuPermitResult;
import com.zjdx.user.security.entity.Menu;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * 动态菜单持久层对象
 * @author yuntian
 * @since 2022-02-21
 */
@Repository
@ConditionalOnMissingBean(MenuMapper.class)
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 查询菜单信息
     * @return
     */
    List<MenuPermitResult> listMenuPermit();
}
