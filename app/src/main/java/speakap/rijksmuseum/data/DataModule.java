package speakap.rijksmuseum.data;

import android.app.Application;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

@Module
public class DataModule {
    private static final int DISK_CACHE_SIZE = 50 * 1024 * 1024;
    private static final String BASE_URL = "https://www.rijksmuseum.nl/api/";

    @Provides
    @Singleton
    static Gson provideGson() {
        return new GsonBuilder()
                .create();
    }

    @Provides
    @Singleton
    static HttpUrl provideBaseUrl() {
        return HttpUrl.parse(BASE_URL);
    }

    @Provides
    @Singleton
    static OkHttpClient provideOkHttpClient(Application app) {

        File cacheDir = new File(app.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);

        return new OkHttpClient.Builder()
                .cache(cache)
                .build();
    }

    @Provides
    @Singleton
    static Retrofit provideRetrofit(HttpUrl baseUrl,
                                    OkHttpClient okHttpClient,
                                    Gson gson) {

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    static RijksmuseumInternet provideRijksmuseumApi(Retrofit retrofit) {
        return retrofit.create(RijksmuseumInternet.class);
    }

    @Provides
    @Singleton
    static Picasso providePicasso(Application app, OkHttpClient client) {
        return new Picasso.Builder(app)
                .downloader(new OkHttp3Downloader(client))
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Timber.e(exception);
                    }
                })
                .build();
    }

}
