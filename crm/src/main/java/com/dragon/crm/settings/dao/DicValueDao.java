package com.dragon.crm.settings.dao;

import com.dragon.crm.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getListByCode(String code);
}
