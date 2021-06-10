package com.exportexcel.export.exportMapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PmTenantUserMapper{
    List<Map<String,String>> selectTaskList(Map map);

    List<Map<String,String>> list(Map map);

    Map<String,String> startList(Map map);

    Map<String,String> endList(Map map);

    List<Map> getList(Map map);

    List<Map> getValFlag(Map map);

    Map getAlar(String eqId);

    List<Map> eqId();

    void updataValue(Map map);

    List<Map> selectTuisong1(Map map);

    List<Map> selectTuisong2(Map map);

    List<Map> selectTuisong3(Map map);

    List<Map> selectTuisongData(Map map);

    List<Map> selectNoTuisongCop(Map map);

    List<Map> selectTuisongCop1(Map map);

    List<Map> selectTuisongCop2(Map map);

    List<Map> selectTuisongCop3(Map map);

    List<Map> selectTuisongCopData(Map map);

    List<Map>  eqIds();

    List<Map> selectName(Map map);

    Map<String,String> getDotList(Map map);

    List<Map> getEveryDaySend(Map map);
}
