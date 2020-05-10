package com.ximen.plan.VO;

import com.ximen.plan.entity.SysRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ZhiShun.Cai
 * @date 2019/12/27 17:10
 * @note
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateParamsVO extends SysRole {
    private static final long serialVersionUID = -6113461758661404436L;

    private List<Long> permissionIds;
}
