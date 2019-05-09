package com.chendd.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chendd.test.download.Cons;
import com.chendd.test.download.DownLoadService;
import com.chendd.test.download.FileBean;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_start;
//    private Button btn_stop;
    private UpdateReceiver mUpdateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        register();

        btn_start = findViewById(R.id.btn_start);
//        btn_stop = findViewById(R.id.btn_stop);
        btn_start.setOnClickListener(this);
//        btn_stop.setOnClickListener(this);
    }

    /**
     *
     * @param url 下载地址
     * @param fileName  保存的文件名
     * @param fileDir  保存的文件路径
     */
    private void start(String url,String fileName,String fileDir) {
        //创建FileBean对象
        FileBean fileBean = new FileBean(0, url, fileName, 0, 0,fileDir);
        Intent intent = new Intent(MainActivity.this, DownLoadService.class);
        intent.setAction(Cons.ACTION_START);
        intent.putExtra(Cons.SEND_FILE_BEAN, fileBean);//使用intent携带对象
        startService(intent);//开启服务--下载标示
        Toast.makeText(MainActivity.this,"开始下载："+fileBean.getFileName(),Toast.LENGTH_SHORT).show();
    }

    /**
     * 点击停止下载逻辑
     */
//    private void stop() {
//        Intent intent = new Intent(MainActivity.this, DownLoadService.class);
//        intent.setAction(Cons.ACTION_STOP);
//        startService(intent);//启动服务---停止标示
//        Toast.makeText(MainActivity.this,"停止下载",Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                start("https://imtt.dd.qq.com/16891/4611E43165D203CB6A52E65759FE7641.apk?fsname=com.daimajia.gold_5.6.2_196.apk&csr=1bbd","掘金.apk",  "/b_download/");
                break;
//            case R.id.btn_stop:
//                stop();
//                break;
        }
    }

    /**
     * 注册广播接收者
     */
    private void register() {
        //注册广播接收者
        mUpdateReceiver = new UpdateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Cons.ACTION_UPDATE);
        registerReceiver(mUpdateReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUpdateReceiver != null) {//注销广播
            unregisterReceiver(mUpdateReceiver);
        }
    }

    static class UpdateReceiver extends BroadcastReceiver {


        public UpdateReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Cons.ACTION_UPDATE.equals(intent.getAction())) {
                int progress = intent.getIntExtra(Cons.SEND_LOADED_PROGRESS, 0);
                if (progress == 100) {//下载完成通知关闭下载服务
                    Intent serviceIntent = new Intent(context, DownLoadService.class);
                    serviceIntent.setAction(Cons.ACTION_STOP);
                    context.startService(serviceIntent);
                }
                Toast.makeText(context,"下载进度："+progress,Toast.LENGTH_SHORT).show();
            }
        }
    }

}
