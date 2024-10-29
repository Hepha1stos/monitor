package com.monitor.process;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class MyProcess {

    private String name;
    private Integer pid;
    private Long rss;
    private Long start_time;

    public MyProcess(String name, Integer pid, Long rss, Long start_time) {
        this.name = name;
        this.pid = pid;
        this.rss = rss;
        this.start_time = start_time;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPid() {
        return this.pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Long getRss() {
        return this.rss;
    }

    public void setRss(Long rss) {
        this.rss = rss;
    }

    public Long getStart_time() {
        return this.start_time;
    }

    public void setStart_time(Long start_time) {
        this.start_time = start_time;
    }

    public String renderUnixTimeStamp(Long unixTimeStamp) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(start_time), ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String start = dateTime.format(formatter);
        return start;
    }

    @Override
    public String toString() {
        return "MyProcess{"
                + "name='" + name + '\''
                + ", pid=" + pid
                + ", rss=" + rss
                + ", start_time=" + renderUnixTimeStamp(start_time)
                + '}';
    }
}
