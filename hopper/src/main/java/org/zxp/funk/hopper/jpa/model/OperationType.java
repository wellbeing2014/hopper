package org.zxp.funk.hopper.jpa.model;


public enum OperationType {
    新建(0), 修改(1), 删除(2),启动(3), 停止 (4),未知(9);

    private int value;

    OperationType(int value) { this.value = value; }    

    public int getValue() { return value; }

    public static OperationType parse(int id) {
    	OperationType plat = OperationType.未知; // Default
        for (OperationType item : OperationType.values()) {
            if (item.getValue()==id) {
            	plat = item;
                break;
            }
        }
        return plat;
    }

};