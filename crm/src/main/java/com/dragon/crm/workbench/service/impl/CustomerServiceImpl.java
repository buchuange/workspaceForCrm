package com.dragon.crm.workbench.service.impl;

import com.dragon.crm.workbench.dao.CustomerDao;
import com.dragon.crm.workbench.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("customerService")
public class CustomerServiceImpl implements CustomerService {

    @Resource(name = "customerDao")
    private CustomerDao customerDao;

    @Override
    public List<String> getCustomerName(String name) {

        List<String> list = customerDao.getCustomerName(name);
        return list;
    }
}
