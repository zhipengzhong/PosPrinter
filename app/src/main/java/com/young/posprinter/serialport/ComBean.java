package com.young.posprinter.serialport;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author benjaminwan
 *         ????????
 */
public class ComBean {
    public byte[] bRec = null;
    public String sRecTime = "";
    public String sComPort = "";

    public ComBean(String sPort, byte[] buffer, int size) {
        sComPort = sPort;
        bRec = new byte[size];
        System.arraycopy(buffer, 0, bRec, 0, buffer.length);
        SimpleDateFormat sDateFormat = new SimpleDateFormat("hh:mm:ss");
        sRecTime = sDateFormat.format(new Date());
    }
}