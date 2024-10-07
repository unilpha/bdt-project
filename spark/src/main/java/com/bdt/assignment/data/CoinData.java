package com.bdt.assignment.data;

public class CoinData {
    Double usd;

    public Double getUsd() {
        return usd;
    }

    public void setUsd(Double usd) {
        this.usd = usd;
    }

    @Override
    public String toString() {
        return "CoinData{" +
                "usd=" + usd +
                '}';
    }
}
