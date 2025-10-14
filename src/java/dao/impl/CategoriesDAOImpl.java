package dao.impl;

import dao.CategoriesDAO;
import java.util.List;
import java.util.Optional;
import model.Categories;
import util.JdbcTemplateUtil;
import util.di.annotation.Repository;

@Repository
public class CategoriesDAOImpl implements CategoriesDAO {

    @Override
    public List<Categories> getAllCategories() {
        String sql = "SELECT * FROM dbo.Categories";
        return JdbcTemplateUtil.query(sql, Categories.class);
    }

    @Override
    public Optional<Categories> getCategoryById(Integer categoryId) {
        String sql = "SELECT * FROM dbo.Categories WHERE categoryId = ?";
        Categories category = JdbcTemplateUtil.queryOne(sql, Categories.class, categoryId);
        return Optional.ofNullable(category);
    }

    @Override
    public boolean addCategory(Categories category) {
        String sql = "INSERT INTO dbo.Categories(categoryName) VALUES (?)";
        int result = JdbcTemplateUtil.insertAndReturnKey(sql, category.getCategoryName());
        if (result > 0) {
            category.setCategoryId(result);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateCategory(Categories category) {
        String sql = "UPDATE dbo.Categories SET categoryName = ? WHERE categoryId = ?";
        int result = JdbcTemplateUtil.update(sql, category.getCategoryName(), category.getCategoryId());
        return result > 0;
    }

    @Override
    public boolean deleteCategory(Integer categoryId) {
        String sql = "DELETE FROM dbo.Categories WHERE categoryId = ?";
        int result = JdbcTemplateUtil.update(sql, categoryId);
        return result > 0;
    }
}
