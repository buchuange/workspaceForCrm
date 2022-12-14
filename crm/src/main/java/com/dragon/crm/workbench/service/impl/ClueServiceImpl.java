package com.dragon.crm.workbench.service.impl;

import com.dragon.crm.utils.DateTimeUtil;
import com.dragon.crm.utils.UUIDUtil;
import com.dragon.crm.workbench.dao.*;
import com.dragon.crm.workbench.domain.*;
import com.dragon.crm.workbench.service.ClueService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service("clueService")
public class ClueServiceImpl implements ClueService {

    // 线索相关表
    @Resource(name = "clueDao")
    private ClueDao clueDao;
    @Resource(name = "clueRemarkDao")
    private ClueRemarkDao clueRemarkDao;
    @Resource(name = "clueActivityRelationDao")
    private ClueActivityRelationDao clueActivityRelationDao;

    // 客户相关表
    @Resource(name = "customerDao")
    private CustomerDao customerDao;
    @Resource(name = "customerRemarkDao")
    private CustomerRemarkDao customerRemarkDao;

    // 联系人相关表
    @Resource(name = "contactsDao")
    private ContactsDao contactsDao;
    @Resource(name = "contactsRemarkDao")
    private ContactsRemarkDao contactsRemarkDao;
    @Resource(name = "contactsActivityRelationDao")
    private ContactsActivityRelationDao contactsActivityRelationDao;

    // 交易相关表
    @Resource(name = "tranDao")
    private TranDao tranDao;
    @Resource(name = "tranHistoryDao")
    private TranHistoryDao tranHistoryDao;

    @Transactional
    @Override
    public boolean save(Clue clue) {
        boolean flag = true;

        int count = clueDao.save(clue);

        if (count != 1) {
            flag = false;
        }

        return flag;
    }

    @Override
    public Clue detail(String id) {

        Clue clue = clueDao.detail(id);
        
        return clue;
    }

    @Transactional
    @Override
    public boolean unbund(String id) {
        boolean flag = true;

        int count = clueActivityRelationDao.unbund(id);

        if (count != 1) {
            flag = false;
        }
        return flag;
    }

    @Transactional
    @Override
    public boolean bund(String clueId, String[] activityId) {

        boolean flag = true;
        for (String aid : activityId) {

            // 取得每一个aid和clueId做关联
            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(clueId);

            // 添加关联关系表中的记录
            int count = clueActivityRelationDao.bund(car);
            if (count != 1) {
                flag = false;
            }
        }
        return flag;
    }

    @Transactional
    @Override
    public boolean convert(Tran tran, String clueId, String createBy) {

        String createTime = DateTimeUtil.getSysTime();

        boolean flag = true;

        // (1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue clue = clueDao.getById(clueId);

        // (2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户
        //    （根据公司的名称精确匹配，判断该客户是否存在！）

        String company = clue.getCompany();
        Customer customer = customerDao.getCustomerByName(company);
        // 如果customer为null,说明以前没有这个客户，需要新建一个
        if (customer == null) {

            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(clue.getOwner());
            customer.setName(company);
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setCreateTime(createTime);
            customer.setCreateBy(createBy);
            customer.setContactSummary(clue.getContactSummary());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setDescription(clue.getDescription());
            customer.setAddress(clue.getAddress());

            // 添加客户
            int count1 = customerDao.save(customer);
            if (count1 != 1) {
                flag = false;
            }
        }

        /*
           经过第二步处理后，客户的信息我们已经拥有了，将来在处理其他表的时候，如果要使用
           到客户的id 直接使用customer.getId()
         */
        // (3) 通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setDescription(clue.getDescription());
        contacts.setAddress(clue.getAddress());
        // 添加联系人
        int count2 = contactsDao.save(contacts);
        if (count2 != 1) {
            flag = false;
        }

        /*
           经过第三步处理后，联系人的信息我们已经拥有了，将来在处理其他表的时候，如果要使用
           到联系人的id 直接使用customer.getId()
         */

        // (4) 线索备注转换到客户备注以及联系人备注
        // 查询出与该线索关联的备注信息列表
        List<ClueRemark> clueRemarkList = clueRemarkDao.getListByClueId(clueId);
        // 取出每一条线索的备注
        for (ClueRemark clueRemark : clueRemarkList) {

            // 取出备注信息（主要转换到客户备注和联系人备注的就是这个备注信息）
            String noteContent = clueRemark.getNoteContent();

            // 创建客户备注对象，添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setEditFlag("0");
            customerRemark.setCustomerId(customer.getId());
            int count3 = customerRemarkDao.save(customerRemark);
            if (count3 != 1) {
                flag = false;
            }

            // 创建联系人备注对象，添加联系人
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setEditFlag("0");
            contactsRemark.setContactsId(contacts.getId());
            int count4 = contactsRemarkDao.save(contactsRemark);
            if (count4 != 1) {
                flag = false;
            }
        }

        // (5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        // 查询出与该条线索关联的市场活动，查询与市场活动关联的关系列表
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getListByClueId(clueId);
        // 遍历出每一条与市场活动的关联关系记录
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {

            // 从每一天遍历处理的记录中取出关联的市场活动id
            String activityId = clueActivityRelation.getActivityId();

            // 创建 联系人与市场活动的关联关系对象，让第三步生成的联系人与市场活动做关联
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelation.setActivityId(activityId);
            // 添加联系人与市场活动的关联关系
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5 != 1) {
                flag = false;
            }
        }

        // (6) 如果有创建交易需求，创建一条交易
        if (tran != null) {
            /*
               tran对象在controller里面已经封装好的信息如下
                 id,money,name,expectedDate,stage,activityId,createBy,createTime

               接下来可以通过第一步生成的clue对象，取出一些信息，继续完善对tran对象的封装
             */
            tran.setSource(clue.getSource());
            tran.setOwner(clue.getOwner());
            tran.setContactSummary(clue.getContactSummary());
            tran.setNextContactTime(clue.getNextContactTime());
            tran.setDescription(clue.getDescription());
            tran.setContactsId(contacts.getId());
            tran.setCustomerId(customer.getId());
            // 添加交易
            int count6 = tranDao.save(tran);
            if (count6 != 1) {
                flag = false;
            }

            // (7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory = new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setTranId(tran.getId());
            tranHistory.setStage(tran.getStage());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(createTime);
            // 添加交易历史
            int count7 = tranHistoryDao.save(tranHistory);
            if (count7 != 1) {
                flag = false;
            }
        }

        // (8) 删除线索备注 根据外键clueId输出该线索所有的备注
        int count8 = clueRemarkDao.delete(clueId);
        if (count8 != clueRemarkList.size()) {
            flag = false;
        }
        // (9) 删除线索和市场活动的关系
        int count9 = clueActivityRelationDao.delete(clueId);
        if (count9 != clueActivityRelationList.size()) {
            flag = false;
        }

        // (10) 删除线索
        int count10 = clueDao.delete(clueId);
        if (count10 != 1) {
            flag = false;
        }

        return flag;
    }
}
