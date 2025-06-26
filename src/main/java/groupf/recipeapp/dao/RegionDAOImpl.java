// src/main/java/groupf/recipeapp/dao/RegionDAOImpl.java
package groupf.recipeapp.dao;

import groupf.recipeapp.entity.Region;
import groupf.recipeapp.util.DBUtil;

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
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            connection = DBUtil.getConnection();
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                regions.add(extractRegionFromResultSet(rs));
            }
        } finally {
            // ensure the resources are closed
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("Error closing ResultSet: " + e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }
            DBUtil.closeConnection(connection);
        }
        return regions;
    }

    @Override
    public Region getRegionById(int id) {
        String sql = "SELECT * FROM Region WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractRegionFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting region by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Region getRegionByCode(String code) throws SQLException { // fix: add throws SQLException
        String sql = "SELECT id, name, code FROM Region WHERE code = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, code);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractRegionFromResultSet(rs);
            }
        }
        // if no matching region is found, or SQLException occurs, let it throw.
        // because the method signature already declares throws SQLException.
        return null; // if not found, return null
    }

    private Region extractRegionFromResultSet(ResultSet rs) throws SQLException {
            // fix: use the constructor with parameters
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String code = rs.getString("code");
        return new Region(id, name, code);
    }

}