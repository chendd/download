package com.chendd.test.download;

import java.io.Serializable;

/**
 * @author Administrator
 * @Time 2019/5/8.
 */
public class FileBean implements Serializable {

    private int id;//文件id
    private String url;//文件下载地址
    private String fileName;//文件名
    private long length;//文件长度
    private long loadedLen;//文件已下载长度
    private String dir;//文件本地保存路径

    public FileBean(int id, String url, String fileName, long length, long loadedLen, String dir) {
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.length = length;
        this.loadedLen = loadedLen;
        this.dir = dir;
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public long getLoadedLen() {
        return loadedLen;
    }

    public void setLoadedLen(long loadedLen) {
        this.loadedLen = loadedLen;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
