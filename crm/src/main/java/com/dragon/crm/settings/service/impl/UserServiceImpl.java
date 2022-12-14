package com.dragon.crm.settings.service.impl;

import com.dragon.crm.exception.LoginException;
import com.dragon.crm.settings.dao.UserDao;
import com.dragon.crm.settings.domain.User;
import com.dragon.crm.settings.service.UserService;
import com.dragon.crm.utils.DateTimeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource(name = "userDao")
    private UserDao userDao;

    @Override
    public List<User> getUserList() {
        List<User> userList = userDao.getUserList();
        return userList;
    }

    @Transactional
    @Override
    public User login(String loginAct, String loginPwd, String ip) throws LoginException {

        Map<String, String> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);

        User user = userDao.login(map);

        if (user == null) {
            throw new LoginException("账号密码错误");
        }

        // 如果程序能够成功的执行到该行，说明账号密码正确
        // 需要继续向下验证其它3项信息

        // 验证失效时间
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if (expireTime.compareTo(currentTime) < 0) {
            throw new LoginException("账号已失效");
        }

        // 判断锁定状态
        String lockState = user.getLockState();
        if ("0".equals(lockState)) {
            throw new LoginException("账号涉嫌违规操作已被锁定，请联系管理员！");
        }

        // 判断ip地址
        String allowIps = user.getAllowIps();
        if (!allowIps.contains(ip)) {
            throw new LoginException("ip地址受限");
        }
        return user;
    }
}
