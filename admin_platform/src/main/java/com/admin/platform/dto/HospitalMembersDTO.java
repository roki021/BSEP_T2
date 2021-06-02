package com.admin.platform.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class HospitalMembersDTO {
    private Integer hospitalId;
    private List<HospitalMemberDTO> members;
}
