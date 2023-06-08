package rest;


import com.google.gson.JsonElement;

import java.util.Map;

import model.VersionResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


public interface ApiInterface {

    @GET("app_version.htm")
    Call<VersionResponse> getVersionCode();

    @GET
    Call<JsonElement> postDataGET(@Url String remainingURL, @QueryMap Map<String, String> map);

    @GET
    Call<JsonElement> postDataGET(@Url String remainingURL);

}


