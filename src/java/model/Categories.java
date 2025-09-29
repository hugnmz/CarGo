package model;

import java.util.List;


public class Categories {
    private Integer categoryId;       // ID danh mục
    private String categoryName;      
    private List<Cars> cars;         
    
    // Constructors
    public Categories() {}
    
    public Categories(String categoryName) {
        this.categoryName = categoryName;
    }
    
    // Getters and Setters
    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
    
    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    public List<Cars> getCars() { return cars; }
    public void setCars(List<Cars> cars) { this.cars = cars; }
}
