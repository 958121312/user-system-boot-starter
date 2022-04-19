package com.zjdx.user.security.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;

import com.zjdx.user.security.SecurityUserDetails;
import com.zjdx.user.security.annotation.CreatorFillIgnore;
import com.zjdx.user.security.annotation.IdFillIgnore;
import com.zjdx.user.security.base.BaseEntity;
import com.zjdx.user.security.base.SimpleEntity;
import com.zjdx.user.security.utils.IdUtil;
import com.zjdx.user.security.utils.SecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
public class MyBatisFillMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Class<?> aClass = metaObject.getOriginalObject().getClass();
        if(aClass.getSuperclass() == BaseEntity.class || aClass.getSuperclass() == SimpleEntity.class) {
            IdFillIgnore declaredAnnotation = aClass.getDeclaredAnnotation(IdFillIgnore.class);
            if(Objects.isNull(declaredAnnotation)) {
                this.strictInsertFill(metaObject, "id", IdUtil::nextId, String.class);
            }
            this.strictInsertFill(metaObject, "createDate", Date::new, Date.class);
            CreatorFillIgnore creatorFillIgnore = aClass.getDeclaredAnnotation(CreatorFillIgnore.class);
            if(Objects.isNull(creatorFillIgnore)) {
                this.strictInsertFill(metaObject, "creator", this::getUserId, String.class);
            }
        }

    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Class<?> aClass = metaObject.getOriginalObject().getClass();
        if(aClass.getSuperclass() == BaseEntity.class || aClass.getSuperclass() == SimpleEntity.class) {
            this.strictUpdateFill(metaObject, "modifyDate", Date::new, Date.class);
            this.strictUpdateFill(metaObject, "modifier", this::getUserId, String.class);
        }
    }

    private String getUserId() {
        SecurityUserDetails user = SecurityUtils.getUser();
        return Objects.nonNull(user) ? user.getId() : "系统";
    }
}
