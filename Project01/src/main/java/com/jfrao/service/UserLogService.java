package com.jfrao.service;


import com.jfrao.domain.User;

public interface UserLogService {
    void LogLogin(User user, String ip, String Operation);

    void LogCreateBusiness(User user, String i, String seq);
}
