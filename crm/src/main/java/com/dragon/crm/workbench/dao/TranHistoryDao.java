package com.dragon.crm.workbench.dao;

import com.dragon.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranHistoryDao {

    int save(TranHistory tranHistory);

    List<TranHistory> getHistoryListByTranId(String tranId);

    int getTotal();

    List<Map<String, Object>> getCharts();
}
