package com.shuvo.ttit.petukfund.monthlyHistory.lists;

public class InOutBalanceSheetList {

    private String sl_no;
    private String date_balance;
    private String name;
    private String in_amount;
    private String out_amount;

    public InOutBalanceSheetList(String sl_no, String date_balance, String name, String in_amount, String out_amount) {
        this.sl_no = sl_no;
        this.date_balance = date_balance;
        this.name = name;
        this.in_amount = in_amount;
        this.out_amount = out_amount;
    }

    public String getSl_no() {
        return sl_no;
    }

    public void setSl_no(String sl_no) {
        this.sl_no = sl_no;
    }

    public String getDate_balance() {
        return date_balance;
    }

    public void setDate_balance(String date_balance) {
        this.date_balance = date_balance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIn_amount() {
        return in_amount;
    }

    public void setIn_amount(String in_amount) {
        this.in_amount = in_amount;
    }

    public String getOut_amount() {
        return out_amount;
    }

    public void setOut_amount(String out_amount) {
        this.out_amount = out_amount;
    }
}
