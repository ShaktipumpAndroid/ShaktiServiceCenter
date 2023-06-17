package activity.retrofit.otp;

/**
 * Created by Suruchi on 11/17/2016.
 */
public interface RequestReciever {
    void onSuccess(int APINumber, String Json, Object object);

    void onFailure(int APINumber, String errorCode, String message);

    void onNetworkFailure(int APINumber, String message);
}
