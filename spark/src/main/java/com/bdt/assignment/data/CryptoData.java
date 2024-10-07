package com.bdt.assignment.data;

public class CryptoData {

    CoinData bitcoin;
    CoinData ethereum;

    public CoinData getBitcoin() {
        return bitcoin;
    }

    public void setBitcoin(CoinData bitcoin) {
        this.bitcoin = bitcoin;
    }

    public CoinData getEthereum() {
        return ethereum;
    }

    public void setEthereum(CoinData ethereum) {
        this.ethereum = ethereum;
    }

    @Override
    public String toString() {
        return "CryptoData{" +
                "bitcoin=" + bitcoin +
                ", ethereum=" + ethereum +
                '}';
    }
}
