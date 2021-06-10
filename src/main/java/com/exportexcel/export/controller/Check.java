package com.exportexcel.export.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Check {
    public static void main(String[] args) {
        try {
            String shpath="/root/check.sh";
            Process ps = Runtime.getRuntime().exec(shpath);
            ps.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String result = sb.toString();
            System.out.println(result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        test2();
    }
    public static void test2() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                Date date = new Date();
                System.out.println(date);
            }
        }, 1000,1000);// 设定指定的时间time,此处为1秒
    }


}
