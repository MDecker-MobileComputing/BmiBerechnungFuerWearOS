package de.mide.wear.bmi_berechnung;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This project is licensed under the terms of the BSD 3-Clause License.
 */
public class MainActivity extends WearableActivity
                          implements SeekBar.OnSeekBarChangeListener,
                                     View.OnClickListener {


    /** Schlüssel, um einem expliziten intent den errechneten Wert BMI-Wert mitzugeben. */
    public static final String EXTRA_KEY_BMI_WERT = "bmi_wert";

    /** Schlüssel, um einem expliziten intent das Geschlecht (Mann oder Frau) mitzugeben. */
    public static final String EXTRA_IST_MANN = "ist_mann";

    /** Gewicht, wenn entsprechendes SeekBar-Element links am Anschlag ist. */
    protected static final int UNTERGRENZE_GEWICHT_KG = 40;

    /** Gewicht, wenn entsprechendes SeekBar-Element rechts am Anschlag ist. */
    protected static final int UNTERGRENZE_GROESSE_CM = 100;


    /** UI-Element zur Eingabe des Körpergewichts in kg. */
    protected SeekBar _gewichtSeekBar = null;

    /** UI-Element zur Eingabe der Körpergröße in kg. */
    protected SeekBar _groesseSeekBar = null;

    /** UI-Element zur Eingabe der aktuell eingestellten Körpergröße. */
    protected TextView _gewichtTextView = null;

    /** UI-Element zur Eingabe des aktuell eingestellten Körpergewichts. */
    protected TextView _groesseTextView = null;

    /** RadioButton für männliches Geschlecht. */
    protected RadioButton _mannRadioButton = null;


    /**
     * Lifecycle-Methode. Es werden zuerst die entsprechenden Member-Variablen
     * für die Objekte mit den UI-Elemente gefüllt.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _gewichtSeekBar = findViewById( R.id.gewichtSeekbar );
        _groesseSeekBar = findViewById( R.id.groesseSeekbar );

        _gewichtTextView = findViewById( R.id.gewichtTextView );
        _groesseTextView = findViewById( R.id.groesseTextView );

        _mannRadioButton = findViewById( R.id.radio_button_mann );

        _gewichtSeekBar.setOnSeekBarChangeListener(this);
        _groesseSeekBar.setOnSeekBarChangeListener(this);

        Button berechnungsButton = findViewById( R.id.berechnenButton );
        berechnungsButton.setOnClickListener( this );

        aktualisiereAnzeigeGewicht();
        aktualisiereAnzeigeGroesse();

        setAmbientEnabled(); // Enables Always-on
    }


    /**
     * Anzeige mit aktuell eingestellten Körpergewicht aktualisieren.
     * Diese Methode sollte nach jeder Änderung des SeekBar-Elements
     * aufgerufen werden.
     */
    protected void aktualisiereAnzeigeGewicht() {
        int neuerWert = _gewichtSeekBar.getProgress() + UNTERGRENZE_GEWICHT_KG;
        _gewichtTextView.setText( neuerWert + " kg" );
    }


    /**
     * Anzeige mit aktuell eingestellten Körpergröße aktualisieren.
     * Diese Methode sollte nach jeder Änderung des SeekBar-Elements
     * aufgerufen werden.
     */
    protected void aktualisiereAnzeigeGroesse() {
        int neuerWert = _groesseSeekBar.getProgress() + UNTERGRENZE_GROESSE_CM;
        _groesseTextView.setText( neuerWert + " cm" );
    }


    /**
     * Event-Handler-Methode aus Interface
     * {@link android.widget.SeekBar.OnSeekBarChangeListener}.
     * Diese Methode ist leer, denn wir benötigten für unsere App dieses
     * Event nicht, aber wir müssen die Methode trotzdem überschreiben,
     * damit die Activity-Klasse eine nicht-abstrakte Klasse ist, die
     * instanziiert werden kann.
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // absichtlich leer gelassen.
    }


    /**
     * Event-Handler-Methode aus Interface
     * {@link android.widget.SeekBar.OnSeekBarChangeListener}.
     */
    @Override
    public void onProgressChanged(SeekBar seekBar,
                                  int progress,
                                  boolean fromUser) {

        if (progress % 2 == 0) {
            return;
        }

        if (seekBar == _gewichtSeekBar) {
            aktualisiereAnzeigeGewicht();
        } else {
            aktualisiereAnzeigeGroesse();
        }
    }


    /**
     * Event-Handler-Methode aus Interface
     * {@link android.widget.SeekBar.OnSeekBarChangeListener}.
     * Diese Methode wird aufgerufen, wenn die Wert-Änderung mit
     * einem der beiden SeekBar-Elementen beendet ist (Finger
     * von Nutzer wieder hochgehoben).
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar == _gewichtSeekBar) {
            aktualisiereAnzeigeGewicht();
        } else {
            aktualisiereAnzeigeGroesse();
        }
    }


    /**
     * Einzige Event-Handler-Methode aus Interface {@link android.view.View.OnClickListener}.
     * <br><br>
     * In dieser Methode wird die BMI-Berechnung ausgeführt. Danach wird mit einem expliziten
     * Intent die Activity zur Anzeige
     *
     * @param view Button, der Event ausgelöst hat.
     */
    @Override
    public void onClick(View view) {
        Log.i("BMI-Rechner", "Berechnung sollte jetzt ausgeführt werden.");

        int gewicht = _gewichtSeekBar.getProgress() + UNTERGRENZE_GEWICHT_KG;

        int groesseZentimeter = _groesseSeekBar.getProgress() + UNTERGRENZE_GROESSE_CM;
        double groesseMeter   = groesseZentimeter / 100.0;

        double bmiWert = gewicht / ( groesseMeter * groesseMeter );

        Log.i("BMI-Rechner", "BMI-Wert=" + bmiWert);


        // Activity zur Anzeige des Ergebnisses aufrufen
        Intent intent = new Intent(this, ErgebnisActivity.class);

        intent.putExtra(EXTRA_KEY_BMI_WERT, bmiWert);

        if (_mannRadioButton.isChecked() ) {
            intent.putExtra(EXTRA_IST_MANN, true);
        } else {
            intent.putExtra(EXTRA_IST_MANN, false);
        }

        startActivity(intent);
    }

}
