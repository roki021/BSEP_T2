package com.hospitalplatform.hospital_platform.models;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class HospitalAdmin extends HospitalUser {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Authority> authorityList = new ArrayList<>();
        authorityList.add(new Authority("ROLE_ADMIN"));
        return authorityList;
    }
}
