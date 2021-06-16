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
 * @Description: 报警信息打点情况导出
 * @Version: 1.0
 */
@Service
public class ExportDotValueService {
    @Autowired(required = false)
    private PmTenantUserMapper pmTenantUserMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SneakyThrows
    public void exportDotValue(HttpServletRequest request, HttpServletResponse respons) {List<Map<String,String>> timeList = new ArrayList();
        for(int i = 0; i<3;i++){
            switch (i){
                case 0:
                    Map<String,String> tmpMap = getTime(-2,20,8,-1);
                    timeList.add(tmpMap);
                    break;
                case 1:
                    Map<String,String> tmpMap1 = getTime(-1,8,12,-1);
                    timeList.add(tmpMap1);
                    break;
                case 2:
                    Map<String,String> tmpMap2 = getTime(-1,12,20,-1);
                    timeList.add(tmpMap2);
                    break;
            }
        }
        List<Map> eqIds = pmTenantUserMapper.eqId();
        List<List<String>> getDotValues = new ArrayList<>();
        for (Map eqId : eqIds) {
            List dotCount = new ArrayList();
            Map mapTmp = new HashMap();
            List<String> valueList = new ArrayList<>();
            //时间三层循环
            int index = 0;
            valueList.add(StringUtils.checkNull(eqId.get("eqName")));
            valueList.add(StringUtils.checkNull(eqId.get("eqId")));
            for (Map tmp : timeList) {
                Map map = new HashMap();
                map.put("eqId", StringUtils.checkNull(eqId.get("eqId")));
                map.put("startTime", StringUtils.checkNull(tmp.get("startTime")));
                map.put("endTime", StringUtils.checkNull(tmp.get("endTime")));
                Map<String, String> mapValue = pmTenantUserMapper.getDotList(map);
                Map<String, String> mapAlarm = pmTenantUserMapper.getAlarmValue(StringUtils.checkLong(eqId.get("eqId")));
                if(mapAlarm !=null){
                    String fileIds = StringUtils.checkNull(mapAlarm.get("values")).replace("[","").replace("]","");
                    String[] finalValue = fileIds.split(",");
                    mapTmp = new HashMap();
                    if (mapValue != null) {
                        valueList.add(StringUtils.checkNull(mapValue.get("dotcount")));
                        valueList.add(finalValue[index]);
                    }else {
                        valueList.add(StringUtils.checkNull("0"));
                        valueList.add(finalValue[index]);
                    }
                    index++;
                }
            }
            getDotValues.add(valueList);
        }


        String fileName = "换帽打点信息";
        respons.setCharacterEncoding("UTF-8");
        respons.setContentType("application/vnd.ms-excel;charset=UTF-8");
        respons.setHeader("Content-Transfer-Encoding", "binary");
        respons.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        respons.setHeader("Pragma", "public");
        respons.addHeader("Content-Disposition", "attachment; filename=" +new String(fileName.getBytes("utf-8"),"iso-8859-1")+ ".xlsx");
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
        for (List<String> tmp : getDotValues) {
            int k = 0;
            XSSFRow row = sheet.getRow(index);
            if (row == null) {
                row = sheet.createRow(index);
            }
            for (String key : tmp) {
                XSSFCell cell = row.getCell(k);
                if (cell == null) {
                    cell = row.createCell(k);
                }
                sheet.getRow(index).getCell(k).setCellValue(key);
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
    public Map getTime(Integer date,Integer yHour,Integer tHour,Integer aaa){
        Map queryMap = new HashMap();
        Calendar yesterdayStart = Calendar.getInstance();
        Date startDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        yesterdayStart.setTime(startDate);
        yesterdayStart.add(Calendar.DATE, date);
        yesterdayStart.set(Calendar.HOUR_OF_DAY, yHour);
        yesterdayStart.set(Calendar.MINUTE, 00);
        yesterdayStart.set(Calendar.SECOND, 00);
        yesterdayStart.set(Calendar.MILLISECOND, 00);
        Long yesterday=yesterdayStart.getTimeInMillis();
        System.out.println(yesterday);
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTime(startDate);
        todayEnd.add(Calendar.DATE, aaa);
        todayEnd.set(Calendar.HOUR_OF_DAY, tHour);
        todayEnd.set(Calendar.MINUTE, 00);
        todayEnd.set(Calendar.SECOND, 00);
        todayEnd.set(Calendar.MILLISECOND, 00);
        Long today=todayEnd.getTimeInMillis();
        System.out.println(today);
        queryMap.put("startTime", StringUtils.checkNull(yesterday));
        queryMap.put("endTime", StringUtils.checkNull(today));
        System.out.println(queryMap);
        return queryMap;
    }

    @SneakyThrows
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("MM-dd");
        List<Map> timeList = new ArrayList<>();
        String startTime = "2021-6-1 00:00:00";
        String endTime = "2021-6-5 00:00:00";
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
//        System.out.println(timeList);
        List finalValue = new ArrayList();
        for (Map tmp:timeList) {
            Map tmpValueMap = new HashMap();
            String time1 = sdf1.format(new Date(StringUtils.checkLong(tmp.get("startTime"))));
            String time2 = sdf1.format(new Date(StringUtils.checkLong(tmp.get("endTime"))));
            tmpValueMap.put("time",time1+"-->"+time2);
            finalValue.add(tmpValueMap);
        }
        System.out.println(finalValue);
    }
}
