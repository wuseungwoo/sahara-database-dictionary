package com.sahara.dictionary;

import com.sahara.dictionary.util.YouDaoUtils;

public class YoudaoTranslationTest {

    private static final String APP_KEY = "0be0f2830a3291de";
    private static final String APP_SECRET = "FTA6Jq8jRODOOcUhHgvITfYj4pAMuK2L";

    public static void main(String[] args) {
        String textToTranslate = "need_transcode";
        String translatedText = YouDaoUtils.translate(textToTranslate, "en", "zh");
        System.out.println("Translated Text: " + translatedText);
    }
}

