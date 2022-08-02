package com.dragon.crm.workbench.service;

import com.dragon.crm.workbench.domain.Tran;
import com.dragon.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    boolean save(Tran tran, String customerName);

    Tran detail(String id);

    List<TranHistory> getHistoryListByTranId(String tranId);

    boolean changeStage(TranHistory tranHistory);

    Map<String, Object> getCharts();
}
