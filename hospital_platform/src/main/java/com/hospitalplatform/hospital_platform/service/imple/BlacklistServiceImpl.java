package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.mercury.alarm.constants.Relation;
import com.hospitalplatform.hospital_platform.mercury.alarm.trigger.Trigger;
import com.hospitalplatform.hospital_platform.repository.TriggerRepository;
import com.hospitalplatform.hospital_platform.service.BlacklistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BlacklistServiceImpl implements BlacklistService {
    @Autowired
    private TriggerRepository triggerRepository;

    private Trigger trigger;

    public BlacklistServiceImpl(@Autowired TriggerRepository triggerRepository) {
       this.triggerRepository = triggerRepository;

        Optional<Trigger> triggerOptional = this.triggerRepository.findById(1L);
        if (triggerOptional.isEmpty())
            this.trigger = this.triggerRepository.save(new Trigger(1L, Relation.CONTAINS, "127.0.0.1,127.0.0.7"));
        else
            this.trigger = triggerOptional.get();
    }

    @Override
    public Trigger getBlacklistTrigger() {
        return this.trigger;
    }

    @Override
    public void addIP(String address) {
        this.trigger.setValue(this.trigger.getValue() + "," + address);
        this.triggerRepository.save(this.trigger);
    }
}
