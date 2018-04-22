package com.hispeed.development.util;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author wuwl@19pay.com.cn
 * @date 2018-4-22
 * @desc okhttp客户端
 */
public class OkClient {

    private static volatile OkClient okClient = null;

    private OkClient() {}

    public static OkClient getInstance() {
        if (okClient == null) {
           synchronized (OkClient.class) {
               if (okClient == null) {
                   okClient = new OkClient();
               }
            }
        }
        return okClient;
    }

    public String sendGet(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        System.out.println(url);
        Call call = client.newCall(request);
        String result;
        try {
            Response response = call.execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
