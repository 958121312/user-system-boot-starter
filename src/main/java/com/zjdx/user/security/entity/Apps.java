package com.zjdx.user.security.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zjdx.user.security.base.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * 应用实体
 * @author yuntian
 * @since 2022-02-22
 */
@Data
@Accessors(chain = true)
@TableName("sys_apps")
public class Apps extends BaseEntity {
    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用id
     */
    private String clientId;

    /**
     * 应用图标
     */
    private String appIcon;

    /**
     * 授权类型
     */
    @TableField(select = false)
    private Set<String> grantTypes;

    /**
     * 回调地址
     */
    private String redirectUrl;

    /**
     * 加盐(用户成功申请后返回加密后的密钥给用户)
     */
    private String secret;

    /**
     * 授权范围
     */
    private String scopes;

    /**
     * 用户id
     */
    private String userId;

    /**
     * appJson
     */
    private String applicationJson;

    /**
     * 标志
     */
    private String flag;

    /**
     * 状态
     */
    private String status;
}
