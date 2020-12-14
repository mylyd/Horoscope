package com.mobo.horoscope.common;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @Description: TODO
 * @Author: jzhou
 * @CreateDate: 19-8-15 下午9:36
 */
public class FileUtil {

    //从assets 文件夹中获取文件并读取数据
    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStream in = context.getAssets().open(fileName);
            //获取文件的字节数
            int length = in.available();
            //创建byte数组
            byte[] buffer = new byte[length];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getFromLocalFile(String fileName) {
        String result = "";
        try {
            InputStream in = new FileInputStream(fileName);
            //获取文件的字节数
            int length = in.available();
            //创建byte数组
            byte[] buffer = new byte[length];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static File getLocalFile(Context context) {
        File file;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        } else {
            //外部存储不可用
            file = context.getCacheDir();
        }

        if (file == null) {
            return null;
        }
        // 判断文件目录是否存在
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static File getLocalFile(Context context, String fileName) {
        File file;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            file = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        } else {
            //外部存储不可用
            file = context.getCacheDir();
        }

        if (file == null) {
            return null;
        }
        // 判断文件目录是否存在
//        if (!file.exists()) {
//            file.mkdir();
//        }
        return new File(file.getPath(), fileName);
    }
}
