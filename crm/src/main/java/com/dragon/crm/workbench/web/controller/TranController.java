package com.dragon.crm.workbench.web.controller;

import com.dragon.crm.settings.domain.User;
import com.dragon.crm.settings.service.UserService;
import com.dragon.crm.utils.DateTimeUtil;
import com.dragon.crm.utils.UUIDUtil;
import com.dragon.crm.workbench.domain.Tran;
import com.dragon.crm.workbench.domain.TranHistory;
import com.dragon.crm.workbench.service.CustomerService;
import com.dragon.crm.workbench.service.TranService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/transaction")
public class TranController {

    @Resource(name = "tranService")
    private TranService tranService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "customerService")
    private CustomerService customerService;

    @RequestMapping("/add")
    public ModelAndView getUserList() {
        // 进入到跳转到交易添加页的操作
        ModelAndView mv = new ModelAndView();

        // 取得用户信息列表
        List<User> userList = userService.getUserList();

        mv.addObject("userList", userList);

        mv.setViewName("forward:/workbench/transaction/save.jsp");
        return mv;
    }

    @ResponseBody
    @RequestMapping("/getCustomerName")
    public List<String> getCustomerName(String name) {

        // 取得客户名称列表（按照客户名称进行模糊查询）
        List<String> list = customerService.getCustomerName(name);

        return list;
    }

    @RequestMapping("/save")
    public ModelAndView save(HttpSession session, Tran tran, String customerName) {

        ModelAndView mv = new ModelAndView();

        tran.setId(UUIDUtil.getUUID());
        // 创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        tran.setCreateTime(createTime);
        // 创建人：当前登录用户
        String createBy = ((User) session.getAttribute("user")).getName();
        tran.setCreateBy(createBy);

        boolean flag = tranService.save(tran, customerName);

        if (flag) {

            // 如果添加交易成功，跳转到列表页
            mv.setViewName("redirect:/workbench/transaction/index.jsp");
        }
        return mv;
    }

    @RequestMapping("/detail")
    public ModelAndView detail(HttpServletRequest request, String id) {
        ModelAndView mv = new ModelAndView();

        Tran tran = tranService.detail(id);

        // 处理可能性
        /*
          阶段 t
          阶段和可能性之间的对应关系 pMap
         */
        String stage = tran.getStage();
        Map<String, String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");

        String possibility = pMap.get(stage);

        tran.setPossibility(possibility);
        mv.addObject("tran", tran);

        mv.setViewName("forward:/workbench/transaction/detail.jsp");
        return mv;
    }

    @ResponseBody
    @RequestMapping("/getHistoryListByTranId")
    public List<TranHistory> getHistoryListByTranId(HttpServletRequest request, String tranId) {

        // 根据交易id 取得相应的历史列表
        List<TranHistory> tranHistoryList = tranService.getHistoryListByTranId(tranId);

        Map<String, String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");

        // 遍历交易历史列表
        for (TranHistory tranHistory : tranHistoryList) {
            // 取得交易阶段
            String stage = tranHistory.getStage();
            String possibility = pMap.get(stage);
            tranHistory.setPossibility(possibility);
        }
        return tranHistoryList;
    }

    @ResponseBody
    @RequestMapping("/changeStage")
    public Map<String, Object> changeStage(HttpServletRequest request, TranHistory tranHistory) {

        String createBy = ((User) request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setCreateTime(createTime);
        tranHistory.setCreateBy(createBy);

        boolean flag = tranService.changeStage(tranHistory);

        Map<String, String> pMap = (Map<String, String>) request.getServletContext().getAttribute("pMap");

        String possibility = pMap.get(tranHistory.getStage());
        tranHistory.setPossibility(possibility);

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("tran", tranHistory);

        return map;
    }


    @ResponseBody
    @RequestMapping("/getCharts")
    public Map<String, Object> getCharts() {

        // 取得交易阶段数量统计图表的数据

        /*
          业务层为我们返回
            total
            dataList

          通过map 打包以上两项进行返回
         */
        Map<String, Object> map = tranService.getCharts();

        return map;
    }
}
