package com.ecell.end_eavour.events.ragstoriches;

public class Stock_Model {

    public String bidrate;
    public String highestBid;
    public String image;
    public String stockDesc;
    public String stockHolder;
    public String stockHolderId;
    public String stockId;
    public String stockName;

    public Stock_Model() {
    }

    public Stock_Model(String bidrate, String highestBid, String image, String stockDesc, String stockHolder, String stockHolderID, String stockid, String stockName) {
        this.bidrate = bidrate;
        this.highestBid = highestBid;
        this.image = image;
        this.stockDesc = stockDesc;
        this.stockHolder = stockHolder;
        this.stockHolderId = stockHolderID;
        this.stockId = stockid;
        this.stockName = stockName;
    }

    public String getBidrate() {
        return bidrate;
    }

    public void setBidrate(String bidrate) {
        this.bidrate = bidrate;
    }

    public String getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(String highestBid) {
        this.highestBid = highestBid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStockDesc() {
        return stockDesc;
    }

    public void setStockDesc(String stockDesc) {
        this.stockDesc = stockDesc;
    }

    public String getStockHolder() {
        return stockHolder;
    }

    public void setStockHolder(String stockHolder) {
        this.stockHolder = stockHolder;
    }

    public String getStockHolderId() {
        return stockHolderId;
    }

    public void setStockHolderId(String stockHolderId) {
        this.stockHolderId = stockHolderId;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
}
