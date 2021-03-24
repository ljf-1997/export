package com.exportexcel.export.server;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {
        public static void main(String[] args) {
            List<String> list = test();
            for (String string : list) {
                System.out.println(string);
            }
        }

        public static List<String> test() {
            List<String> strs = new ArrayList<String>();
            String str1 = "1";
            String str2 = "2";
            String str3 = "3";
            String str4 = "4";
            String str5 = "5";
            String str6 = "6";
            String str7 = "7";
            String str8 = "8";
            String str9 = "9";
            String str10 = "10";
            strs.add(str1);
            strs.add(str2);
            strs.add(str3);
            strs.add(str4);
            strs.add(str5);
            strs.add(str6);
            strs.add(str7);
            strs.add(str8);
            strs.add(str9);
            strs.add(str10);
            int totalNum = strs.size();
            int pageSize = 5;
            int pageIndex =0;
            MyPageinfo paging = new MyPageinfo().subPaging(totalNum, pageSize, pageIndex);
            int fromIndex = paging.getQueryIndex();
            int totalIndex = 0;
            if (fromIndex + paging.getPageIndex() >= totalNum) {
                totalIndex = totalNum;
            } else {
                totalIndex = fromIndex + paging.getPageSize();
            }
            if (fromIndex > totalIndex) {
                return Collections.emptyList();
            }
            return strs.subList(fromIndex, totalIndex);
        }
    }
