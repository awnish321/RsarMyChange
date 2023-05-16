package rsarapp.com.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitAPIService {

    private static final String BASE_URL = "https://rachnasagar.in/";
    private static final String BASE_URL1 = "https://api.first.org/data/v1/";
    private static final String BASE_URL21 = "https://api.first.org/data/v1/";

    public static MyAPIs getApiClient() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

//        if (AllStaticFields.uName != null && AllStaticFields.uPass != null) {
//            clientBuilder.addInterceptor(new BasicAuthInterceptor(AllStaticFields.uName, AllStaticFields.uPass));
//        }

        OkHttpClient client = clientBuilder.addNetworkInterceptor(httpLoggingInterceptor)
                .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES) // read timeout
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(MyAPIs.class);
    }
    public static MyAPIs getTestApiClient() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

//        if (AllStaticFields.uName != null && AllStaticFields.uPass != null) {
//            clientBuilder.addInterceptor(new BasicAuthInterceptor(AllStaticFields.uName, AllStaticFields.uPass));
//        }

        OkHttpClient client = clientBuilder.addNetworkInterceptor(httpLoggingInterceptor)
                .connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                .readTimeout(5, TimeUnit.MINUTES) // read timeout
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL1)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        return retrofit.create(MyAPIs.class);
    }

}
