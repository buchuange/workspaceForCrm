package com.dragon.crm.workbench.dao;

import com.dragon.crm.workbench.domain.Activity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ActivityDao {
    int save(Activity activity);

    List<Activity> getActivityListByCondition(Activity activity);

    int getTotalByCondition(Activity activity);

    int delete(String[] ids);

    Activity getById(String id);

    int update(Activity activity);

    Activity detail(String id);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(@Param("aname") String aname,
                                                       @Param("clueId") String clueId);

    List<Activity> getActivityListByName(String aname);
}
