package scraper.project.testScraper;

import java.util.ArrayList;
import java.util.List;
import scraper.project.core.dbUtil.dao.ModelsDao;
import scraper.project.core.dbUtil.dao.ModelsDaoImpl;

public class TestScraper {
    private final ModelsDao dao = ModelsDaoImpl.getInstance("Chris", this.getClass().getPackageName());
    private List<ResultModel> resultModels = new ArrayList<>();
    private List<SearchModel> searchModels = new ArrayList<>();

    public static void main(String[] args) {
        TestScraper scraper = new TestScraper();
        scraper.initiate();
    }

    private void initiate() {
        try {
            run();
        } finally {
            dao.closeConnection();
        }
    }

    private void run() {
        dao.dropDatabase();
        for (int i = 0; i < 100; i++) {
            ResultModel model = new ResultModel();
            model.setName(String.valueOf(i));
            resultModels.add(model);
        }
        dao.saveModels(resultModels);
        for (int i = 100; i < 200; i++) {
            ResultModel model = new ResultModel();
            model.setName(String.valueOf(i));
            resultModels.add(model);
        }
        dao.saveModels(resultModels);
    }
}
