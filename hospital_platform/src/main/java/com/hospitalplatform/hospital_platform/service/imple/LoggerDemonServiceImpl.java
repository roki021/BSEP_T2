package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.mercury.logger.Logger;
import com.hospitalplatform.hospital_platform.mercury.logger.impl.LogSimulatorLogger;
import com.hospitalplatform.hospital_platform.mercury.message.Message;
import com.hospitalplatform.hospital_platform.mercury.message.MessageBroker;
import com.hospitalplatform.hospital_platform.service.LoggerDemonService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class LoggerDemonServiceImpl implements LoggerDemonService {
    private MessageBroker messageBroker;
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public static Properties loadProperties(String resourceFileName) throws IOException {
        Properties configuration = new Properties();
        InputStream inputStream = LoggerDemonServiceImpl.class
                .getClassLoader()
                .getResourceAsStream(resourceFileName);
        configuration.load(inputStream);
        inputStream.close();
        return configuration;
    }

    private String configPath;

    public LoggerDemonServiceImpl(MessageBroker broker) throws IOException, ParseException {
        this.messageBroker = broker;
        this.threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        this.threadPoolTaskScheduler.setPoolSize(5);
        this.threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        this.threadPoolTaskScheduler.initialize();
        this.configPath = loadProperties("application.properties").getProperty("hospital.logs_config");

        System.out.println("VREDNOST CONF: " + configPath);

        this.loadConfig();
    }

    public void addLogger(Logger logger) {
        logger.subscribeBroker(this.messageBroker);
        this.threadPoolTaskScheduler.scheduleAtFixedRate(logger, logger.getReadPeriod() * 1000L);
    }

    private void loadConfig() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(configPath));
        JSONObject jsonObject = (JSONObject) obj;
        JSONArray loggersList = (JSONArray) jsonObject.get("loggers");
        Iterator<JSONObject> iterator = loggersList.iterator();
        while (iterator.hasNext()) {
            JSONObject logger = iterator.next();
            String logPath = logger.get("logPath").toString();
            System.err.println(logger.get("readPeriod").toString());
            Integer readPeriod = Integer.parseInt(logger.get("readPeriod").toString());
            LinkedHashMap<String, String> lm = new LinkedHashMap<>();
            JSONArray params = (JSONArray) logger.get("params");

            for (Iterator it = params.iterator(); it.hasNext();) {
                JSONObject param = (JSONObject) it.next();

                String key = param.get("name").toString();
                String val = param.get("regex").toString();
                System.out.println(key + ": " + val);
                lm.put(key, val);
            }

            this.addLogger(new LogSimulatorLogger(
                    readPeriod,
                    logPath,
                    lm));
        }
    }
}
