package com.chendd.test.download;

import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Administrator
 * @Time 2019/5/8.
 */
public class DownLoadThread extends Thread {

    private ThreadBean mThreadBean;//下载线程的信息
    private FileBean mFileBean;//下载文件的信息
    private long mLoadedLen;//已下载的长度
    public boolean isDownLoading;//是否在下载
//    private DownLoadDao mDao;//数据访问接口
    private Context mContext;//上下文

    public DownLoadThread(ThreadBean threadBean, FileBean fileBean, Context context) {
        mThreadBean = threadBean;
//        mDao = new DownLoadDaoImpl(context);
        mFileBean = fileBean;
        mContext = context;
    }

    @Override
    public void run() {
        if (mThreadBean == null) {//1.下载线程的信息为空,直接返回
            return;
        }
        //2.如果数据库没有此下载线程的信息，则向数据库插入该线程信息
//        if (!mDao.isExist(mThreadBean.getUrl(), mThreadBean.getId())) {
//            mDao.insertThread(mThreadBean);
//        }

        HttpURLConnection conn = null;
        RandomAccessFile raf = null;
        InputStream is = null;
        try {
            //3.连接线程的url
            URL url = new URL(mThreadBean.getUrl());
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            //4.设置下载位置
            long start = mThreadBean.getStart() + mThreadBean.getLoadedLen();//开始位置
            //conn设置属性，标记资源的位置(这是给服务器看的)
            conn.setRequestProperty("Range", "bytes=" + start + "-" + mThreadBean.getEnd());
            //5.寻找文件的写入位置
            File file = new File(mFileBean.getDir(), mFileBean.getFileName());
            //创建随机操作的文件流对象,可读、写、删除
            raf = new RandomAccessFile(file, "rwd");
            raf.seek(start);//设置文件写入位置
            //6.下载的核心逻辑
            Intent intent = new Intent(Cons.ACTION_UPDATE);//更新进度的广播intent
            mLoadedLen += mThreadBean.getLoadedLen();
            //206-----部分内容和范围请求  不要200写顺手了...
            if (conn.getResponseCode() == 206) {
                //读取数据
                is = conn.getInputStream();
                byte[] buf = new byte[1024 * 4];
                int len = 0;
                long time = System.currentTimeMillis();
                while ((len = is.read(buf)) != -1) {
                    //写入文件
                    raf.write(buf, 0, len);
                    //发送广播给Activity,通知进度
                    mLoadedLen += len;
                    if (System.currentTimeMillis() - time > 500) {//减少UI的渲染速度
                        mContext.sendBroadcast(intent);
                        intent.putExtra(Cons.SEND_LOADED_PROGRESS,
                                (int) (mLoadedLen * 100 / mFileBean.getLength()));
                        mContext.sendBroadcast(intent);
                        time = System.currentTimeMillis();
                    }
                    //暂停保存进度到数据库
                    if (!isDownLoading) {
//                        mDao.updateThread(mThreadBean.getUrl(), mThreadBean.getId(), mLoadedLen);
                        return;
                    }
                }
            }
            //下载完成，删除线程信息
//            mDao.deleteThread(mThreadBean.getUrl(), mThreadBean.getId());
            //下载完成后，发送完成度100%的广播
            intent.putExtra(Cons.SEND_LOADED_PROGRESS, 100);
            mContext.sendBroadcast(intent);
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
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
