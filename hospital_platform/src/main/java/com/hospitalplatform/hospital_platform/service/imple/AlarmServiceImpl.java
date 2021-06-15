package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.dto.AlarmDTO;
import com.hospitalplatform.hospital_platform.exception.impl.SQLConflict;
import com.hospitalplatform.hospital_platform.exception.impl.UnexpectedSituation;
import com.hospitalplatform.hospital_platform.mapper.AlarmMapper;
import com.hospitalplatform.hospital_platform.mercury.alarm.Alarm;
import com.hospitalplatform.hospital_platform.mercury.alarm.trigger.Trigger;
import com.hospitalplatform.hospital_platform.mercury.message.MessageBroker;
import com.hospitalplatform.hospital_platform.repository.AlarmRepository;
import com.hospitalplatform.hospital_platform.repository.TriggerRepository;
import com.hospitalplatform.hospital_platform.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private TriggerRepository triggerRepository;

    @Autowired
    private MessageBroker messageBroker;

    private AlarmMapper alarmMapper;

    public AlarmServiceImpl() {
        alarmMapper = new AlarmMapper();
    }

    @Override
    public AlarmDTO addAlarm(AlarmDTO alarmDTO) throws SQLConflict {
        if(alarmRepository.findByName(alarmDTO.getName()).isEmpty()) {
            Random rand = new Random();
            for(String key : alarmDTO.getTriggers().keySet()) {
                int id;
                do {
                    id = rand.nextInt(1000);
                } while(triggerRepository.findById((long)id).isPresent());
                alarmDTO.getTriggers().get(key).setId(id);
            }
            Alarm alarm = alarmMapper.toModel(alarmDTO);
            LinkedHashMap<String, Trigger> triggers = new LinkedHashMap<>();
            for(String key : alarmDTO.getTriggers().keySet()) {
                triggers.put(key, triggerRepository.save(
                        new Trigger(
                                (long)alarmDTO.getTriggers().get(key).getId(),
                                alarmDTO.getTriggers().get(key).getRelation(),
                                alarmDTO.getTriggers().get(key).getValue()
                        )
                ));
            }
            alarm.setTriggers(triggers);
            AlarmDTO added = alarmMapper.toDto(alarmRepository.save(alarm));
            this.messageBroker.addAlarm(alarm);
            return added;
        } else {
            throw new SQLConflict("Alarm with given name already exist.");
        }
    }

    @Override
    public void removeAlarm(Long id) throws UnexpectedSituation {
        Optional<Alarm> alarm = alarmRepository.findById(id);
        if(alarm.isPresent()) {
            alarmRepository.delete(alarm.get());
        } else {
            throw new UnexpectedSituation("Alarm with given id do not exist.");
        }
    }

    @Override
    public List<AlarmDTO> getAllByTags(int activationTags) {
        return alarmMapper.toDtos(alarmRepository.findAllByActivationTags(activationTags));
    }
}
