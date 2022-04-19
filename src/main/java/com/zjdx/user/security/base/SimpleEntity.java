package com.zjdx.user.security.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 简单实体对象
 * @author yuntian
 * @since 2022-02-21
 */
@Data
@Accessors(chain = true)
public class SimpleEntity {
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
}
