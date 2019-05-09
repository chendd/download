package com.chendd.test.download;

/**
 * @author Administrator
 * @Time 2019/5/8.
 */
public class ThreadBean {

    private int id;//线程id
    private String url;//线程所下载文件的url
    private long start;//线程开始的下载位置(为多线程准备)
    private long end;//线程结束的下载位置
    private long loadedLen;//该线程已下载的长度

    public ThreadBean() {
    }

    public ThreadBean(int id, String url, long start, long end, long loadedLen) {
        this.id = id;
        this.url = url;
        this.start = start;
        this.end = end;
        this.loadedLen = loadedLen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getLoadedLen() {
        return loadedLen;
    }

    public void setLoadedLen(long loadedLen) {
        this.loadedLen = loadedLen;
    }
}
