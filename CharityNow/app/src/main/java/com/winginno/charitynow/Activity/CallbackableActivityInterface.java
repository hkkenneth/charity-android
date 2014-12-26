package com.winginno.charitynow;

public interface CallbackableActivityInterface {

    public final static String CALLBACK_TYPE_DATA_FETCH = "DATA_FETCH";

    public void callback(String type);
}
