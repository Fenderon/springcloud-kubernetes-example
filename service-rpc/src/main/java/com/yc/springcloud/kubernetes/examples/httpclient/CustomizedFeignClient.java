package com.yc.springcloud.kubernetes.examples.httpclient;

import com.netflix.loadbalancer.Server;
import feign.Client;
import feign.Request;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 自定义feignClient
 *
 * @author yangchuan
 * @version 1.0
 * @createDate 2020/1/7
 */
@Slf4j
public class CustomizedFeignClient implements Client {


    private LocalServerListLoader localServerListLoader;

    private okhttp3.OkHttpClient delegate;

    public CustomizedFeignClient(okhttp3.OkHttpClient client,
                                 LocalServerListLoader localServerListLoader) {
        this.delegate = client;
        this.localServerListLoader = localServerListLoader;
    }

    static okhttp3.Request toOkHttpRequest(feign.Request input) {

        okhttp3.Request.Builder requestBuilder = new okhttp3.Request.Builder();
        requestBuilder.url(input.url());

        MediaType mediaType = null;
        boolean hasAcceptHeader = false;
        for (String field : input.headers().keySet()) {
            if (field.equalsIgnoreCase("Accept")) {
                hasAcceptHeader = true;
            }

            for (String value : input.headers().get(field)) {
                requestBuilder.addHeader(field, value);
                if (field.equalsIgnoreCase("Content-Type")) {
                    mediaType = MediaType.parse(value);
                    if (input.charset() != null) {
                        mediaType.charset(input.charset());
                    }
                }
            }
        }
        // Some servers choke on the default accept string.
        if (!hasAcceptHeader) {
            requestBuilder.addHeader("Accept", "*/*");
        }

        byte[] inputBody = input.requestBody().asBytes();
        boolean isMethodWithBody =
                Request.HttpMethod.POST == input.httpMethod() || Request.HttpMethod.PUT == input.httpMethod()
                        || Request.HttpMethod.PATCH == input.httpMethod();
        if (isMethodWithBody) {
            requestBuilder.removeHeader("Content-Type");
            if (inputBody == null) {
                // write an empty BODY to conform with okhttp 2.4.0+
                // http://johnfeng.github.io/blog/2015/06/30/okhttp-updates-post-wouldnt-be-allowed-to-have-null-body/
                inputBody = new byte[0];
            }
        }

        RequestBody body = inputBody != null ? RequestBody.create(mediaType, inputBody) : null;
        requestBuilder.method(input.httpMethod().name(), body);
        return requestBuilder.build();
    }

    private static feign.Response toFeignResponse(okhttp3.Response response, feign.Request request)
            throws IOException {
        return feign.Response.builder()
                .status(response.code())
                .reason(response.message())
                .request(request)
                .headers(toMap(response.headers()))
                .body(toBody(response.body()))
                .build();
    }

    private static Map<String, Collection<String>> toMap(Headers headers) {
        return (Map) headers.toMultimap();
    }

    private static feign.Response.Body toBody(final ResponseBody input) throws IOException {
        if (input == null || input.contentLength() == 0) {
            if (input != null) {
                input.close();
            }
            return null;
        }
        final Integer length = input.contentLength() >= 0 && input.contentLength() <= Integer.MAX_VALUE
                ? (int) input.contentLength()
                : null;

        return new feign.Response.Body() {

            @Override
            public void close() throws IOException {
                input.close();
            }

            @Override
            public Integer length() {
                return length;
            }

            @Override
            public boolean isRepeatable() {
                return false;
            }

            @Override
            public InputStream asInputStream() throws IOException {
                return input.byteStream();
            }

            @Override
            public Reader asReader() throws IOException {
                return input.charStream();
            }

            @Override
            public Reader asReader(Charset charset) throws IOException {
                return asReader();
            }
        };
    }

    @Override
    public feign.Response execute(feign.Request input, feign.Request.Options options)
            throws IOException {
        okhttp3.OkHttpClient requestScoped;
        if (delegate.connectTimeoutMillis() != options.connectTimeoutMillis()
                || delegate.readTimeoutMillis() != options.readTimeoutMillis()) {
            requestScoped = delegate.newBuilder()
                    .connectTimeout(options.connectTimeoutMillis(), TimeUnit.MILLISECONDS)
                    .readTimeout(options.readTimeoutMillis(), TimeUnit.MILLISECONDS)
                    .followRedirects(options.isFollowRedirects())
                    .build();
        } else {
            requestScoped = delegate;
        }

        //重写URL到本地服务
        input = rewrite(input);

        okhttp3.Request request = toOkHttpRequest(input);

        okhttp3.Response response = requestScoped.newCall(request).execute();
        return toFeignResponse(response, input).toBuilder().request(input).build();
    }

    /**
     * 重写URL，转发到本地
     *
     * @param input
     * @return
     */
    private Request rewrite(Request input) throws MalformedURLException {

        URL url = new URL(input.url());
        Server server = localServerListLoader.getLocalServer(url.getHost());

        if (server != null) {

            //重写URL
            String domain = String.format("%s://%s", server.getScheme(), server.getHostPort());
            StringBuilder builder = new StringBuilder();
            builder.append(domain).append(url.getPath());

            log.info("Rewrite Url to local: {}", builder.toString());

            return feign.Request.create(input.httpMethod(),
                    builder.toString(),
                    input.headers(),
                    input.requestBody().asBytes(),
                    input.charset());
        }

        return input;
    }
}
