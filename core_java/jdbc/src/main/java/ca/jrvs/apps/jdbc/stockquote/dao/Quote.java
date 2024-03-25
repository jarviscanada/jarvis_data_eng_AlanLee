package ca.jrvs.apps.jdbc.stockquote.dao;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Date;
import java.sql.Timestamp;

public class Quote {
    @JsonProperty("01. symbol")
    private String ticker; // Maps to "01. symbol"
    @JsonProperty("02. open")
    private double open; // Maps to "02. open"
    @JsonProperty("03. high")
    private double high; // Maps to "03. high"
    @JsonProperty("04. low")
    private double low; // Maps to "04. low"
    @JsonProperty("05. price")
    private double price; // Maps to "05. price"
    @JsonProperty("06. volume")
    private int volume; // Maps to "06. volume"
    @JsonProperty("07. latest trading day")
    private Date latestTradingDay; // Maps to "07. latest trading day"
    @JsonProperty("08. previous close")
    private double previousClose; // Maps to "08. previous close"
    @JsonProperty("09. change")
    private double change; // Maps to "09. change"
    @JsonProperty("10. change percent")
    private String changePercent; // Maps to "10. change percent"
    private Timestamp timestamp; //time when the info was pulled

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public Date getLatestTradingDay() {
        return latestTradingDay;
    }

    public void setLatestTradingDay(Date latestTradingDay) {
        this.latestTradingDay = latestTradingDay;
    }

    public double getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(double previousClose) {
        this.previousClose = previousClose;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "ticker='" + ticker + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", price=" + price +
                ", volume=" + volume +
                ", latestTradingDay=" + latestTradingDay +
                ", previousClose=" + previousClose +
                ", change=" + change +
                ", changePercent='" + changePercent + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
