package com.mobo.horoscope.common;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownLoadUtil {
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
        if (!file.exists()) {
            file.mkdir();
        }
        return new File(file.getPath(), fileName);
    }

    /**
     * 下载文件并写入文件到本地
     *
     * @param downloadUrl
     * @param fileName
     * @return
     */
    public static boolean downloadFile(Context context, String downloadUrl, String fileName) {
        try {
            if (TextUtils.isEmpty(downloadUrl)) {
                return false;
            }
            // 获得存储卡的路径
            // 判断SD卡是否存在，并且是否具有读写权限
            if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                return false;
            }

            File file = getLocalFile(context, fileName);
            if (file == null) {
                return false;
            }

            URL url = new URL(downloadUrl);
            // 创建连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            // 获取文件大小
            int length = conn.getContentLength();
            // 创建输入流
            InputStream is = conn.getInputStream();

            FileOutputStream fos = new FileOutputStream(file);
            int count = 0;
            // 缓存
            byte buf[] = new byte[1024];
            // 写入到文件中
            int len = 0;
            while ((len = is.read(buf)) > 0) {
                count += len;
                // 计算进度条位置
                int progress = (int) (((float) count / length) * 100);
                // 更新进度
                //publishProgress(progress);
                // 写入文件
                fos.write(buf, 0, len);
            }
            fos.close();
            is.close();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}