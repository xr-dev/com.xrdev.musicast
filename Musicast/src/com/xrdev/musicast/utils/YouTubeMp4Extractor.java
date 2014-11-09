package com.xrdev.musicast.utils;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;

/**
 * Created by Guilherme on 10/10/2014.
 */
public class YouTubeMp4Extractor {
    private static final String TAG = "MP4Extractor";
    private static final String URL_ENCODED_STREAM_MAP     = "\"url_encoded_fmt_stream_map\":";

    private static final String YT_UTRL_PREFIX = "http://www.youtube.com/watch?v=";

    public static String extractMP4(String videoId) throws IOException {
        String urlSite = YT_UTRL_PREFIX + videoId;

        System.out.println(TAG + ": Extraindo MP4 da URL: " + urlSite);

        SSLContext sslContext = SSLContexts.createSystemDefault();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslContext,
                SSLConnectionSocketFactory.STRICT_HOSTNAME_VERIFIER);

        CloseableHttpClient  httpclient = HttpClientBuilder.create()
                .setSSLSocketFactory(sslsf)
                .build();
        // CloseableHttpClient httpclient = HttpClients.createDefault();

        String mp4Path = "";

        try {
            HttpGet httpget = new HttpGet(urlSite);

            System.out.println(TAG + ": Executando request: " + httpget.getRequestLine());


            // Criar response handler
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {

                public String handleResponse(
                        final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status >= 200 && status < 300) {
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }

            };

            String responseBody = httpclient.execute(httpget, responseHandler);

            if (responseBody.contains(URL_ENCODED_STREAM_MAP)) {
                // find the string we are looking for
                int start = responseBody.indexOf(URL_ENCODED_STREAM_MAP) + URL_ENCODED_STREAM_MAP.length() + 1;  // is the opening "
                String urlMap = responseBody.substring(start);
                int end = urlMap.indexOf("\"");
                if (end > 0) {
                    urlMap = urlMap.substring(0, end);
                }
                mp4Path = getURLEncodedStream(urlMap);

                System.out.println("Path MP4 extraído: " + mp4Path);

            }

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpclient.close();
        }
        return mp4Path;

    }

    private static String getURLEncodedStream(String stream) throws UnsupportedEncodingException {
        // replace all the \u0026 with &
        String str = stream.replace("\\u0026", "&");
        //str = java.net.URLDecoder.decode(stream, "UTF-8");
        //System.out.println("Raw URL map = " + str);
        String urlMap = str.substring(str.indexOf("url=http") + 4);
        // Percorrer urlMap até encontrar uma vírgula (,)
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < urlMap.length(); i++) {
            if ((urlMap.charAt(i) == '&') || (urlMap.charAt(i) == ','))
                break;
            else
                sb.append(urlMap.charAt(i));
        }
        //System.out.println(java.net.URLDecoder.decode(sb.toString(),"UTF-8"));
        return java.net.URLDecoder.decode(sb.toString(),"UTF-8");

    }

}
