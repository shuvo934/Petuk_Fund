package com.shuvo.ttit.petukfund.contribution.lists;

public class ContributionLIst {
    private String name;
    private String amount;

    public ContributionLIst(String name, String amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
