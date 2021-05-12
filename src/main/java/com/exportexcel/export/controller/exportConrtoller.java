package com.exportexcel.export.controller;

import com.exportexcel.export.server.ExportServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;

@RestController
public class exportConrtoller {
    @Autowired
    private ExportServer server;

    @RequestMapping(value = "export")
    public void export(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        server.export();
    }
}
