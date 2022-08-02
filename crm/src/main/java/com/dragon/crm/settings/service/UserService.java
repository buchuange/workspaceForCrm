package com.dragon.crm.settings.service;

import com.dragon.crm.exception.LoginException;
import com.dragon.crm.settings.domain.User;

import java.util.List;

public interface UserService {

    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
