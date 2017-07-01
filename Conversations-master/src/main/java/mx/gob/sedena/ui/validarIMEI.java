package mx.gob.sedena.ui;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import eu.siacs.conversations.R;

public class validarIMEI extends Activity {

    static boolean errored = false;
    ProgressBar webservicePG;
    String IMEI;
    boolean loginStatus;
    TextView statusTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File database=getApplicationContext().getDatabasePath("history");

        if (!database.exists()) {
            setContentView(R.layout.activity_validar_imei);
            TelephonyManager mngr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
            IMEI = mngr.getDeviceId();
            Toast.makeText(validarIMEI.this,IMEI, Toast.LENGTH_LONG).show();

            //Display Text control
            // Button to trigger web service invocation
            //Display progress bar until web service invocation completes
            webservicePG = (ProgressBar) findViewById(R.id.progressBar1);
            statusTV = (TextView) findViewById(R.id.tv_result);
            statusTV.setText("");
            //Button Click Listener

            AsyncCallWS task = new AsyncCallWS();
            //Call execute
            task.execute();
        }else{
            Intent intObj = new Intent(validarIMEI.this,VistaIngreso.class);
            startActivity(intObj);
        }
    }

    @Override
    public void onBackPressed() {

    }



    private class AsyncCallWS extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            //Call Web Method
            loginStatus = WebService.invokeLoginWS(IMEI,IMEI,"authenticateUser");
            return null;
        }

        @Override
        //Once WebService returns response
        protected void onPostExecute(Void result) {
            //Make Progress Bar invisible
            webservicePG.setVisibility(View.INVISIBLE);
            Intent intObj = new Intent(validarIMEI.this,VistaIngreso.class);
            //Error status is false
            if(!errored){
                //Based on Boolean value returned from WebService
                if(loginStatus){
                    //Navigate to Home Screen
                    startActivity(intObj);
                }else{
                    //Set Error message
                    statusTV.setText("Fallo el acceso");
                }
                //Error status is true
            }else{
               statusTV.setText("Un error occurrio al invocar el WebService");
            }
            //Re-initialize Error Status to False
            errored = false;
        }

        @Override
        //Make Progress Bar visible
        protected void onPreExecute() {
            webservicePG.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }


    }
}








