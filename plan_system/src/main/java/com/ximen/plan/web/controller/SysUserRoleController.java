package com.ximen.plan.web.controller;

import com.ximen.plan.constants.StatusCode;
import com.ximen.plan.dto.responsedto.ResultDTO;
import com.ximen.plan.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ZhiShun.Cai
 * @date 2019/12/22 11:58
 * @note
 */
@RestController
@RequestMapping("sysUserRole")
public class SysUserRoleController {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 根据用户ID查询角色ID集合
     */
    @GetMapping("findRoleIdsByUserId/{userId}")

    public ResultDTO findRolesByUserId(@PathVariable("userId") Long userId) {
        return new ResultDTO(StatusCode.SUCCESS, sysUserRoleService.findRolesByUserId(userId));
    }
}
