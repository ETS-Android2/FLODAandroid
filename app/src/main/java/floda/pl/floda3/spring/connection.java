package floda.pl.floda3.spring;

import com.android.volley.toolbox.StringRequest;


public interface connection {
    String getLoginAbout(String login, String password);
    StringRequest getLoginAboutID(String ID);
}
