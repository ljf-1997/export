package com.exportexcel.export.server;

import com.exportexcel.export.exportMapper.PmTenantUserMapper;
import com.exportexcel.utils.StringUtils;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author: 12858
 * @date: 2021.03.24
 * @Description: 红旗报警数据（没有推送但换帽）
 * @Version: 1.0
 */
@Service
public class ChangeCupService {
    @Autowired(required = false)
    private PmTenantUserMapper pmTenantUserMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @SneakyThrows
    public void changeCup(HttpServletRequest request, HttpServletResponse respons) {
        Map queryMap = new HashMap();
        //需求时间范围求得推送点
        queryMap.put("startTime", "2021-6-18 00:00:00");
        queryMap.put("endTime", "2021-6-25 00:00:00");
        //当天时间范围拿到换帽点
        queryMap.put("endTImes", "2021-6-25 00:00:00");
        List<Map> selectTuisongCopData = pmTenantUserMapper.selectTuisongCopData(queryMap);
        List<Map<String, String>> list = new LinkedList<>();
        for (Map tmp : selectTuisongCopData) {
            //获取报警点旁的换帽点
            List<Map> getChangeValue = new LinkedList<>();
            getChangeValue = getTopValues(StringUtils.checkNull(tmp.get("eqId")), StringUtils.checkNull(tmp.get("taskTime")), StringUtils.checkNull(queryMap.get("endTImes")));
            if (getChangeValue.size() > 0) {
                Map getTopMap = getChangeValue.get(0);
                Map tmpMap = new HashMap();
                tmpMap.put("time", StringUtils.checkNull(getTopMap.get("time")));
                tmpMap.put("name", StringUtils.checkNull(getTopMap.get("eqName")));
                tmpMap.put("dotSum", StringUtils.checkNull(getTopMap.get("dotSum")));
                list.add(tmpMap);
            }
        }
        //获取所有的换帽点
        List<Map<String, String>> getTopValue = getTopValue(StringUtils.checkNull(queryMap.get("startTime")),StringUtils.checkNull(queryMap.get("endTImes")));
        List<String> listG = new ArrayList<>();
        List<String> getTopValueG = new ArrayList<>();
        //遍历推送换帽点组合成字符串
        for (Map<String,String> value:list) {
            StringBuilder sb = new StringBuilder();
            sb.append(StringUtils.checkNull(value.get("time")));
            sb.append(",");
            sb.append(StringUtils.checkNull(value.get("name")));
            sb.append(",");
            sb.append(StringUtils.checkNull(value.get("dotSum")));
            listG.add(StringUtils.checkNull(sb));
        }
        //遍历所有换帽点组合成字符串
        for (Map<String,String> value:getTopValue) {
            StringBuilder sb1 = new StringBuilder();
            sb1.append(StringUtils.checkNull(value.get("time")));
            sb1.append(",");
            sb1.append(StringUtils.checkNull(value.get("name")));
            sb1.append(",");
            sb1.append(StringUtils.checkNull(value.get("dotSum")));
            getTopValueG.add(StringUtils.checkNull(sb1));
        }
        System.out.println(getTopValueG);
        System.out.println(listG);
        getTopValueG.removeAll(listG);

        String fileName = "无推送有换帽信息";
        respons.setCharacterEncoding("UTF-8");
        respons.setContentType("application/vnd.ms-excel;charset=UTF-8");
        respons.setHeader("Content-Transfer-Encoding", "binary");
        respons.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        respons.setHeader("Pragma", "public");
        respons.addHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "iso-8859-1") + ".xlsx");
        //导出生成工作表
        OutputStream outputStream = null;
        XSSFWorkbook workbook = null;
        workbook = new XSSFWorkbook();
        try {
            outputStream = respons.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet();
        int index = 0;
        for (String tmp : getTopValueG) {
            XSSFRow row = sheet.getRow(index);
            if (row == null) {
                row = sheet.createRow(index);
            }
                XSSFCell cell = row.getCell(0);
                if (cell == null) {
                    cell = row.createCell(0);
                }
                sheet.getRow(index).getCell(0).setCellValue(StringUtils.checkNull(tmp));
            index++;
        }
        try {
            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("统计成功！");
    }

    //获取报警点旁的换帽点
    @SneakyThrows
    private List<Map> getTopValues(String eqId, String time, String endTime) {
        List<Map> listMap = new ArrayList();
        Map map = new HashMap();
        map.put("eqId", eqId);
        map.put("startTime", sdf.parse(time).getTime());
        map.put("endTime", sdf.parse(endTime).getTime());
        List<Map<String, String>> list = pmTenantUserMapper.list(map);
        Map<String, String> tmpMap = new HashMap();
        tmpMap.put("dotSum", "0");
        int indexs = 0;
        for (Map tmp : list) {
            Map mp = new HashMap();
            if (StringUtils.checkInt(tmp.get("dotSum")) > StringUtils.checkInt(tmpMap.get("dotSum"))) {
                tmpMap.put("dotSum", StringUtils.checkNull(tmp.get("dotSum")));
                tmpMap.put("eqName", StringUtils.checkNull(tmp.get("eqName")));
                tmpMap.put("eqId", StringUtils.checkNull(tmp.get("equimentId")));
                tmpMap.put("time", StringUtils.checkNull(tmp.get("timestamp")));
            }
            if (StringUtils.checkInt(tmp.get("dotSum")) == 0) {
                mp.put("dotSum", StringUtils.checkInt(tmpMap.get("dotSum")));
                mp.put("eqName", StringUtils.checkNull(tmpMap.get("eqName")));
                mp.put("time", StringUtils.checkNull(tmpMap.get("time")));
                listMap.add(mp);
                tmpMap.clear();
            }
            if (indexs == (list.size()) && mp == null) {
                listMap.add(tmpMap);
            }
            indexs++;
        }
        int flag = 0;
        int index = 0;
        List<Map> listFinal = new ArrayList<>();
        for (Map tmp : listMap) {
            if (index == 0) {
                flag = StringUtils.checkInt(tmp.get("dotSum"));
            }
            if (StringUtils.checkInt(tmp.get("dotSum")) <= flag) {
                listFinal.add(tmp);
            }
            index++;
        }
        return listFinal;
    }
    //获取所有的换帽点
    @SneakyThrows
    private List<Map<String, String>> getTopValue(String time, String endTime) {
        List<Map> eqIds = pmTenantUserMapper.eqId();
        List listMap = new ArrayList();
        for (Map eqId : eqIds) {
            Map map = new HashMap();
            map.put("eqId", StringUtils.checkNull(eqId.get("eqId")));
            map.put("startTime", sdf.parse(time).getTime());
            map.put("endTime", sdf.parse(endTime).getTime());
            List<Map<String, String>> list = pmTenantUserMapper.list(map);
            Map<String, String> tmpMap = new HashMap();
            tmpMap.put("dotSum", "0");
            for (Map tmp : list) {
                if (StringUtils.checkInt(tmp.get("dotSum")) > StringUtils.checkInt(tmpMap.get("dotSum"))) {
                    tmpMap.put("dotSum", StringUtils.checkNull(tmp.get("dotSum")));
                    tmpMap.put("name", StringUtils.checkNull(tmp.get("eqName")));
                    tmpMap.put("time", StringUtils.checkNull(tmp.get("timestamp")));
                }
                if (StringUtils.checkInt(tmp.get("dotSum")) == 0) {
                    Map mp = new HashMap();
                    mp.put("dotSum", StringUtils.checkInt(tmpMap.get("dotSum")));
                    mp.put("name", StringUtils.checkNull(tmpMap.get("name")));
                    mp.put("time", StringUtils.checkNull(tmpMap.get("time")));
                    listMap.add(mp);
                    tmpMap.clear();
                }
            }
        }
        return listMap;
    }
}
