package floda.pl.floda3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Floda_LOGIN extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_floda__login);
        Button singiup = (Button) findViewById(R.id.sing_up_login);
        Button login = (Button) findViewById(R.id.login_in_login);
        singiup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Floda_SING_UP.class);
                startActivity(i);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder g = new AlertDialog.Builder(Floda_LOGIN.this);
                LayoutInflater background = (LayoutInflater) getApplicationContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
                final View dialogView = background.inflate(R.layout.dialoglogin, null);
                final EditText login = (EditText) dialogView.findViewById(R.id.login_edit);
                final EditText password = (EditText) dialogView.findViewById(R.id.login_edit2);
                final Button log = (Button) dialogView.findViewById(R.id.button);
                final Button ca = (Button) dialogView.findViewById(R.id.button2);

                g.setView(dialogView);

                final AlertDialog alert = g.create();
                ca.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });
                log.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Floda_connect floda_connect = new Floda_connect();
                        floda_connect.usrLogin(login.getText().toString(),password.getText().toString());
                        if(floda_connect.getUsr()==null){
                            AlertDialog.Builder f= new AlertDialog.Builder(Floda_LOGIN.this);
                            f.setTitle("Failed to login");
                            f.setNeutralButton("Try again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alert.show();
                                }
                            });
                            f.create().show();

                        }else{
                            Intent i = new Intent(getBaseContext(),Floda_main.class);
                            i.putExtra("ID",floda_connect.getUsr().ID);
                            startActivity(i);
                        }
                    }
                });
                alert.show();
            }
        });
    }
}
