package com.jfrao.service;


import com.jfrao.domain.User;
import com.jfrao.domain.User_In_Queue;

import java.util.List;

public interface UserLogService {
    void LogLogin(User user, String ip, String Operation);

    void LogCreateBusiness(User user, String i, String seq);

    List<User_In_Queue> GetLog(User user, String time1, String time2);
}
