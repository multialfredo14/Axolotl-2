package mx.gob.sedena.utils;

/**
 * Created by duran on 4/28/2017.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class Visualizador extends View {

    private static final int ANCHO = 2;
    private static final int ESCALA = 75;
    private List<Float> amplitudes;
    private int ancho;
    private int alto;
    private Paint linea;


    public Visualizador(Context context, AttributeSet attrs) {
        super(context, attrs);
        linea = new Paint();
        linea.setColor(Color.BLUE);
        linea.setStrokeWidth(ANCHO);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        ancho = w;
        alto = h;
        amplitudes = new ArrayList<Float>(ancho / ANCHO);
    }


    public void clear() {
        amplitudes.clear();
    }

    public void agregarAmplitud(float amplitude) {
        amplitudes.add(amplitude);

        if (amplitudes.size() * ANCHO >= ancho) {
            amplitudes.remove(0);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        int middle = alto / 2;
        float curX = 0;

        for (float power : amplitudes) {
            float scaledHeight = power / ESCALA;
            curX += ANCHO;

            canvas.drawLine(curX, middle + scaledHeight / 2, curX, middle
                    - scaledHeight / 2, linea);
        }
    }

}
