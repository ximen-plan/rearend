package com.ximen.plan.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@TableName(value = "sys_role")
@AllArgsConstructor
@NoArgsConstructor
public class SysRole implements Serializable {

    private static final long serialVersionUID = -1714476694755654924L;

    @TableId
    private Long roleId;

    // 角色名称
    private String roleName;

    // 角色描述
    private String remark;

    // 创建时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    // 修改时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    private transient String createTimeFrom;
    private transient String createTimeTo;
    private transient String menuId;

}