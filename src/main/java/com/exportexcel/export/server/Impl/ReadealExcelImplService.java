package com.exportexcel.export.server.Impl;

import com.exportexcel.export.server.ReadealExcelService;
import com.exportexcel.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.openxmlformats.schemas.drawingml.x2006.main.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openxmlformats.schemas.drawingml.x2006.chart.STOrientation.MIN_MAX;
import static org.openxmlformats.schemas.drawingml.x2006.chart.STTickLblPos.NEXT_TO;

/**
 * @author: 12858
 * @date: 2021.04.06
 * @Description: 阅读并处理exce文档（Linda姐）
 * @Version: 1.0
 */
@Slf4j
@Service
public class ReadealExcelImplService implements ReadealExcelService {
    @Override
    public void readeal() {
        XSSFWorkbook workbook = null;
        try {
            File dir = new File("C:\\Users\\12858\\Desktop\\明珞管理");
            String[] names = dir.list();
            for (int j = 0; j < names.length; j++) {
                String template = "C:\\Users\\12858\\Desktop\\明珞管理\\" + names[j];
                workbook = new XSSFWorkbook(new FileInputStream(template));
                int index = workbook.getNumberOfSheets();
                List<Map> value = calculData(workbook, index, names[j]);
                System.out.println(value);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public List<Map> calculData(XSSFWorkbook wb, int index, String template) {
        int num = 0;
        List<Map> list = new ArrayList<>();
        List<String> list3 = new ArrayList();
        List<String> list4 = new ArrayList();
        List<String> list5 = new ArrayList();
        List<Integer> list6 = new ArrayList();
        XSSFSheet sheet = wb.getSheetAt(1);
        // 行数。
        int rowNumbers = sheet.getLastRowNum() + 1;
        // Excel第一行。
        Row temp = sheet.getRow(0);
        // 行读取。
        for (int rows = 2; rows < rowNumbers; rows++) {
            Row row = sheet.getRow(rows);
            if (row == null) continue;
            Map val = new HashMap();
            //列读取。
            for (int column = 3; column < 7; column++) {
                Cell cell = row.getCell(column);
                if (cell == null || ((XSSFCell) cell).getRawValue() == null) continue;
                if(column == 3)list3.add(StringUtils.checkNull(cell));
                if(column == 4)list4.add(StringUtils.checkNull(cell));
                if(column == 5)list5.add(StringUtils.checkNull(cell));
                if(column == 6)list6.add(StringUtils.checkInt(cell));
            }
        }
        //处理五行
        List wuXing = new ArrayList();
        int flagValue = 0;
        String flagKey = null;
        Map map = new HashMap();
        map.put(list3.get(0),flagValue);
        for (int indexs = 0; indexs < rowNumbers-3; indexs++) {
            String val = list3.get(indexs);
            if(!map.containsKey(val) && indexs != 0){
                for(int i = num;i<indexs-1;i++){
                    num = indexs;
                    flagValue+=list6.get(i);
                }
                flagKey = list3.get(indexs-1);
                map.put(flagKey,flagValue);
                flagValue = 0;
                map.put(list3.get(indexs),flagValue);
            }
        }
        wuXing.add(map);
        return wuXing;
    }

    @Override
    public void createRadar() throws IOException {
        final String sheetName = "RadarChart";
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet(sheetName);
            for (int i = 0; i < 16; i++) {
                XSSFRow row = sheet.createRow(i);
                XSSFCell cell = row.createCell(0);
                cell.setCellValue(i + "行");
                XSSFCell cell1 = row.createCell(1);
                cell1.setCellValue(RandomUtils.nextInt(20));
            }
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 2, 0, 7, 16);
            XSSFChart chart = drawing.createChart(anchor);
            CTChart ctChart = chart.getCTChart();
            ctChart.addNewAutoTitleDeleted().setVal(false);
            CTPlotArea ctPlotArea = ctChart.addNewPlotArea();
            ctPlotArea.addNewLayout();
            CTRadarChart ctRadarChart = ctPlotArea.addNewRadarChart();
            // 这两个值应该对应着catAx和valAx
            ctRadarChart.addNewAxId().setVal(123456);
            ctRadarChart.addNewAxId().setVal(123457);
            // 分类标签等是否显示
            CTDLbls ctdLbls = ctRadarChart.addNewDLbls();
            ctdLbls.addNewShowLegendKey().setVal(false);
            ctdLbls.addNewShowVal().setVal(false);
            ctdLbls.addNewShowCatName().setVal(false);
            ctdLbls.addNewShowSerName().setVal(false);
            ctdLbls.addNewShowPercent().setVal(false);
            ctdLbls.addNewShowBubbleSize().setVal(false);
            // 不允许自定义颜色、以及标记的形状
            ctRadarChart.addNewRadarStyle().setVal(STRadarStyle.MARKER);
            ctRadarChart.addNewVaryColors().setVal(false);
            CTRadarSer ctRadarSer = ctRadarChart.addNewSer();
            ctRadarSer.addNewIdx().setVal(0);
            ctRadarSer.addNewOrder().setVal(0);
            CTLineProperties ctLineProperties = ctRadarSer.addNewSpPr().addNewLn();
            ctLineProperties.addNewRound();
            ctLineProperties.addNewSolidFill().addNewSchemeClr().setVal(STSchemeColorVal.ACCENT_1);
            // 渲染数据
            ctRadarSer.addNewCat().addNewStrRef().setF(sheetName + "!$A$1:$A$6");
            ctRadarSer.addNewVal().addNewNumRef().setF(sheetName + "!$B$2:$B$6");
            CTCatAx ctCatAx = ctPlotArea.addNewCatAx();
            ctCatAx.addNewAxId().setVal(123456);
            ctCatAx.addNewScaling().addNewOrientation().setVal(MIN_MAX);
            ctCatAx.addNewDelete().setVal(false);
            ctCatAx.addNewAxPos().setVal(STAxPos.B);
            ctCatAx.addNewCrossAx().setVal(123457);
            ctCatAx.addNewMajorTickMark().setVal(STTickMark.NONE);
            ctCatAx.addNewMinorTickMark().setVal(STTickMark.NONE);
            ctCatAx.addNewTickLblPos().setVal(NEXT_TO);
            CTValAx ctValAx = ctPlotArea.addNewValAx();
            ctValAx.addNewAxId().setVal(123457);
            ctValAx.addNewScaling().addNewOrientation().setVal(MIN_MAX);
            ctValAx.addNewDelete().setVal(false);
            ctValAx.addNewAxPos().setVal(STAxPos.L);
            ctValAx.addNewCrossAx().setVal(123456);
            ctValAx.addNewMajorTickMark().setVal(STTickMark.NONE);
            ctValAx.addNewMinorTickMark().setVal(STTickMark.NONE);
            ctValAx.addNewTickLblPos().setVal(NEXT_TO);
            // 设置网格线
            CTShapeProperties ctShapeProperties = ctValAx.addNewMajorGridlines().addNewSpPr();
            CTLineProperties ctLineProperties1 = ctShapeProperties.addNewLn();
            ctLineProperties1.setW(9525);
            ctLineProperties1.setCap(STLineCap.FLAT);
            ctLineProperties1.setCmpd(STCompoundLine.SNG);
            ctLineProperties1.setAlgn(STPenAlignment.CTR);
            // 值的纵坐标线，不显示
            ctValAx.addNewSpPr().addNewLn().addNewNoFill();
            // 值的纵坐标值，不显示
            ctValAx.addNewTxPr().addNewP().addNewPPr().addNewDefRPr().addNewNoFill();
            System.out.println(ctChart);
            FileOutputStream outputStream = new FileOutputStream("C:\\Users\\12858\\Desktop\\新建 XLSX 工作表.xlsx");
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        }
    }
}
