package com.updesigns.moneycircle;

//This class is not an entity but we have it used as an abstract data type.. To store the transactions in a list form..

public class Transaction {

    private String circle;
    private long amount;
    private int type;       //Type specifies inflow or outflow
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
