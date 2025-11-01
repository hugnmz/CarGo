/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service.impl;

import dao.CategoriesDAO;
import dto.CategoryDTO;
import java.util.ArrayList;
import java.util.List;
import mapper.CategoryMapper;
import model.Categories;
import service.CategoryService;
import util.di.annotation.Autowired;
import util.di.annotation.Service;

/**
 *
 * @author Admin
 */
@Service
public class CategoryServiceIml implements CategoryService {

    @Autowired
    private CategoriesDAO categoryDAO;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryDTO> getAllLocations() {
        try {
            List<Categories> listCategories = categoryDAO.getAllCategories();
            List<CategoryDTO> listDTO = new ArrayList<>();
            for (Categories c : listCategories) {
                listDTO.add(categoryMapper.toDTO(c));
            }
            return listDTO;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

}
