package com.chendd.test.download;

import android.os.Handler;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Administrator
 * @Time 2019/5/8.
 */
public class LinkURLThread extends Thread {

    private FileBean mFileBean;
    private Handler mHandler;

    public LinkURLThread(FileBean fileBean, Handler handler) {
        mFileBean = fileBean;
        mHandler = handler;
    }

    @Override
    public void run() {
        HttpURLConnection conn = null;
        RandomAccessFile raf = null;
        try {
            //1.连接网络文件
            URL url = new URL(mFileBean.getUrl());
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                //2.获取文件长度
                long len = conn.getContentLength();
                if (len > 0) {
                    File dir = new File(mFileBean.getDir());
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    //3.创建等大的本地文件
                    File file = new File(dir, mFileBean.getFileName());
                    //创建随机操作的文件流对象,可读、写、删除
                    raf = new RandomAccessFile(file, "rwd");
                    raf.setLength(len);//设置文件大小
                    mFileBean.setLength(len);
                    //4.从mHandler的消息池中拿个消息，附带mFileBean和MSG_CREATE_FILE_OK标示发送给mHandler
                    mHandler.obtainMessage(Cons.MSG_CREATE_FILE_OK, mFileBean).sendToTarget();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            try {
                if (raf != null) {
                    raf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
    }
}
