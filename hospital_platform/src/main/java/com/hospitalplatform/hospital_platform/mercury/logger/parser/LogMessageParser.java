package com.hospitalplatform.hospital_platform.mercury.logger.parser;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogMessageParser {
    private HashMap<String, String> fieldsRegex;
    private String statusRegex;
    private String timeRegex;

    public LogMessageParser(LinkedHashMap<String, String> fieldsRegex) {
        this(fieldsRegex, "", "");
    }

    public LogMessageParser(LinkedHashMap<String, String> fieldsRegex, String statusRegex, String timeRegex) {
        this.fieldsRegex = fieldsRegex;
        this.statusRegex = statusRegex;
        this.timeRegex = timeRegex;
    }

    public LinkedHashMap<String, Object> parse(String logLine) {
        System.out.println(logLine);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(
                "\\[(INFO|WARNING|ERROR)\\] ([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2}) -");

        for (String field : fieldsRegex.keySet()) {
            String regex = fieldsRegex.get(field);
            stringBuilder.append(" " + field + " (" + regex + ")");
        }

        Pattern pattern = Pattern.compile(stringBuilder.toString(), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(logLine);

        if (!matcher.find())
            return null;

        LinkedHashMap<String, Object> res = new LinkedHashMap<>();

        // status
        String status = matcher.group(1);
        res.put("status", status);

        //naive date
        LocalDate date = LocalDate.of(
                Integer.parseInt(matcher.group(2)),
                Integer.parseInt(matcher.group(3)),
                Integer.parseInt(matcher.group(4)));
        LocalTime time = LocalTime.of(
                Integer.parseInt(matcher.group(5)),
                Integer.parseInt(matcher.group(6)),
                Integer.parseInt(matcher.group(7)));

        LocalDateTime datetime = LocalDateTime.of(date, time);
        ZoneId zoneId = ZoneId.systemDefault();
        res.put("time", datetime.atZone(zoneId).toEpochSecond());

        Object[] keys = fieldsRegex.keySet().toArray();
        for (int i = 8; i < 8 + keys.length; i++)
            res.put(keys[i - 8].toString(), matcher.group(i));

        return res;
    }
}
