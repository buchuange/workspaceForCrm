package com.dragon.crm.workbench.dao;

import com.dragon.crm.workbench.domain.Tran;
import com.dragon.crm.workbench.domain.TranHistory;

public interface TranDao {

    int save(Tran tran);

    Tran detail(String id);

    int changeStage(TranHistory tranHistory);
}
