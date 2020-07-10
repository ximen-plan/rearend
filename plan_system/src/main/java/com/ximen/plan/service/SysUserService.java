package com.ximen.plan.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ximen.plan.constants.StatusCode;
import com.ximen.plan.dto.responsedto.PageResultDTO;
import com.ximen.plan.entity.*;
import com.ximen.plan.exception.ApiException;
import com.ximen.plan.mapper.SysUserMapper;
import com.ximen.plan.mapper.SysUserRoleMapper;
import com.ximen.plan.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author ZhiShun.Cai
 * @date 2019/12/17 16:29
 * @note
 */
@Service
@Transactional
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {

    @Value("${user.password}")
    private String password;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysRolePermissionService sysRolePermissionService;

    @Autowired
    private SysPermissionService sysPermissionService;

    /**
     * 添加用户
     *
     * @param user
     */
    public void add(SysUser user) {
        // 创建用户(未指定密码使用默认密码)
        if (StringUtils.isNotBlank(user.getPassword())) {
            password = user.getPassword();
        }
        user.setPassword(passwordEncoder.encode(password));
        user.setCreateTime(new Date());
        user.setAvatar(SysUser.DEFAULT_AVATAR);
        sysUserMapper.insert(user);

        // 保存用户角色
        String[] roles = user.getRoleId().split(StringPool.COMMA);
        setUserRoles(user, roles);

    }

    /**
     * 更新用户
     *
     * @param user
     */
    public void update(SysUser user) {
        //更新用户
        user.setModifyTime(new Date());
        user.setPassword(null);
        this.sysUserMapper.updateById(user);

        //跟新角色 删除原先角色 添加新角色
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().eq("user_id", user.getUserId()));
        setUserRoles(user, user.getRoleId().split(","));

    }

    /**
     * 插入用户角色
     *
     * @param user
     * @param roleIds
     */
    private void setUserRoles(SysUser user, String[] roleIds) {
        Arrays.asList(roleIds).stream().forEach(roleId -> {
            SysUserRole userRole = SysUserRole.builder()
                    .userId(user.getUserId())
                    .roleId(Long.valueOf(roleId)).build();
            this.sysUserRoleMapper.insert(userRole);
        });
    }

    /**
     * 分页条件查询
     *
     * @param pageNumber
     * @param pageSize
     * @param searchKey
     * @return
     */
    public PageResultDTO page(Integer pageNumber, Integer pageSize, String searchKey) {
        Wrapper<SysUser> queryWrapper = null;
        if (StringUtils.isNotBlank(searchKey)) {
            queryWrapper = new QueryWrapper<SysUser>()
                    .like("username", searchKey)
                    .or()
                    .like("email", searchKey)
                    .like("mobile", searchKey);
        }
        IPage<SysUser> userIPage = this.sysUserMapper.selectPage(new Page<SysUser>(pageNumber, pageSize), queryWrapper);
        return new PageResultDTO(userIPage.getTotal(), userIPage.getRecords());
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return
     */
    public SysUser findById(Integer id) {
        SysUser user = this.sysUserMapper.findById(id);
        return user;
    }

    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    public SysUser findUserByLoginnameAndPassword(String username, String password) {
        SysUser sysUser = this.sysUserMapper.selectOne(new QueryWrapper<SysUser>().eq("username", username));
        if (sysUser == null) {
            throw new ApiException(StatusCode.ERROR_USERNOTEXIST);
        }
        if (!passwordEncoder.matches(password, sysUser.getPassword())) {
            throw new ApiException(StatusCode.ERROR_PASSWORDERROR);
        }
        return sysUser;
    }

    /**
     * 获取用户信息
     *
     * @param userId
     * @return
     */
    public SysUser getUserInfo(Long userId) {
        //角色集合
        List<SysRole> roles = null;
        //权限集合
        List<SysPermission> permissions = null;
        //1.获取用户信息
        SysUser sysUser = this.sysUserMapper.selectById(userId);
        sysUser.setPassword(null);
        //2.获取用户角色
        List<SysUserRole> userRoles = sysUserRoleService.findByUserId(userId);
        if (!org.springframework.util.CollectionUtils.isEmpty(userRoles)) {
            //根据角色ID集合查询
            roles = this.sysRoleService.findByIds(userRoles.stream().map(userRole -> userRole.getRoleId()).collect(Collectors.toList()));
            if (!org.springframework.util.CollectionUtils.isEmpty(roles)) {
                List<String> roleNames = roles.stream().map(role -> role.getRoleName()).collect(Collectors.toList());
                sysUser.setRoles(roleNames);
            }
        }
        //3.获取用户权限
        if (!org.springframework.util.CollectionUtils.isEmpty(roles)) {
            //根据角色查询权角色权限
            List<SysRolePermission> rolePermissions = this.sysRolePermissionService.findByRoleIds(roles.stream().map(role -> role.getRoleId()).collect(Collectors.toList()));
            if (!CollectionUtils.isEmpty(rolePermissions)) {
                permissions = this.sysPermissionService.findByIds(rolePermissions.stream().map(rolePermission -> rolePermission.getPermissionId()).collect(Collectors.toList()));
                //过滤掉重复权限(通过权限标识)
                Set<String> permissionSet = new HashSet<>(permissions.stream().map(permission -> permission.getPerms()).collect(Collectors.toList()));
                sysUser.setPermissions(new ArrayList<>(permissionSet));
            }
        }
        return sysUser;
    }

    /**
     * 导出用户信息
     *
     * @param request
     * @param response
     */
    public void exportUserInfo(HttpServletRequest request, HttpServletResponse response) {
        ServletOutputStream sos = null;
        ZipOutputStream zos = null;
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;
        try {
            sos = response.getOutputStream();
            zos = new ZipOutputStream(sos);
            //1.设置响应头信息
            this.setResponseHeaderInfo(request, response);
            //2.获取需导出的数据
            List<SysUser> users = this.sysUserMapper.selectList(null);
            //3.设置数据
            for (SysUser user : users) {
                //读取模板
                baos = new ByteArrayOutputStream();
                fis = new FileInputStream("c:\\user.xlsx");
                //设置数据
                this.setDatas(user, baos, fis);
                //添加到zipStream
                this.compressFileToZipStream(zos, baos, user.getUsername() + ".xlsx");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (sos != null) {
                    sos.close();
                }
                if (zos != null) {
                    zos.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {

            }
        }
    }

    /**
     * 设置Excel数据
     */
    private void setDatas(SysUser user, ByteArrayOutputStream baos, FileInputStream fis) throws Exception {
        XSSFRow row = null;
        //1.创建wb
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        //2.获取sheet
        XSSFSheet sheet = wb.getSheet("user");
        row = sheet.createRow(1);
        row.createCell(0).setCellValue(1);
        row.createCell(1).setCellValue(user.getUsername());
        row.createCell(2).setCellValue(user.getEmail());
        row.createCell(3).setCellValue(user.getStatus() == 0 ? "未激活" : "激活");
        row.createCell(3).setCellValue(user.getCreateTime());
        wb.write(baos);
    }

    /**
     * file to zipstream
     *
     * @param zos
     * @param baos
     * @param excelName
     */
    private void compressFileToZipStream(ZipOutputStream zos, ByteArrayOutputStream baos, String excelName) throws Exception {
        byte[] buf = new byte[1024];
        ByteArrayInputStream is = null;
        BufferedInputStream bis = null;
        // Compress the files
        byte[] content = baos.toByteArray();
        is = new ByteArrayInputStream(content);
        bis = new BufferedInputStream(is);
        // Add ZIP entry to output stream.
        zos.putNextEntry(new ZipEntry(excelName));
        // Transfer bytes from the file to the ZIP file
        int len;
        while ((len = bis.read(buf)) > 0) {
            zos.write(buf, 0, len);
        }
    }

    /**
     * 设置响应头信息
     *
     * @param response
     */
    private void setResponseHeaderInfo(HttpServletRequest request, HttpServletResponse response) {
        try {

//            String agent = request.getHeader("User-Agent");
//            String excelExportName = FileUtils.encodeDownloadFilename("用户信息.xlsx", agent);
//            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//            response.setHeader("file-name", excelExportName);
//            response.setHeader("Access-Control-Expose-Headers", "file-name");
//            response.setHeader("content-disposition", "attachment;filename=" + excelExportName);
            String agent = request.getHeader("User-Agent");
            String zipName = FileUtils.encodeDownloadFilename("用户信息.zip", agent);
            response.setContentType("application/zip");
            response.setHeader("file-name", zipName);
            response.setHeader("Access-Control-Expose-Headers", "file-name");
            response.setHeader("content-disposition", "attachment;filename=" + zipName);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
