
package ModelVK;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class LoginModel {

    @SerializedName("message")
    private String mMessage;
    @SerializedName("response")
    private List<LoginResponse> mResponse;
    @SerializedName("status")
    private String mStatus;

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public List<LoginResponse> getResponse() {
        return mResponse;
    }

    public void setResponse(List<LoginResponse> response) {
        mResponse = response;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
