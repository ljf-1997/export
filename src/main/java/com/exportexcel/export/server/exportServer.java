package com.exportexcel.export.server;

import com.exportexcel.export.exportMapper.PmTenantUserMapper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.exportexcel.utils.StringUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@EnableScheduling
public class exportServer {
    @Autowired(required = false)
    private PmTenantUserMapper pmTenantUserMapper;

    @Scheduled(cron = "30 * * * * ?")
    public void export() {
        //查询红旗电极帽报警任务推送的信息
        Map queryMap = new HashMap();
        Calendar yesterdayStart = Calendar.getInstance();
        yesterdayStart.add(Calendar.DATE, -2);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        yesterdayStart.set(Calendar.HOUR_OF_DAY, 23);
        yesterdayStart.set(Calendar.MINUTE, 58);
        yesterdayStart.set(Calendar.SECOND, 59);
        System.out.println(sdf.format(yesterdayStart.getTime()));
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.add(Calendar.DATE, -1);
        todayEnd.set(Calendar.HOUR_OF_DAY, 23);
        todayEnd.set(Calendar.MINUTE, 58);
        todayEnd.set(Calendar.SECOND, 59);
        System.out.println(sdf.format(todayEnd.getTime()));
        queryMap.put("startTime", sdf.format(yesterdayStart.getTime()));
        queryMap.put("endTime", sdf.format(todayEnd.getTime()));
        List<Map> selectTaskList = pmTenantUserMapper.selectTaskList(queryMap);
        String template = "./template";
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(new FileInputStream(template));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int t = 0; t < workbook.getNumberOfSheets(); t++) {
            Sheet sheet = workbook.getSheetAt(t);
            Row row = null;
            int lastRowNum = sheet.getLastRowNum();
            //循环读取每行
            for (int i = 0; i <= lastRowNum; i++) {
                row = sheet.getRow(i);
                if (row != null && i == 3) {
                    // 获取每一列的值
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        Cell cell = row.getCell(j);
                        String value = getCellValue(cell);
                        for (Map tmp : selectTaskList) {
                            Calendar cal = Calendar.getInstance();
                            Date time = null;
                            try{
                                time = sdf.parse(StringUtils.checkNull(tmp.get("taskTime")));
                            }catch (Exception e) {

                            }
                            cal.setTime(time);
                        }
                    }
                }

            }
        }
    }

    /***
     * 读取单元格的值
     */
    private String getCellValue(Cell cell) {
        Object result = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    result = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    result = cell.getNumericCellValue();
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    result = cell.getBooleanCellValue();
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    result = cell.getCellFormula();
                    break;
                case Cell.CELL_TYPE_ERROR:
                    result = cell.getErrorCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                default:
                    break;
            }
        }
        return result.toString();
    }
}
