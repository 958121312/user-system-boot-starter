package com.zjdx.user.security.core.menu.vo;

import lombok.Data;

import java.util.List;

/**
 * @author yuntian
 * @since 2022-04-20
 */
@Data
public class MenuPermitResult {
    private String id;

    private String name;

    private String path;

    private String redirect;

    private String scope;

    private String component;

    private Boolean hidden;

    private String icon;

    private String title;

    private Integer menuOrder;

    private List<MenuPermitResult> children;

    private List<String> permission;
}
