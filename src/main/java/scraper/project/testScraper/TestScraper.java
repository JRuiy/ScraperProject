package scraper.project.testScraper;

import java.util.ArrayList;
import java.util.List;
import scraper.project.core.dbUtil.service.ModelsService;
import scraper.project.core.dbUtil.service.ModelsServiceImpl;

public class TestScraper {
    private final ModelsService db = ModelsServiceImpl.getInstance("Chris", this.getClass().getPackageName());
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
            db.closeConnection();
        }
    }

    private void run() {
        db.dropDatabase();
        for (int i = 0; i < 100; i++) {
            ResultModel model = new ResultModel();
            model.setName(String.valueOf(i));
            resultModels.add(model);
        }
        db.saveModels(resultModels);
        for (int i = 100; i < 200; i++) {
            ResultModel model = new ResultModel();
            model.setName(String.valueOf(i));
            resultModels.add(model);
        }
        db.saveModels(resultModels);
    }
}
