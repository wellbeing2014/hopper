package org.zxp.funk.hopper.utils;


public enum Platform {
    X64(2), X86(1), ANY (0);

    private int value;

    Platform(int value) { this.value = value; }    

    public int getValue() { return value; }

    public static Platform parse(int id) {
    	Platform plat = Platform.ANY; // Default
        for (Platform item : Platform.values()) {
            if (item.getValue()==id) {
            	plat = item;
                break;
            }
        }
        return plat;
    }

};