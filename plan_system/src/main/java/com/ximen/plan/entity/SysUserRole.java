package com.ximen.plan.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@TableName("sys_user_role")
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = -3166012934498268403L;

    private Long userId;

    private Long roleId;

}