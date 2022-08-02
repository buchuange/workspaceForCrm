package com.dragon.crm.handler;

import com.dragon.crm.exception.LoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(LoginException.class)
    public Map<String, Object> doLoginException(Exception e) {
        // 打印异常日志
        e.printStackTrace();
        // 处理LoginException {"success":false, "msg":?}
        /*
           我们现在作为controller, 需要为ajax请求提供多项信息

           可以由两种手段来处理：
              （1）将多项信息打包成为map, 将map解析为json串
              （2）创建一个vo
                     private boolean success;
                     private String msg;

           如果对于展现的信息将来还会大量的使用，我们创建一个vo类，使用方便
           如果对于展现的信息只有在这个需求中能够使用，我们使用map就可以了
         */

        String msg = e.getMessage();

        Map<String, Object> map = new HashMap<>();
        map.put("success", false);
        map.put("msg", msg);

        return map;
    }
}
