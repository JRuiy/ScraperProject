package scraper.project.core.baseParsers.playwright;

import java.util.List;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.Proxy;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scraper.project.core.readers.FileReaders;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlaywrightParser {
    private static final Logger log = LogManager.getLogger(PlaywrightParser.class);
    private Playwright playwright;
    private Browser browser;
    private BrowserContext browserContext;
    private Page page;
    private static boolean headless;
    private static String defaultProxy;

    public static PlaywrightParser intiPlaywright(boolean withWindow, boolean isProxy, int proxy) {
        headless = !withWindow;
        if (isProxy) {
            defaultProxy = FileReaders.loadProxy().getProperty(String.valueOf(proxy));
        }
        return new PlaywrightParser();
    }

    public void openPlaywright() {
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(headless).setArgs(List.of("--start-maximized"));
        if (defaultProxy != null) {
            options.setProxy(new Proxy("http://" + defaultProxy));
        }
        playwright = Playwright.create();
        browser = playwright.chromium().launch(options);
        browserContext = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        page = browserContext.newPage();
        log.info("Open an playwright");
    }

    public void getPage(String url) {
        getPage(url, 0);
    }

    private void getPage(String url, int attempt) {
        if (attempt >= 5) {
            log.info("Can't load the page {}", url);
            return;
        }

        try {
            log.info("Try to load page: {}", url);
            Response response = page.navigate(url);
            String status = String.valueOf(response.status());
            if ((status.startsWith("4") || status.startsWith("5"))) {
                log.info("Try to load page [attempt {}]: {}", attempt, url);
                getPage(url, attempt + 1);
            }
        } catch (Exception e) {
            log.info("Try to load page [attempt {}]: {}", attempt, url);
            getPage(url, attempt + 1);
        }
    }

    public void getNewContext() {
        browserContext.close();
        browserContext = browser.newContext(new Browser.NewContextOptions().setViewportSize(null));
        page = browserContext.newPage();
        log.info("Loaded new context");
    }

    public void closeContext() {
        browserContext.close();
        log.info("Close context");
    }

    public void close() {
        browserContext.close();
        browser.close();
        playwright.close();
    }

    public void sleep(long millis) {
        try {
            log.info("Sleep for {} millis", millis);
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
