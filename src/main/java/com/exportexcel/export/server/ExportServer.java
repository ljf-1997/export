package com.exportexcel.export.server;

import com.exportexcel.export.exportMapper.PmTenantUserMapper;
import com.exportexcel.utils.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ExportServer {

    private final static Logger log = LoggerFactory.getLogger(ExportChangeCop.class);

    @Autowired(required = false)
    private PmTenantUserMapper pmTenantUserMapper;

    public void export() throws FileNotFoundException {
        //查询红旗电极帽报警任务推送的信息
        Map queryMap = new HashMap();
        Calendar yesterdayStart = Calendar.getInstance();
        Date startDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            startDate = sdf.parse("2021-03-18 00:00:00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        yesterdayStart.setTime(startDate);
        yesterdayStart.add(Calendar.DATE, -2);
        yesterdayStart.set(Calendar.HOUR_OF_DAY, 23);
        yesterdayStart.set(Calendar.MINUTE, 58);
        yesterdayStart.set(Calendar.SECOND, 59);
        System.out.println(sdf.format(yesterdayStart.getTime()));
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.setTime(startDate);
        todayEnd.add(Calendar.DATE, -1);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 58);
        todayEnd.set(Calendar.SECOND, 59);
        System.out.println(sdf.format(todayEnd.getTime()));
        queryMap.put("startTime", sdf.format(yesterdayStart.getTime()));
        queryMap.put("endTime", sdf.format(todayEnd.getTime()));
        List<Map<String, String>> selectTaskList = pmTenantUserMapper.selectTaskList(queryMap);
        if (selectTaskList.size() > 0 && selectTaskList != null) {
            String[] timeArray = {"00:00:00", "00:30:00", "01:00:00", "01:30:00", "02:00:00", "02:30:00", "03:00:00", "03:30:00", "04:00:00", "04:30:00"
                    , "05:00:00", "05:30:00", "06:00:00", "06:30:00", "07:00:00", "07:30:00", "08:00:00", "08:30:00", "09:00:00", "09:30:00", "10:00:00", "10:30:00"
                    , "11:00:00", "11:30:00", "12:00:00", "12:30:00", "13:00:00", "13:30:00", "14:00:00", "14:30:00", "15:00:00", "15:30:00", "16:00:00"
                    , "16:30:00", "17:00:00", "17:30:00", "18:00:00", "18:30:00", "19:00:00", "19:30:00", "20:00:00", "20:30:00", "21:00:00", "21:30:00"
                    , "22:00:00", "22:30:00", "23:00:00", "23:30:00", "23:58:00"};
            String[] nameArray = {"侧围SB010R1GUN1", "侧围SB010R1GUN2", "侧围SB010R2GUN1", "侧围SB010R2GUN2", "侧围SB040R1GUN1", "侧围SB040R1GUN2", "侧围SB040R2GUN1"
                    , "侧围SB050LR1GUN1", "侧围SB050LR1GUN2", "侧围SB050RR1GUN1", "侧围SB050RR1GUN2", "侧围SB070R1GUN1", "侧围SB070R1GUN2", "侧围SB070R2GUN1"
                    , "侧围SB070R2GUN2", "侧围SB070R2GUN3", "侧围SB090R1GUN1", "侧围SB100R1GUN1", "侧围SB100R1GUN2", "侧围SB100R1GUN3", "侧围SB100R2GUN1", "侧围SB100R2GUN2"
                    , "侧围SB100R2GUN3", "侧围SB110LR1GUN1", "侧围SB110RR1GUN1", "地板主焊UB010R1GUN1", "地板主焊UB010R2GUN1", "地板主焊UB010R3GUN1", "地板主焊UB010R4GUN1"
                    , "地板主焊UB020R6GUN1", "地板主焊UB020R7GUN1", "地板主焊UB030R8GUN1", "地板主焊UB030R9GUN1", "地板主焊UB030R9GUN2", "地板主焊UB030R10GUN1", "地板主焊UB030R10GUN2"
                    , "后地板R1GUN1", "后地板R2GUN1", "后地板R2GUN2", "后地板R2GUN3", "后地板R3GUN1", "后地板R3GUN2", "后地板R5GUN1", "后地板R5GUN2", "后地板R6GUN1", "后地板R6GUN2"
                    , "后地板R7GUN1", "后地板R7GUN2", "后地板R8GUN1", "后地板R8GUN2", "后地板R9GUN1", "后地板R9GUN2", "后地板R12GUN1", "后地板R12GUN2", "后地板R13GUN1", "后地板R13GUN2"
                    , "后地板R14GUN1", "前地板010R2GUN1", "前地板010R2GUN2", "前地板010R3GUN1", "前地板010R3GUN2", "前地板050R4GUN1"
                    , "前机舱R1GUN1", "前机舱R1GUN2", "前机舱R2GUN1", "前机舱R2GUN2", "前机舱R3GUN1", "前机舱R4GUN1", "前机舱R6GUN1", "前机舱R7GUN1"
                    , "前机舱R8GUN1", "前机舱R8GUN2", "前机舱R9GUN1", "前机舱R9GUN2", "前机舱R11GUN1", "前机舱R11GUN2", "前机舱R12GUN1", "前机舱R12GUN2"
                    , "主焊MB020R01GUN1", "主焊MB020R01GUN2", "主焊MB020R02GUN1", "主焊MB020R02GUN2", "主焊MB020R03GUN1", "主焊MB020R03GUN2", "主焊MB020R04GUN1", "主焊MB020R04GUN2"
                    , "主焊MB030R24H7GUN1", "主焊MB030R24H7GUN2", "主焊MB030R24HS7GUN1", "主焊MB030R24HS7GUN2", "主焊MB040R05GUN1", "主焊MB040R05GUN2", "主焊MB040R06GUN1", "主焊MB040R06GUN2"
                    , "主焊MB040R07GUN1", "主焊MB040R08GUN1", "主焊MB040R09GUN1", "主焊MB040R10GUN1", "主焊MB050R11GUN1", "主焊MB050R11GUN2", "主焊MB050R12GUN1", "主焊MB050R12GUN2", "主焊MB070R13GUN1"
                    , "主焊MB070R13GUN2", "主焊MB070R14GUN1", "主焊MB070R14GUN2", "主焊MB070R15GUN1", "主焊MB070R15GUN2", "主焊MB070R16GUN1", "主焊MB070R16GUN2", "主焊MB080R17GUN1", "主焊MB080R17GUN2"
                    , "主焊MB080R18GUN1", "主焊MB080R18GUN2", "主焊MB080R19GUN1", "主焊MB080R19GUN2", "主焊MB080R20GUN1", "主焊MB080R20GUN2"};
            //获取打勾的坐标
            List<String> zb = new ArrayList();
            for (Map tmp : selectTaskList) {
                for (int i = 0; i < timeArray.length; i++) {
                    for (int j = 0; j < nameArray.length; j++) {
                        Calendar cal = Calendar.getInstance();
                        Date time = null;
                        try {
                            SimpleDateFormat sdff = new SimpleDateFormat("HH:mm:ss");
                            String[] oldTime = StringUtils.checkNull(tmp.get("taskTime")).split(" ");
                            String[] oldTimeTmp = null;
                            if (oldTime[1].contains("\\.")) {
                                oldTimeTmp = oldTime[1].split("\\.");
                                time = sdff.parse(oldTimeTmp[0]);
                            } else {
                                time = sdff.parse(oldTime[1]);
                            }
                            cal.setTime(time);
                            if (cal.get(Calendar.MINUTE) >= 0 && cal.get(Calendar.MINUTE) <= 15) {
                                cal.set(Calendar.MINUTE, 0);
                                cal.set(Calendar.SECOND, 0);
                            } else if (cal.get(Calendar.MINUTE) > 15 && cal.get(Calendar.MINUTE) < 30) {
                                cal.set(Calendar.MINUTE, 30);
                                cal.set(Calendar.SECOND, 0);
                            } else if (cal.get(Calendar.MINUTE) >= 30 && cal.get(Calendar.MINUTE) <= 45) {
                                cal.set(Calendar.MINUTE, 30);
                                cal.set(Calendar.SECOND, 0);
                            } else if (cal.get(Calendar.MINUTE) > 45 && cal.get(Calendar.MINUTE) <= 59) {
                                cal.add(Calendar.HOUR_OF_DAY, 1);
                                cal.set(Calendar.MINUTE, 0);
                                cal.set(Calendar.SECOND, 0);
                            }
                            String newTime = sdff.format(cal.getTime());
                            String equipmentName = StringUtils.checkNull(tmp.get("objName"));
                            if (newTime.equals(timeArray[i]) && equipmentName.equals(nameArray[j])) {
                                String zuobiao = (j + 5) + "," + (i + 13);
                                zb.add(zuobiao);
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
            //读取工作表
            String template = "./template/红旗电极帽点检数据统计.xlsx";
            XSSFWorkbook workbook = null;
            try {
                FileInputStream inputStream = new FileInputStream(template);
                workbook = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = workbook.getSheet("Sheet2");
                for (String val : zb) {
                    String[] value = val.split(",");
                    sheet.getRow(Integer.valueOf(value[0]).intValue()).getCell(Integer.valueOf(value[1]).intValue()).setCellValue("√");
                }
                FileOutputStream out = new FileOutputStream(new File("C:\\Users\\12858\\Desktop\\点检表\\红旗电极帽点检数据统计1-.xlsx"));
                workbook.write(out);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("导出成功！！！");
        } else {
            System.out.println("红旗今天不生产！！！");
        }
    }
}
