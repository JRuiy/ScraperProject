package scraper.project.core.dbUtil.service;

import java.sql.SQLException;
import java.util.List;
import scraper.project.core.models.Exportable;

public interface ModelsService {
    <T extends Exportable> void saveModels(List<T> models);
    void dropDatabase();
    <T> void deleteAllModels(Class<T> clazz);
    void closeConnection();
}
