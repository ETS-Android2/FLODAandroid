package floda.pl.floda3.spring;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

public class Database_connection implements connection {
    String id;
    RequestQueue q;
    public Database_connection(RequestQueue q){this.q=q; id="0";
    }


    @Override
    public String getLoginAbout(String login, String password) {
        return null;
    }


    @Override
    public StringRequest getLoginAboutID(String ID) {
        String sql="select ID,Nick,Name,Surname,Email,su from floda_user_detail where ID=?";
        return null;

    }
}
