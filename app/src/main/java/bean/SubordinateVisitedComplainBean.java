package bean;

public class SubordinateVisitedComplainBean    {
    public String cmpno ;
    public String cmpdt ;
    public String cstname ;
    public String delname ;
    public String mblno1 ;

    public SubordinateVisitedComplainBean() {
    }

    public SubordinateVisitedComplainBean(String cmpno, String cmpdt, String cstname, String delname, String mblno1) {
        this.cmpno = cmpno;
        this.cmpdt = cmpdt;
        this.cstname = cstname;
        this.delname = delname;
        this.mblno1 = mblno1;
    }

    public String getCmpno() {
        return cmpno;
    }

    public void setCmpno(String cmpno) {
        this.cmpno = cmpno;
    }

    public String getCmpdt() {
        return cmpdt;
    }

    public void setCmpdt(String cmpdt) {
        this.cmpdt = cmpdt;
    }

    public String getCstname() {
        return cstname;
    }

    public void setCstname(String cstname) {
        this.cstname = cstname;
    }

    public String getDelname() {
        return delname;
    }

    public void setDelname(String delname) {
        this.delname = delname;
    }

    public String getMblno1() {
        return mblno1;
    }

    public void setMblno1(String mblno1) {
        this.mblno1 = mblno1;
    }
}