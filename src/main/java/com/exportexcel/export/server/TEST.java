package com.exportexcel.export.server;

import org.springframework.stereotype.Service;

@Service
public class TEST implements Runnable{
//    public void test(){
//        System.out.println("hello World!");
//    }
        private String name;

        public void setName(String name)
        {
            this.name = name;
        }
        public void run()
        {
            System.out.println("hello " + name);
        }
        public static void main(String[] args)
        {
            TEST myThread = new TEST();
            myThread.setName("world");
            Thread thread = new Thread(myThread);
            thread.start();
        }
    }
