package com.baomidou.service.impl;

import com.baomidou.config.security.JwtTokenUtil;
import com.baomidou.entity.Admin;
import com.baomidou.entity.RespBean;
import com.baomidou.mapper.AdminMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.service.IAdminService;
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
 * @since 2022-03-04
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    /*
    登录之后返回token
     */
    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
        String captcha = (String) request.getSession().getAttribute("captcha");
        if (StringUtils.isBlank(code)||!captcha.equalsIgnoreCase(code)){
            return RespBean.error(1004,"验证码错误",null);
        }
        //登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (null==userDetails||!passwordEncoder.matches(password,userDetails.getPassword())){
            return RespBean.error(1002,"用户或密码错误",null);
        }
        if (!userDetails.isEnabled()){
            return RespBean.error(1003,"账号被禁用，请联系管理员",null);
        }
        //更新Security登陆用户对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //生成Token
        String token = jwtTokenUtil.generateToken(userDetails);
        Map<String,String> tokenMap= new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return RespBean.success(200,"登陆成功",tokenMap);
    }

    /*
    根据用户名获取用户
     */
    @Override
    public Admin getAdminByUserName(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username).eq("enabled",true));
    }

    @Override
    public RespBean register(Admin admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setEnabled(true);
        if (adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", admin.getUsername())) != null) {
            return new RespBean(1001, "用户已存在", null);
        }
        adminMapper.insert(admin);
        return new RespBean(200, "注册成功", null);
    }
}
