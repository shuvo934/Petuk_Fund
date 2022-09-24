package com.shuvo.ttit.petukfund.userInfoLists;

public class UserInfoList {

    private String p_id;
    private String p_name;
    private String p_designation;
    private String p_phone;
    private String p_email;
    private String p_jdate;
    private String p_type;

    public UserInfoList(String p_id, String p_name, String p_designation, String p_phone, String p_email, String p_jdate, String p_type) {
        this.p_id = p_id;
        this.p_name = p_name;
        this.p_designation = p_designation;
        this.p_phone = p_phone;
        this.p_email = p_email;
        this.p_jdate = p_jdate;
        this.p_type = p_type;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_designation() {
        return p_designation;
    }

    public void setP_designation(String p_designation) {
        this.p_designation = p_designation;
    }

    public String getP_phone() {
        return p_phone;
    }

    public void setP_phone(String p_phone) {
        this.p_phone = p_phone;
    }

    public String getP_email() {
        return p_email;
    }

    public void setP_email(String p_email) {
        this.p_email = p_email;
    }

    public String getP_jdate() {
        return p_jdate;
    }

    public void setP_jdate(String p_jdate) {
        this.p_jdate = p_jdate;
    }

    public String getP_type() {
        return p_type;
    }

    public void setP_type(String p_type) {
        this.p_type = p_type;
    }
}
