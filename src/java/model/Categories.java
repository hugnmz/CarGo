package model;

import java.util.List;
import util.di.annotation.Column;


public class Categories {
    
    @Column()
    private Integer categoryId;       // ID danh mục
    
    @Column()
    private String categoryName;      
    
    @Column()
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
