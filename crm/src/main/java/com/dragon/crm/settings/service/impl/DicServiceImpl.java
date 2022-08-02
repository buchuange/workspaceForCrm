package com.dragon.crm.settings.service.impl;

import com.dragon.crm.settings.dao.DicTypeDao;
import com.dragon.crm.settings.dao.DicValueDao;
import com.dragon.crm.settings.domain.DicType;
import com.dragon.crm.settings.domain.DicValue;
import com.dragon.crm.settings.service.DicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("dicService")
public class DicServiceImpl implements DicService {

    @Resource(name = "dicTypeDao")
    private DicTypeDao dicTypeDao;

    @Resource(name = "dicValueDao")
    private DicValueDao dicValueDao;

    @Override
    public Map<String, List<DicValue>> getAll() {

        Map<String, List<DicValue>> map = new HashMap<>();

        // 将字典类型列表取出
        List<DicType> dtList = dicTypeDao.getTypeList();

        // 将字典类型列表遍历
        for (DicType dt : dtList) {

            // 取得每一种类型的字典类型编码
            String code = dt.getCode();

            // 根据每一个字典类型来取得字典值列表
            List<DicValue> dvList = dicValueDao.getListByCode(code);

            map.put(code+"List", dvList);
        }
        return map;
    }

}
