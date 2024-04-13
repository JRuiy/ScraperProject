package scraper.project.core.dbUtil.service;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scraper.project.core.dbUtil.dao.ModelsDao;
import scraper.project.core.dbUtil.dao.ModelsDaoImpl;
import scraper.project.core.models.Exportable;

public class ModelsServiceImpl implements ModelsService {
    private static final Logger log = LogManager.getLogger(ModelsServiceImpl.class);
    private final ModelsDao modelsDao;

    public static ModelsServiceImpl getInstance(String dbName, String creationPackage) {
        return new ModelsServiceImpl(dbName, creationPackage);
    }

    private ModelsServiceImpl(String dbName, String creationPackage) {
        this.modelsDao = ModelsDaoImpl.getInstance(dbName, creationPackage);
    }

    @Override
    public <T extends Exportable> void saveModels(List<T> models) {
        log.info("Saved {} models to {}", models.size(), modelsDao.getDbName());
        modelsDao.saveModels(models);
    }

    @Override
    public void dropDatabase() {
        log.info("Droped all tables in {}", modelsDao.getDbName());
        modelsDao.dropDatabase();
    }

    @Override
    public <T> void deleteAllModels(Class<T> clazz) {
        log.info("Deleted models from {} table in {}", clazz.getSimpleName(), modelsDao.getDbName());
        modelsDao.deleteAllModels(clazz);
    }

    @Override
    public void closeConnection() {
        log.info("Close connection to {}", modelsDao.getDbName());
        modelsDao.closeConnection();
    }
}
