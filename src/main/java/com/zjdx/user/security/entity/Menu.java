package com.zjdx.user.security.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zjdx.user.security.base.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author yuntian
 * @since 2022-04-20
 * 动态菜单
 */
@Data
@Accessors(chain = true)
@TableName("sys_menu")
public class Menu extends BaseEntity {
    private String menuCode;

    private String name;

    private String path;

    private String redirect;

    private String component;

    private Boolean hidden;

    private String icon;

    private String title;

    private Integer menuOrder;

    private String parentMenuId;
}
