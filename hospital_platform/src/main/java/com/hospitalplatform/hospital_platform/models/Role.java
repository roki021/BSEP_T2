package com.hospitalplatform.hospital_platform.models;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String name;

    @ManyToMany(mappedBy = "roles")
    private Collection<HospitalUser> users;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Collection<HospitalUser> getUsers() {
        return users;
    }
}