package me.zhouzhuo810.zznote;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.schedulers.Schedulers;

public class RxHelper {

    public static <T> ObservableTransformer<T, T> io_main() {
        return new ObservableTransformer<T, T>() {
            @Override
            public Observable<T> apply(Observable<T> tObservable) {
                return tObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(JavaFxScheduler.platform());
            }
        };
    }
}
