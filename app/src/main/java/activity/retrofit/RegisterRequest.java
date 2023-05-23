package activity.retrofit;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterRequest{
    @JsonProperty("MOBILE")
    public String mOBILE;
    @JsonProperty("NAME")
    public String nAME;
    @JsonProperty("AADHAR")
    public String aADHAR;
    @JsonProperty("DISTRICT")
    public String dISTRICT;
    @JsonProperty("STATE")
    public String sTATE;
    @JsonProperty("PASSWARD")
    public String pASSWARD;
    @JsonProperty("kunnr")
    public String kunnr;

    public RegisterRequest(String mOBILE, String nAME, String aADHAR, String dISTRICT, String sTATE, String pASSWARD, String kunnr) {
        this.mOBILE = mOBILE;
        this.nAME = nAME;
        this.aADHAR = aADHAR;
        this.dISTRICT = dISTRICT;
        this.sTATE = sTATE;
        this.pASSWARD = pASSWARD;
        this.kunnr = kunnr;
    }

    public String getmOBILE() {
        return mOBILE;
    }

    public void setmOBILE(String mOBILE) {
        this.mOBILE = mOBILE;
    }

    public String getnAME() {
        return nAME;
    }

    public void setnAME(String nAME) {
        this.nAME = nAME;
    }

    public String getaADHAR() {
        return aADHAR;
    }

    public void setaADHAR(String aADHAR) {
        this.aADHAR = aADHAR;
    }

    public String getdISTRICT() {
        return dISTRICT;
    }

    public void setdISTRICT(String dISTRICT) {
        this.dISTRICT = dISTRICT;
    }

    public String getsTATE() {
        return sTATE;
    }

    public void setsTATE(String sTATE) {
        this.sTATE = sTATE;
    }

    public String getpASSWARD() {
        return pASSWARD;
    }

    public void setpASSWARD(String pASSWARD) {
        this.pASSWARD = pASSWARD;
    }

    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }
}

