package com.smartstrap.contact;

/**
 * Created by Nick on 2017/7/1.
 */

public class Person {
    public int levelColor;
    public String name;
    public String phone;

    public int getLevelColor() {
        return levelColor;
    }
    public void setImage(int levelColor) {
        this.levelColor = levelColor;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Person(int levelColor, String name, String phone) {
        super();
        this.levelColor = levelColor;
        this.name = name;
        this.phone = phone;
    }
}