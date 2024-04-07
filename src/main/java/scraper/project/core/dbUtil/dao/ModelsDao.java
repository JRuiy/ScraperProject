package scraper.project.core.dbUtil.dao;

import java.sql.SQLException;
import java.util.List;
import scraper.project.core.models.Exportable;

public interface ModelsDao {
    <T extends Exportable> void saveModels(List<T> models) throws SQLException;
    void dropDatabase();
    <T> void deleteAllModels(T model);
}
