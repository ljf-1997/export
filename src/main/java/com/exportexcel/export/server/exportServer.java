package com.exportexcel.export.server;

import com.exportexcel.export.exportMapper.PmTenantUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class exportServer {
    @Autowired(required = false)
    private PmTenantUserMapper pmTenantUserMapper;
    @Scheduled(cron = "30 * * * * ?")
    public void export() {
//        private static String driverLocal = "com.mysql.jdbc.Driver";
//        private static String urlLocal = "jdbc:mysql://misp.server:8066/misp";
//        private static String userLocal = "misp";
//        private static String passwordLocal = "Misp.Mysql2018";
        Map queryMap = new HashMap();
        Calendar yesterdayStart = Calendar.getInstance();
        yesterdayStart.add(Calendar.DATE,-2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        yesterdayStart.set(Calendar.HOUR_OF_DAY, 23);
        yesterdayStart.set(Calendar.MINUTE, 58);
        yesterdayStart.set(Calendar.SECOND, 59);
        System.out.println(sdf.format(yesterdayStart.getTime()));
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.add(Calendar.DATE,-1);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 58);
        todayEnd.set(Calendar.SECOND, 59);
        System.out.println(sdf.format(todayEnd.getTime()));
        queryMap.put("startTime",sdf.format(yesterdayStart.getTime()));
        queryMap.put("endTime",sdf.format(todayEnd.getTime()));
        List<Map> selectTaskList = pmTenantUserMapper.selectTaskList(queryMap);
        for (Map tmp:selectTaskList) {

        }
        System.out.println("hello world");
    }
}
