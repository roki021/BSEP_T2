package com.admin.platform.constants;

public enum TemplateTypes {
    ROOT("ROOT"),
    LEAF_HOSPITAL("LEAF_HOSPITAL");

    private final String text;

    TemplateTypes(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
