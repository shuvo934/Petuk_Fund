package com.shuvo.ttit.petukfund.homePage.lists;

public class MonthWiseDataList {

    private String monthNo;
    private String monthName;
    private String amountIn;
    private String amountOut;
    private String amountBalance;
    private String year;
    private boolean monthIn;
    private boolean monthOut;
    private boolean updatedIn;
    private boolean updatedOut;

    public MonthWiseDataList(String monthNo, String monthName, String amountIn, String amountOut, String amountBalance,String year, boolean monthIn, boolean monthOut, boolean updatedIn, boolean updatedOut) {
        this.monthNo = monthNo;
        this.monthName = monthName;
        this.amountIn = amountIn;
        this.amountOut = amountOut;
        this.amountBalance = amountBalance;
        this.year = year;
        this.monthIn = monthIn;
        this.monthOut = monthOut;
        this.updatedIn = updatedIn;
        this.updatedOut = updatedOut;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public boolean isMonthIn() {
        return monthIn;
    }

    public void setMonthIn(boolean monthIn) {
        this.monthIn = monthIn;
    }

    public boolean isMonthOut() {
        return monthOut;
    }

    public void setMonthOut(boolean monthOut) {
        this.monthOut = monthOut;
    }

    public boolean isUpdatedIn() {
        return updatedIn;
    }

    public void setUpdatedIn(boolean updatedIn) {
        this.updatedIn = updatedIn;
    }

    public boolean isUpdatedOut() {
        return updatedOut;
    }

    public void setUpdatedOut(boolean updatedOut) {
        this.updatedOut = updatedOut;
    }
}
