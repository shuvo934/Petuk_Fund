package com.shuvo.ttit.petukfund.homePage.lists;

public class MonthWiseDataList {

    private String monthNo;
    private String monthName;
    private String amountIn;
    private String amountOut;
    private String amountBalance;

    public MonthWiseDataList(String monthNo, String monthName, String amountIn, String amountOut, String amountBalance) {
        this.monthNo = monthNo;
        this.monthName = monthName;
        this.amountIn = amountIn;
        this.amountOut = amountOut;
        this.amountBalance = amountBalance;
    }

    public String getMonthNo() {
        return monthNo;
    }

    public void setMonthNo(String monthNo) {
        this.monthNo = monthNo;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getAmountIn() {
        return amountIn;
    }

    public void setAmountIn(String amountIn) {
        this.amountIn = amountIn;
    }

    public String getAmountOut() {
        return amountOut;
    }

    public void setAmountOut(String amountOut) {
        this.amountOut = amountOut;
    }

    public String getAmountBalance() {
        return amountBalance;
    }

    public void setAmountBalance(String amountBalance) {
        this.amountBalance = amountBalance;
    }
}
