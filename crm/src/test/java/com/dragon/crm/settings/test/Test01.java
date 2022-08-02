package com.dragon.crm.settings.test;

import com.dragon.crm.settings.dao.DicTypeDao;
import com.dragon.crm.settings.domain.DicType;
import com.dragon.crm.settings.domain.DicValue;
import com.dragon.crm.settings.service.DicService;
import com.dragon.crm.settings.service.impl.DicServiceImpl;
import com.dragon.crm.utils.*;
import org.junit.Test;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Test01 {

    @Test
    public void test01() {
        // 验证失效时间
        // 失效时间
        String expireTime = "2021-10-10 10:10:10";
        // 当前系统时间
        String currentTime = DateTimeUtil.getSysTime();
        int count = expireTime.compareTo(currentTime);
        System.out.println(count);
    }

    @Test
    public void test02() {
        // 验证锁定状态
        String lockState = "0";
        if ("0".equals(lockState)) {
            System.out.println("账号已锁定");
        }
    }

    @Test
    public void test03() {
        // 验证浏览器端的ip地址是否有效
        // 浏览器端的ip地址
        String ip = "192.168.1.13";
        // 允许访问的ip地址群
        String allowIps = "192.168.1.1,192.168.1.2";
        if (allowIps.contains(ip)) {
            System.out.println("有效的ip地址，允许访问系统");
        } else {
            System.out.println("ip地址受限，请联系管理员");
        }
    }

    @Test
    public void test04() {
        String pwd = "qa2z147";
        pwd = MD5Util.getMD5(pwd);
        System.out.println(pwd);
    }
}
