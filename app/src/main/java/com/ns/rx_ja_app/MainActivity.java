package com.ns.rx_ja_app;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ns.rx_ja_app.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Supplier;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
private ActivityMainBinding mainBinding;
private static final String TAG = "RxAndroidSamples";
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        List<String> list=new ArrayList<>();
        list.add("John");
        list.add("Allen");


        Observable.just("A","Z","D","Y").subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(@NonNull String s) {
                        Log.d(TAG, "onNext(" + s + ")");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError()", e);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete()");
                    }
                });

        mainBinding.btnScheduler.setOnClickListener(v -> {
            onRunSchedulerClick();
        });

    }

   void onRunSchedulerClick(){
        disposables.add(sampleObservable().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>(){
                    @Override
                    public void onNext(@NonNull String s) {
                        Log.e(TAG, "onNext(" + s + ")");
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError()", e);
                    }
                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete()");
                    }
                }));
    }

    static Observable<String> sampleObservable(){
        return Observable.defer(new Supplier<ObservableSource<? extends String>>() {
            @Override
            public ObservableSource<? extends String> get() throws Throwable {
              //  SystemClock.sleep(5000);
                return Observable.just("One","Two","Three","Four");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}