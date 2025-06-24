package groupf.recipeapp.dao;

import groupf.recipeapp.entity.Region;
import java.util.List;
import java.sql.SQLException;

public interface RegionDAO {
    List<Region> getAllRegions() throws SQLException;
    Region getRegionById(int id) throws SQLException; // 可选：根据ID获取地区
    Region getRegionByCode(String code) throws SQLException; 
}