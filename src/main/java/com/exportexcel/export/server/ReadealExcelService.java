package com.exportexcel.export.server;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ReadealExcelService {
    void readeal();

    void createRadar() throws IOException;
}
