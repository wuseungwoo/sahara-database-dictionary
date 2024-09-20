package com.sahara.dictionary.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class YouDaoUtils {
    private static final String APP_KEY = "0be0f2830a3291de";
    private static final String APP_SECRET = "FTA6Jq8jRODOOcUhHgvITfYj4pAMuK2L";
    private static int MAX_RETRIES = 3;

    public static String translate(String text, String fromLang, String toLang) {
        if (MAX_RETRIES < 0) {
            MAX_RETRIES = 3;
            return text;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (text.contains("_")) {
            text = text.replace("_", " ");
        }
        String url = "https://openapi.youdao.com/api";
        String salt = String.valueOf(System.currentTimeMillis());
        String sign = makeSign(APP_KEY, text, salt, APP_SECRET);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

            StringEntity entity = new StringEntity(
                    "q=" + text +
                            "&from=" + fromLang +
                            "&to=" + toLang +
                            "&appKey=" + APP_KEY +
                            "&salt=" + salt +
                            "&sign=" + sign,
                    StandardCharsets.UTF_8
            );
            httpPost.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    String result = EntityUtils.toString(responseEntity, StandardCharsets.UTF_8);
                    return parseTranslationResult(result, text, fromLang, toLang);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String makeSign(String appKey, String text, String salt, String appSecret) {
        StringBuilder sb = new StringBuilder(appKey);
        sb.append(text).append(salt).append(appSecret);
        return DigestUtils.md5Hex(sb.toString());
    }

    private static String parseTranslationResult(String result, String text, String fromLang, String toLang) {
        // 假设结果为 JSON 格式
        // {"errorCode":0,"translation":["你好，世界！"],"smartResult":{"type":1,"entries":["hello world"]}}
        try {
            JSONObject jsonObject = JSONObject.parseObject(result);
            int errorCode = jsonObject.getIntValue("errorCode");
            if (errorCode == 0) {
                JSONArray translationArray = jsonObject.getJSONArray("translation");
                if (!translationArray.isEmpty()) {
                    System.out.println(translationArray.toJSONString());
                    return translationArray.getString(0);
                } else {
                    MAX_RETRIES--;
                    translate(text, fromLang, toLang);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
