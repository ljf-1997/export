package com.exportexcel.export.server;

import com.exportexcel.export.exportMapper.PmTenantUserMapper;
import com.exportexcel.utils.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.exportexcel.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  更新报警值
 *
 * @author ljf
 *
 */
@Service
public class ExportExcel {

    @Autowired(required = false)
    private PmTenantUserMapper pmTenantUserMapper;

    public Result importValue() throws IOException {
        Result result = new Result();
        String template = "C:\\Users\\12858\\Desktop\\立即报警(1).xlsx";
        List<Map> excelValues = readExcel(template);
        for (Map tmp:excelValues) {
            Map queryMap = new HashMap();
            queryMap.put("consId",StringUtils.checkInt(tmp.get("consId")));
            queryMap.put("eqId",StringUtils.checkInt(tmp.get("eqId")));
//            queryMap.put("firstThreshold",StringUtils.checkInt(tmp.get("firstThreshold")));
//            queryMap.put("secondThreshold",StringUtils.checkInt(tmp.get("secondThreshold")));
            queryMap.put("finalThreshold",StringUtils.checkInt(tmp.get("finalThreshold")));
            pmTenantUserMapper.updataValue(queryMap);
        }
        result.setData("success!");
        System.out.println("更新成功！");
        return result;
    }
    private static List<Map> readExcel(String filePath) throws IOException {
        List<Map> values = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filePath));
        int index = workbook.getNumberOfSheets();
        for (int i = 0; i < index; i++) {
            XSSFSheet sheet = workbook.getSheetAt(i);
            String sheetName = StringUtils.checkNull(sheet.getSheetName());
            // 行数。
            int rowNumbers = sheet.getLastRowNum() + 1;
            // Excel第一行。
            Row temp = sheet.getRow(0);
            if (temp == null) {
                continue;
            }
            int cells = temp.getPhysicalNumberOfCells();
            // 读数据。
            for (int rows = 1; rows < rowNumbers; rows++) {
                Row row = sheet.getRow(rows);
                if (row == null) continue;
                Map val = new HashMap();
                for (int col = 0; col < cells; col++) {
                    Cell cell = row.getCell(col);
                    if (cell == null || ((XSSFCell) cell).getRawValue() == null) continue;
                    switch (col) {
                        case 0:
                            val.put("consId", StringUtils.checkNull(cell).split("\\.")[0]);
                            break;
                        case 1:
                            val.put("eqId", StringUtils.checkNull(cell).split("\\.")[0]);
                            break;
//                        case 3:
//                            val.put("firstThreshold", StringUtils.checkNull(cell).split("\\.")[0]);
//                            break;
//                        case 4:
//                                val.put("secondThreshold", StringUtils.checkNull(cell).split("\\.")[0]);
//                            break;
                        case 3:
                            val.put("finalThreshold", StringUtils.checkNull(cell).split("\\.")[0]);
                            break;
                        default:
                            System.out.println("不在目标列中:" + col + ",value为：" + cell);
                    }
                }
                if (!val.isEmpty()) {
                    values.add(val);
                }
            }
        }
        workbook.close();
        return values;
    }
}
