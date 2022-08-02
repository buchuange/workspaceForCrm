package com.dragon.crm.workbench.web.controller;

import com.dragon.crm.settings.domain.User;
import com.dragon.crm.settings.service.UserService;
import com.dragon.crm.utils.DateTimeUtil;
import com.dragon.crm.utils.UUIDUtil;
import com.dragon.crm.workbench.domain.Activity;
import com.dragon.crm.workbench.domain.Clue;
import com.dragon.crm.workbench.domain.Tran;
import com.dragon.crm.workbench.service.ActivityService;
import com.dragon.crm.workbench.service.ClueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/clue")
public class ClueController {

    @Resource(name = "clueService")
    private ClueService clueService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "activityService")
    private ActivityService activityService;

    @ResponseBody
    @RequestMapping("/getUserList")
    public List<User> getUserList() {
        List<User> list = userService.getUserList();
        return list;
    }

    @ResponseBody
    @RequestMapping("/save")
    public Map<String, Object> save(HttpSession session, Clue clue) {

        // 执行线索添加操作
        String id = UUIDUtil.getUUID();
        clue.setId(id);

        // 创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        clue.setCreateTime(createTime);
        // 创建人：当前登录用户
        String createBy = ((User) session.getAttribute("user")).getName();
        clue.setCreateBy(createBy);

        boolean flag = clueService.save(clue);

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        return map;
    }

    @RequestMapping("/detail")
    public ModelAndView detail(String id) {
        // 跳转到线索详细信息页

        ModelAndView mv = new ModelAndView();

        Clue clue = clueService.detail(id);

        mv.addObject("clue", clue);

        mv.setViewName("forward:/workbench/clue/detail.jsp");

        return mv;
    }

    @ResponseBody
    @RequestMapping("/getActivityListByClueId")
    public List<Activity> getActivityListByClueId(String clueId) {

        // 根据线索id查询关联的市场活动列表

        List<Activity> list = activityService.getActivityListByClueId(clueId);

        return list;
    }

    @ResponseBody
    @RequestMapping("/unbund")
    public Map<String, Object> unbund(String id) {

        // 执行解除关联操作
        boolean flag = clueService.unbund(id);

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);

        return map;
    }

    @ResponseBody
    @RequestMapping("/getActivityListByNameAndNotByClueId")
    public List<Activity> getActivityListByNameAndNotByClueId(String aname, String clueId) {

        // 查询市场活动列表（根据名称模糊查询+排除掉已经关联指定线索的列表）

        List<Activity> list = activityService.getActivityListByNameAndNotByClueId(aname, clueId);

        return list;
    }

    @ResponseBody
    @RequestMapping("/bund")
    public Map<String, Object> bund(String clueId, String[] activityId) {

        boolean flag = clueService.bund(clueId, activityId);
        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        return map;
    }

    @ResponseBody
    @RequestMapping("/getActivityListByName")
    public List<Activity> getActivityListByName(String aname) {

        // 查询市场活动列表（根据名称模糊查询）

        List<Activity> list = activityService.getActivityListByName(aname);

        return list;
    }

    @RequestMapping("/convert")
    public ModelAndView convert(HttpServletRequest request, Tran t, String clueId, String flag) {
        ModelAndView mv = new ModelAndView();

        User user = (User) request.getSession().getAttribute("user");

        String createBy = user.getName();

        Tran tran = null;
        // 如果需要创建交易
        if ("true".equals(flag)) {

            tran = new Tran();
            tran.setActivityId(t.getActivityId());
            tran.setMoney(t.getMoney());
            tran.setExpectedDate(t.getExpectedDate());
            tran.setName(t.getName());
            tran.setStage(t.getStage());

            String id = UUIDUtil.getUUID();
            tran.setId(id);
            // 创建时间：当前系统时间
            String createTime = DateTimeUtil.getSysTime();
            tran.setCreateTime(createTime);
            // 创建人：当前登录用户
            tran.setCreateBy(createBy);
        }

        /**
         *   为业务层传递的参数
         *   1、必须传递的参数clueId,有了这个clueId之后我们才知道要转换的是哪条记录
         *   2、必须传递的参数tran
         *      因为在线索的转换过程中，有可能会临时创建一笔交易（业务层接收的tran也有可能是个null）
         */
        boolean flag1 = clueService.convert(tran, clueId, createBy);

        if (flag1) {
            mv.setViewName("redirect:/workbench/clue/index.jsp");
        }
        return mv;
    }
}

