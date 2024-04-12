package net.bis5.sandbox.criteria.thecomputerstore;

import java.math.BigDecimal;

public class ManufacturerProduct {

    private String manufacturerName;
    private String productName;
    private BigDecimal price;

    public String getManufacturerName() {
        return manufacturerName;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}