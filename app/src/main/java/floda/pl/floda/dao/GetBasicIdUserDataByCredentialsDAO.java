package floda.pl.floda.dao;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetBasicIdUserDataByCredentialsDAO {
    public String[] getBasicIdUserDataByCredentialsDAO(String login, String password, RequestQueue q) {
        String sql = "http://www.serwer1727017.home.pl/2ti/floda/floda_login.php";
        final String[] data = {"0","pl"};
        StringRequest stringRequest = new StringRequest(Request.Method.POST, sql, response -> {

            try {
                JSONArray j = new JSONArray(response);
                JSONObject o = j.getJSONObject(0);
                data[0] = o.getString("id");
                data[1] = o.getString("lang");

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }, error -> {

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<>();
                map.put("login", login);
                map.put("password", password);
                return map;
            }
        };
        q.add(stringRequest);
        q.start();
        return data;
    }
}
