package com.lunlunbu.controller;

import com.lunlunbu.entity.RespBean;
import com.lunlunbu.entity.Admin;
import com.lunlunbu.entity.AdminLoginParam;
import com.lunlunbu.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Api(tags = "LoginController")
@RestController
public class LoginController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "登陆后返回token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLoginParam adminLoginParam, HttpServletRequest request){
        return adminService.login(adminLoginParam.getUsername(), adminLoginParam.getPassword(),request);
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
    @GetMapping("/user/info")
    public Admin getUserInfo(Principal principal){
        if (null==principal){
            RespBean.error(1003,"当前用户异常",null);
        }
        String username = principal.getName();
        Admin admin = adminService.getAdminByUserName(username);
        admin.setPassword(null);
        return admin;
    }

    @ApiOperation(value = "退出登录")
    @PostMapping("/logout")
    public RespBean logout(){
        //TODO 清理用户token信息
        return RespBean.success(200,"注销成功",null);
    }
}
