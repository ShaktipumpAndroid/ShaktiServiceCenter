package bean;

/**
 * Created by shakti on 10/7/2016.
 */

public class LoginBean {

    public static String userid = "";
    public static String username = "";
    public static String usertype = "";

    public LoginBean() {
    }

    public LoginBean(String id_txt,
                     String name_txt,
                     String type_txt
    ) {
        userid = id_txt;
        username = name_txt;
        usertype = type_txt;

    }

    public static String getUseid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public static String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
