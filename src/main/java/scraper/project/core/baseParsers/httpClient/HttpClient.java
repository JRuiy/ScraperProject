package scraper.project.core.baseParsers.httpClient;


import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import scraper.project.core.baseParsers.httpClient.dto.HttpResponseData;
import scraper.project.core.baseParsers.httpClient.handlers.HttpEntityHandler;
import scraper.project.core.readers.FileReaders;

import java.io.IOException;

public class HttpClient {
    private static final Logger log = LogManager.getLogger(HttpClient.class);
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
                HttpResponseData httpResponse = httpClient.execute(request, new HttpEntityHandler());
                int statusCode = httpResponse.statusCode();
                if (statusCode >= 400) {
                    log.warn("Received error response {} for {}", statusCode, url);
                    attempt++;
                    if (attempt >= maxTries) {
                        throw new RuntimeException("Request failed after " + maxTries + " attempts");
                    }
                    sleep(5);
                    continue;
                }
                return httpResponse.entity();
            } catch (Exception e) {
                log.warn("Failed to load page: {}", url);
                attempt++;
                if (attempt >= maxTries) {
                    throw new RuntimeException("Request failed after " + maxTries + " attempts", e);
                }
                sleep(5);
            }
        }
        return null;
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
                HttpResponseData httpResponse = httpClient.execute(request, new HttpEntityHandler());
                int statusCode = httpResponse.statusCode();
                if (httpResponse.statusCode() >= 400) {
                    log.warn("Received error response {} for {}", statusCode, url);
                    attempt++;
                    if (attempt >= maxTries) {
                        throw new RuntimeException("Request failed after " + maxTries + " attempts");
                    }
                    sleep(5);
                    continue;
                }
                return httpResponse.entity();
            } catch (Exception e) {
                log.warn("Failed to load page: {}", url);
                attempt++;
                if (attempt >= maxTries) {
                    throw new RuntimeException("Request failed after " + maxTries + " attempts", e);
                }
                sleep(5);
            }
        }
        return null;
    }

    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ignored) {
        }
    }

    public void close() {
        try {
            httpClient.close();
        } catch (IOException e) {
            log.warn("Failed to close HttpClient", e);
            throw new RuntimeException(e);
        }
    }
}
