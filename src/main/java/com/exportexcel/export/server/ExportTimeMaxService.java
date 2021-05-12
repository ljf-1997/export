package com.exportexcel.export.server;

import com.exportexcel.export.exportMapper.PmTenantUserMapper;
import com.exportexcel.utils.StringUtils;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: 12858
 * @date: 2021.05.12
 * @Description: 导出每天时间段内最大打点数
 * @Version: 1.0
 */
@Service
public class ExportTimeMaxService {
    private final static Logger log = LoggerFactory.getLogger(ExportChangeCop.class);

    @Autowired(required = false)
    private PmTenantUserMapper pmTenantUserMapper;

    @SneakyThrows
    public void exportTimeMaxService(HttpServletRequest request, HttpServletResponse respons) throws FileNotFoundException {
        //查询红旗电极帽报警任务推送的信息
        Map queryMap = new HashMap();
        Date startDate = null;
        Date endDate = null;
        int i = 0;
        int j = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startDate = sdf.parse("2021-1-13 08:00:00");
        endDate = sdf.parse("2021-2-11 23:00:00");
        Integer duration = daysBetween(sdf.format(startDate), sdf.format(endDate));
        System.out.println("总天数：" + duration);
        List<Map> timeList = new ArrayList<>();
        for (int k = 0; k < 3 * duration; k++) {
            Map timeMap = new HashMap();
            int flag = 0;
            if (k % 3 == 0) {
                i = 8;
                j = 12;
            } else if (k % 3 == 1) {
                i = 12;
                j = 20;
            } else if (k % 3 == 2) {
                i = 20;
                j = 8;
                flag = 1;
            }
            Calendar startTime = Calendar.getInstance();
            startTime.setTime(startDate);
            startTime.add(Calendar.DATE, 0);
            startTime.set(Calendar.HOUR_OF_DAY, i);
            startTime.set(Calendar.MINUTE, 0);
            startTime.set(Calendar.SECOND, 0);
            Calendar endTime = Calendar.getInstance();
            endTime.setTime(startDate);
            endTime.add(Calendar.DATE, flag);
            endTime.set(Calendar.HOUR_OF_DAY, j);
            endTime.set(Calendar.MINUTE, 0);
            endTime.set(Calendar.SECOND, 0);
            timeMap.put("startTime", sdf.parse(sdf.format(startTime.getTime())).getTime());
            timeMap.put("endTime", sdf.parse(sdf.format(endTime.getTime())).getTime());
            timeList.add(timeMap);
            if (k % 3 == 2) {
                startDate = endTime.getTime();
            }
        }
        List eightList = new ArrayList();
        List twelveList = new ArrayList();
        List twentyList = new ArrayList();
        for (int m = 0; m < timeList.size(); m++) {
            if (m % 3 == 0) {
                eightList.add(timeList.get(m));
            } else if (m % 3 == 1) {
                twelveList.add(timeList.get(m));
            } else if (m % 3 == 2) {
                twentyList.add(timeList.get(m));
            }
        }
//        List eightDataList = getList(eightList);
//        List twelveDataList = getList(twelveList);
        List twentyDataList = getList(twentyList);
        exportvalue(twentyDataList, request, respons);
    }

