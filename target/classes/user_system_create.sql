CREATE TABLE sys_users  (
        id varchar(32)  NOT NULL DEFAULT '' COMMENT '主键',
        user_code varchar(50)  NOT NULL COMMENT '用户代码',
        user_abbr_name varchar(100)  DEFAULT NULL COMMENT '用户中文名',
        username varchar(100) NOT NULL COMMENT '账号',
        phone_number varchar(20)  DEFAULT NULL COMMENT '手机号',
        password varchar(200)  NOT NULL COMMENT '密码',
        account_type int(11) NOT NULL DEFAULT 0 COMMENT '用户类型(0.账号密码 1.手机号 2.身份证)',
        flag varchar(32)  NOT NULL DEFAULT '0000000000000000000000' COMMENT '标志',
        create_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
        creator varchar(32)  NOT NULL COMMENT '创建人',
        modify_date timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
        modifier varchar(32) DEFAULT NULL COMMENT '修改人',
        PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB COMMENT = '用户账号表' ROW_FORMAT = Dynamic;

INSERT INTO sys_users(id, user_code, user_abbr_name, username, password, account_type, creator) VALUES(1, 'ADMIN', '超级管理员', 'admin','2LT8yzTP1tal3Lh8sSpaLU7Ikp9RGEw1H08bRUXhvWhfmF2iVyrGHZt/BKtfGtzrcD9N3qoP4ox5rQpBo7kJy2VNp9TEmkBn2UMK1ysTFIBuY0wEv9W3qCOwKvrcwAj1pev62uk1M0kgBFxUiLLVPHG1YuW87kKJnim44QyEPQs=', 1, '系统');

CREATE TABLE sys_user_info  (
        id varchar(32)  NOT NULL COMMENT '主键',
        user_id varchar(32) NOT NULL COMMENT '用户id',
        name varchar(50) DEFAULT NULL COMMENT '姓名',
        avatar varchar(1000) DEFAULT 'https://webiste-test.oss-cn-hangzhou.aliyuncs.com/avatar/defualt.jpeg' COMMENT '头像',
        birthday varchar(25)  DEFAULT NULL COMMENT '生日 格式:1988-5-12',
        sex tinyint(2) NOT NULL DEFAULT 1 COMMENT '(0.女 1.男)',
        phone varchar(20)  DEFAULT NULL COMMENT '手机号',
        email varchar(50)  DEFAULT NULL COMMENT '邮箱',
        address varchar(255)  DEFAULT NULL COMMENT '地址',
        certificate varchar(25)  DEFAULT NULL COMMENT '证件类型 default身份证',
        certificate_code varchar(50)  DEFAULT NULL COMMENT '证件编号 \r\n',
        is_verify tinyint(2) NULL DEFAULT 0 COMMENT '是否通过实名认证',
        PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB  ROW_FORMAT = Dynamic;

INSERT INTO sys_user_info(id, user_id) VALUES(1, 1);

CREATE TABLE sys_authority  (
        id varchar(32)  NOT NULL COMMENT ' 主键',
        authority_code varchar(100)  NOT NULL COMMENT '权限代码',
        type varchar(100)  NOT NULL COMMENT '权限类型(menu、interface)',
        authority_name varchar(100)  NOT NULL COMMENT '权限名称',
        url varchar(200)  NOT NULL COMMENT '访问地址',
        create_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
        creator varchar(32)  NOT NULL COMMENT '创建人',
        modify_date timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
        modifier varchar(32)  NULL DEFAULT NULL COMMENT '修改人',
        PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB  COMMENT = '权限表' ROW_FORMAT = Dynamic;

CREATE TABLE sys_menu  (
        id varchar(32)  NOT NULL COMMENT '主键',
        menu_code varchar(100)  NOT NULL COMMENT '菜单代码',
        name varchar(100)  NOT NULL COMMENT '菜单名称',
        path varchar(255)  NOT NULL COMMENT '路径',
        redirect varchar(255)  NULL DEFAULT NULL COMMENT '重定向地址',
        component varchar(255)  NULL DEFAULT NULL COMMENT '组件',
        hidden int NOT NULL COMMENT '是否可见',
        icon varchar(100)  NULL DEFAULT NULL COMMENT '图标',
        title varchar(100)  NULL DEFAULT NULL COMMENT '标题',
        menu_order int NOT NULL COMMENT '排序',
        parent_menu_id varchar(32)  NULL DEFAULT NULL COMMENT '父菜单id',
        create_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
        creator varchar(32)  NOT NULL COMMENT '创建人',
        modify_date timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
        modifier varchar(32)  NULL DEFAULT NULL COMMENT '修改人',
        PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB  COMMENT = '菜单表' ROW_FORMAT = Dynamic;

CREATE TABLE sys_menu_role_asso  (
        menu_id varchar(32)  NOT NULL COMMENT '菜单id',
        role_id varchar(32)  NOT NULL COMMENT '角色id',
        UNIQUE INDEX sys_menu_role_asso_pk(menu_id, role_id) USING BTREE
) ENGINE = InnoDB  ROW_FORMAT = Dynamic;

CREATE TABLE sys_roles  (
       id varchar(32)  NOT NULL COMMENT '主键',
       role_code varchar(100)  NOT NULL COMMENT '角色代码',
       role_name varchar(100)  NOT NULL COMMENT '角色名称',
       default_role tinyint NOT NULL DEFAULT 0 COMMENT '系统预置角色(0.否 1.是)',
       enabled tinyint NOT NULL DEFAULT 1 COMMENT '状态(0.禁用 1.正常)',
       remark varchar(255)  NULL DEFAULT NULL COMMENT '描述',
       create_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
       creator varchar(32)  NOT NULL COMMENT '创建人',
       modify_date timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
       modifier varchar(32)  NULL DEFAULT NULL COMMENT '修改人',
       PRIMARY KEY (id) USING BTREE,
       INDEX role_code_index(role_code) USING BTREE
) ENGINE = InnoDB  COMMENT = '角色表' ROW_FORMAT = Dynamic;

INSERT INTO sys_roles(id, role_code,default_role ,enabled ,role_name, remark, creator) VALUES(1, 'ROLE_ADMIN',1,1,'超级管理员','系统超级管理员', '系统');
INSERT INTO sys_roles(id, role_code,default_role ,enabled,role_name, remark, creator) VALUES(2, 'ROLE_USER',1 ,1 , '普通用户','系统普通用户角色', '系统');

CREATE TABLE sys_user_role_asso  (
      user_id varchar(32)  NOT NULL COMMENT '用户id',
      role_id varchar(32)  NOT NULL COMMENT '角色id',
      PRIMARY KEY (user_id, role_id) USING BTREE
) ENGINE = InnoDB  COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

INSERT INTO sys_user_role_asso(user_id, role_id) VALUES(1, 1);


CREATE TABLE sys_auth_role_asso  (
      id varchar(32)  NOT NULL COMMENT '主键',
      authority_id varchar(32)  NOT NULL COMMENT '权限id',
      role_id varchar(32)  NOT NULL COMMENT '角色id',
      PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB  ROW_FORMAT = Dynamic;


CREATE TABLE sys_apps  (
                             id varchar(32)  NOT NULL COMMENT '主键',
      app_name varchar(100)  NOT NULL COMMENT '应用名称',
      client_id varchar(100)  NOT NULL COMMENT '应用id',
      app_icon varchar(255)  NOT NULL COMMENT '应用图标',
      redirect_url varchar(200)  NOT NULL COMMENT '回调地址',
      secret varchar(200)  NOT NULL COMMENT '加盐(用户成功申请后返回加密后的密钥给用户)',
      scopes varchar(100)  NOT NULL COMMENT '授权范围',
      application_json json NOT NULL COMMENT 'appJson',
      user_id varchar(32)  NOT NULL COMMENT '应用持有人',
      flag varchar(32)  NOT NULL DEFAULT '0000000000000000000000' COMMENT '标志',
      status varchar(50)  NOT NULL DEFAULT 'fitness' COMMENT '默认fitness',
      create_date timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
      creator varchar(32)  NOT NULL COMMENT '创建人',
      modify_date timestamp(0) NULL DEFAULT NULL COMMENT '修改时间',
      modifier varchar(32)  NULL DEFAULT NULL COMMENT '修改人',
      PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB  COMMENT = '应用程序表' ROW_FORMAT = Dynamic;

CREATE TABLE sys_app_grant_types  (
      app_id varchar(32) NOT NULL COMMENT '应用id',
      grant_type varchar(100) NOT NULL COMMENT '授权方式',
      PRIMARY KEY (app_id, grant_type) USING BTREE
) ENGINE = InnoDB