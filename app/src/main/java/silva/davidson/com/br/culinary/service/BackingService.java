package silva.davidson.com.br.culinary.service;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import silva.davidson.com.br.culinary.model.Recipe;

public class BackingService {

    private static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    private static final OkHttpClient HTTP_CLIENT;
    static {
        HTTP_CLIENT = new OkHttpClient.Builder().build();
    }

    public interface BakingServiceClient {
        @GET("baking.json")
        retrofit2.Call<ArrayList<Recipe>> listRecipes();
    }

    private BackingService(){

    }

    public static BakingServiceClient getClient() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(HTTP_CLIENT)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BakingServiceClient.class);
    }

}
