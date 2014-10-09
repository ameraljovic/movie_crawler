package ba.aljovic.amer.component.service;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Component
public class HttpRetriever
{
    private CloseableHttpClient httpClient;

    @PostConstruct
    public void init()
    {
        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        httpClient = clientBuilder.build();
    }

    public String retrieveDocument(String url) throws IOException
    {
        HttpGet getRequest = new HttpGet(url);
        HttpClientContext context = HttpClientContext.create();
        CloseableHttpResponse response = httpClient.execute(getRequest, context);
        try
        {
            final int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != HttpStatus.SC_OK)
                return null;

            HttpEntity getResponseEntity = response.getEntity();

            return EntityUtils.toString(getResponseEntity);
        }
        finally
        {
            response.close();
        }
    }

    public String searchMovie(String url, List<NameValuePair> urlParameters) throws IOException
    {
        StringBuffer result;
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.153 Safari/537.36");
        post.setHeader("Accept-Encoding", "gzip,deflate,sdch");
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        CloseableHttpResponse response = httpClient.execute(post);
        try
        {
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            result = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null)
            {
                result.append(line);
            }

            return result.toString();
        }
        finally
        {
            response.close();
        }
    }
}
