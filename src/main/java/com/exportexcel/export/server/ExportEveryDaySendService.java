package com.exportexcel.export.server;

import com.exportexcel.export.exportMapper.PmTenantUserMapper;
import com.exportexcel.utils.StringUtils;
import jdk.internal.dynalink.linker.LinkerServices;
import lombok.SneakyThrows;
import org.apache.ibatis.annotations.Delete;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: 12858
 * @date: 2021.06.01
 * @Description: 统计每天报警推送信息
 * @Version: 1.0
 */
@Service
public class ExportEveryDaySendService {
    @Autowired(required = false)
    private PmTenantUserMapper pmTenantUserMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @SneakyThrows
    public void exportEveryDaySend(HttpServletRequest request, HttpServletResponse respons) {
        Map map = new HashMap();
        map.put("startTime","2021-05-31 5:00:00");
        map.put("endTime","2021-06-01 5:00:00");
        List<Map> getEveryDaySend = pmTenantUserMapper.getEveryDaySend(map);

        String fileName = "每天报警推送信息";
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
        for (Map<String, String> tmp : getEveryDaySend) {
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
                sheet.getRow(index).getCell(k).setCellValue(StringUtils.checkNull(tmp.get(key)));
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
