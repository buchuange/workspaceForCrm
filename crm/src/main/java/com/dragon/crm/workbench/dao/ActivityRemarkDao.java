package com.dragon.crm.workbench.dao;

import com.dragon.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    int getCountByIds(String[] ids);

    int deleteByIds(String[] ids);

    List<ActivityRemark> getRemarkListByAId(String activityId);

    int deleteActivityRemark(String id);

    int saveRemark(ActivityRemark activityRemark);

    int updateRemark(ActivityRemark activityRemark);
}
