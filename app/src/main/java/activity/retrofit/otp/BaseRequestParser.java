package activity.retrofit.otp;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class BaseRequestParser {
    public String message = "Something went wrong.";
    public String mResponseCode = "0";

    private JSONObject mRespJSONObject = null;
    /**
     *
     * @param json
     * @return
     */
    public boolean parseJson(String json) {
        if (!TextUtils.isEmpty(json)) {
            try {
                mRespJSONObject = new JSONObject(json);
                if (null != mRespJSONObject) {
                    mResponseCode = mRespJSONObject.optString("status",
                            "SettingModelResponse code not found");
                    message = mRespJSONObject.optString("message",
                            "Something went wrong.");
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }


    public JSONArray getDataArray() {
        if (null == mRespJSONObject) {
            return null;
        }

        return mRespJSONObject.optJSONArray("SettingModelResponse");
    }

    public JSONObject getDataObject() {
        if (null == mRespJSONObject) {
            return null;
        }

        return mRespJSONObject.optJSONObject("Authentication");
    }

}
