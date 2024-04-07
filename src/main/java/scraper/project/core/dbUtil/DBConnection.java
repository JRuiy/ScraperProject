package scraper.project.core.dbUtil;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;
import scraper.project.core.models.Exportable;


public class DBConnection {
    private static final String FOLDER_PATH = "data";
    private static final String JDBC_DRIVER = "jdbc:sqlite:";
    private final ConnectionSource connectionSource;
    private static DBConnection db = null;
    @Getter
    private static final Map<Class<?>, Dao<?, ?>> daoMap = new HashMap<>();
    private final String creationPackage;

    public static DBConnection getInstance(String dbName, String creationPackage) {
        if (db == null) {
            db = new DBConnection(FOLDER_PATH + File.separator + dbName + ".db", creationPackage);
        }
        return db;
    }

    private DBConnection(String dbAddress, String creationPackage) {
        this.creationPackage = creationPackage;
        try {
            connectionSource = new JdbcConnectionSource(JDBC_DRIVER + dbAddress);
        } catch (SQLException e) {
            throw new RuntimeException( "Failed to connect to database: " + dbAddress, e);
        }
        createTableAndDao();
    }

    private void createTableAndDao() {
        List<Class<?>> classes = getExportableClasses();
        for (Class<?> clazz : classes) {
            
            try {
                TableUtils.createTableIfNotExists(connectionSource, clazz);
                try {
                    TableUtils.createTableIfNotExists(connectionSource, clazz);
                    Dao<?, ?> dao = DaoManager.createDao(connectionSource, clazz);
                    daoMap.put(clazz, dao);
                } catch (SQLException e) {
                    throw new RuntimeException("Can't create table or dao for class " + clazz, e);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Can't create table or dao for class " + clazz, e);
            }
        }

    }

    private List<Class<?>> getExportableClasses() {
        List<Class<?>> entityClasses = new ArrayList<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            String packagePath = creationPackage.replace('.', '/');
            URL pkgURL = classLoader.getResource(packagePath);
            if (pkgURL != null) {
                String filePath = pkgURL.getFile();
                File directory = new File(filePath);
                if (directory.exists()) {
                    String[] files = directory.list();
                    if (files != null) {
                        for (String fileName : files) {
                            if (fileName.endsWith(".class")) {
                                String className = creationPackage + '.' + fileName.substring(0, fileName.length() - 6);
                                Class<?> cls = Class.forName(className);
                                if (Exportable.class.isAssignableFrom(cls)) {
                                    entityClasses.add(cls);
                                }
                            }
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't find Exportable class in package: " + creationPackage);
        }
        return entityClasses;
    }

    public <T> Dao<T, ?> getDaoForClass(Class<T> clazz) {
        @SuppressWarnings("unchecked")
        Dao<T, ?> dao = (Dao<T, ?>) daoMap.get(clazz);
        if (dao == null) {
            throw new IllegalArgumentException("No Dao found for class: " + clazz);
        }
        return dao;
    }
}
