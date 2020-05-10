package com.ximen.plan.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author damoncai
 * @since 2019-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_user")
public class SysUser extends Model<SysUser> implements Serializable {

    private static final long serialVersionUID = -4852732617765810959L;
    /**
     * 账户状态
     */
    public static final String STATUS_VALID = "1";

    public static final String STATUS_LOCK = "0";

    public static final String DEFAULT_AVATAR = "default.jpg";

    /**
     * 性别
     */
    public static final String SEX_MALE = "0";

    public static final String SEX_FEMALE = "1";

    public static final String SEX_UNKNOW = "2";

    // 默认密码
    public static final String DEFAULT_PASSWORD = "1234qwer";

    /**
     * 用户ID
     */
    @TableId
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 部门
     */
    @TableField(exist = false)
    private transient String deptName;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 状态 0锁定 1有效
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     * 最近访问时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;

    /**
     * 性别 0男 1女 2保密
     */
    private Integer sex;

    /**
     * 描述
     */
    private String description;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户权限名称集合
     */
    @TableField(exist = false)
    private transient List<String> permissions;

    @TableField(exist = false)
    private transient List<String> roles;

    // 角色ID集合
    @TableField(exist = false)
    private transient String roleId;

    // 排序字段
    @TableField(exist = false)
    private String sortField;

    // 排序规则 ascend 升序 descend 降序
    @TableField(exist = false)
    private String sortOrder;

    @TableField(exist = false)
    private transient String createTimeFrom;

    @TableField(exist = false)
    private String createTimeTo;

    @Override
    protected Serializable pkVal() {
        return this.userId;
    }

}
