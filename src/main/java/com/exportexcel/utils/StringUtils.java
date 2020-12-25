package com.exportexcel.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static Pattern regex = Pattern.compile("[\u4e00-\u9fa5]");

    public static String checkNull(Object inputField) {
        if (inputField == null) return "";
        String tempStr = inputField.toString();
        if (tempStr == null) return "";
        tempStr = tempStr.trim();
        if (tempStr.equalsIgnoreCase("null")) return "";
        return tempStr;
    }

    public static String checkNull(Object inputField, String def) {
        if (inputField == null) return def;
        String tempStr = inputField.toString();
        if (tempStr == null) return def;
        tempStr = tempStr.trim();
        if (tempStr.equals("")) return def;
        if (tempStr.equalsIgnoreCase("null")) return def;
        return tempStr;
    }

    public static boolean isCheckNull(Object inputField) {
        return checkNull(inputField).length() == 0;
    }

    public static Long checkLong(Object inputField) {
        if (inputField == null) return 0L;
        if (inputField instanceof Long) return (Long) inputField;
        String temp = checkNull(inputField);
        if (temp.length() == 0) return null;
        return Long.valueOf(temp);
    }

    public static Long checkLong(Object inputField, Long def) {
        if (inputField == null) return def;
        if (inputField instanceof Long) return (Long) inputField;
        String temp = checkNull(inputField);
        if (temp.length() == 0) return def;
        return Long.valueOf(temp);
    }

    public static Double checkDouble(Object inputField) {
        if (inputField == null) return 0d;
        if (inputField instanceof Double) return (Double) inputField;
        String temp = checkNull(inputField);
        if (temp.length() == 0) return 0d;
        return Double.valueOf(temp);
    }

    public static Double checkDouble(Object inputField, Double def) {
        if (inputField == null) return def;
        if (inputField instanceof Double) return (Double) inputField;
        String temp = checkNull(inputField);
        if (temp.length() == 0) return def;
        return Double.valueOf(temp);
    }

    public static Integer checkInt(Object inputField) {
        if (inputField == null) return 0;
        if (inputField instanceof Integer)
            return (Integer) inputField;
        String temp = checkNull(inputField);
        if (temp.length() == 0) return 0;
        if (temp.contains(".")) {
            temp = temp.substring(0, temp.indexOf("."));
        }
        return Integer.valueOf(temp);
    }

    public static Integer checkInt(Object inputField, Integer def) {
        if (inputField == null) return def;
        if (inputField instanceof Integer) return (Integer) inputField;
        String temp = checkNull(inputField);
        if (temp.length() == 0) return def;
        if (temp.contains(".")) {
            temp = temp.substring(0, temp.indexOf("."));
        }
        return Integer.valueOf(temp);
    }

    public static Boolean checkBoolean(Object inputField) {
        return checkBoolean(inputField, false);
    }

    public static Boolean checkBoolean(Object inputField, boolean def) {
        if (isCheckNull(inputField)) return def;
        String temp = inputField.toString().trim();
        return "true".equalsIgnoreCase(temp);
    }

    /**
     * 返回中英文字符串的字节长度
     *
     * @param str
     * @return
     */
    public static int getLength(String str) {
        try {
            return str.getBytes("UTF-8").length; // 一个中文占3个字节。  
        } catch (UnsupportedEncodingException e) {

        }
        return 0;
    }

    /**
     * 返回中英文字符串的字节长度
     *
     * @param str
     * @return
     */
    public static int getStrLength(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        int len = 0;
        for (int i = 0; i < str.length(); i++) {
            //UTF-8编码格式中文占三个字节，GBK编码格式 中文占两个字节 ;  
            len += (str.charAt(i) > 255 ? 3 : 1);
        }
        return len;
    }

    public static String encode(String str, String ecod) {
        Matcher m = regex.matcher(str);
        StringBuffer b = new StringBuffer();
        try {
            while (m.find()) {
                String key = m.group();
                m.appendReplacement(b, URLEncoder.encode(key, ecod));
            }
        } catch (Exception e) {

        }
        m.appendTail(b);
        return b.toString();
    }


    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(Arrays.asList(array), separator);
    }

    public static String join(Collection collection, String separator) {
        if (collection == null) {
            return null;
        }
        return join(collection.iterator(), separator);
    }

    public static String join(Iterator iterator, String separator) {
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return checkNull(first);
        }

        // two or more elements
        StringBuilder buf = new StringBuilder(256);
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }


    public static String substringBefore(String str, String separator) {
        if (!isCheckNull(str) && separator != null) {
            if (separator.isEmpty()) {
                return "";
            } else {
                int pos = str.indexOf(separator);
                return pos == -1 ? str : str.substring(0, pos);
            }
        } else {
            return str;
        }
    }

    public static String substringAfter(String str, String separator) {
        if (isCheckNull(str)) {
            return str;
        } else if (separator == null) {
            return "";
        } else {
            int pos = str.indexOf(separator);
            return pos == -1 ? "" : str.substring(pos + separator.length());
        }
    }

    public static boolean equalsIgnoreCase(CharSequence cs1, CharSequence cs2) {
        if (cs1 == cs2) {
            return true;
        } else if (cs1 != null && cs2 != null) {
            return cs1.length() != cs2.length() ? false : CharSequenceUtils.regionMatches(cs1, true, 0, cs2, 0, cs1.length());
        } else {
            return false;
        }
    }
}
