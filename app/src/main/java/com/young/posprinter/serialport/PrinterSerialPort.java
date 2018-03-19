package com.young.posprinter.serialport;

import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;

import android_serialport_api.SerialPortFinder;

/**
 * Created by Zhipe on 2018/3/19.
 */

public class PrinterSerialPort extends SerialHelper {

    private static SerialPortFinder mSerialPortFinder;
    private static PrinterSerialPort sPrinterSerialPort;
    private static boolean isConnect;

    public PrinterSerialPort() {
    }

    public PrinterSerialPort(String sPort) {
        super(sPort);
    }

    public PrinterSerialPort(String sPort, String sBaudRate) {
        super(sPort, sBaudRate);
    }

//    public static PrinterSerialPort getSerialPortFroAuto() {
//        mSerialPortFinder = new SerialPortFinder();
//        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
//        for (String entryValue : entryValues) {
//            sPrinterSerialPort = new PrinterSerialPort(entryValue, "115200");
//            try {
//                sPrinterSerialPort.open();
//            } catch (Exception e) {
//                sPrinterSerialPort = null;
//            }
//            if (sPrinterSerialPort != null) {
//                sPrinterSerialPort.send(new byte[]{0x10, 0x04, 0x04, 0x10, 0x04, 0x04});
//                SystemClock.sleep(100);
//                if (isConnect) {
//                    isConnect = false;
//                    return sPrinterSerialPort;
//                } else {
//                    sPrinterSerialPort.close();
//                }
//            }
//        }
//        return null;
//    }

    @Override
    protected void onDataReceived(ComBean ComRecData) {
        isConnect = true;
        Log.d("TAG", "onDataReceived: ");
    }
}
