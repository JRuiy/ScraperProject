package scraper.project.core.baseParsers.htmlUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.htmlunit.BrowserVersion;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import scraper.project.core.readers.FileReaders;

public class HtmlUnitParser {
    private static final Logger log = LogManager.getLogger(HtmlUnitParser.class);
    private WebClient webClient;
    private HtmlPage page;
    private static String PROXY_HOST;
    private static int PROXY_PORT;

    public static HtmlUnitParser intiHtmlUnit(boolean isProxy, int proxy) {
        if (isProxy) {
            String[] fullProxy = FileReaders.loadProxy().getProperty(String.valueOf(proxy)).split(":");
            PROXY_HOST = fullProxy[0];
            PROXY_PORT = Integer.parseInt(fullProxy[1]);
        }
        return new HtmlUnitParser();
    }

    private void openWebClient() {
        if (PROXY_HOST != null) {
            webClient = new WebClient(BrowserVersion.CHROME, PROXY_HOST, PROXY_PORT);
        } else {
            webClient = new WebClient(BrowserVersion.CHROME);
        }
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setCssEnabled(false);
        webClient.setJavaScriptTimeout(30000);
        webClient.waitForBackgroundJavaScript(100 * 1000);
    }

    public HtmlPage getPage(String url) {
        if (webClient == null) {
            openWebClient();
        }
        int maxTries = 3;
        int attempt = 0;
        while (attempt < maxTries) {
            try {
                log.info("Try to load page [attempt {}]: {}", attempt + 1, url);
                page = webClient.getPage(url);
                return page;
            } catch (Exception e) {
                log.warn("Failed to load page: {}", url);
                attempt++;
                sleep(5000);
            }
        }
        return null;
    }

    public HtmlElement getByXpath(String xpath) {
        return page.getFirstByXPath(xpath);
    }

    public String getText(String path) {
        HtmlElement element = getByXpath(path);
        return element == null ? "" : element.asNormalizedText().trim();
    }

    public String getText(HtmlElement element, String path) {
        HtmlElement child = element.getFirstByXPath(path);
        return child == null ? "" : child.asNormalizedText().trim();
    }

    public String getAttribute(String xpath, String attribute) {
        return getByXpath(xpath).getAttribute(attribute).trim();
    }
    public String getAttribute(HtmlElement element, String xpath, String attribute) {
        return ((HtmlElement) element.getFirstByXPath(xpath)).getAttribute(attribute).trim();
    }

    public void setJavaScript(boolean isEnabled) {
        webClient.getOptions().setJavaScriptEnabled(isEnabled);
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (webClient != null) {
            webClient.close();
        }
    }

}
