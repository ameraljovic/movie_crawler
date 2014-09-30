package ba.aljovic.amer.component.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
class HttpRetriever
{
    public String retrieveDocument(String url) throws IOException
    {
        HttpGet getRequest = new HttpGet(url);
        getRequest.addHeader("Accept", "application/json");
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        CloseableHttpClient httpClient = clientBuilder.build();
        HttpResponse getResponse = httpClient.execute(getRequest);
        final int statusCode = getResponse.getStatusLine().getStatusCode();

        if (statusCode != HttpStatus.SC_OK)
            return null;

        HttpEntity getResponseEntity = getResponse.getEntity();
        return EntityUtils.toString(getResponseEntity);
    }

    public String searchMovie(String url, List<NameValuePair> urlParameters) throws IOException
    {
        StringBuffer result;
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        CloseableHttpClient client = clientBuilder.build();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36");
        post.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        HttpResponse response = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        result = new StringBuffer();
        String line;
        while ((line = rd.readLine()) != null)
        {
            result.append(line);
        }

        return result.toString();
    }
}
