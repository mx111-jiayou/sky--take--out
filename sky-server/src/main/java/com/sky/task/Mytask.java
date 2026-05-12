package com.sky.task;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component//开启定时任务功能
public class Mytask {
    @Scheduled(cron = "0/5 * * * *?")
    public void executeTask(){
        log.info("定时任务开始执行",  new Date());
    }
}
