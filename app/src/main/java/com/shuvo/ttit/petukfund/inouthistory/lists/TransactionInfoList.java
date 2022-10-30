package com.shuvo.ttit.petukfund.inouthistory.lists;

public class TransactionInfoList {

    private String pId;
    private String pName;
    private String amount;
    private String tDate;

    public TransactionInfoList(String pId, String pName, String amount, String tDate) {
        this.pId = pId;
        this.pName = pName;
        this.amount = amount;
        this.tDate = tDate;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String gettDate() {
        return tDate;
    }

    public void settDate(String tDate) {
        this.tDate = tDate;
    }
}
