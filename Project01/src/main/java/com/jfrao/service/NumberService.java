package com.jfrao.service;

import com.jfrao.domain.Queue_Number;

import java.util.Date;

public interface NumberService {
    void addNumber(Queue_Number number);

    void addCallTime(String numid, Date date);

    void overtime(String numid);

    void addStartTime(String numid, Date date);
}
