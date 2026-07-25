package com.seckill.entity;

public class Stock {
    private Long id;
    private Long productId;
    private String productName;
    private Integer totalStock;
    private Integer remainingStock;
    private Integer version;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getTotalStock() { return totalStock; }
    public void setTotalStock(Integer totalStock) { this.totalStock = totalStock; }
    public Integer getRemainingStock() { return remainingStock; }
    public void setRemainingStock(Integer remainingStock) { this.remainingStock = remainingStock; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
}
