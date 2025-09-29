/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package dao;

import java.util.List;
import java.util.Optional;
import model.Categories;

/**
 *
 * @author admin
 */
public interface CategoriesDAO {
    List<Categories> getAllCategories();
    Optional<Categories> getCategoryById(Integer CategoryId);
    boolean addCategory(Categories category);
    boolean updateCategory(Categories category);
    boolean deleteCategory(Integer categoryId);
}
