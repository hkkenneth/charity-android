package com.winginno.charitynow;

public class Settings {
    private boolean otherNoti;
    private boolean flagSaleNotiHk;
    private boolean flagSaleNotiKowloon;
    private boolean flagSaleNotiNt;

    private Settings(boolean otherNotiOption,
                    boolean flagSaleNotiHkOption,
                    boolean flagSaleNotiKowloonOption,
                    boolean flagSaleNotiNtOption) {
        otherNoti = otherNotiOption;
        flagSaleNotiHk = flagSaleNotiHkOption;
        flagSaleNotiKowloon = flagSaleNotiKowloonOption;
        flagSaleNotiNt = flagSaleNotiNtOption;
    }

    public static Settings getDefaultInstance() {
        return new Settings(true, true, true, true);
    }

    public boolean isOtherNotiEnabled() {
        return otherNoti;
    }

    public void setOtherNotiEnabled(boolean enabled) {
        otherNoti = enabled;
    }

    public boolean isFlagSaleNotiHkEnabled() {
        return flagSaleNotiHk;
    }

    public void setFlagSaleNotiHkEnabled(boolean enabled) {
        flagSaleNotiHk = enabled;
    }

    public boolean isFlagSaleNotiKowloonEnabled() {
        return flagSaleNotiKowloon;
    }

    public void setFlagSaleNotiKowloonEnabled(boolean enabled) {
        flagSaleNotiKowloon = enabled;
    }

    public boolean isFlagSaleNotiNtEnabled() {
        return flagSaleNotiNt;
    }

    public void setFlagSaleNotiNtEnabled(boolean enabled) {
        flagSaleNotiNt = enabled;
    }
}
