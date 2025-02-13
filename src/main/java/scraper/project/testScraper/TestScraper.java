package scraper.project.testScraper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scraper.project.core.baseParsers.htmlUnit.HtmlUnitParser;
import scraper.project.core.baseParsers.httpClient.HttpClient;
import scraper.project.core.baseParsers.playwright.PlaywrightParser;
import scraper.project.core.dbUtil.service.ModelsService;
import scraper.project.core.dbUtil.service.ModelsServiceImpl;

public class TestScraper {
    private static final Logger log = LogManager.getLogger(TestScraper.class);
    private final ModelsService db = ModelsServiceImpl.getInstance(this.getClass().getSimpleName(),
            this.getClass().getPackageName());
    public static final boolean WITH_WINDOW = true;
    public static final boolean IS_PROXY = true;
    public static final int PROXY = 1;
    private final PlaywrightParser pl = PlaywrightParser.intiPlaywright(WITH_WINDOW, IS_PROXY, PROXY);
    private final HtmlUnitParser hu = HtmlUnitParser.intiHtmlUnit(IS_PROXY, PROXY);
    private final HttpClient hc = HttpClient.createHttpClient(IS_PROXY, PROXY);

    public static void main(String[] args) {
        TestScraper scraper = new TestScraper();
        scraper.initiate();
    }

    private void initiate() {
        try {
            run();
        } finally {
            db.closeConnection();
            pl.close();
            hu.close();
            hc.close();
        }
    }

    private void run() {
        log.info("Start scrape");
        String s = hc.get("https://aca-prod.accela.com/AACO/Cap/CapHome.aspx?module=Permits&TabName=Permits&TabList=Home");
        System.out.println("a");
        pl.openPlaywright();
//        pl.getPage("https://aca-prod.accela.com/AACO/Cap/CapHome.aspx?module=Permits&TabName=Permits&TabList=Home");
//        pl.sleep(3000);
//        pl.getNewContext();
//        pl.sleep(3000);
    }
}
