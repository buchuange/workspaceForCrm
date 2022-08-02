package com.dragon.crm.settings.web.controller;

import com.dragon.crm.settings.domain.User;
import com.dragon.crm.settings.service.UserService;
import com.dragon.crm.utils.MD5Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/settings/user")
public class UserController {

    @Resource(name = "userService")
    private UserService userService;

    @ResponseBody

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(HttpServletRequest request, String loginAct, String loginPwd) throws Exception {
        // 将密码的明文形式转换为MD5的密文形式
        loginPwd = MD5Util.getMD5(loginPwd);
        // 接收浏览器端的ip地址
        String ip = request.getRemoteAddr();

        // 未来业务层开发，统一使用代理类形态的接口对象
        User user = userService.login(loginAct, loginPwd, ip);

        /*
           如果业务层验证登录失败，为controller抛出了异常，表示登录失败
           将由异常处理类去处理发生的异常
         */

        // 如果程序执行到此处，说明业务层没有为controller抛出任何的异常
        // 表示登录成功  {"success": true}
        request.getSession().setAttribute("user", user);
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        return map;
    }
}
