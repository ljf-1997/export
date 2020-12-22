package com.exportexcel.export.export.exportMapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface PmTenantUserMapper{
    List<Map> selectTaskList(Map map);
}
