package com.monitor.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import oshi.SystemInfo;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

public class ProcessManager {

    private final List<String> processNames = new ArrayList<>();
    public final Map<String, List<MyProcess>> processMap = new HashMap<>();
    private static final Logger LOGGER = LogManager.getLogger(ProcessManager.class);

    public final void getProcessNames() {
        JSONParser parser = new JSONParser();
        try {
            URL resource = getClass().getClassLoader().getResource("process.json");
            if (resource == null) {
                throw new FileNotFoundException("Resource 'process.json' not found");
            }
            FileReader reader = new FileReader(new File(resource.toURI()));

            JSONArray jsonA = (JSONArray) parser.parse(reader);

            JSONObject jsonO = (JSONObject) jsonA.get(0);
            JSONArray namesArray = (JSONArray) jsonO.get("names");

            for (int i = 0; i < namesArray.size(); i++) {
                processNames.add((namesArray.get(i).toString()));
                LOGGER.info("Added Process " + namesArray.get(i).toString() + " to processNames");
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    public final void searchProcess() {
        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();

        for (String processNameToSearch : this.processNames) {
            List<OSProcess> processes = os.getProcesses();

            List<MyProcess> foundProcesses = new ArrayList<>();
            boolean processFound = false;

            for (OSProcess process : processes) {
                String name = process.getName();
                if (name.toLowerCase().contains(processNameToSearch.toLowerCase())) {
                    Integer process_id = process.getProcessID();
                    Long rss = process.getResidentSetSize();
                    Long start_time = process.getStartTime();

                    MyProcess mp = new MyProcess(name, process_id, rss, start_time);
                    LOGGER.info("Process " + mp.toString() + " added to processMap under key " + processNameToSearch);
                    foundProcesses.add(mp);

                    processFound = true;
                }
            }
            if (processFound) {
                processMap.put(processNameToSearch, foundProcesses);
            } else {
                LOGGER.error("Kein Prozess gefunden mit dem Namen: " + processNameToSearch);
            }
        }
    }

    public ProcessManager() {
        getProcessNames();
        searchProcess();
    }
}
