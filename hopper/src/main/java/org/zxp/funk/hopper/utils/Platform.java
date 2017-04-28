package org.zxp.funk.hopper.utils;


public enum Platform {
    X64(1), X86(0), ORTHER (9);

    private int value;

    Platform(int value) { this.value = value; }    

    public int getValue() { return value; }

    public static Platform parse(int id) {
    	Platform plat = null; // Default
        for (Platform item : Platform.values()) {
            if (item.getValue()==id) {
            	plat = item;
                break;
            }
        }
        return plat;
    }

};