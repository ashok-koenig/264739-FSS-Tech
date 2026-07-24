package com.example.demo.model;

public class Payment {
    private Integer orderId;
    private Double amount;
    private String status;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "orderId=" + orderId +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}
