package com.ximen.plan.web.controller;

import com.ximen.plan.constants.StatusCode;
import com.ximen.plan.dto.responsedto.ResultDTO;
import com.ximen.plan.entity.SysPermission;
import com.ximen.plan.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ZhiShun.Cai
 * @date 2019/12/23 14:49
 * @note
 */
@RestController
@RequestMapping("sysMenu")
public class SysMenuController {

    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * 菜单
     *
     * @return
     */
    @GetMapping("list")
    public ResultDTO list(Integer type) {
        return new ResultDTO(StatusCode.SUCCESS, sysPermissionService.pageList(type));
    }

    /**
     * 添加
     *
     * @param sysPermission
     * @return
     */
    @PostMapping("add")
    public ResultDTO add(@RequestBody SysPermission sysPermission) {
        sysPermissionService.add(sysPermission);
        return new ResultDTO(StatusCode.SUCCESS);
    }

    /**
     * 删除
     */
    @DeleteMapping("del/{id}")
    public ResultDTO del(@PathVariable Long id) {
        Boolean flag = sysPermissionService.del(id);
        if (flag) {
            return new ResultDTO(StatusCode.SUCCESS);
        } else {
            return new ResultDTO(StatusCode.ERROR_EXISTCHIDRED);
        }
    }

    /**
     * 菜单和按钮权限
     */
    @GetMapping("menuAndButton")
    public ResultDTO menuAndButton() {
        return new ResultDTO(StatusCode.SUCCESS, sysPermissionService.menuAndButton());
    }

}
