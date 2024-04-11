package scraper.project.core.dbUtil.service;

import java.sql.SQLException;
import java.util.List;
import scraper.project.core.dbUtil.dao.ModelsDao;
import scraper.project.core.dbUtil.dao.ModelsDaoImpl;
import scraper.project.core.models.Exportable;

public class ModelsServiceImpl implements ModelsService{

    private final ModelsDao modelsDao;


    private ModelsServiceImpl(String dbName, String creationPackage) {
        this.modelsDao = ModelsDaoImpl.getInstance(dbName, creationPackage);
    }

    @Override
    public <T extends Exportable> void saveModels(List<T> models) {
        modelsDao.saveModels(models);
    }

    @Override
    public void dropDatabase() {
        modelsDao.dropDatabase();
    }

    @Override
    public <T> void deleteAllModels(Class<T> clazz) {
        modelsDao.deleteAllModels(clazz);
    }

    @Override
    public void closeConnection() {
        modelsDao.closeConnection();
    }
}
