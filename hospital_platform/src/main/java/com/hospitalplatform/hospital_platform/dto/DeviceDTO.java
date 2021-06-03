package com.hospitalplatform.hospital_platform.dto;

public class DeviceDTO {
    private Integer id;
    private String commonName;
    private String ipAddress;
    private int port;

    public DeviceDTO() {}

    public DeviceDTO(Integer id, String commonName, String ipAddress, int port) {
        this.id = id;
        this.commonName = commonName;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
