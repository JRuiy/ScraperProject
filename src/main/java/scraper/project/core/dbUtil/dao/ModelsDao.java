package scraper.project.core.dbUtil.dao;

import java.util.List;
import scraper.project.core.models.Exportable;

public interface ModelsDao {
    <T extends Exportable> void saveModels(List<T> models);
    void dropDatabase();
    <T> void deleteAllModels(Class<T> clazz);
    void closeConnection();
}
