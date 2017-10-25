package com.su.doublesampling;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 苏照亮 on 2017/10/25.
 */

public class OkHttpManager {

    private static OkHttpManager okHttpManager;
    private OkHttpClient okHttpClient;
    private ResultCallback resultCallBack;
    private Gson gson;

    private OkHttpManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        okHttpClient = builder.build();
        gson = new Gson();
    }

    public static OkHttpManager getIntance() {
        if (okHttpManager == null) {
            synchronized (OkHttpManager.class) {
                if (okHttpManager == null) {
                    okHttpManager = new OkHttpManager();
                }
            }
        }
        return okHttpManager;
    }

    public void request(String url, Map<String, Object> params, ResultCallback callBack) {

        this.resultCallBack = callBack;

        Request request;
        if (params == null || params.size() == 0) {
            request = new Request.Builder().url(url).get().build();
        } else {
            MultipartBody.Builder multipartBody = new MultipartBody.Builder();
            multipartBody.setType(MultipartBody.FORM);
            for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
                if (stringObjectEntry.getValue() instanceof File) {
                    File file = (File) stringObjectEntry.getValue();
                    multipartBody.addFormDataPart(stringObjectEntry.getKey(),
                            file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
                } else {
                    multipartBody.addFormDataPart(stringObjectEntry.getKey(), (String) stringObjectEntry.getValue());
                }
            }
            MultipartBody build = multipartBody.build();
            request = new Request.Builder().url(url).post(build).build();
        }

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                resultCallBack.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    resultCallBack.onResponse(call, gson.fromJson(json,resultCallBack.mType), json);
                }
            }
        });

    }

    public static abstract class ResultCallback<T>
    {
        Type mType;

        public ResultCallback()
        {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass)
        {
            //通过反射得到泛型参数
            //Type是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class)
            {
                throw new RuntimeException("Missing type parameter.");
            }
            //ParameterizedType参数化类型，即泛型
            ParameterizedType parameterized = (ParameterizedType) superclass;
            //getActualTypeArguments获取参数化类型的数组，泛型可能有多个
            //将Java 中的Type实现,转化为自己内部的数据实现,得到gson解析需要的泛型
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onFailure(Call call, IOException e);
        public abstract void onResponse(Call call,T response,String json);
    }

}
