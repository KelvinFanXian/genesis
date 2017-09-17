package com.flame.tx.user.service;

import com.flame.mapper.UserMapper;
import com.flame.model.User;
import com.flame.tx.user.client.UserMoneyClient;
import com.lorne.tx.annotation.TxTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by sungang on 2017/9/16.
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserMoneyClient userMoneyClient;

    public List<User> findAll() {
        return userMapper.selectAll();
    }

    public User findById(Long id) {
        User user = userMapper.selectByPrimaryKey(id);
//        TUserMoney userMoney = userMoneyClient.findByUserId(id);
//        user.setUserMoney(userMoney);
        return user;
    }

    @TxTransaction
    @Transactional
    public int save() {

        User user = new User();
        user.setUserName("Test Tx");
        user.setPassword("11111");
        int rs1 = userMapper.insert(user);
        /**
         * 保存 余额 分布式服务
         */
        int rs2 = userMoneyClient.save();

        /**
         * 抛出异常
         */
        int v = 100 / 0;
        return rs1 + rs2;
    }
}
