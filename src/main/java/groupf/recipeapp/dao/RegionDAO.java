package groupf.recipeapp.dao;

import groupf.recipeapp.entity.Region;
import java.util.List;
import java.sql.SQLException;

public interface RegionDAO {
    /**
     * Retrieves a list of all regions from the database.
     * @return A list of Region objects.
     * @throws SQLException If a database access error occurs.
     */
    List<Region> getAllRegions() throws SQLException;
    /**
     * Retrieves a single region by its unique ID.
     * @param id The unique ID of the region to retrieve.
     * @return The Region object corresponding to the given ID, or null if not found.
     * @throws SQLException If a database access error occurs.
     */
    Region getRegionById(int id) throws SQLException; 
    /**
     * Retrieves a single region by its unique code (e.g., country code).
     * @param code The unique code of the region to retrieve.
     * @return The Region object corresponding to the given code, or null if not found.
     * @throws SQLException If a database access error occurs.
     */
    Region getRegionByCode(String code) throws SQLException; 
}