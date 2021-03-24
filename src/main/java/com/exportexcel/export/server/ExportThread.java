package com.exportexcel.export.server;

import com.exportexcel.export.exportMapper.PmTenantUserMapper;
import com.exportexcel.utils.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: 12858
 * @date: 2021.03.18
 * @Description:
 * @Version: 1.0
 */
public class ExportThread extends Thread {
    int number = 0;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    private final static Logger log = LoggerFactory.getLogger(ExportChangeCop.class);

    @Autowired(required = false)
    private PmTenantUserMapper pmTenantUserMapper;

    @Override
        public void run() {

    }


}
