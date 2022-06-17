package com.lunlunbu.service;

import com.lunlunbu.entity.RespBean;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lunlunbu.entity.Admin;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lunlunbu
 * @since 2022-06-15
 */
public interface IAdminService extends IService<com.lunlunbu.entity.Admin> {

    /**
     * 登陆后返回token
     * @param username
     * @param password
     * @param request
     * @return
     */
    RespBean login(String username, String password, HttpServletRequest request);

    /**
     * 获取当前登录用户信息
     * @param username
     * @return
     */
    com.lunlunbu.entity.Admin getAdminByUserName(String username);

    /**
     * 注册用户
     * @param admin
     * @return
     */
    RespBean register(Admin admin);
}
