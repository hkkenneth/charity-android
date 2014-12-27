package com.winginno.charitynow;

import java.util.Date;

public class SectionHeader implements ListItemInterface {

    private String name;

    public SectionHeader(String nameParam) {
        name = nameParam;
    }

    public String getName() {
        return name;
    }

    public boolean isSectionHeader() {
        return true;
    }
}
