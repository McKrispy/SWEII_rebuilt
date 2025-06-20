// src/main/java/groupf/recipeapp/dao/RegionDAOImpl.java
package groupf.recipeapp.dao;

import groupf.recipeapp.entity.Region;
import groupf.recipeapp.util.DBUtil;
import groupf.recipeapp.entity.Recipe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RegionDAOImpl implements RegionDAO {

    @Override
    public List<Region> getAllRegions() throws SQLException {
        List<Region> regions = new ArrayList<>();
        String sql = "SELECT id, name, code FROM region";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String code = rs.getString("code");
                regions.add(new Region(id, name, code));
            }
        }
        return regions;
    }

    @Override
    public Region getRegionById(int id) throws SQLException {
        String sql = "SELECT id, name, code FROM region WHERE id = ?";
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String code = rs.getString("code");
                    return new Region(id, name, code);
                }
            }
        }
        return null;
    }

}