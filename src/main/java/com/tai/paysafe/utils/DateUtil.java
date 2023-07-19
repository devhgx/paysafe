package com.tai.paysafe.utils;

import com.tai.paysafe.errors.exception.ErrorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@Component
public class DateUtil {
    DateUtil(){}
    public static Timestamp getCurrentDate() {
        Timestamp today = null;
        try {
            Date nowDate = Calendar.getInstance().getTime();
            today = new Timestamp(nowDate.getTime());
        } catch (Exception e) {
            log.error("error msg : {} ", e);
            throw new ErrorException(e.getMessage());
        }
        return today;
    }
}