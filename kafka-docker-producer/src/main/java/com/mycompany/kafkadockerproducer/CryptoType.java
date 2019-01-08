package com.mycompany.kafkadockerproducer;

public enum CryptoType {
    UNKNOW("Unknow", "", ""),
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

    public String getName() {
        return name;
    }

    public String getInitials() {
        return initials;
    }

    public String getUrl() {
        return url;
    }
}
