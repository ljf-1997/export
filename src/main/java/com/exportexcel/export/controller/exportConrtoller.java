package com.exportexcel.export.controller;

import com.exportexcel.export.server.exportServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
public class exportConrtoller {
    @Autowired
    private exportServer server;

    @RequestMapping(value = "export")
    public void export() throws FileNotFoundException {
        server.export();
    }
}
