package bean;

public class VisitedSiteBean  {

    String cmpNo;
    String subordinate;
    String subordinateName;
    String cmpDate;
    String distance;
    String cost;

    public VisitedSiteBean(String cmpNo, String subordinate, String subordinateName, String cmpDate, String distance, String cost) {
        this.cmpNo = cmpNo;
        this.subordinate = subordinate;
        this.subordinateName = subordinateName;
        this.cmpDate = cmpDate;
        this.distance = distance;
        this.cost = cost;
    }

    public VisitedSiteBean() {
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
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
