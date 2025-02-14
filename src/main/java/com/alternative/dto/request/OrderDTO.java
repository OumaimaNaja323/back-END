package com.alternative.dto.request;

import java.util.Date;
import java.util.List;

public class OrderDTO {
    private Long id;
    private UserDTO client;
    private Date orderDate;
    private String status;
    private double totalAmount;
    private UserDTO user;
    private List<OrderItemDTO> orderItems;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public UserDTO getClient() { return client; }
    public void setClient(UserDTO client) { this.client = client; }
    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public UserDTO getUser() { return user; }
    public void setUser(UserDTO user) { this.user = user; }
    public List<OrderItemDTO> getOrderItems() { return orderItems; }
    public void setOrderItems(List<OrderItemDTO> orderItems) { this.orderItems = orderItems; }
}