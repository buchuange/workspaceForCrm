package com.dragon.crm.workbench.dao;

import com.dragon.crm.workbench.domain.Clue;

public interface ClueDao {


    int save(Clue clue);

    Clue detail(String id);

    Clue getById(String clueId);

    int delete(String clueId);
}
