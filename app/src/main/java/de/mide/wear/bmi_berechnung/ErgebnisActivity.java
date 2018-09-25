package de.mide.wear.bmi_berechnung;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


/**
 * Activity zur Anzeige des berechneten BMI-Werts; wird über einen
 * sog. "expliziten Intent" von der MainActivity aufgerufen.
 * <br><br>
 *
 * This project is licensed under the terms of the BSD 3-Clause License.
 */
public class ErgebnisActivity extends WearableActivity {

    /** UI-Element zur Anzeige des BMI-Werts. */
    protected TextView _bmiTextView = null;

    /** UI-Element zur Anzeige der Klassifikation des BMIs (z.B. "Normalgewicht"). */
    protected TextView _klassifikationsTextView = null;


    /**
     * Lifecycle-Methode.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ergebnis);

        _bmiTextView             = findViewById( R.id.bmiTextView             );
        _klassifikationsTextView = findViewById( R.id.klassifikationsTextView );

        ergebnisAnzeigen();

        // Enables Always-on
        setAmbientEnabled();
    }


    /**
     * Der berechnete BMI-Wert wird aus dem Intent ausgelesen und zur
     * Anzeige gebracht.
     * <br/>
     * Für BMI-Klassifikation siehe
     * <a href="https://www.uni-hohenheim.de/wwwin140/info/interaktives/bmi.htm">diese Seite</a>.
     */
    protected void ergebnisAnzeigen() {

        Intent intent = getIntent();
        double bmiWert  = intent.getDoubleExtra( MainActivity.EXTRA_KEY_BMI_WERT, -1);
        boolean istMann = intent.getBooleanExtra( MainActivity.EXTRA_IST_MANN, true);

        // Zweite und folgende Nachkommastelle abschneiden
        bmiWert = (int)(bmiWert * 10);
        bmiWert = bmiWert / 10.0;

        _bmiTextView.setText( "BMI-Wert:\n" + bmiWert );


        // Klassifikation bestimmen
        String klassifikationsString = "";
        if (istMann) {

            if (bmiWert < 20) {
                klassifikationsString = "Untergewicht";
            } else if (bmiWert <= 25) {
                klassifikationsString = "Normalgewicht";
            } else if (bmiWert <= 30) {
                klassifikationsString = "Übergewicht";
            } else if (bmiWert<= 40) {
                klassifikationsString = "Adipositas";
            } else {
                klassifikationsString = "Massive Adipositas";
            }

        } else {

            if (bmiWert < 19) {
                klassifikationsString = "Untergewicht";
            } else if (bmiWert <= 24) {
                klassifikationsString = "Normalgewicht";
            } else if (bmiWert <= 30) {
                klassifikationsString = "Übergewicht";
            } else if (bmiWert<= 40) {
                klassifikationsString = "Adipositas";
            } else {
                klassifikationsString = "Massive Adipositas";
            }
        }

        _klassifikationsTextView.setText( "Klassifikation:\n" + klassifikationsString );
    }

}
