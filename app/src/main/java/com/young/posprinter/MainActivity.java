package com.young.posprinter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.young.posprinter.serialport.PrinterSerialPort;
import com.young.posprinter.util.PicFromPrintUtils;
import com.young.posprinter.util.PrintUtil;

import java.util.ArrayList;
import java.util.List;

import android_serialport_api.SerialPortFinder;

public class MainActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE_UPLOAD_LOGO = 101;
    private static final int SELECT_IMAGE_PRINT_NV = 102;
    private static final int SELECT_IMAGE_PRINT_RASTER = 103;
    private Spinner mPort;
    private EditText mPrintFormat;
    private TextView mUploadLogo;
    private TextView mPrintTest;
    private TextView mPrintNv;
    private TextView mPrintRaster;
    private SerialPortFinder mSerialPortFinder;
    private int mPosition = 0;
    private PrinterSerialPort mPrinterSerialPort;
    private String[] mEntryValues;
    private Spinner mBaudRate;
    private int mBaudRatePosition = 0;
    public static String[] sBaudRateCom = new String[]{"50"
            , "75"
            , "110"
            , "134"
            , "150"
            , "200"
            , "300"
            , "600"
            , "1200"
            , "1800"
            , "2400"
            , "4800"
            , "9600"
            , "19200"
            , "38400"
            , "57600"
            , "115200"
            , "230400"
            , "460800"
            , "500000"
            , "576000"
            , "921600"
            , "1000000"
            , "1152000"
            , "1500000"
            , "2000000"
            , "2500000"
            , "3000000"
            , "3500000"
            , "4000000"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
        connectPrinter(mPosition, 0);
    }

    private void connectPrinter(int position, int baudRatePosition) {
//        if (position == 0) {
//            mPrinterSerialPort = PrinterSerialPort.getSerialPortFroAuto();
//        } else {
        mPrinterSerialPort = new PrinterSerialPort(mEntryValues[position], sBaudRateCom[baudRatePosition]);
        try {
            mPrinterSerialPort.open();
        } catch (Exception e) {
            mPrinterSerialPort = null;
        }
//        }
    }

    private void setListener() {
        mSerialPortFinder = new SerialPortFinder();
        mEntryValues = mSerialPortFinder.getAllDevicesPath();
        List<String> allDevices = new ArrayList<String>();
//        allDevices.add("Auto");
        for (int i = 0; i < mEntryValues.length; i++) {
            allDevices.add(mEntryValues[i]);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, allDevices);
        mPort.setAdapter(adapter);
        mPort.setSelection(mPosition);
        mPort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mPosition = position;
                connectPrinter(mPosition, mBaudRatePosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sBaudRateCom);
        mBaudRate.setAdapter(adapter);
        mBaudRate.setSelection(mBaudRatePosition);
        mBaudRate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mBaudRatePosition = position;
                connectPrinter(mPosition, mBaudRatePosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mPrintTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                byte[] bytes = PrintUtil.formatPrintContent(mPrintFormat.getText().toString());
                if (mPrinterSerialPort != null) {
                    mPrinterSerialPort.send(bytes);
                }
            }
        });

        mUploadLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(MainActivity.this)
                        .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(1)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(6)// 每行显示个数
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                        .previewImage(true)// 是否可预览图片
                        .previewVideo(true)// 是否可预览视频
                        .enablePreviewAudio(true) // 是否可播放音频
                        .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                        .isCamera(true)// 是否显示拍照按钮
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .enableCrop(true)// 是否裁剪
                        .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)
                        .withAspectRatio(0, 0)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                        .isGif(false)// 是否显示gif图片
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .forResult(SELECT_IMAGE_UPLOAD_LOGO);//结果回调onActivityResult code
            }

        });

        mPrintNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(MainActivity.this)
                        .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(1)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(6)// 每行显示个数
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                        .previewImage(true)// 是否可预览图片
                        .previewVideo(true)// 是否可预览视频
                        .enablePreviewAudio(true) // 是否可播放音频
                        .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                        .isCamera(true)// 是否显示拍照按钮
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .enableCrop(true)// 是否裁剪
                        .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)
                        .withAspectRatio(0, 0)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                        .isGif(false)// 是否显示gif图片
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .forResult(SELECT_IMAGE_PRINT_NV);//结果回调onActivityResult code
            }
        });

        mPrintRaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(MainActivity.this)
                        .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                        .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                        .maxSelectNum(1)// 最大图片选择数量
                        .minSelectNum(1)// 最小选择数量
                        .imageSpanCount(6)// 每行显示个数
                        .selectionMode(PictureConfig.SINGLE)// 多选 or 单选
                        .previewImage(true)// 是否可预览图片
                        .previewVideo(true)// 是否可预览视频
                        .enablePreviewAudio(true) // 是否可播放音频
                        .compressGrade(Luban.THIRD_GEAR)// luban压缩档次，默认3档 Luban.FIRST_GEAR、Luban.CUSTOM_GEAR
                        .isCamera(true)// 是否显示拍照按钮
                        .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                        .enableCrop(true)// 是否裁剪
                        .compressMode(PictureConfig.SYSTEM_COMPRESS_MODE)
                        .withAspectRatio(0, 0)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                        .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
                        .isGif(false)// 是否显示gif图片
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                        .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
                        .forResult(SELECT_IMAGE_PRINT_RASTER);//结果回调onActivityResult code
            }
        });
    }

    private void initView() {
        mPort = findViewById(R.id.port);
        mPrintFormat = findViewById(R.id.print_format);
        mUploadLogo = findViewById(R.id.upload_logo);
        mPrintTest = findViewById(R.id.print_test);
        mPrintNv = findViewById(R.id.print_nv);
        mPrintRaster = findViewById(R.id.print_raster);
        mBaudRate = findViewById(R.id.baud_rate);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            List<LocalMedia> localMedias;
            LocalMedia localMedia;
            switch (requestCode) {
                case SELECT_IMAGE_UPLOAD_LOGO:
                    try {
                        localMedias = PictureSelector.obtainMultipleResult(data);
                        localMedia = localMedias.get(0);
                        Bitmap bitmap = BitmapFactory.decodeFile(localMedia.getCutPath());

                        if (mPrinterSerialPort != null) {
                            mPrinterSerialPort.send(PicFromPrintUtils.uploadLogo(bitmap));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SELECT_IMAGE_PRINT_NV:
                    try {
                        localMedias = PictureSelector.obtainMultipleResult(data);
                        localMedia = localMedias.get(0);
                        Bitmap bitmap = BitmapFactory.decodeFile(localMedia.getCutPath());

                        if (mPrinterSerialPort != null) {
                            mPrinterSerialPort.send(PicFromPrintUtils.draw4PxPoint(bitmap));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SELECT_IMAGE_PRINT_RASTER:
                    try {
                        localMedias = PictureSelector.obtainMultipleResult(data);
                        localMedia = localMedias.get(0);

//                        String file = localMedia.getCutPath();
//                        BitmapFactory.Options opts = new BitmapFactory.Options();
//                        opts.inJustDecodeBounds = true;
//                        BitmapFactory.decodeFile(file, opts);
//                        opts.inSampleSize = Math.round(opts.outWidth * 1.0f / 576);
//                        opts.inJustDecodeBounds = false;
//                        Bitmap bitmap = BitmapFactory.decodeFile(file, opts);


//                        String file = localMedia.getCutPath();
//                        Bitmap bitmap = BitmapFactory.decodeFile(file);
//                        //80打印机最大宽度567 58打印机最大宽度384
//                        float v = 576 * 1.0f / bitmap.getWidth();
//
//                        Matrix scaleMatrix = new Matrix();
//                        scaleMatrix.setScale(v, v, 0, 0);
//                        Bitmap bit = Bitmap.createBitmap((int) (bitmap.getWidth() * v), (int) (bitmap.getHeight() * v), Bitmap.Config.ARGB_8888);
//                        Canvas canvas = new Canvas(bit);
//                        canvas.setMatrix(scaleMatrix);
//
//                        canvas.drawBitmap(bitmap, 0, 0, new Paint(Paint.FILTER_BITMAP_FLAG));
//                        if (mPrinterSerialPort != null) {
//                            mPrinterSerialPort.send(PicFromPrintUtils.disposeRaster(bit));
//                        }

                        String file = localMedia.getCutPath();
                        Bitmap bitmap = BitmapFactory.decodeFile(file);
                        if (mPrinterSerialPort != null) {
                            mPrinterSerialPort.send(PicFromPrintUtils.disposeRaster(bitmap));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}
