package com.shuvo.ttit.petukfund.userInfoLists;

public class UsersList {
    private String p_id;
    private String p_name;

    public UsersList(String p_id, String p_name) {
        this.p_id = p_id;
        this.p_name = p_name;
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
}
