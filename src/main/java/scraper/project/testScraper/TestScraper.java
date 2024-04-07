package scraper.project.testScraper;

import java.util.ArrayList;
import java.util.List;
import scraper.project.core.dbUtil.DBConnection;
import scraper.project.core.dbUtil.dao.ModelsDao;
import scraper.project.core.dbUtil.dao.ModelsDaoImpl;
import scraper.project.core.models.Exportable;

public class TestScraper {
//    private final ModelsDao dao = new ModelsDaoImpl("Chris", this.getClass().getPackageName());
    private List<ResultModel> resultModels = new ArrayList<>();
    private List<SearchModel> searchModels = new ArrayList<>();

    {
        ResultModel model = new ResultModel();
        model.setAge(1);
        model.setName("1");
        SearchModel model1 = new SearchModel();
        model1.setAsdas(2);
        model1.setName("2");
        resultModels.add(model);
        searchModels.add(model1);
    }
    public static void main(String[] args) {
        TestScraper scraper = new TestScraper();
        scraper.run();
    }

    private void run() {
//        try {
//            dao.saveModels(resultModels);
//        } catch (Exception e) {
//
//        }
//        try {
//            dao.saveModels(searchModels);
//        } catch (Exception e) {
//
//        }
    }
}
