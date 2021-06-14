package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.dto.AlarmDTO;
import com.hospitalplatform.hospital_platform.exception.impl.SQLConflict;
import com.hospitalplatform.hospital_platform.exception.impl.UnexpectedSituation;
import com.hospitalplatform.hospital_platform.mapper.AlarmMapper;
import com.hospitalplatform.hospital_platform.mercury.alarm.Alarm;
import com.hospitalplatform.hospital_platform.mercury.message.MessageBroker;
import com.hospitalplatform.hospital_platform.repository.AlarmRepository;
import com.hospitalplatform.hospital_platform.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private AlarmRepository alarmRepository;

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
            Long id;
            do {
                id = (long) rand.nextInt(1000);
            } while(alarmRepository.findById(id).isPresent());
            alarmDTO.setId(id);
            Alarm alarm = alarmMapper.toModel(alarmDTO);
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
    public List<AlarmDTO> getAll() {
        return alarmMapper.toDtos(alarmRepository.findAll());
    }
}
