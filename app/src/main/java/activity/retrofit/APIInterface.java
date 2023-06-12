package activity.retrofit;

import org.json.JSONArray;

import activity.retrofit.Model.Assgin.AssignResponse;
import activity.retrofit.Model.LoginResponse.FiledLoginResponse;
import activity.retrofit.Model.PendingSiteComplain.PendingSiteComplainResponse;
import activity.retrofit.Model.Register.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIInterface {

    @GET("subordinate_register.htm?")
    Call<RegisterResponse> sendData(@Query("add") JSONArray jsonArray);

    @GET("cmp_assigned_subord.htm?")
    Call<AssignResponse> assignComplain(@Query("assign") JSONArray jsonArray);

    @GET("login_feild_technician.htm?")
    Call<FiledLoginResponse> login(@Query("userid") String username, @Query("pass") String password);

    @GET("pending_complain_service.htm?")
    Call<PendingSiteComplainResponse> getPendingVisiting(@Query("kunnr")String kunnr, @Query("fdate")String mStart, @Query("tdate") String mEnd);

    @GET("visited_complain.htm?")
    Call<PendingSiteComplainResponse> getVisitedComplain(@Query("kunnr")String kunnr, @Query("fdate")String mStart, @Query("tdate") String mEnd);
}
