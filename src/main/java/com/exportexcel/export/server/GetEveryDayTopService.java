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

/**
 * @author: 12858
 * @date: 2021.05.24
 * @Description: 导出每天的top值
 * @Version: 1.0
 */
@Service
public class GetEveryDayTopService {
    @Autowired(required = false)
    private PmTenantUserMapper pmTenantUserMapper;

    private SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd");


    @SneakyThrows
    public void getEveryDayTop(HttpServletRequest request, HttpServletResponse response) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Map> timeList = new ArrayList<>();
        List<Map> finalValue = new ArrayList();
        String startTime = "2021-6-1 00:00:00";
        String endTime = "2021-6-3 00:00:00";
        Long startDate = StringUtils.checkLong(sdf.parse(startTime).getTime());
        Long endDate = StringUtils.checkLong(sdf.parse(endTime).getTime());
        Long tmpDate = 0L;
        while (tmpDate<endDate){
            Map queryMap = new HashMap();
            tmpDate = startDate+86400000;
            queryMap.put("startTime", startDate);
            queryMap.put("endTime", tmpDate);
            timeList.add(queryMap);
            startDate = tmpDate;
        }

        for (Map tmp:timeList) {
            List<Map> value = getTopValues(tmp);
            Map tmpValueMap = new HashMap();
            String time1 = sdf1.format(new Date(StringUtils.checkLong(tmp.get("startTime"))));
            String time2 = sdf1.format(new Date(StringUtils.checkLong(tmp.get("endTime"))));
            String time = time1+"-->"+time2;
            tmpValueMap.put("time",StringUtils.checkNull(time));
            tmpValueMap.put("count",StringUtils.checkNull(value.size()));
            finalValue.add(tmpValueMap);
        }
        String fileName = "每天的top值";
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");
        response.addHeader("Content-Disposition", "attachment; filename=" +new String(fileName.getBytes("utf-8"),"iso-8859-1")+ ".xlsx");
        //导出生成工作表
        OutputStream outputStream = null;
        XSSFWorkbook workbook = null;
        workbook = new XSSFWorkbook();
        try {
            outputStream = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 生成一个表格
        XSSFSheet sheet = workbook.createSheet();
        int index = 0;
        for (Map<String, String> tmp : finalValue) {
            int k = 0;
            XSSFRow row = sheet.getRow(index);
            if (row == null) {
                row = sheet.createRow(index);
            }
            for (String key : tmp.keySet()) {
                XSSFCell cell = row.getCell(k);
                if (cell == null) {
                    cell = row.createCell(k);
                }
                sheet.getRow(index).getCell(k).setCellValue(tmp.get(key));
                k++;
            }
            index++;
        }
        try {
            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Map> getTopValues(Map tmpMapo) {
        List<Map> eqIds = pmTenantUserMapper.eqId();
        List listMap = new ArrayList();
        for (Map eqId : eqIds) {
            int flag = 0;
            int flag2 = 0;
            Map map = new HashMap();
            map.put("eqId", StringUtils.checkNull(eqId.get("eqId")));
            map.put("startTime", StringUtils.checkNull(tmpMapo.get("startTime")));
            map.put("endTime", StringUtils.checkNull(tmpMapo.get("endTime")));
            List<Map<String, String>> list = pmTenantUserMapper.list(map);
            if(list.size()>0) {
                Map<String, String> tmpMap = new HashMap();
                tmpMap.put("dotSum", "0");
                int index = 0;
                for (Map tmp : list) {
                    if (StringUtils.checkInt(tmp.get("dotSum")) > StringUtils.checkInt(tmpMap.get("dotSum"))) {
                        flag = 1;
                        tmpMap.put("dotSum", StringUtils.checkNull(tmp.get("dotSum")));
                        tmpMap.put("eqName", StringUtils.checkNull(tmp.get("eqName")));
                    }
                    if (StringUtils.checkInt(tmp.get("dotSum")) == 0) {
                        flag2 = 1;
                        Map mp = new HashMap();
                        mp.put("dotSum", StringUtils.checkInt(tmpMap.get("dotSum")));
                        mp.put("eqName", StringUtils.checkNull(tmpMap.get("eqName")));
                        listMap.add(mp);
                        tmpMap.clear();
                    }
                    if(flag == 1&&index==(list.size()-1)&&flag2!=1){
                        Map mp = new HashMap();
                        mp.put("dotSum", "0");
                        mp.put("eqName", StringUtils.checkNull(eqId.get("eqName")));
                        listMap.add(mp);
                    }
                    index++;
                }
            }else {
                Map mp = new HashMap();
                mp.put("dotSum", "0");
                mp.put("eqName", StringUtils.checkNull(eqId.get("eqName")));
                listMap.add(mp);
            }
        }
        return listMap;
    }
}
