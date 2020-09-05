package com.mycompany.kafkadockerproducer;

import lombok.Getter;

@Getter
public enum CryptoType {
    UNKNOWN("Unknown", "", ""),
    BTC("Bitcoin", "BTC", "https://www.bitstamp.net/api/v2/ticker_hour/btcusd/"),
    LTC("Litecoin", "LTC", "https://www.bitstamp.net/api/v2/ticker_hour/ltcusd/"),
    BCH("Bitcoin Cash", "BCH", "https://www.bitstamp.net/api/v2/ticker_hour/bchusd/"),
    XRP("Ripple", "XRP", "https://www.bitstamp.net/api/v2/ticker_hour/xrpusd/"),
    ETH("Ethereum", "ETH", "https://www.bitstamp.net/api/v2/ticker_hour/ethusd/");

    private String name;
    private String initials;
    private String url;

    CryptoType(String name, String initials, String url) {
        this.name = name;
        this.initials = initials;
        this.url = url;
    }

    static CryptoType getCryptoType(String type) {
        switch (type.toUpperCase()) {
            case "BTC": return CryptoType.BTC;
            case "LTC": return CryptoType.LTC;
            case "BCH": return CryptoType.BCH;
            case "XRP": return CryptoType.XRP;
            case "ETH": return CryptoType.ETH;
            default: return CryptoType.UNKNOWN;
        }
    }
}
