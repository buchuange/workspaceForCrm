package com.dragon.crm.workbench.service.impl;

import com.dragon.crm.utils.UUIDUtil;
import com.dragon.crm.workbench.dao.CustomerDao;
import com.dragon.crm.workbench.dao.TranDao;
import com.dragon.crm.workbench.dao.TranHistoryDao;
import com.dragon.crm.workbench.domain.Customer;
import com.dragon.crm.workbench.domain.Tran;
import com.dragon.crm.workbench.domain.TranHistory;
import com.dragon.crm.workbench.service.TranService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("tranService")
public class TranServiceImpl implements TranService {

    @Resource(name = "tranDao")
    private TranDao tranDao;

    @Resource(name = "tranHistoryDao")
    private TranHistoryDao tranHistoryDao;

    @Resource(name = "customerDao")
    private CustomerDao customerDao;

    @Transactional
    @Override
    public boolean save(Tran tran, String customerName) {

        boolean flag = true;

        /*
           交易添加业务：
             在做添加之前，参数tran里面就少了一项信息，就是客户的主键，customerId

             先处理客户相关的需求
             （1）判断customerName, 根据客户名称在客户表进行精确查询
                    如果有这个客户，则取出这个客户的id，封装到tran对象中
                    如果没有这个客户，则在客户表新建一条客户信息，然后将新建的客户id取出，封装到tran对象中

             （2）经过以上操作后，tran对象中的信息就全了，需要执行添加交易的操作

             （3）添加交易完毕后，需要创建一条交易历史
         */

        Customer customer = customerDao.getCustomerByName(customerName);

        // 如果customer为null,需要创建客户
        if (customer == null) {
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(tran.getName());
            customer.setOwner(tran.getOwner());
            customer.setDescription(tran.getDescription());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setContactSummary(tran.getContactSummary());
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(tran.getCreateTime());
            // 添加客户信息
            int count1 = customerDao.save(customer);
            if (count1 != 1) {
                flag = false;
            }
        }

        // 通过以上对于客户的处理，不论是查询出来已有的客户，还是以前没有我们新增的客户，总之客户已经有了，客户的id就有了
        // 将客户id封装到tran对象中
        String customerId = customer.getId();
        tran.setCustomerId(customerId);

        // 添加交易
        int count2 = tranDao.save(tran);
        if (count2 != 1) {
            flag = false;
        }

        // 如果创建了交易，则创建一条该交易下的交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setCreateTime(tran.getCreateTime());
        // 添加交易历史
        int count3 = tranHistoryDao.save(tranHistory);
        if (count3 != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public Tran detail(String id) {
        Tran tran = tranDao.detail(id);
        return tran;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {

        List<TranHistory> tranHistoryList = tranHistoryDao.getHistoryListByTranId(tranId);

        return tranHistoryList;
    }

    @Transactional
    @Override
    public boolean changeStage(TranHistory tranHistory) {

        boolean flag = true;

        // 改变交易阶段
        int count1 = tranDao.changeStage(tranHistory);
        if (count1 != 1) {
            flag = false;
        }

        // 交易阶段改变后，生成一条交易历史
        int count2 = tranHistoryDao.save(tranHistory);
        if (count2 != 1) {
            flag = false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {

        // 取得total
        int total = tranHistoryDao.getTotal();

        // 取得dataList
        List<Map<String, Object>> dataList = tranHistoryDao.getCharts();

        // 将total和dataList保存到map中
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("dataList", dataList);

        // 返回map
        return map;
    }
}
