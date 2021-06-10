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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: 12858
 * @date: 2021.03.24
 * @Description:
 * @Version: 1.0
 */
@Service
public class ChangeValueServer {
    @Autowired(required = false)
    private PmTenantUserMapper pmTenantUserMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @SneakyThrows
    public void ChangeValue(HttpServletRequest request, HttpServletResponse respons) {
        Map queryMap = new HashMap();
        queryMap.put("startTime", "2021-5-27 00:00:00");
        queryMap.put("endTime", "2021-6-4 00:00:00");
        List<Map> selectTuisongCopData = pmTenantUserMapper.selectTuisongCopData(queryMap);
        List<Map> list2 = new ArrayList<>();
        List<Map> eqIds = pmTenantUserMapper.eqIds();
        List<String> a = new ArrayList<>();
        List<String> b = new ArrayList<>();
        for (Map tmp:eqIds) {
            a.add(StringUtils.checkNull(tmp.get("eqId")));
        }
        List<String> eqidsss = new ArrayList<>();
        //存在报警点
        for (Map tmp:selectTuisongCopData)  {
            b.add(StringUtils.checkNull(tmp.get("eqId")));
            eqidsss.add(StringUtils.checkNull(tmp.get("eqId")));
        }
        a.removeAll(b);
        list2 = getTopValues(a,StringUtils.checkNull(queryMap.get("startTime")));

        String fileName = "没有推送换帽";
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
        for (Map<String, String> tmp : list2) {
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
        System.out.println("统计成功！");
    }
    @SneakyThrows
    private List<Map> getTopValues(List<String> a,String time) {
        List<Map> listMap = new ArrayList();
        for (String eqId : a) {
            Map map = new HashMap();
            map.put("eqId", eqId);
            map.put("startTime", sdf.parse(time).getTime());
            map.put("endTime", "1622736000000");
            List<Map<String, String>> list = pmTenantUserMapper.list(map);
            Map<String, String> tmpMap = new HashMap();
            tmpMap.put("dotSum", "0");
            for (Map tmp : list) {
                if (StringUtils.checkInt(tmp.get("dotSum")) > StringUtils.checkInt(tmpMap.get("dotSum"))) {
                    tmpMap.put("dotSum", StringUtils.checkNull(tmp.get("dotSum")));
                    tmpMap.put("eqName", StringUtils.checkNull(tmp.get("eqName")));
                    tmpMap.put("time", StringUtils.checkNull(tmp.get("timestamp")));
                }
                if (StringUtils.checkInt(tmp.get("dotSum")) == 0) {
                    Map<String, String> mp = new HashMap();
                    mp.put("dotSum", StringUtils.checkNull(tmpMap.get("dotSum")));
                    mp.put("eqName", StringUtils.checkNull(tmpMap.get("eqName")));
                    mp.put("time", StringUtils.checkNull(tmpMap.get("time")));
                    listMap.add(mp);
                    tmpMap.clear();
                }
            }
        }
        return listMap;
    }
}
