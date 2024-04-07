package scraper.project.core.dbUtil.dao;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.DatabaseConnection;
import scraper.project.core.dbUtil.DBConnection;
import scraper.project.core.models.Exportable;

public class ModelsDaoImpl implements ModelsDao {

    private final DBConnection dbConnection;

    private static ModelsDao modelsDao = null;

    public static ModelsDao getInstance(String dbName, String creationPackage) {
        if (modelsDao == null) {
            modelsDao = new ModelsDaoImpl(dbName, creationPackage);
        }
        return modelsDao;
    }


    private ModelsDaoImpl(String dbName, String creationPackage) {
        this.dbConnection = DBConnection.getInstance(dbName, creationPackage);
    }

    @Override
    public <T extends Exportable> void saveModels(List<T> models) throws SQLException {
        if (!models.isEmpty()) {
            @SuppressWarnings("unchecked")
            Dao<T, ?> dao = (Dao<T, ?>) dbConnection.getDaoForClass(models.getFirst().getClass());
            for (T model : models) {
                insertModel(model, dao);
            }
        }
    }

    @Override
    public void dropDatabase() {

    }

    @Override
    public <T> void deleteAllModels(T model) {

    }

    private <T extends Exportable> void insertModel(T model, Dao<T, ?> dao) {
        if (model != null) {
            try {
                dao.createOrUpdate(model);
            } catch (SQLException e) {
                throw new RuntimeException("Can't insert or update model: " + model, e);
            }
        }
    }
}
