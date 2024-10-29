package com.monitor;

import com.monitor.db.DBManager;
import com.monitor.process.ProcessManager;

/**
 * Hello world!
 *
 */
public class App {

    public static void main(String[] args) {
        DBManager dm = new DBManager();
        ProcessManager pm = new ProcessManager();
        System.out.println(pm.processMap);
        System.out.println("HELOOOOOOOOOOOOOO");
    }
}
