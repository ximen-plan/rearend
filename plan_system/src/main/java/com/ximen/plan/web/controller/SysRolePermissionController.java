package com.ximen.plan.web.controller;

import com.ximen.plan.constants.StatusCode;
import com.ximen.plan.dto.responsedto.ResultDTO;
import com.ximen.plan.service.SysRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ZhiShun.Cai
 * @date 2019/12/27 15:17
 * @note
 */
@RestController
@RequestMapping("sysRolePermission")
public class SysRolePermissionController {

    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    /**
     * 根据角色ID集合查询没有子权限ID集合
     */
    @GetMapping("findPermissionIdsByRoleIdExcludeExistChild/{roleId}")
    public ResultDTO findPermissionIdsByRoleId(@PathVariable Long roleId) {
        List<Long> permissionIds = sysRolePermissionService.findPermissionIdsByRoleIdExcludeExistChild(roleId);
        return new ResultDTO(StatusCode.SUCCESS, permissionIds);
    }
}
