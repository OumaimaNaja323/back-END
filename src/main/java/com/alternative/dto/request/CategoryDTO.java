package com.alternative.dto.request;

import java.util.List;

public class CategoryDTO {
    private Long id;
    private String name;
    private String description;
//    private List<ProductDTO> products;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
//    public List<ProductDTO> getProducts() { return products; }
//    public void setProducts(List<ProductDTO> products) { this.products = products; }
}