package com.hospitalplatform.hospital_platform.models;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
public class HospitalAdmin extends HospitalUser {
    public HospitalAdmin() {}

    public HospitalAdmin(String username, String firstName, String lastName, String password, String email) {
        super(username, firstName, lastName, password, email);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Authority> authorityList = new ArrayList<>();
        authorityList.add(new Authority("ROLE_ADMIN"));
        return authorityList;
    }
}
