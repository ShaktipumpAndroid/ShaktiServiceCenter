package activity.retrofit;

import org.json.JSONArray;

import activity.retrofit.Model.Register.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {


    @GET("subordinate_register.htm?")
    Call<RegisterResponse> sendData(@Query("add") JSONArray jsonArray);

}
