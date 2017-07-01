package mx.gob.sedena.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import java.io.File;

import eu.siacs.conversations.R;
import eu.siacs.conversations.services.XmppConnectionService;
import eu.siacs.conversations.ui.*;

import android.widget.EditText;
import android.widget.Toast;


public class VistaIngreso extends Activity {


    private  EditText contraseña1;
    private  EditText contraseña2;
    private Button btnIngresar;
    static boolean errored = false;
    boolean loginStatus;
    boolean imei = true;

    Intent i;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        File database=getApplicationContext().getDatabasePath("history");
        setContentView(R.layout.activity_vista_ingreso);
        contraseña1 =(EditText) findViewById(R.id.editTextPass1);
        contraseña2 =(EditText) findViewById(R.id.editTextPass2);
        btnIngresar = (Button) findViewById(R.id.boton_passFrase);


        if (!database.exists()) {
                Toast.makeText(VistaIngreso.this,"Base de datos no encontrada", Toast.LENGTH_LONG).show();
                contraseña1 =(EditText) findViewById(R.id.editTextPass1);
                contraseña2 =(EditText) findViewById(R.id.editTextPass2);
                btnIngresar = (Button) findViewById(R.id.boton_passFrase);
                btnIngresar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(contraseña1.getText().toString().equals(contraseña2.getText().toString())){
                            i = new Intent(getApplicationContext(), ConversationActivity.class);
                            i.putExtra("pass", contraseña1.getText().toString());
                            startActivity(i);
                        }else{
                            Toast.makeText(VistaIngreso.this,"Llaves  son diferentes", Toast.LENGTH_SHORT).show();
                        }
                        contraseña1.setText("");
                        contraseña2.setText("");

                    }
                });
        } else {
            contraseña2.setVisibility(View.INVISIBLE);
            btnIngresar.setText("INGRESAR");
            btnIngresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(XmppConnectionService.PASSFRASE != null){
                        if(contraseña1.getText().toString().equals(XmppConnectionService.PASSFRASE)){
                            i = new Intent(getApplicationContext(), ConversationActivity.class);
                            i.putExtra("pass", contraseña1.getText().toString());
                            startActivity(i);
                        }else{
                            Toast.makeText(VistaIngreso.this,"Contraseña inválida", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        i = new Intent(getApplicationContext(), ConversationActivity.class);
                        i.putExtra("pass", contraseña1.getText().toString());
                        startActivity(i);
                    }

                    contraseña1.setText("");
                    contraseña2.setText("");
                }


            });

        }
    }

    @Override
    public void onBackPressed() {

    }

}
