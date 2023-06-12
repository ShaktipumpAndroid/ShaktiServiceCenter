package bean;

public class PendingSiteBean {

    String cmpNo;
    String subordinate;
    String subordinateName;
    String cmpDate;

    public PendingSiteBean() {
    }

    public PendingSiteBean(String cmpNo, String subordinate, String subordinateName, String cmpDate) {
        this.cmpNo = cmpNo;
        this.subordinate = subordinate;
        this.subordinateName = subordinateName;
        this.cmpDate = cmpDate;
    }

    public String getCmpNo() {
        return cmpNo;
    }

    public void setCmpNo(String cmpNo) {
        this.cmpNo = cmpNo;
    }

    public String getSubordinate() {
        return subordinate;
    }

    public void setSubordinate(String subordinate) {
        this.subordinate = subordinate;
    }

    public String getSubordinateName() {
        return subordinateName;
    }

    public void setSubordinateName(String subordinateName) {
        this.subordinateName = subordinateName;
    }

    public String getCmpDate() {
        return cmpDate;
    }

    public void setCmpDate(String cmpDate) {
        this.cmpDate = cmpDate;
    }
}
