package com.lunlunbu.service.impl;

import com.lunlunbu.entity.RespBean;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lunlunbu.config.security.JwtTokenUtil;
import com.lunlunbu.entity.Admin;
import com.lunlunbu.mapper.AdminMapper;
import com.lunlunbu.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lunlunbu
 * @since 2022-06-15
 */
@Service
public class AdminServiceImpl extends ServiceImpl<com.lunlunbu.mapper.AdminMapper, com.lunlunbu.entity.Admin> implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;//密码加密
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;

    /**
     * 登陆后返回token
     * @param username
     * @param password
     * @param request
     * @return
     */
    @Override
    public RespBean login(String username, String password, HttpServletRequest request) {
        //登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (null==userDetails||!passwordEncoder.matches(password,userDetails.getPassword())){
            return RespBean.error(1002,"用户或密码错误",null);
        }
        if (!userDetails.isEnabled()){
            return RespBean.error(1003,"账号被禁用，请联系管理员",null);
        }
        //更新security登陆用户对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //生成token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String,String> tokenMap=new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return RespBean.success(200,"登陆成功",tokenMap);
    }

    /**
     * 获取当前登录用户的信息
     * @param username
     * @return
     */
    @Override
    public com.lunlunbu.entity.Admin getAdminByUserName(String username) {
        //TODO 判断查出来的对象是否为空
        return adminMapper.selectOne(new QueryWrapper<com.lunlunbu.entity.Admin>().eq("username",username).eq("enabled",true));
    }

    @Override
    public RespBean register(com.lunlunbu.entity.Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        if (adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", admin.getUsername()))!=null){
            return new RespBean(1001,"用户已存在",null);
        }
        adminMapper.insert(admin);
        return new RespBean(200,"注册成功",null);
    }
}
