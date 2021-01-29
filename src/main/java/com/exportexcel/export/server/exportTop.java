package com.exportexcel.export.server;

import com.exportexcel.export.exportMapper.PmTenantUserMapper;
import com.exportexcel.utils.thread.Executer;
import com.exportexcel.utils.thread.Job;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.exportexcel.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class exportTop {

    private final static Logger log = LoggerFactory.getLogger(exportChangeCop.class);

    @Autowired(required = false)
    private PmTenantUserMapper pmTenantUserMapper;

    public void dealAndExportValues() {
        List<Map> value = new ArrayList<>();
        value = getTopValues();
        System.out.println("数据读取成功！");
        XSSFWorkbook workbook = null;
        try {
            String templates = "C:\\Users\\12858\\Desktop\\top.xlsx";
            workbook = new XSSFWorkbook(new FileInputStream(templates));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
        // 生成一个表格
        XSSFSheet sheet = workbook.getSheetAt(0);
        // 遍历集合数据，产生数据行
        int indexs = 1;
        for (Map m : value) {
            int i = 0;
            if (value != null) {
                XSSFRow row = sheet.getRow(indexs);
                if (row == null) {
                    row = sheet.createRow(indexs);
                }
                for (Object entryNew : m.values()) {
                    XSSFCell cell = row.getCell(i);
                    if (cell == null) {
                        cell = row.createCell(i);
                    }
                    cell.setCellValue(entryNew.toString());
                    i++;
                }
                indexs++;
            }
            try {
                FileOutputStream out = new FileOutputStream(new File("C:\\Users\\12858\\Desktop\\top.xlsx"));
                workbook.write(out);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(e);
            }
        }
        System.out.println("导出成功！");
    }

    private List<Map> getTopValues() {
        List<Map> eqIds = pmTenantUserMapper.eqId();
        System.out.println(eqIds);
        List listMap = new ArrayList();
        for (Map eqId : eqIds) {
            Map map = new HashMap();
            map.put("eqId", StringUtils.checkNull(eqId.get("eqId")));
            map.put("startTime", "1609290000000");
            map.put("endTime", "1611882000000");
            List<Map<String, String>> list = pmTenantUserMapper.list(map);
            Map<String, String> tmpMap = new HashMap();
            tmpMap.put("dotSum", "0");
            for (Map tmp : list) {
                if (StringUtils.checkInt(tmp.get("dotSum")) > StringUtils.checkInt(tmpMap.get("dotSum"))) {
                    tmpMap.put("dotSum", StringUtils.checkNull(tmp.get("dotSum")));
                    tmpMap.put("eqName", StringUtils.checkNull(tmp.get("eqName")));
                    tmpMap.put("timestamp", StringUtils.checkNull(tmp.get("timestamp")));
                }
                if (StringUtils.checkInt(tmp.get("dotSum")) == 0) {
                    Map mp = new HashMap();
                    mp.put("dotSum", StringUtils.checkInt(tmpMap.get("dotSum")));
                    mp.put("eqName", StringUtils.checkNull(tmpMap.get("eqName")));
                    mp.put("timestamp", StringUtils.checkNull(tmpMap.get("timestamp")));
                    listMap.add(mp);
                    tmpMap.clear();
                }
            }
        }
        return listMap;
    }
}
