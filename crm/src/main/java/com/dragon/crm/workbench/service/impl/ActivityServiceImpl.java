package com.dragon.crm.workbench.service.impl;

import com.dragon.crm.settings.dao.UserDao;
import com.dragon.crm.settings.domain.User;
import com.dragon.crm.vo.PaginationVO;
import com.dragon.crm.workbench.dao.ActivityDao;
import com.dragon.crm.workbench.dao.ActivityRemarkDao;
import com.dragon.crm.workbench.domain.Activity;
import com.dragon.crm.workbench.domain.ActivityRemark;
import com.dragon.crm.workbench.service.ActivityService;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {

    @Resource(name = "activityDao")
    private ActivityDao activityDao;

    @Resource(name = "activityRemarkDao")
    private ActivityRemarkDao activityRemarkDao;

    @Resource(name = "userDao")
    private UserDao userDao;

    @Transactional
    @Override
    public boolean delete(String[] ids) {
        boolean flag = true;

        // 先查询出需要删除的备注的数量
        int count1 = activityRemarkDao.getCountByIds(ids);

        // 删除备注，返回受到影响的行数（实际删除的数量）
        int count2 = activityRemarkDao.deleteByIds(ids);

        if (count1 != count2) {
            flag = false;
        }
        // 删除市场活动
        int count3 = activityDao.delete(ids);

        if (count3 != ids.length) {
            flag = false;
        }

        return flag;
    }


    @Override
    public List<ActivityRemark> getRemarkListByAId(String activityId) {
        List<ActivityRemark> list = activityRemarkDao.getRemarkListByAId(activityId);
        return list;
    }

    @Transactional
    @Override
    public boolean deleteActivityRemark(String id) {

        boolean flag = true;

        int count = activityRemarkDao.deleteActivityRemark(id);

        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public PaginationVO<Activity> pageList(int pageNum, int pageSize, Activity activity) {

        // 取得total
        int total = activityDao.getTotalByCondition(activity);

        // 调用PageHelper的方法，分页
        PageHelper.startPage(pageNum, pageSize);
        // 取得dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(activity);

        // 将total和dataList封装到vo中
        PaginationVO<Activity> vo = new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        // 将vo返回
        return vo;
    }

    @Transactional
    @Override
    public boolean save(Activity activity) {
        boolean flag = true;

        int count = activityDao.save(activity);

        if (count != 1) {
            flag = false;
        }

        return flag;
    }

    @Transactional
    @Override
    public boolean saveRemark(ActivityRemark activityRemark) {
        boolean flag = true;

        int count = activityRemarkDao.saveRemark(activityRemark);

        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Transactional
    @Override
    public boolean updateRemark(ActivityRemark activityRemark) {
        boolean flag = true;

        int count = activityRemarkDao.updateRemark(activityRemark);

        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {

        // 取userList
        List<User> userList = userDao.getUserList();

        // 取activity
        Activity activity = activityDao.getById(id);

        // 将userList和activity打包成map
        Map<String, Object> map = new HashMap<>();
        map.put("userList", userList);
        map.put("activity", activity);

        // 返回map
        return map;
    }

    @Override
    public boolean update(Activity activity) {
        boolean flag = true;

        int count = activityDao.update(activity);

        if (count != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public Activity detail(String id) {

        Activity activity = activityDao.detail(id);

        return activity;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {

        List<Activity> list = activityDao.getActivityListByClueId(clueId);

        return list;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(String aname, String clueId) {
        List<Activity> list = activityDao.getActivityListByNameAndNotByClueId(aname, clueId);
        return list;
    }

    @Override
    public List<Activity> getActivityListByName(String aname) {
        List<Activity> list = activityDao.getActivityListByName(aname);
        return list;
    }
}
