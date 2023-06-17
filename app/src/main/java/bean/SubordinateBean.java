package bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class SubordinateBean implements Parcelable {

    String mobileNo;
    String aadharNo;
    String name;
    String fromDate;
    String toDate;
    String state;
    String district;
    String password;


    public SubordinateBean(String mobileNo, String aadharNo, String name, String fromDate, String toDate, String state, String district, String password) {
        this.mobileNo = mobileNo;
        this.aadharNo = aadharNo;
        this.name = name;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.state = state;
        this.district = district;
        this.password = password;
    }

    public SubordinateBean() {

    }

    protected SubordinateBean(Parcel in) {
        mobileNo = in.readString();
        aadharNo = in.readString();
        name = in.readString();
        fromDate = in.readString();
        toDate = in.readString();
        state = in.readString();
        district = in.readString();
        password = in.readString();
    }

    public static final Creator<SubordinateBean> CREATOR = new Creator<SubordinateBean>() {
        @Override
        public SubordinateBean createFromParcel(Parcel in) {
            return new SubordinateBean(in);
        }

        @Override
        public SubordinateBean[] newArray(int size) {
            return new SubordinateBean[size];
        }
    };

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(String aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(mobileNo);
        dest.writeString(aadharNo);
        dest.writeString(name);
        dest.writeString(fromDate);
        dest.writeString(toDate);
        dest.writeString(state);
        dest.writeString(district);
        dest.writeString(password);
    }
}
