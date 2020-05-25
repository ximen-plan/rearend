package com.ximen.plan.web.controller;

import com.ximen.plan.constants.StatusCode;
import com.ximen.plan.dto.responsedto.PageResultDTO;
import com.ximen.plan.dto.responsedto.ResultDTO;
import com.ximen.plan.entity.*;
import com.ximen.plan.service.*;
import com.ximen.plan.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ZhiShun.Cai
 * @date 2019/12/17 16:08
 * @note
 */
@RestController
@RequestMapping("sysUser")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    @Autowired
    private SysPermissionService sysPermissionService;


    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 登录
     *
     * @param sysUser
     * @return
     */
    @PostMapping("login")
    public ResultDTO login(@RequestBody SysUser sysUser) {
        //存放需存放到token中的信息
        Map<String, Object> jwtMap = new HashMap<>();
        //角色集合
        List<SysRole> roles = null;
        //权限集合
        List<SysPermission> permissions = null;
        SysUser user = userService.findUserByLoginnameAndPassword(sysUser.getUsername(), sysUser.getPassword());
        //1.获取用户角色
        List<SysUserRole> userRoles = sysUserRoleService.findByUserId(user.getUserId());
        if (!CollectionUtils.isEmpty(userRoles)) {
            //根据角色ID集合查询
            roles = this.sysRoleService.findByIds(userRoles.stream().map(userRole -> userRole.getRoleId()).collect(Collectors.toList()));
            if (!CollectionUtils.isEmpty(roles)) {
                List<String> roleNames = roles.stream().map(role -> role.getRoleName()).collect(Collectors.toList());
                jwtMap.put("roles", roleNames.toString().substring(1, roleNames.toString().length() - 1));
            }
        }
        //2.获取用户权限
        if (!CollectionUtils.isEmpty(roles)) {
            //根据角色查询权角色权限
            List<SysRolePermission> rolePermissions = this.sysRolePermissionService.findByRoleIds(roles.stream().map(role -> role.getRoleId()).collect(Collectors.toList()));
            if (!CollectionUtils.isEmpty(rolePermissions)) {
                permissions = this.sysPermissionService.findByIds(rolePermissions.stream().map(rolePermission -> rolePermission.getPermissionId()).collect(Collectors.toList()));
                //过滤掉重复权限(通过权限标识)
                Set<String> permissionSet = new HashSet<>(permissions.stream().map(permission -> permission.getPerms()).collect(Collectors.toList()));
                jwtMap.put("permissions", permissionSet.toString().substring(1, permissionSet.toString().length() - 1));
            }
        }
        String token = jwtUtil.createJWT(user.getUserId() + "", user.getUsername(), jwtMap);
        return new ResultDTO(StatusCode.SUCCESS, token);
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping(value = "getUserInfo")
    public ResultDTO getUserInfo(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("user_claims");
//        String userId = claims.getId();
        String userId = "1";
        SysUser sysUser = this.userService.getUserInfo(Long.valueOf(userId));
        return new ResultDTO(StatusCode.SUCCESS, sysUser);
    }

    /**
     * 分页条件查询
     * 只支持 用户名、邮箱、手机号查询
     *
     * @param searchKey
     * @param pageNumber
     * @param pageSize
     * @return
     */

    @GetMapping("page/{pageNumber}/{pageSize}")
    public ResultDTO page(String searchKey,
                          @PathVariable("pageNumber") Integer pageNumber,
                          @PathVariable("pageSize") Integer pageSize) {

        PageResultDTO pageResult = this.userService.page(pageNumber, pageSize, searchKey);
        return new ResultDTO(StatusCode.SUCCESS, pageResult);
    }

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    @PostMapping(value = "add", name = "user:list")
    public ResultDTO add(@RequestBody SysUser user) {
        userService.add(user);
        return new ResultDTO(StatusCode.SUCCESS);
    }

    @PutMapping("update")
    public ResultDTO update(@RequestBody SysUser user) {
        userService.update(user);
        return new ResultDTO(StatusCode.SUCCESS);
    }

    @GetMapping("findById/{id}")
    public ResultDTO findById(@PathVariable Integer id) {
        return new ResultDTO(StatusCode.SUCCESS, userService.findById(id));
    }

    /**
     * 退出系统
     */
    @GetMapping("logout")
    public ResultDTO logout() {
        return new ResultDTO(StatusCode.SUCCESS);
    }

    /**
     * 生成路由
     */
    @GetMapping("generateRoute/{userId}")
    public ResultDTO generateRoute(@PathVariable Long userId) {
//        List<VueRouter<SysPermission>> userRouters = sysPermissionService.getUserRouters(userId);
        Object object = sysPermissionService.getUserRouters(userId);
        return new ResultDTO(StatusCode.SUCCESS, object);
    }
}
