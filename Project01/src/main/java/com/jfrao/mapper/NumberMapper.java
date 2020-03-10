package com.jfrao.mapper;

import com.jfrao.domain.Queue_Number;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;

public interface NumberMapper extends Mapper<Queue_Number> {
    void addCallTime(@Param(value = "numid") String numid, @Param(value = "date") Date date);

    void overtime(@Param(value = "numid") String numid);

    void addStartTime(@Param(value = "numid") String numid, @Param(value = "date") Date date);
}
