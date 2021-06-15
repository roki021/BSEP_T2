package com.hospitalplatform.hospital_platform.service.imple;

import com.hospitalplatform.hospital_platform.mercury.alarm.constants.ActivationTag;
import com.hospitalplatform.hospital_platform.mercury.message.Message;
import com.hospitalplatform.hospital_platform.mercury.repository.MessageRepository;
import com.hospitalplatform.hospital_platform.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Message saveDeviceMessage(Message message) {
        try {
            return messageRepository.save(message);
        } catch (Exception e) {
            System.out.println("Error write.");
            return message;
        }
    }

    @Override
    public List<Message> getAllDeviceMessages() {
        return messageRepository.findAllByTag(ActivationTag.SEC.name());
    }
}
