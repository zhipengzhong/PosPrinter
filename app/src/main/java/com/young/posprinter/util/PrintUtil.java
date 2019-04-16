package com.young.posprinter.util;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Zhipe on 2018/2/2.
 */

public class PrintUtil {

    public static byte[] formatPrintContent(String trim) {

        trim = new String(new byte[]{0x1B, 0x33, 0x5}) + trim + new String(new byte[]{0x1B, 0x4A, 0x7F, 0x1B, 0x4A, 0x2F, 0x1D, 0x56, 0x00});
        trim = trim.replaceAll("\\{##\\}", "1001");
        trim = trim.replaceAll("\\{@@\\}", "2");
        trim = trim.replaceAll("\\{\\$\\$1\\}", "VIP业务");
        String s = "";
        String userName = "钟志鹏";
        if (!TextUtils.isEmpty(userName)) {
            String userId = "362321911111111111";
            userId.substring(0, userId.length() - 6);
            s = userName + userId + "******";
        }
        trim = trim.replaceAll("\\{!!\\}", s);

        trim = trim.replaceAll("\\{0,0\\}", new String(new byte[]{0x1D, 0x21, 0x00}));
        trim = trim.replaceAll("\\{0,1\\}", new String(new byte[]{0x1D, 0x21, 0x01}));
        trim = trim.replaceAll("\\{0,2\\}", new String(new byte[]{0x1D, 0x21, 0x02}));
        trim = trim.replaceAll("\\{0,3\\}", new String(new byte[]{0x1D, 0x21, 0x03}));
        trim = trim.replaceAll("\\{0,4\\}", new String(new byte[]{0x1D, 0x21, 0x04}));
        trim = trim.replaceAll("\\{1,0\\}", new String(new byte[]{0x1D, 0x21, 0x10}));
        trim = trim.replaceAll("\\{1,1\\}", new String(new byte[]{0x1D, 0x21, 0x11}));
        trim = trim.replaceAll("\\{1,2\\}", new String(new byte[]{0x1D, 0x21, 0x12}));
        trim = trim.replaceAll("\\{1,3\\}", new String(new byte[]{0x1D, 0x21, 0x13}));
        trim = trim.replaceAll("\\{1,4\\}", new String(new byte[]{0x1D, 0x21, 0x14}));
        trim = trim.replaceAll("\\{2,0\\}", new String(new byte[]{0x1D, 0x21, 0x20}));
        trim = trim.replaceAll("\\{2,1\\}", new String(new byte[]{0x1D, 0x21, 0x21}));
        trim = trim.replaceAll("\\{2,2\\}", new String(new byte[]{0x1D, 0x21, 0x22}));
        trim = trim.replaceAll("\\{2,3\\}", new String(new byte[]{0x1D, 0x21, 0x23}));
        trim = trim.replaceAll("\\{2,4\\}", new String(new byte[]{0x1D, 0x21, 0x24}));
        trim = trim.replaceAll("\\{3,0\\}", new String(new byte[]{0x1D, 0x21, 0x30}));
        trim = trim.replaceAll("\\{3,1\\}", new String(new byte[]{0x1D, 0x21, 0x31}));
        trim = trim.replaceAll("\\{3,2\\}", new String(new byte[]{0x1D, 0x21, 0x32}));
        trim = trim.replaceAll("\\{3,3\\}", new String(new byte[]{0x1D, 0x21, 0x33}));
        trim = trim.replaceAll("\\{3,4\\}", new String(new byte[]{0x1D, 0x21, 0x34}));
        trim = trim.replaceAll("\\{4,0\\}", new String(new byte[]{0x1D, 0x21, 0x40}));
        trim = trim.replaceAll("\\{4,1\\}", new String(new byte[]{0x1D, 0x21, 0x41}));
        trim = trim.replaceAll("\\{4,2\\}", new String(new byte[]{0x1D, 0x21, 0x42}));
        trim = trim.replaceAll("\\{4,3\\}", new String(new byte[]{0x1D, 0x21, 0x43}));
        trim = trim.replaceAll("\\{4,4\\}", new String(new byte[]{0x1D, 0x21, 0x44}));

        trim = trim.replaceAll("\\{0\\}", new String(new byte[]{0x1D, 0x21, 0x00}));
        trim = trim.replaceAll("\\{1\\}", new String(new byte[]{0x1D, 0x21, 0x11}));
        trim = trim.replaceAll("\\{2\\}", new String(new byte[]{0x1D, 0x21, 0x22}));
        trim = trim.replaceAll("\\{3\\}", new String(new byte[]{0x1D, 0x21, 0x33}));
        trim = trim.replaceAll("\\{4\\}", new String(new byte[]{0x1D, 0x21, 0x44}));

        trim = trim.replaceAll("\\{L\\}", new String(new byte[]{0x1B, 0x61, 0x00}));
        trim = trim.replaceAll("\\{M\\}", new String(new byte[]{0x1B, 0x61, 0x01}));
        trim = trim.replaceAll("\\{R\\}", new String(new byte[]{0x1B, 0x61, 0x02}));

        trim = trim.replaceAll("\\{B\\}", new String(new byte[]{0x1B, 0x21, 0x08}));
        trim = trim.replaceAll("\\{N\\}", new String(new byte[]{0x1B, 0x21, 0x00}));

        trim = trim.replaceAll("\\{LOGO\\}", new String(new byte[]{0x1C, 0x70, 0x01, 0x00, 0x0A}));

        trim = trim.replaceAll("\n", new String(new byte[]{0x0A}));
        trim = trim.replaceAll("\r", "");

        Pattern pattern;
        Matcher matcher;

        pattern = Pattern.compile("(?<=\\{H)(.+?)(?=\\})");
        matcher = pattern.matcher(trim);
        while (matcher.find()) {
            String group = matcher.group();
            try {
                int lineHeight = Integer.parseInt(group);
                if (lineHeight >= 0 && lineHeight <= 255)
                    trim = trim.replaceAll("\\{H" + group + "\\}", new String(new byte[]{0x1B, 0x33, (byte) lineHeight}));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        Date date = new Date(System.currentTimeMillis());
        int hours = DateUtil.getHours(date);
        int minutes = DateUtil.getMinutes(date);
        int seconds = DateUtil.getSeconds(date);
        int year = DateUtil.getYear(date);
        int month = DateUtil.getMonth(date);
        int day = DateUtil.getDay(date);
        String week = DateUtil.getWeekString(date);

        pattern = Pattern.compile("(?<=\\{T\\{)(.+?)(?=\\}T\\})");
        matcher = pattern.matcher(trim);
        while (matcher.find()) {
            String group = matcher.group();
            String time = group.replaceAll("YYYY", String.valueOf(year));
            time = time.replaceAll("MM", String.valueOf(month));
            time = time.replaceAll("DD", String.valueOf(day));
            time = time.replaceAll("hh", String.valueOf(hours));
            time = time.replaceAll("mm", String.valueOf(minutes));
            time = time.replaceAll("ss", String.valueOf(seconds));
            time = time.replaceAll("XQ", String.valueOf(week));
            trim = trim.replaceAll("\\{T\\{" + group + "\\}T\\}", time);
        }
        try {
            return trim.getBytes("GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
