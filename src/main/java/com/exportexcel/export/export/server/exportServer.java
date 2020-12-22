package com.exportexcel.export.export.server;

import com.exportexcel.export.export.exportMapper.PmTenantUserMapper;
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
        Map queryMap = new HashMap();
        Calendar yesterdayStart = Calendar.getInstance();
        yesterdayStart.add(Calendar.DATE,-2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        yesterdayStart.set(Calendar.HOUR, 23);
        yesterdayStart.set(Calendar.MINUTE, 58);
        yesterdayStart.set(Calendar.SECOND, 59);
        System.out.println(sdf.format(yesterdayStart.getTime()));
        List<Map> selectTaskList = pmTenantUserMapper.selectTaskList(queryMap);

        System.out.println("hello world");
    }
}
