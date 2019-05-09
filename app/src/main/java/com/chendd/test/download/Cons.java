package com.chendd.test.download;

/**
 * @author Administrator
 * @Time 2019/5/8.
 */
public class Cons {

    //intent传递数据----开始下载时，传递FileBean到Service 标示
    public static final String SEND_FILE_BEAN = "send_file_bean";
    //广播更新进度
    public static final String SEND_LOADED_PROGRESS = "send_loaded_length";

    //Handler的Message处理的常量
    public static final int MSG_CREATE_FILE_OK = 0x00;

    public static final String ACTION_START = "action_start";
    public static final String ACTION_STOP = "action_stop";
    public static final String ACTION_UPDATE = "action_update";


}
