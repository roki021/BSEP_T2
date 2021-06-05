package com.hospitalplatform.hospital_platform.models;

import javax.persistence.*;

@Entity
public class Device {

    @Id
    @SequenceGenerator(name="seq", initialValue=1000, allocationSize=100) // TODO: CHANGE THIS TO HEX VALUE ???
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seq")
    private Integer id;

    @Column(unique = true)
    private String commonName;

    @Column
    private String ipAddress;

    @Column
    private int port;

    public Device() {}

    public Device(String commonName, String ipAddress, int port) {
        this.commonName = commonName;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public Integer getId() {
        return id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
