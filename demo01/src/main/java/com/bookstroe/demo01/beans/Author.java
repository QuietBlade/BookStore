package com.bookstroe.demo01.beans;

public class Author {
    private String uid;
    private String loginuser;
    private String loginpass;
    private String email;
    private int status;
    private String activarionCode;
    private String loginGroup;
    private String registTime;

    public String getRegistTime() {
        return registTime;
    }

    public String getActivarionCode() {
        return activarionCode;
    }

    public String getEmail() {
        return email;
    }

    public String getLoginGroup() {
        return loginGroup;
    }

    public String getLoginpass() {
        return loginpass;
    }

    public String getLoginuser() {
        return loginuser;
    }

    public int getStatus() {
        return status;
    }

    public String getUid() {
        return uid;
    }

    public void setActivarionCode(String activarionCode) {
        this.activarionCode = activarionCode;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLoginGroup(String loginGroup) {
        this.loginGroup = loginGroup;
    }

    public void setLoginpass(String loginpass) {
        this.loginpass = loginpass;
    }

    public void setLoginuser(String loginuser) {
        this.loginuser = loginuser;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setRegistTime(String registTime) {
        this.registTime = registTime;
    }

    @Override
    public String toString() {
        return "{" +
                "uid:'" + uid + '\'' +
                ", loginuser:'" + loginuser + '\'' +
                ", loginpass:'" + loginpass + '\'' +
                ", email:'" + email + '\'' +
                ", status:" + status +
                ", activarionCode:'" + activarionCode + '\'' +
                ", loginGroup:'" + loginGroup + '\'' +
                '}';
    }
}
