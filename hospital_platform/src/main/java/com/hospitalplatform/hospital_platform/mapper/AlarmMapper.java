package com.hospitalplatform.hospital_platform.mapper;

import com.hospitalplatform.hospital_platform.dto.AlarmDTO;
import com.hospitalplatform.hospital_platform.dto.TriggerDTO;
import com.hospitalplatform.hospital_platform.mercury.alarm.Alarm;
import com.hospitalplatform.hospital_platform.mercury.alarm.trigger.Trigger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AlarmMapper {
    public Alarm toModel(AlarmDTO alarmDTO) {
        LinkedHashMap<String, Trigger> triggers = new LinkedHashMap<>();
        for(String key : alarmDTO.getTriggers().keySet()) {
            triggers.put(key, new Trigger(
                    alarmDTO.getTriggers().get(key).getRelation(),
                    alarmDTO.getTriggers().get(key).getValue()
            ));
        }

        for(String key : triggers.keySet())
            System.out.println(triggers.get(key).getValue());

        return new Alarm(
                alarmDTO.getName(),
                alarmDTO.getAlarmMessage(),
                (alarm) -> {
                    System.out.println(alarm.message());
                },
                alarmDTO.getActivationTags(),
                triggers,
                alarmDTO.getActivationThreshold(),
                alarmDTO.getFactWait()
        );
    }

    public List<Alarm> toModels(List<AlarmDTO> alarmDTOs) {
        return alarmDTOs.stream().map(this::toModel).collect(Collectors.toList());
    }

    public AlarmDTO toDto(Alarm alarm) {
        AlarmDTO dto = new AlarmDTO(alarm.getId());
        dto.setName(alarm.getName());
        dto.setAlarmMessage(alarm.getAlarmMessage());
        return dto;
    }

    public List<AlarmDTO> toDtos(List<Alarm> alarms) {
        return alarms.stream().map(this::toDto).collect(Collectors.toList());
    }
}
