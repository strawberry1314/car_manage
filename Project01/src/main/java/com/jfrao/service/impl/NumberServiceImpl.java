package com.jfrao.service.impl;

import com.jfrao.domain.Queue_Number;
import com.jfrao.mapper.NumberMapper;
import com.jfrao.service.NumberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class NumberServiceImpl implements NumberService {

    @Autowired
    private NumberMapper numberMapper;

    @Override
    public void addNumber(Queue_Number number) {
        numberMapper.insert(number);
    }

    @Override
    public void addCallTime(String numid, Date time) {
        numberMapper.addCallTime(numid,time);
    }

    @Override
    public void overtime(String numid) {
        numberMapper.overtime(numid);
    }

    @Override
    public void addStartTime(String numid, Date date) {
        numberMapper.addStartTime(numid,date);
    }

}
