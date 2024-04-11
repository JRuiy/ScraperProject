package scraper.project.core.dbUtil.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.table.TableUtils;
import scraper.project.core.dbUtil.DBConnection;
import scraper.project.core.models.Exportable;

public class ModelsDaoImpl implements ModelsDao {

    private final DBConnection dbConnection;


    public static ModelsDao getInstance(String dbName, String creationPackage) {
        return new ModelsDaoImpl(dbName, creationPackage);
    }


    private ModelsDaoImpl(String dbName, String creationPackage) {
        this.dbConnection = DBConnection.getInstance(dbName, creationPackage);
    }

    @Override
    public <T extends Exportable> void saveModels(List<T> models) {
        if (!models.isEmpty()) {
            @SuppressWarnings("unchecked")
            Dao<T, ?> dao = (Dao<T, ?>) dbConnection.getDaoForClass(models.getFirst().getClass());
            try {
                for (T model : models) {
                    insertModel(model, dao);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Can't insert or update models: " + models, e);
            }
            models.clear();
        }
    }

    @Override
    public void dropDatabase() {
        for (Map.Entry<Class<?>, Dao<?, ?>> dao : dbConnection.getDaoMap().entrySet()) {
            try {
                TableUtils.dropTable(dao.getValue(), true);
            } catch (SQLException e) {
                throw new RuntimeException("Can't drop table for: " + dao.getKey(), e);
            }
        }
        dbConnection.createTableAndDao();
    }

    @Override
    public <T> void deleteAllModels(Class<T> clazz) {
        try {
            TableUtils.dropTable(dbConnection.getConnectionSource(), clazz,true);
            TableUtils.createTableIfNotExists(dbConnection.getConnectionSource(), clazz);
        } catch (SQLException e) {
            throw new RuntimeException("Can't drop table for: " + clazz, e);
        }
    }

    private <T extends Exportable> void insertModel(T model, Dao<T, ?> dao) throws SQLException {
        if (model != null) {
            dao.createOrUpdate(model);
        }
    }

    @Override
    public void closeConnection() {
        dbConnection.closeDatabaseConnection();
    }
}
