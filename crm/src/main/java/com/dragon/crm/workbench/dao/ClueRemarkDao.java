package com.dragon.crm.workbench.dao;

import com.dragon.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListByClueId(String clueId);

    int delete(String clueId);
}
