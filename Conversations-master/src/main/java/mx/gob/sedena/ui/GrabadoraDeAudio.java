package mx.gob.sedena.ui;

import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import eu.siacs.conversations.R;
import mx.gob.sedena.utils.Visualizador;

public class GrabadoraDeAudio extends Activity implements View.OnClickListener {

    private TextView mTexto;
    private Button mCancelar;
    private Button mCompartir;

    private MediaRecorder grabadora;
    private long renombrar = 0;
    private TextView mensajeTexto;
    private int[] amplitud = new int[100];
    private int i = 0;
    public static final int intervalo = 40;
    private Visualizador visualizador;
    private Handler handler;
    private boolean estaGrabando = false;





    private Handler gestionador = new Handler();
    private Runnable reloj = new Runnable() {
        @Override
        public void run() {
            pulso();
            gestionador.postDelayed(reloj, 100);
        }
    };
    private File archivoDeAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_grabadora_de_audio);
        this.mTexto = (TextView) this.findViewById(R.id.timer);
        this.mCancelar = (Button) this.findViewById(R.id.cancelar_boton);
        this.mCancelar.setOnClickListener(this);
        this.mCompartir = (Button) this.findViewById(R.id.compartir_boton);
        this.mCompartir.setOnClickListener(this);
        visualizador = (Visualizador) findViewById(R.id.visualizer);
        this.mensajeTexto = (TextView)findViewById(R.id.mensajeTexto);
        this.setFinishOnTouchOutside(false);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        handler = new Handler();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!comenzarGrabadora()) {
            mCompartir.setEnabled(false);
            mCompartir.setTextColor(0x8a000000);
            Toast.makeText(this, R.string.unable_to_start_recording, Toast.LENGTH_SHORT).show();
            handler.post(actualizarVisualizador);

        }

        handler.post(actualizarVisualizador);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (grabadora != null) {
            pararGrabadora(false);

        }


    }

    private boolean comenzarGrabadora() {
        grabadora = new MediaRecorder();
        grabadora.setAudioSource(MediaRecorder.AudioSource.MIC);
        grabadora.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            grabadora.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
            grabadora.setAudioEncodingBitRate(48000);
        } else {
            grabadora.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            grabadora.setAudioEncodingBitRate(64000);
        }
        grabadora.setAudioSamplingRate(16000);
        archivoDeAudio = obtenerArchivoSalida();
        archivoDeAudio.getParentFile().mkdirs();
        grabadora.setOutputFile(archivoDeAudio.getAbsolutePath());

        try {
            grabadora.prepare();
            grabadora.start();
            renombrar = SystemClock.elapsedRealtime();
            gestionador.postDelayed(reloj, 100);
            estaGrabando = true; // we are currently recording
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    protected void pararGrabadora(boolean saveFile) {
        grabadora.stop();
        grabadora.release();
        grabadora = null;
        renombrar = 0;
        gestionador.removeCallbacks(reloj);
        if (!saveFile && archivoDeAudio != null) {
            archivoDeAudio.delete();
        }
    }

    private File obtenerArchivoSalida() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmssSSS", Locale.US);
        return new File(Environment.getExternalStorageDirectory().getAbsolutePath().toString()
                + "/Voice Recorder/RECORDING_"
                + dateFormat.format(new Date())
                + ".m4a");
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancelar_boton:
                pararGrabadora(false);
                setResult(RESULT_CANCELED);
                estaGrabando = false; // stop recording
                handler.removeCallbacks(actualizarVisualizador);
                visualizador.clear();
                finish();
                break;
            case R.id.compartir_boton:

                pararGrabadora(true);
                Uri uri = Uri.parse("file://" + archivoDeAudio.getAbsolutePath());
                setResult(Activity.RESULT_OK, new Intent().setData(uri));
               estaGrabando = false; // stop recording
                handler.removeCallbacks(actualizarVisualizador);
                visualizador.clear();
                finish();
                break;


        }
    }

   Runnable actualizarVisualizador = new Runnable() {
        @Override
        public void run() {
            if (estaGrabando)
            {

                int x = grabadora.getMaxAmplitude();
                visualizador.agregarAmplitud(x);
                visualizador.invalidate();
                handler.postDelayed(this, intervalo);


            }
        }
    };

    private void pulso() {
        long time = (renombrar < 0) ? 0 : (SystemClock.elapsedRealtime() - renombrar);
        int minutes = (int) (time / 60000);
        int seconds = (int) (time / 1000) % 60;
        int milliseconds = (int) (time / 100) % 10;
        mTexto.setText(minutes + ":" + (seconds < 10 ? "0" + seconds : seconds) + "." + milliseconds);
        if (grabadora != null) {
            amplitud[i] = grabadora.getMaxAmplitude();
            if (i >= amplitud.length - 1) {
                i = 0;
            } else {
                ++i;
            }
        }
    }

}
