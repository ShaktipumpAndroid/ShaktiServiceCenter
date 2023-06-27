package bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppConfig {
    @SerializedName("ServiceCenterAppVersion")
    @Expose
    private String ServiceCenterAppVersion;
    @SerializedName("ServiceCenterAppURL")
    @Expose
    private String ServiceCenterAppURL;

    public String getServiceCenterAppVersion() {
        return ServiceCenterAppVersion;
    }

    public void setServiceCenterAppVersion(String serviceCenterAppVersion) {
        ServiceCenterAppVersion = serviceCenterAppVersion;
    }

    public String getServiceCenterAppURL() {
        return ServiceCenterAppURL;
    }

    public void setServiceCenterAppURL(String serviceCenterAppURL) {
        ServiceCenterAppURL = serviceCenterAppURL;
    }
}
