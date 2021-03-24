package com.exportexcel.export.server;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.exportexcel.utils.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ExportChangeCop {

    private final static Logger log = LoggerFactory.getLogger(ExportChangeCop.class);

//    public static void main(String[] args) throws IOException {
//        //读取工作表
//        XSSFWorkbook workbook = null;
//        XSSFWorkbook wb = null;
//        Map<String, Map<String, String>> map = new HashMap();
//        try {
//            int indexs = 1;
//            File dir = new File("C:\\Users\\12858\\Desktop\\20年项目出差");
//            String[] names = dir.list();
//            List<Map> value = new ArrayList<>();
//            for (int j = 0; j < names.length; j++) {
//                System.out.println(names[j]);
//                String template = "C:\\Users\\12858\\Desktop\\20年项目出差\\" + names[j];
//                System.out.println(template);
//                workbook = new XSSFWorkbook(new FileInputStream(template));
//                int index = workbook.getNumberOfSheets();
//                calculateData(value, workbook, index, names[j]);
//            }
//            map = dealValue(value);
//            try {
//                String templates = "C:\\Users\\12858\\Desktop\\1.xlsx";
//                wb = new XSSFWorkbook(new FileInputStream(templates));
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println(e);
//            }
//            XSSFSheet sheet = wb.getSheetAt(0);
//            for (Map.Entry<String, Map<String, String>> entry : map.entrySet()) {
//                int i = 0;
//                XSSFRow row = sheet.getRow(indexs);
//                if (row == null) {
//                    row = sheet.createRow(indexs);
//                }
//                Map<String, String> tmp = entry.getValue();
//                for (Map.Entry<String, String> entryNew : tmp.entrySet()) {
//                    if (entryNew.getKey().equals("number")) {
//                        int k = StringUtils.checkInt(tmp.get("mounth")) + 4;
//                        XSSFCell cell = row.getCell(k);
//                        if (cell == null) {
//                            cell = row.createCell(k);
//                        }
//                        cell.setCellValue(entryNew.getValue());
//                    } else if (entryNew.getKey().equals("mounth")) {
//                        XSSFCell cell = row.getCell(i);
//                    } else {
//                        XSSFCell cell = row.getCell(i);
//                        if (cell == null) {
//                            cell = row.createCell(i);
//                        }
//                        cell.setCellValue(entryNew.getValue());
//                    }
//                    i++;
//                }
//                indexs++;
//            }
//            FileOutputStream out = new FileOutputStream(new File("C:\\Users\\12858\\Desktop\\1.xlsx"));
//            wb.write(out);
//            out.close();
//            System.out.println("导出完成！");
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.out.println(e);
//        } finally {
//            if (workbook != null) {
//                workbook.close();
//            }
//        }
//    }

    private static List<Map> calculateData(List<Map> values, XSSFWorkbook workbook, int index, String fileName) throws IOException {
        for (int i = 0; i < index; i++) {
            XSSFSheet sheet = workbook.getSheetAt(i);
            String sheetName = StringUtils.checkNull(sheet.getSheetName());
            if (sheetName.contains("出勤") || sheetName.contains("人员") || sheetName.contains("外勤") || sheetName.contains("出差"))
                continue;
            // 行数。
            int rowNumbers = sheet.getLastRowNum() + 1;
            // Excel第一行。
            Row temp = sheet.getRow(0);
            if (temp == null) {
                continue;
            }
            int cells = temp.getPhysicalNumberOfCells();
            // 读数据。
            for (int rows = 3; rows < rowNumbers; rows++) {
                Row row = sheet.getRow(rows);
                if (row == null) continue;
                Map val = new HashMap();
                for (int col = 0; col < cells; col++) {
                    Cell cell = row.getCell(col);
                    if (cell == null || ((XSSFCell) cell).getRawValue() == null) continue;
                    if ("".equalsIgnoreCase(StringUtils.checkNull(cell))) {
                        System.out.println("cell为空" + "-----sheet：" + sheetName + "-----行名：" + row + "-----列名：" + col + "-----文件名：" + fileName);
                    }
                    switch (col) {
                        case 1:
                            val.put("name", StringUtils.checkNull(cell));
                            break;
                        case 2:
                            val.put("attendance", StringUtils.checkNull(cell));
                            break;
                        case 3:
                            String[] wbsArray = null;
                            if(cell !=null){
                                wbsArray = StringUtils.checkNull(cell).split("-");
                            }
                            String wbs = null;
                            if(wbsArray.length>=2){
                                wbs = wbsArray[0]+"-"+wbsArray[1];
                                val.put("wbs", wbs);
                            }else {
                                val.put("wbs", StringUtils.checkNull(cell));
                            }
                            break;
                        default:
                            System.out.println("不在目标列中:" + col + ",value为：" + cell);
                    }
                    String[] mounth = fileName.split("年|月");
                    val.put("mounth", mounth[1]);
                }
                if (!val.isEmpty()) {
                    values.add(val);
                }
            }
        }
        workbook.close();
        return values;
    }

    private static Map dealValue(List<Map> value) {
        Map<String, Map<String, String>> map = new IdentityHashMap(1000);
        //处理数据，生成报表
        for (Map tmp : value) {
            String name = StringUtils.checkNull(tmp.get("name"));
            String attendance = StringUtils.checkNull(tmp.get("attendance"));
            String wbs = StringUtils.checkNull(tmp.get("wbs"));
            if ((map.containsKey(name) && map.get(name).get("attendance").equals(attendance) && map.get(name).get("wbs").equals(wbs))) {
                Map<String, String> data = map.get(name);
                String number = StringUtils.checkInt(data.get("number")) + 1 + "";
                data.put("number", number);
                map.put(name, data);
            } else {
                Map<String, String> tmpMap = new TreeMap<>();
                tmpMap.put("name", StringUtils.checkNull(tmp.get("name")));
                tmpMap.put("wbs", StringUtils.checkNull(tmp.get("wbs")));
                tmpMap.put("attendance", StringUtils.checkNull(tmp.get("attendance")));
                tmpMap.put("number", "1");
                tmpMap.put("mounth", StringUtils.checkNull(tmp.get("mounth")));
                map.put(name, tmpMap);
            }
        }
        return map;
    }

    public static void main(String[] args) {
        int i = timerTest();
        System.out.println(i);
    }
    private static int timerTest() {
        AtomicInteger i = new AtomicInteger();
        //线程1 查询数据，休眠x秒，修改i值为99
        Thread t1 = new Thread(() -> {
            try{
                System.out.println(Thread.currentThread().getName() + " 线程启动");
                Thread.sleep(15 * 1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Thread.activeCount()>3) {
                i.set(99);
                System.out.println(Thread.currentThread().getName() + " 线程赋值i==" + i);
            }
        }, "t1");
        t1.start();
        //定时器超过x秒，停止线程1，修改i值为88
        Thread t2 = new Thread(() -> {
            try{
                System.out.println(Thread.currentThread().getName() + " 线程启动");
                Thread.sleep(5 * 1000);
                if (Thread.activeCount()>3) {
                    i.set(88);
                    System.out.println(Thread.currentThread().getName() + " 线程赋值i==" + i);
                    t1.interrupt();
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "t2");
        t2.start();
        //当线程数>2时，主线程等待
        while (Thread.activeCount() > 3) {
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName() + " 最终结果为：" + i);
        return i.get();
    }
}

