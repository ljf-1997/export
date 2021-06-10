package com.exportexcel.export.server;

import com.exportexcel.export.exportMapper.PmTenantUserMapper;
import lombok.SneakyThrows;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.exportexcel.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * @author: 12858
 * @date: 2021.05.31
 * @Description: 导出区间最多top得到报警值
 * @Version: 1.0
 */
@Service
public class NewImmAlarmService {
    private final static Logger log = LoggerFactory.getLogger(ExportChangeCop.class);

    @Autowired(required = false)
    private PmTenantUserMapper pmTenantUserMapper;

    @SneakyThrows
    public void newImmAlarm(HttpServletRequest request, HttpServletResponse respons) {
        String fileName = "导出区间最多top得到报警值";
        respons.setCharacterEncoding("UTF-8");
        respons.setContentType("application/vnd.ms-excel;charset=UTF-8");
        respons.setHeader("Content-Transfer-Encoding", "binary");
        respons.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        respons.setHeader("Pragma", "public");
        respons.addHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "iso-8859-1") + ".xlsx");
        List<Map> value = getTopValues();

        System.out.println("数据读取成功！");
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
            workbook.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("导出成功！");
    }

    private List<Map> getTopValues() {
        List<Map> eqIds = pmTenantUserMapper.eqId();
        List<Map> listValue = new ArrayList();
        List<Map> data = new ArrayList();
        for (Map eqId : eqIds) {
            List<Map> listMap = new ArrayList();
            Map map = new HashMap();
            map.put("eqId", StringUtils.checkNull(eqId.get("eqId")));
            map.put("startTime", "1610467200000");
            map.put("endTime", "1622217600000");
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
            for (Map tmp : listMap) {
                int flag = 0;
                for (Map tmp1 : listMap) {
                    if (!StringUtils.checkNull(tmp1.get("dotSum")).equals("0") && !StringUtils.checkNull(tmp.get("dotSum")).equals("0")) {
                        if (StringUtils.checkNull(tmp1.get("dotSum")).contains(",") || StringUtils.checkNull(tmp.get("dotSum")).contains(",")) {
                            String[] tmpValue = StringUtils.checkNull(tmp.get("dotSum")).split(",");
                            String[] tmpValue1 = StringUtils.checkNull(tmp1.get("dotSum")).split(",");
                            if (StringUtils.checkInt(tmpValue1[0]) >= StringUtils.checkInt(tmpValue[0])
                                    && StringUtils.checkInt(tmpValue1[0]) < (StringUtils.checkInt(tmpValue[0]) + 50)) {
                                flag++;
                                StringBuilder sb = new StringBuilder();
                                sb.append(StringUtils.checkNull(tmpValue[0]));
                                sb.append(",");
                                sb.append(StringUtils.checkNull(flag));
                                StringUtils.checkNull(tmp.put("dotSum", sb.toString()));
                            }
                        } else {
                            if (StringUtils.checkInt(tmp1.get("dotSum")) >= StringUtils.checkInt(tmp.get("dotSum"))
                                    && StringUtils.checkInt(tmp1.get("dotSum")) < (StringUtils.checkInt(tmp.get("dotSum")) + 50)) {
                                flag++;
                            }
                            StringBuilder sb = new StringBuilder();
                            sb.append(StringUtils.checkNull(tmp.get("dotSum")));
                            sb.append(",");
                            sb.append(StringUtils.checkNull(flag));
                            StringUtils.checkNull(tmp.put("dotSum", sb.toString()));
                        }
                    }
                }
            }
            int inddex = 0;
            String flag = "";
            String dot = "";
            String[] vals = null;
            for (Map val : listMap) {
                if (!StringUtils.checkNull(val.get("dotSum")).equals("0")) {
                    if (inddex == 0 && StringUtils.checkNull(val.get("dotSum")).contains(",")) {
                        vals = StringUtils.checkNull(val.get("dotSum")).split(",");
                        dot = vals[0];
                        flag = vals[1];
                    }
                    if (StringUtils.checkNull(val.get("dotSum")).contains(",")) {
                        String[] value = StringUtils.checkNull(val.get("dotSum")).split(",");
                        if (StringUtils.checkInt(value[1]) >= StringUtils.checkInt(flag)) {
                            listValue.clear();
                            dot = value[0];
                            flag = value[1];
                            Map tmp = new HashMap();
                            tmp.put("eqName", StringUtils.checkNull(val.get("eqName")));
                            tmp.put("dotSum", StringUtils.checkNull(val.get("dotSum")));
                            listValue.add(tmp);
                        } else if (StringUtils.checkInt(value[1]) == StringUtils.checkInt(flag)) {
                            if (StringUtils.checkInt(value[0]) <= StringUtils.checkInt(dot)) {
                                listValue.clear();
                                dot = value[0];
                                flag = value[1];
                                Map tmp = new HashMap();
                                tmp.put("eqName", StringUtils.checkNull(val.get("eqName")));
                                tmp.put("dotSum", StringUtils.checkNull(val.get("dotSum")));
                                listValue.add(tmp);
                            }
                        }
                    }
                    inddex++;
                }
            }
            if (listValue.size() > 0) {
                Map value = new HashMap();
                value.put("eqName", StringUtils.checkNull(listValue.get(0).get("eqName")));
                value.put("dotSum", StringUtils.checkNull(listValue.get(0).get("dotSum")));
                data.add(value);
                listValue.clear();
            }
        }
        return data;
    }
}
