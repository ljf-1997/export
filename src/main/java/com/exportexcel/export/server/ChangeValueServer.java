package com.exportexcel.export.server;

import com.exportexcel.export.exportMapper.PmTenantUserMapper;
import com.exportexcel.utils.StringUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: 12858
 * @date: 2021.03.24
 * @Description: 转换红旗报警数据
 * @Version: 1.0
 */
@Service
public class ChangeValueServer {
    @Autowired(required = false)
    private PmTenantUserMapper pmTenantUserMapper;

    public void ChangeValue(HttpServletRequest request, HttpServletResponse respons) {
        Map queryMap = new HashMap();
        queryMap.put("startTime", "1613869200000");
        queryMap.put("endTime", "1616461200000");
        //按钉钉推送换帽
        List<Map> selectTuisong1 = pmTenantUserMapper.selectTuisong1(queryMap);
        List<Map> selectTuisong2 = pmTenantUserMapper.selectTuisong2(queryMap);
        List<Map> selectTuisong3 = pmTenantUserMapper.selectTuisong3(queryMap);
        List<Map> selectTuisong = Stream.concat(Stream.concat(selectTuisong2.stream(), selectTuisong3.stream()), selectTuisong1.stream()).collect(Collectors.toList());
        //换帽了没推送
        List<Map> selectNoTuisongCop = pmTenantUserMapper.selectNoTuisongCop(queryMap);
        //推送了没换帽
        List<Map> selectTuisongCop1 = pmTenantUserMapper.selectTuisongCop1(queryMap);
        List<Map> selectTuisongCop2 = pmTenantUserMapper.selectTuisongCop2(queryMap);
        List<Map> selectTuisongCop3 = pmTenantUserMapper.selectTuisongCop3(queryMap);
        List<Map> selectTuisongCop = Stream.concat(Stream.concat(selectTuisongCop2.stream(), selectTuisongCop3.stream()), selectTuisongCop1.stream()).collect(Collectors.toList());
        List<List<String>> selectTuisongList = getValue(selectNoTuisongCop);

        respons.setCharacterEncoding("UTF-8");
        respons.setContentType("application/vnd.ms-excel;charset=UTF-8");
        respons.setHeader("Content-Transfer-Encoding", "binary");
        respons.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        respons.setHeader("Pragma", "public");
        respons.setHeader("Content-Disposition", "attachment;filename=\"" + "报警信息" + ".xlsx\"");
        //导出生成工作表
        OutputStream outputStream = null;
        XSSFWorkbook workbook = null;
        workbook = new XSSFWorkbook();
        try {
            outputStream = respons.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet = workbook.createSheet();
        int index = 0;
        for (List<String> tmp : selectTuisongList) {
            int i = 0;
            XSSFRow row = sheet.getRow(index);
            if (row == null) {
                row = sheet.createRow(index);
            }
            for (String value:tmp) {
                XSSFCell cell = row.getCell(i);
                if (cell == null) {
                    cell = row.createCell(i);
                }
                sheet.getRow(index).getCell(i).setCellValue(value);
                i++;
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

    //获取数据
    public List getValue(List<Map> listMap){
        List list = new ArrayList();
        String flag = "0";
        int index = 0;
        List<String> listValue = new ArrayList();
        for (Map tmp : listMap) {
            if (index == 0) {
                flag = StringUtils.checkNull(tmp.get("id"));
            }
            if (!StringUtils.checkNull(tmp.get("id")).equals(flag)) {
                flag = StringUtils.checkNull(tmp.get("id"));
                List tmpList = new ArrayList();
                for (String tmpListValue:listValue) {
                    tmpList.add(tmpListValue);
                }
                list.add(tmpList);
                listValue.clear();
                listValue.add(StringUtils.checkNull(tmp.get("id")));
                listValue.add(StringUtils.checkNull(tmp.get("eqName")));
                listValue.add(StringUtils.checkNull(tmp.get("time")));
                listValue.add(StringUtils.checkNull(tmp.get("changCop")));
                if (tmp.containsKey("userLow")) {
                    listValue.add(StringUtils.checkNull(tmp.get("userLow")));
                } else if (tmp.containsKey("userHigh")) {
                    listValue.add(StringUtils.checkNull(tmp.get("userHigh")));
                } else if (tmp.containsKey("timeAlar")) {
                    listValue.add(StringUtils.checkNull(tmp.get("timeAlar")));
                }
                listValue.add(StringUtils.checkNull(tmp.get("userName")));
            } else {
                if (listValue.size()==0) {
                    listValue.add(StringUtils.checkNull(tmp.get("id")));
                    listValue.add(StringUtils.checkNull(tmp.get("eqName")));
                    listValue.add(StringUtils.checkNull(tmp.get("time")));
                    listValue.add(StringUtils.checkNull(tmp.get("changCop")));
                    if (tmp.containsKey("userLow")) {
                        listValue.add(StringUtils.checkNull(tmp.get("userLow")));
                    } else if (tmp.containsKey("userHigh")) {
                        listValue.add(StringUtils.checkNull(tmp.get("userHigh")));
                    } else if (tmp.containsKey("timeAlar")) {
                        listValue.add(StringUtils.checkNull(tmp.get("timeAlar")));
                    }
                    listValue.add(StringUtils.checkNull(tmp.get("userName")));
                } else {
                    listValue.add(StringUtils.checkNull(tmp.get("userName")));
                }
            }
            index++;
        }
        return list;
    }
}
