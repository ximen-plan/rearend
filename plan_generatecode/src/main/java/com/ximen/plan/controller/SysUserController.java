package com.ximen.plan.controller;


import com.ximen.plan.service.impl.SysPermissionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author damoncai
 * @since 2019-12-19
 */
@Controller
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    SysPermissionServiceImpl permission;

    public String find() {
        return "";
    }
}
