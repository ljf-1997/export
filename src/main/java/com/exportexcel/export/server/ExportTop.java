package com.exportexcel.export.server;

import com.exportexcel.export.exportMapper.PmTenantUserMapper;
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
public class ExportTop {

    private final static Logger log = LoggerFactory.getLogger(ExportChangeCop.class);

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
        int indexs = 0;
        String flag = null;
        Map<String, List<String>> map = new HashMap();
        List<String> listMap = new ArrayList();
        int j = 1;
        for (Map m : value) {
            if (StringUtils.checkNull(m.get("eqName")).equals(flag)) {
                if (j == value.size()) {
                    listMap.add(StringUtils.checkNull(m.get("dotSum")));
                    List tmp = new ArrayList();
                    for (String tmpValue : listMap) {
                        tmp.add(tmpValue);
                    }
                    map.put(flag, tmp);
                }
                listMap.add(StringUtils.checkNull(m.get("dotSum")));
            } else {
                if (j == value.size()) {
                    List tmp1 = new ArrayList();
                    tmp1.add(StringUtils.checkNull(m.get("dotSum")));
                    map.put(StringUtils.checkNull(m.get("eqName")), tmp1);
                }
                List tmp = new ArrayList();
                for (String tmpValue : listMap) {
                    tmp.add(tmpValue);
                }
                map.put(flag, tmp);
                listMap.clear();
                flag = StringUtils.checkNull(m.get("eqName"));
                listMap.add(StringUtils.checkNull(m.get("dotSum")));
            }
            j++;
        }
        for (String mm : map.keySet()) {
            System.out.println(mm + "------" + map.get(mm));
            Iterator<String> it = map.get(mm).iterator();
            if (it.hasNext()) {
                System.out.println(map.get(mm));
            }
            int i = 1;
            if (value != null) {
                XSSFRow row = sheet.getRow(indexs);
                if (row == null) {
                    row = sheet.createRow(indexs);
                }
                for (String mmm : map.get(mm)) {
                    XSSFCell cell = row.getCell(i);
                    if (cell == null) {
                        cell = row.createCell(i);
                    }
                    cell = row.createCell(0);
                    sheet.getRow(indexs).getCell(0).setCellValue(mm);
                    sheet.getRow(indexs).getCell(i).setCellValue(StringUtils.checkNull(mmm));
                    i++;
                }
                indexs++;
            }
    }
        try {
            FileOutputStream out = new FileOutputStream(new File("C:\\Users\\12858\\Desktop\\top.xlsx"));
            workbook.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
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
            map.put("startTime", "1611849600000");
            map.put("endTime", "1614528000000");
            List<Map<String, String>> list = pmTenantUserMapper.list(map);
            Map<String, String> tmpMap = new HashMap();
            tmpMap.put("dotSum", "0");
            for (Map tmp : list) {
                if (StringUtils.checkInt(tmp.get("dotSum")) > StringUtils.checkInt(tmpMap.get("dotSum"))) {
                    tmpMap.put("dotSum", StringUtils.checkNull(tmp.get("dotSum")));
                    tmpMap.put("eqName", StringUtils.checkNull(tmp.get("eqName")));
                }
                if (StringUtils.checkInt(tmp.get("dotSum")) == 0) {
                    Map mp = new HashMap();
                    mp.put("dotSum", StringUtils.checkInt(tmpMap.get("dotSum")));
                    mp.put("eqName", StringUtils.checkNull(tmpMap.get("eqName")));
                    listMap.add(mp);
                    tmpMap.clear();
                }
            }
        }
        return listMap;
    }

    public static void main(String[] args) {
        List<Map> list = new ArrayList();
        Map<String, String> tmpMap = new HashMap();
        tmpMap.put("dotSum", "0");
        Map<String, String> queryMap = new HashMap();
        queryMap.put("eqName", "FF010");
        queryMap.put("dotSum", "1000");
        list.add(queryMap);
        Map<String, String> queryMap1 = new HashMap();
        queryMap1.put("eqName", "FF010");
        queryMap1.put("dotSum", "2000");
        list.add(queryMap1);
        Map<String, String> queryMap2 = new HashMap();
        queryMap2.put("eqName", "FF020");
        queryMap2.put("dotSum", "3000");
        list.add(queryMap2);
        Map<String, String> queryMap3 = new HashMap();
        queryMap3.put("eqName", "FF020");
        queryMap3.put("dotSum", "4000");
        list.add(queryMap3);
        Map<String, String> queryMap4 = new HashMap();
        queryMap4.put("eqName", "FF020");
        queryMap4.put("dotSum", "5000");
        list.add(queryMap4);
        String flag = null;
        Map map = new HashMap();
        List<String> listMap = new ArrayList();
        int i = 1;
        for (Map va : list) {
            if (StringUtils.checkNull(va.get("eqName")).equals(flag)) {
                if(i==list.size()){
                    listMap.add(StringUtils.checkNull(va.get("dotSum")));
                    List tmp = new ArrayList();
                    for (String tmpValue:listMap) {
                        tmp.add(tmpValue);
                    }
                    map.put(flag,tmp);
                }
                listMap.add(StringUtils.checkNull(va.get("dotSum")));
            } else {
                if(i==list.size()){
                    map.put(StringUtils.checkNull(va.get("eqName")),StringUtils.checkNull(va.get("dotSum")));
                }
                List tmp = new ArrayList();
                for (String tmpValue:listMap) {
                    tmp.add(tmpValue);
                }
                map.put(flag,tmp);
                listMap.clear();
                flag = StringUtils.checkNull(va.get("eqName"));
                listMap.add(StringUtils.checkNull(va.get("dotSum")));
            }
            i++;
        }
        System.out.println(map);
    }
}
