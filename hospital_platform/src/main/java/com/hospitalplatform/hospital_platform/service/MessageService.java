package com.hospitalplatform.hospital_platform.service;

import com.hospitalplatform.hospital_platform.mercury.message.Message;

import java.util.List;

public interface MessageService {

    Message saveDeviceMessage(Message message);

    List<Message> getAllDeviceMessages();
}