    @SneakyThrows
    public List<Map> getList(List<Map> list) {
        List<Map> eqIds = pmTenantUserMapper.eqId();
        List<Map> dataList = new ArrayList<>();
        //遍历查询机器人同一时间下的最大打点值
        for (Map eqId : eqIds) {
            //查询
            List<Map> list1 = new ArrayList();
            for (Map time : list) {
                Map timeMap = new HashMap();
                timeMap.put("startTime", time.get("startTime"));
                timeMap.put("endTime", time.get("endTime"));
                timeMap.put("eqId", eqId.get("eqId"));
                //剔除换帽的打点为0的情况
                List<Map> getValFlag = pmTenantUserMapper.getValFlag(timeMap);
                if(getValFlag != null && getValFlag.size()>0)continue;
                List<Map> getList = pmTenantUserMapper.getList(timeMap);
                int size = getList.size();
                for (int i = 0; i < size - 1; i++) {
                    for (int j = 0; j < size - 1 - i; j++) {
                        //交换两数位置
                        if (StringUtils.checkInt(getList.get(j).get("dotSum")) < StringUtils.checkInt(getList.get(j + 1).get("dotSum"))) {
                            Map map1 = getList.get(j);
                            getList.set(j, getList.get(j + 1));
                            getList.set(j + 1, map1);
                        }
                    }
                }
                Map map = new HashMap();
                if (getList.size() != 0 && getList != null){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    map.put("maxDotSum",StringUtils.checkNull(getList.get(0).get("dotSum")));
                    map.put("minDotSum",StringUtils.checkNull(getList.get(getList.size()-1).get("dotSum")));
                    map.put("dotSum",(StringUtils.checkInt(getList.get(0).get("dotSum"))-StringUtils.checkInt(getList.get(getList.size()-1).get("dotSum"))));
                    map.put("time", sdf.format(new Date(StringUtils.checkLong(getList.get(0).get("time")))));
                }
                if(getList.size() == 0){
                    Map maps = new HashMap();
                    maps.put("maxDotSum","0");
                    maps.put("minDotSum","0");
                    maps.put("dotSum","0");
                    list1.add(maps);
                }
                System.out.println(getList);
                if (getList != null) {
                    list1.add(map);
                }
            }
            //拿到机器人在同一时间下最大的打点值
            Map alar = pmTenantUserMapper.getAlar(StringUtils.checkNull(eqId.get("eqId")));
            int size = list1.size();
            for (int i = 0; i < size - 1; i++) {
                for (int j = 0; j < size - 1 - i; j++) {
                    //交换两数位置
                    if (StringUtils.checkInt(list1.get(j).get("dotSum")) < StringUtils.checkInt(list1.get(j + 1).get("dotSum"))) {
                        Map map1 = list1.get(j);
                        list1.set(j, list1.get(j + 1));
                        list1.set(j + 1, map1);
                    }
                }
            }
            Map dotMap = new HashMap();
            System.out.println(list1);
            if (list1 != null && list1.size() > 0 && list1.get(0) != null) {
                for (Map map : list1) {
                    if (alar != null && alar.size() != 0) {
                        if (StringUtils.checkInt(alar.get("alarNum")) > StringUtils.checkInt(map.get("dotSum"))) {
                            dotMap.put("dotSum", StringUtils.checkNull(map.get("dotSum")));
                            dotMap.put("maxDotSum", StringUtils.checkNull(map.get("maxDotSum")));
                            dotMap.put("minDotSum", StringUtils.checkNull(map.get("minDotSum")));
                            dotMap.put("eqName", StringUtils.checkNull(eqId.get("eqName")));
                            dotMap.put("time", StringUtils.checkNull(map.get("time")));
                            break;
                        } else if (StringUtils.checkInt(alar.get("alarNum")) < StringUtils.checkInt(map.get("dotSum"))) {
                            dotMap.put("dotSum", StringUtils.checkNull(alar.get("alarNum")));
                            dotMap.put("maxDotSum", StringUtils.checkNull(map.get("maxDotSum")));
                            dotMap.put("minDotSum", StringUtils.checkNull(map.get("minDotSum")));
                            dotMap.put("eqName", StringUtils.checkNull(eqId.get("eqName")));
                            dotMap.put("time", StringUtils.checkNull(map.get("time")));
                        }
                    } else {
                        dotMap.put("dotSum", StringUtils.checkNull(list1.get(list1.size() - 1).get("dotSum")));
                        dotMap.put("maxDotSum", StringUtils.checkNull(list1.get(list1.size() - 1).get("maxDotSum")));
                        dotMap.put("minDotSum", StringUtils.checkNull(list1.get(list1.size() - 1).get("minDotSum")));
                        dotMap.put("eqName", StringUtils.checkNull(eqId.get("eqName")));
                        dotMap.put("time", StringUtils.checkNull(list1.get(list1.size() - 1).get("time")));
                    }
                }
                dataList.add(dotMap);
            }
        }
        return dataList;
    }

    public static String comparingByName(Map map) {
        return StringUtils.checkNull(map.get("dotSum"));
    }


    /**
     * 字符串的日期格式的计算
     *
     * @param smdate 较小的时间
     * @param bdate  较大的时间
     * @return 相差天数
     * @throws ParseException
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);
        return Integer.parseInt(String.valueOf(between_days));
    }

    @SneakyThrows
    public void exportvalue(List<Map<String, String>> list, HttpServletRequest request, HttpServletResponse respons) {
        String fileName = "打点最大值内容";
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
        XSSFRow rows = sheet.getRow(0);
        if (rows == null) {
            rows = sheet.createRow(0);
        }
        for (Map<String, String> tmp : list) {
            int k = 0;
            for (String key : tmp.keySet()) {
                XSSFCell cell = rows.getCell(k);
                if (cell == null) {
                    cell = rows.createCell(k);
                }
                sheet.getRow(0).getCell(k).setCellValue(key);
                k++;
            }
        }
        int index = 1;
        for (Map<String, String> tmp : list) {
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
                sheet.getRow(0).getCell(k).setCellValue(key);
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
}
