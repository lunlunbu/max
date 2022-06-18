package com.baomidou.controller;


import com.baomidou.entity.Admin;
import com.baomidou.entity.AdminLoginParam;
import com.baomidou.entity.RespBean;
import com.baomidou.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;


/**
 *  Swagger2 常用注解
 *   @Api
 *   @ApiOperation
 *   @ApiImplicitParam
 *   @ApiImplicitParams
 *   @ApiModel
 *   @ApiModelProperty
 */
@Api(tags = "LoginController")
@RestController
public class LoginController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "登录之后返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request){
        return adminService.login(adminLoginParam.getUsername(),adminLoginParam.getPassword(),adminLoginParam.getCode(),request);
    }

    @ApiOperation(value = "注册用户")
    @PostMapping("/register")
    public RespBean register(AdminLoginParam adminLoginParam){
        Admin admin = new Admin();
        admin.setUsername(adminLoginParam.getUsername());
        admin.setPassword(adminLoginParam.getPassword());
        return adminService.register(admin);
    }


    @ApiOperation(value = "获取当前登录用户的信息")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal principal){
        if (null==principal){
            return null;
        }
        String username = principal.getName();
        Admin admin = adminService.getAdminByUserName(username);
        admin.setPassword(null);
        return admin;
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean logout(){
        return RespBean.success(200,"注销成功！",null);
    }

}
