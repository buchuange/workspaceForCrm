package com.dragon.crm.workbench.service;

import com.dragon.crm.vo.PaginationVO;
import com.dragon.crm.workbench.domain.Activity;
import com.dragon.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    boolean save(Activity activity);

    PaginationVO<Activity> pageList(int pageNum, int pageSize, Activity activity);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark> getRemarkListByAId(String activityId);

    boolean deleteActivityRemark(String id);

    boolean saveRemark(ActivityRemark activityRemark);

    boolean updateRemark(ActivityRemark activityRemark);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(String aname, String clueId);

    List<Activity> getActivityListByName(String aname);
}
