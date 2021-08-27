package com.example.MyBuddy.Model;


public class Model3 {
    private String keyval;
    private String value1;
    private String value3;
    private Integer value2;
    public String getKeyval() {
        return keyval;
    }
    public String getValue1() {
        return value1;
    }
    public String getValue3() {
        return value3;
    }
    public Integer getValue2() { return value2; }

    public void setKeyval(String keyval) {
        this.keyval = keyval;
    }
    public void setValue1(String value1) {
        this.value1 = value1;
    }
    public void setValue2(Integer value2) {
        this.value2 = value2;
    }
    public void setValue3(String value3) {
        this.value3 = value3;
    }
    public Model3() {}

    public Model3(String keyval, String value1, Integer value2,String Value3)
    {
        this.keyval = keyval;
        this.value2 = value2;
        this.value1 = value1;
        this.value3 = value3;
}




}