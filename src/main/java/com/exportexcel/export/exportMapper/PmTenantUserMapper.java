package com.exportexcel.export.exportMapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PmTenantUserMapper{
    List<Map<String,String>> selectTaskList(Map map);
}
