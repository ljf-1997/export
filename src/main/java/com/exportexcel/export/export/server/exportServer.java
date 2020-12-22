package com.exportexcel.export.export.server;

import com.exportexcel.export.export.exportMapper.PmTenantUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@EnableScheduling
public class exportServer {
    @Autowired
    private PmTenantUserMapper pmTenantUserMapper;
    @Scheduled(cron = "10 23 13 * * ?")
    public void export() {
        Map queryMap = new HashMap();
        List<Map> selectTaskList = pmTenantUserMapper.selectTaskList(queryMap);
        System.out.println("hello world");
    }
}
