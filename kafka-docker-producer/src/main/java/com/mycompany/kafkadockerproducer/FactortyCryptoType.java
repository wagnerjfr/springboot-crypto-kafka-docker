package com.mycompany.kafkadockerproducer;

public final class FactortyCryptoType {

    public static CryptoType getCryptoType(String type) {
        switch (type.toUpperCase()) {
            case "BTC": return CryptoType.BTC;
            case "LTC": return CryptoType.LTC;
            case "BCH": return CryptoType.BCH;
            case "XRP": return CryptoType.XRP;
            case "ETH": return CryptoType.ETH;
        }
        return CryptoType.UNKNOW;
    }
}
