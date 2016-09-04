package com.bboylin.dlavailableroom.ViewModel;

import com.bboylin.dlavailableroom.Model.DLAvailableRoomResult;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * retrofit网络请求封装的类
 * Created by lin on 2016/8/28.
 * Bean:DLAvailableRoomResult
 */
public class HttpMethod {
    public static final String BASE_URL = "your base url";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;

    private DLAvailableRoomService dlAvailableRoomService;

    private HttpMethod(){
        //如果有多个不同的网络请求,应该将此步骤封装在DLAvailableRoomService的factory类里
        OkHttpClient.Builder okHttpClientBuilder=new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder()
                .client(okHttpClientBuilder.build())
                .baseUrl(BASE_URL)//主机地址
                .addConverterFactory(GsonConverterFactory.create())//解析方法
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        dlAvailableRoomService = retrofit.create(DLAvailableRoomService.class);
    }

    private static class SingletonHolder{
        private static final HttpMethod INSTANCE=new HttpMethod();
    }

    public static HttpMethod getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public void getDLAvailableRoom(Subscriber<DLAvailableRoomResult> subscriber){
        dlAvailableRoomService.getDLAvailableRoomResult()
                //可加入filter，map等操作处理得到的数据
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
