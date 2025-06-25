package groupf.recipeapp.dao;

import groupf.recipeapp.dao.IngredientDAO;
import groupf.recipeapp.entity.Ingredient;
import groupf.recipeapp.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAOImpl implements IngredientDAO {


    @Override
    public Ingredient getIngredientByName(String name) {
        String sql = "SELECT * FROM ingredient WHERE name = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Ingredient(rs.getInt("id"), rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Ingredient> getAllIngredients() {
        List<Ingredient> list = new ArrayList<>();
        String sql = "SELECT * FROM ingredient";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Ingredient(rs.getInt("id"), rs.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 重命名 addIngredient 为 insertIngredient
    @Override
    public boolean insertIngredient(Ingredient ingredient) throws SQLException { // 添加 throws SQLException
        String sql = "INSERT INTO ingredient (name) VALUES (?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, ingredient.getName());
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    ingredient.setId(keys.getInt(1));
                }
                return true;
            }
        } // try-with-resources 会自动关闭 conn 和 stmt
        return false;
    }

    @Override
    public Ingredient getIngredientById(int id) {
        String sql = "SELECT * FROM ingredient WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Ingredient ingredient = new Ingredient();
                ingredient.setId(rs.getInt("id"));
                ingredient.setName(rs.getString("name"));
                return ingredient;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
