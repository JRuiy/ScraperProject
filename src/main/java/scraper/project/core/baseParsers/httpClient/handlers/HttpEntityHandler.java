package scraper.project.core.baseParsers.httpClient.handlers;

import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import scraper.project.core.baseParsers.httpClient.dto.HttpResponseData;

import java.io.IOException;

public class HttpEntityHandler implements HttpClientResponseHandler<HttpResponseData> {
    @Override
    public HttpResponseData handleResponse(ClassicHttpResponse response) throws HttpException, IOException {
        int statusCode = response.getCode();
        String entity = EntityUtils.toString(response.getEntity());
        return new HttpResponseData(statusCode, entity);
    }
}
