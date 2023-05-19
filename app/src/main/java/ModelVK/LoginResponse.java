
package ModelVK;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class LoginResponse {

    @SerializedName("kunnr")
    private String mKunnr;
    @SerializedName("name1")
    private String mName1;

    public String getKunnr() {
        return mKunnr;
    }

    public void setKunnr(String kunnr) {
        mKunnr = kunnr;
    }

    public String getName1() {
        return mName1;
    }

    public void setName1(String name1) {
        mName1 = name1;
    }



}
