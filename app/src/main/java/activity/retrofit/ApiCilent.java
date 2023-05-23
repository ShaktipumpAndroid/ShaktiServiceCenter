package activity.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiCilent {

    private static Retrofit retrofit = null;
    public static Retrofit getClient() {

        retrofit = new Retrofit.Builder()
                .baseUrl("https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmap_srv_center/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }
}
