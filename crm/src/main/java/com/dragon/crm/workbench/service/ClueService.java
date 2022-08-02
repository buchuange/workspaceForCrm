package com.dragon.crm.workbench.service;

import com.dragon.crm.workbench.domain.Clue;
import com.dragon.crm.workbench.domain.Tran;

public interface ClueService {
    boolean save(Clue clue);

    Clue detail(String id);

    boolean unbund(String id);

    boolean bund(String clueId, String[] activityId);

    boolean convert(Tran tran, String clueId, String createBy);
}
