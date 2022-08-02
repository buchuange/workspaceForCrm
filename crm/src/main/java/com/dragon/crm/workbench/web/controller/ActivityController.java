package com.dragon.crm.workbench.web.controller;

import com.dragon.crm.settings.domain.User;
import com.dragon.crm.settings.service.UserService;
import com.dragon.crm.utils.DateTimeUtil;
import com.dragon.crm.utils.UUIDUtil;
import com.dragon.crm.vo.PaginationVO;
import com.dragon.crm.workbench.domain.Activity;
import com.dragon.crm.workbench.domain.ActivityRemark;
import com.dragon.crm.workbench.service.ActivityService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/workbench/activity")
public class ActivityController {

    @Resource(name = "activityService")
    private ActivityService activityService;

    @Resource(name = "userService")
    private UserService userService;

    @ResponseBody
    @RequestMapping("/getUserList")
    public List<User> getUserList() {
        // 取得用户信息列表
        List<User> userList = userService.getUserList();
        return userList;
    }

    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Map<String, Object> save(HttpSession session, Activity activity) {
        // 执行市场活动添加操作
        String id = UUIDUtil.getUUID();
        activity.setId(id);
        // 创建时间：当前系统时间
        String createTime = DateTimeUtil.getSysTime();
        activity.setCreateTime(createTime);
        // 创建人：当前登录用户
        String createBy = ((User) session.getAttribute("user")).getName();
        activity.setCreateBy(createBy);

        boolean flag = activityService.save(activity);

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        return map;
    }

    @ResponseBody
    @RequestMapping("/pageList")
    public PaginationVO<Activity> pageList(int pageNum, int pageSize, Activity activity) {
        // 进入到查询市场活动信息列表的操作（结合条件查询+分页查询）

        /*
           前端要：市场活动信息列表
                  查询的总条数

                  业务层拿到了以上两项信息之后，如果做返回
                  map
                  map.put("dataList":dataList)
                  map.put("total": total)
                  map --> json  {"total":100, "dataList":[{市场活动1},{2},{3}]}


                  vo
                  PaginationVO<T>
                     private int total;
                     private List<T> dataList;

                  PaginationVo<Activity> vo = new PaginationVO<>;
                  vo.setTotal(total);
                  vo.setDataList(dataList);
                  vo --> json  {"total":100, "dataList":[{市场活动1},{2},{3}]}

               将来分页查询，每个模块都有，所以我们选择使用一个通用vo，操作起来比较方便
         */
        PaginationVO<Activity> list = activityService.pageList(pageNum, pageSize, activity);

        return list;
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Map<String, Object> delete(HttpServletRequest request) {

        // 执行市场活动的删除操作

        String[] ids = request.getParameterValues("id");
        boolean flag = activityService.delete(ids);
        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        return map;
    }

    @ResponseBody
    @RequestMapping("/getUserListAndActivity")
    public Map<String, Object> getUserListAndActivity(String id) {
        // 进入到查询用户信息列表和根据市场活动id查询单条记录的操作

        Map<String, Object> map = activityService.getUserListAndActivity(id);

        /*
          总结：
             controller调用service的方法，返回值应该是什么
             你得想一想前端要什么，就要从service层取什么

             前端需要的，管业务层去要
             userList
             activity

             以上两项信息，复用率不高，我们选择使用map打包这两项信息即可
             map
         */
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> update(HttpSession session, Activity activity) {
        // 执行市场活动修改操作

        // 修改时间：当前系统时间
        String editTime = DateTimeUtil.getSysTime();
        activity.setEditTime(editTime);
        // 修改人：当前登录用户
        String editBy = ((User) session.getAttribute("user")).getName();
        activity.setEditBy(editBy);

        boolean flag = activityService.update(activity);

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        return map;
    }

    @RequestMapping("/detail")
    public ModelAndView detail(String id) {
        // 进入到跳转到详细信息页的操作
        ModelAndView mv = new ModelAndView();

        Activity activity =  activityService.detail(id);

        mv.addObject("activity", activity);

        mv.setViewName("forward:/workbench/activity/detail.jsp");
        return mv;
    }

    @ResponseBody
    @RequestMapping("/getRemarkListByAId")
    public List<ActivityRemark> getRemarkListByAId(String activityId) {

        // 根据市场活动id, 取得备注信息列表

        List<ActivityRemark> list = activityService.getRemarkListByAId(activityId);

        return list;
    }

    @ResponseBody
    @RequestMapping("/deleteActivityRemark")
    public Map<String, Object> deleteActivityRemark(String id) {

        // 删除备注操作

        boolean flag = activityService.deleteActivityRemark(id);

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        return map;
    }

    @ResponseBody
    @RequestMapping("/saveRemark")
    public Map<String, Object> saveRemark(HttpSession session, String noteContent, String activityId) {
        ActivityRemark activityRemark = new ActivityRemark();

        String uuid = UUIDUtil.getUUID();
        activityRemark.setId(uuid);

        String createTime = DateTimeUtil.getSysTime();
        activityRemark.setCreateTime(createTime);

        String createBy = ((User) session.getAttribute("user")).getName();
        activityRemark.setCreateBy(createBy);

        activityRemark.setNoteContent(noteContent);
        activityRemark.setActivityId(activityId);
        activityRemark.setEditFlag("0");

        boolean flag = activityService.saveRemark(activityRemark);

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("remark", activityRemark);

        return map;
    }

    @ResponseBody
    @RequestMapping("/updateRemark")
    public Map<String, Object> updateRemark(HttpSession session, String noteContent, String id) {

        // 执行修改备注操作

        ActivityRemark activityRemark = new ActivityRemark();

        String editTime = DateTimeUtil.getSysTime();
        activityRemark.setEditTime(editTime);

        String editBy = ((User) session.getAttribute("user")).getName();
        activityRemark.setEditBy(editBy);

        activityRemark.setNoteContent(noteContent);
        activityRemark.setId(id);
        activityRemark.setEditFlag("1");

        boolean flag = activityService.updateRemark(activityRemark);

        Map<String, Object> map = new HashMap<>();
        map.put("success", flag);
        map.put("remark", activityRemark);

        return map;
    }

}