package com.jfrao.service.impl;

import com.jfrao.domain.User;
import com.jfrao.mapper.UserMapper;
import com.jfrao.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        User user = userMapper.login(username, password);
        return user;
    }
}
