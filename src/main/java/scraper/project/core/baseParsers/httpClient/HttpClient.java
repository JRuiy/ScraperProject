package scraper.project.core.baseParsers.httpClient;


import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scraper.project.core.baseParsers.playwright.PlaywrightParser;
import scraper.project.core.readers.FileReaders;

public class HttpClient {
    private static final Logger log = LogManager.getLogger(PlaywrightParser.class);
    private final CloseableHttpClient httpClient;

    public static HttpClient createHttpClient(boolean isProxy, int proxy) {
        return new HttpClient(isProxy, proxy);
    }

    private HttpClient(boolean isProxy, int proxy) {
        if (isProxy) {
            String[] hostPort = FileReaders.loadProxy().getProperty(String.valueOf(proxy)).split(":");
            httpClient = HttpClients.custom()
                    .setRoutePlanner(new DefaultProxyRoutePlanner(new HttpHost(hostPort[0], Integer.parseInt(hostPort[1]))))
                    .build();
        } else {
            httpClient = HttpClients.createDefault();
        }
    }

    public String get(String url) {
        int maxTries = 3;
        int attempt = 0;

        while (attempt < maxTries) {
            try {
                log.info("Try to load page [attempt {}]: {}", attempt + 1, url);
                HttpGet request = new HttpGet(url);
                ClassicHttpResponse httpResponse = httpClient.execute(request, response -> response);
                int statusCode = httpResponse.getCode();
                if (httpResponse.getCode() >= 400) {
                    log.warn("Received error response {} for {}", statusCode, url);
                    attempt++;
                    if (attempt >= maxTries) {
                        throw new RuntimeException("Request failed after " + maxTries + " attempts");
                    }
                    sleep(5);
                    continue;
                }
                return EntityUtils.toString(httpResponse.getEntity());
            } catch (Exception e) {
                log.warn("Failed to load page: {}", url);
                attempt++;
                if (attempt >= maxTries) {
                    throw new RuntimeException("Request failed after " + maxTries + " attempts", e);
                }
                sleep(5);
            }
        }
        return url;
    }

    public String post(String url, String payload, Header[] headers) {
        int maxTries = 3;
        int attempt = 0;

        while (attempt < maxTries) {
            try {
                log.info("Try to load page [attempt {}]: {}", attempt + 1, url);
                HttpPost request = new HttpPost(url);
                request.setEntity(new StringEntity(payload));
                request.setHeaders(headers);
                ClassicHttpResponse httpResponse = httpClient.execute(request, response -> response);
                int statusCode = httpResponse.getCode();
                if (httpResponse.getCode() >= 400) {
                    log.warn("Received error response {} for {}", statusCode, url);
                    attempt++;
                    if (attempt >= maxTries) {
                        throw new RuntimeException("Request failed after " + maxTries + " attempts");
                    }
                    sleep(5);
                    continue;
                }
                return EntityUtils.toString(httpResponse.getEntity());
            } catch (Exception e) {
                log.warn("Failed to load page: {}", url);
                attempt++;
                if (attempt >= maxTries) {
                    throw new RuntimeException("Request failed after " + maxTries + " attempts", e);
                }
                sleep(5);
            }
        }
        return url;
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ignored) {
        }
    }
}
