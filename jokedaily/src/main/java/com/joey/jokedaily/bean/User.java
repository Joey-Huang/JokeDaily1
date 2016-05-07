package com.joey.jokedaily.bean;
/**
 * 账户   javabean
 */
public class User {
    /**
     * name         varchar(20)
     * password     varchar(20)
     * protectCode  varchar(20)
     */
    private String name;
    private String password;
    private String protectCode;
    public User(String name, String password) {
        this(name,password,"");
    }

    public User(String name, String password, String protectCode) {
        this.name = name;
        this.password = password;
        this.protectCode = protectCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProtectCode() {
        return protectCode;
    }

    public void setProtectCode(String protectCode) {
        this.protectCode = protectCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", protectCode='" + protectCode + '\'' +
                '}';
    }
}
