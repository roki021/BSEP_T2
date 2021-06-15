package com.admin.platform.constants;

public enum TemplateTypes {
    ROOT("ROOT"),
    HOSPITAL("HOSPITAL"),
    DEVICE("DEVICE");

    private final String text;

    TemplateTypes(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
