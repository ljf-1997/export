package com.exportexcel.export.server.Impl;

import com.exportexcel.export.server.ReadealExcelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.poi.xssf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;
import org.openxmlformats.schemas.drawingml.x2006.main.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.openxmlformats.schemas.drawingml.x2006.chart.STOrientation.MIN_MAX;
import static org.openxmlformats.schemas.drawingml.x2006.chart.STTickLblPos.NEXT_TO;

/**
 * @author: 12858
 * @date: 2021.04.06
 * @Description: 阅读并处理exce文档
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
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public List<Map> calculData(XSSFWorkbook workbook, int index, String template) {
        List<Map> list = new ArrayList<>();

        return list;
    }

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
// ctChart.addNewPlotVisOnly().setVal(true);
// ctChart.addNewDispBlanksAs().setVal(STDispBlanksAs.GAP);
// ctChart.addNewShowDLblsOverMax().setVal(false);
            CTPlotArea ctPlotArea = ctChart.addNewPlotArea();
            ctPlotArea.addNewLayout();
// CTShapeProperties ctShapeProperties = ctPlotArea.addNewSpPr();
// ctShapeProperties.addNewNoFill();
// ctShapeProperties.addNewLn().addNewNoFill();
// ctShapeProperties.addNewEffectLst();
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
// ctCatAx.addNewCrosses().setVal(STCrosses.AUTO_ZERO);
// ctCatAx.addNewAuto().setVal(true);
// ctCatAx.addNewLblAlgn().setVal(STLblAlgn.CTR);
// ctCatAx.addNewLblOffset().setVal(100);
// ctCatAx.addNewNoMultiLvlLbl().setVal(false);
// spPr
// CTShapeProperties ctShapeProperties = ctCatAx.addNewSpPr();
// ctShapeProperties.addNewNoFill();
// ctShapeProperties.addNewEffectLst();
// CTLineProperties ctLineProperties1 = ctShapeProperties.addNewLn();
// ctLineProperties1.setW(9525);
// ctLineProperties1.setCap(STLineCap.FLAT);
// ctLineProperties1.setCmpd(STCompoundLine.SNG);
// ctLineProperties1.setAlgn(STPenAlignment.CTR);
// CTSchemeColor ctSchemeColor = ctLineProperties1.addNewSolidFill().addNewSchemeClr();
// ctSchemeColor.setVal(STSchemeColorVal.TX_1);
// ctSchemeColor.addNewLumMod().setVal(15000);
// ctSchemeColor.addNewLumOff().setVal(85000);
// txPr
// CTTextBody ctTextBody = ctCatAx.addNewTxPr();
// CTTextCharacterProperties ctTextCharacterProperties = ctTextBody.addNewP().addNewPPr().addNewDefRPr();
// ctTextCharacterProperties.setU(STTextUnderlineType.NONE);
// ctTextCharacterProperties.setStrike(STTextStrikeType.NO_STRIKE);
// ctTextCharacterProperties.setSz(900);
// ctTextCharacterProperties.setB(false);
// ctTextCharacterProperties.setI(false);
// ctTextCharacterProperties.setBaseline(0);
// ctTextCharacterProperties.setKern(1200);
// CTSchemeColor ctSchemeColor = ctTextCharacterProperties.addNewSolidFill().addNewSchemeClr();
// ctSchemeColor.setVal(STSchemeColorVal.TX_1);
// ctSchemeColor.addNewLumMod().setVal(65000);
// ctSchemeColor.addNewLumOff().setVal(35000);
            CTValAx ctValAx = ctPlotArea.addNewValAx();
            ctValAx.addNewAxId().setVal(123457);
            ctValAx.addNewScaling().addNewOrientation().setVal(MIN_MAX);
            ctValAx.addNewDelete().setVal(false);
            ctValAx.addNewAxPos().setVal(STAxPos.L);
            ctValAx.addNewCrossAx().setVal(123456);
            ctValAx.addNewMajorTickMark().setVal(STTickMark.NONE);
            ctValAx.addNewMinorTickMark().setVal(STTickMark.NONE);
            ctValAx.addNewTickLblPos().setVal(NEXT_TO);
// ctValAx.addNewCrosses().setVal(STCrosses.AUTO_ZERO);
// ctValAx.addNewCrossBetween().setVal(STCrossBetween.BETWEEN);
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
            FileOutputStream outputStream = new FileOutputStream("C:\\Users\\user\\Desktop\\out.xlsx");
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        }
    }
}
