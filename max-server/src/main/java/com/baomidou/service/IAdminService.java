package com.baomidou.service;

import com.baomidou.entity.Admin;
import com.baomidou.entity.RespBean;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lunlunbu
 * @since 2022-03-04
 */
public interface IAdminService extends IService<Admin> {
/*
登录之后返回token
 */
    RespBean login(String username, String password, HttpServletRequest request);

    /*
    根据用户名获取用户
     */
    Admin getAdminByUserName(String username);

    /**
     * 注册用户
     * @param admin
     * @return
     */
    RespBean register(Admin admin);
}
