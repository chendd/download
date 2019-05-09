package com.chendd.test.download;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * @author Administrator
 * @Time 2019/5/8.
 */
public class DownLoadService extends Service {

//    private DownLoadDaoImpl mDao;
    private DownLoadThread mDownLoadThread;
    private ThreadBean mThreadBean;
    /**
     * 处理消息使用的Handler
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Cons.MSG_CREATE_FILE_OK:
                    FileBean fileBean = (FileBean) msg.obj;
                    //已在主线程，可更新UI
                    Toast.makeText(DownLoadService.this, "文件长度:" + fileBean.getLength(),Toast.LENGTH_SHORT);
                    download(fileBean);
                    break;
            }
        }
    };

    /**
     * 下载逻辑
     *
     * @param fileBean 文件信息对象
     */
    public void download(FileBean fileBean) {
        //从数据获取线程信息
//        List<ThreadBean> threads = mDao.getThreads(fileBean.getUrl());
//        if (threads.size() == 0) {//如果没有线程信息，就新建线程信息
            mThreadBean = new ThreadBean(
                    0, fileBean.getUrl(), 0, fileBean.getLength(), 0);//初始化线程信息对象
//        } else {
//            mThreadBean = threads.get(0);//否则取第一个
//        }
        mDownLoadThread = new DownLoadThread(mThreadBean, fileBean, this);//创建下载线程
        mDownLoadThread.start();//开始线程
        mDownLoadThread.isDownLoading = true;
    }

    @Override//每次启动服务会走此方法
    public int onStartCommand(Intent intent, int flags, int startId) {
//        mDao = new DownLoadDaoImpl(this);
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case Cons.ACTION_START:
                    FileBean fileBean = (FileBean) intent.getSerializableExtra(Cons.SEND_FILE_BEAN);
                    if (mDownLoadThread != null) {
                        if (mDownLoadThread.isDownLoading) {
                            return super.onStartCommand(intent, flags, startId);
                        }
                    }
                    new LinkURLThread(fileBean, mHandler).start();
                    break;
                case Cons.ACTION_STOP:
                    if (mDownLoadThread != null) {
                        mDownLoadThread.isDownLoading = false;
                    }
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}