package com.admin.platform.constants;

public enum CsrType {
    HOSPITAL("HOSPITAL"),
    DEVICE("DEVICE");

    private final String text;

    CsrType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}