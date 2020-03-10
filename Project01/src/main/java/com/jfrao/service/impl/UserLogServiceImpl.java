package com.jfrao.service.impl;

import com.jfrao.domain.User;
import com.jfrao.domain.User_In_Queue;
import com.jfrao.mapper.UserOperationMapper;
import com.jfrao.service.UserLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class UserLogServiceImpl implements UserLogService {

    @Autowired
    private UserOperationMapper userOperationMapper;

    @Override
    public void LogLogin(User user, String ip, String operation){
        User_In_Queue user_in_queue = GetEntity(user, ip, operation, null);
        userOperationMapper.insertSelective(user_in_queue);
    }

    @Override
    public void LogCreateBusiness(User user, String ip, String seq){
        User_In_Queue user_in_queue = GetEntity(user, ip, "CreateBusiness", seq);
        userOperationMapper.insertSelective(user_in_queue);
    }

    private User_In_Queue GetEntity(User user, String ip, String operation, String seq){
        User_In_Queue user_in_queue = new User_In_Queue();
        user_in_queue.setUsername(user.getUsername());
        user_in_queue.setIp(ip);
        user_in_queue.setOperation(operation);
        user_in_queue.setOperate_time(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        user_in_queue.setTask_seq(seq);
        return user_in_queue;
    }
}
