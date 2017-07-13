package mki.mobilecomputing.qfe_calculator;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static boolean useMeters,useCelsius;
    private static boolean visitShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!visitShown){
        Toast.makeText(MainActivity.this,"Thanks for using this app! Please visit http://f99th.com" +
                "/!",Toast.LENGTH_LONG).show();
        visitShown = true;
        }
        //Declare EditTexts for input parsing
        final EditText input_runwayQfe, input_runwayElevation, input_targetElevation, input_temperature;
        //Grab radio buttons
        final RadioButton radioCelsius,radioFahrenheit,radioMeters,radioFeet;
        radioCelsius = (RadioButton) findViewById(R.id.radioCelsius);
        radioFahrenheit = (RadioButton) findViewById(R.id.radioFahrenheit);
        radioMeters = (RadioButton) findViewById(R.id.radioMeters);
        radioFeet = (RadioButton) findViewById(R.id.radioFeet);

        radioCelsius.setChecked(true);
        useCelsius = true;
        radioMeters.setChecked(true);
        useMeters = true;


        //Grab EditTexts
        input_runwayQfe = (EditText) findViewById(R.id.input_runwayQfe);
        input_runwayElevation = (EditText) findViewById(R.id.input_runwayElevation);
        input_targetElevation = (EditText) findViewById(R.id.input_targetElevation);
        input_temperature = (EditText) findViewById(R.id.input_temperature);
        //Declare output field
        final EditText output_Qfe = (EditText) findViewById(R.id.output_targetQfe);
        //Prevent editing the Output field
        output_Qfe.setFocusable(false);
        //Declare ImageView
        ImageView logo = (ImageView)findViewById(R.id.f99thLogo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This will open the DEFAULT BROWSER FOR THE PHONE
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.f99th.com/")));
            }
        });
        //Setup web link

        //Declare button
        Button doTheMagic = (Button) findViewById(R.id.button_calculateQfe);

        //Now add an OnClickListener to the button
        //I'd love some lambads but Android Studio decided I may not do that..
        doTheMagic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try{

                    boolean inputsOkay = true;
                    //Parse every inputs after checking them for content
                    String sRunwayQfe,sRunwayElevation,sTargetElevation,sTemperature;
                    sRunwayQfe = input_runwayQfe.getText().toString();
                    sRunwayElevation = input_runwayElevation.getText().toString();
                    sTargetElevation = input_targetElevation.getText().toString();
                    sTemperature = input_temperature.getText().toString();

                    if(sRunwayQfe == null ||sRunwayQfe.length() <= 0){
                        Toast.makeText(MainActivity.this,"Invalid Runway QFE, wrong format?",
                                Toast.LENGTH_SHORT).show();
                        inputsOkay = false;
                    }

                    if(sRunwayElevation == null ||sRunwayElevation.length() <= 0){
                        Toast.makeText(MainActivity.this,"Invalid Runway Elevation, wrong format?",
                                Toast.LENGTH_SHORT).show();
                        inputsOkay = false;
                    }

                    if(sTargetElevation == null || sTargetElevation.length() <= 0){
                        Toast.makeText(MainActivity.this,"Invalid Target Elevation, wrong format?",
                                Toast.LENGTH_SHORT).show();
                        inputsOkay = false;
                    }

                    if(sTemperature == null || sTemperature.length() <= 0){
                        Toast.makeText(MainActivity.this,"Invalid Temperature, wrong format?",
                                Toast.LENGTH_SHORT).show();
                        inputsOkay = false;
                    }
                    //If any of the inputs were bogus, we quit and wait untill the user has input
                    //working ones.
                    if(!inputsOkay){
                        return;
                    }

                    //Note: We just checked if the Strings were valid. Now we parse the data.
                    //Should the Parsing fail, the catch-block takes care of everything else for us.

                    double fRunwayQfe,fRunwayElevation,fTargetElevation,fTemperature;

                    fRunwayQfe = Double.parseDouble(sRunwayQfe);

                    if(useMeters){
                        System.out.println("Using meters");
                    fRunwayElevation = Double.parseDouble(sRunwayElevation);
                    fTargetElevation = Double.parseDouble(sTargetElevation);
                    }

                    else{
                        System.out.println("Converting to meters");
                        fRunwayElevation = feetToMeters(Double.parseDouble(sRunwayElevation));
                        System.out.println("Startelev before Conversion: " +
                                Double.parseDouble(sRunwayElevation) + " after conversion: "
                                + fRunwayElevation);
                        fTargetElevation = feetToMeters(Double.parseDouble(sTargetElevation));
                        System.out.println("Targelev before Conversion: " +
                                Double.parseDouble(sTargetElevation) + " after conversion: "
                                + fTargetElevation);
                    }

                    if(useCelsius){
                        System.out.println("Using Celsius");
                        fTemperature = Double.parseDouble(sTemperature);
                    }

                    else{
                        System.out.println("Converting to Celsius");
                        fTemperature = fahrenheitToCelsius(Double.parseDouble(sTemperature));
                        System.out.println("Temp before Conversion: " +
                                Double.parseDouble(sTemperature) + " after conversion: "
                                + fTemperature);
                    }

                    //Everything okay? Lets do this!



                    double temp0, temp1, temp2, temp3;
                    temp0 = 0.0065 * (fTargetElevation - fRunwayElevation);
                    temp1 = 237.15 + fTemperature;
                    temp2 = 1 - (temp0 / temp1);
                    temp3 = Math.pow(temp2,5.225);

                    double fTargetQfe = fRunwayQfe * temp3;
                    output_Qfe.setText(Double.toString(Math.round(fTargetQfe)));
                }
                //This should never be thrown, but hey, better safe than sorry!
                catch(NumberFormatException nfe){

                    Toast.makeText(MainActivity.this,"Could not parse one of the Inputs!" +
                                    " Verify the format!",
                            Toast.LENGTH_SHORT).show();

                }

                //I heard its bad practice to return in a catch block.
                // So i just put that into a finally block..

                finally{
                    return;
                }


            }
        });

    }


    /***
     * feetToMeters
     * Converts feet to meters
     * @param feet
     * @return given value in meters
     */
    private double feetToMeters(double feet){
        return feet / 3.2808;
    }

    /***
     * fahrenheitToCelsius converts input in fahrenheit to celsius
     * @param fahrenheit
     * @return given value in celsius
     */
    private double fahrenheitToCelsius(double fahrenheit){
        return ((fahrenheit-32.0) * (5.0/9.0));
    }

    public void onRadioButtonClicked(View view) {
        System.out.println("Inside onRadioButtonClicked");
        boolean checked = ((RadioButton) view).isChecked();
        final RadioButton radioCelsius,radioFahrenheit,radioMeters,radioFeet;
        radioCelsius = (RadioButton) findViewById(R.id.radioCelsius);
        radioFahrenheit = (RadioButton) findViewById(R.id.radioFahrenheit);
        radioMeters = (RadioButton) findViewById(R.id.radioMeters);
        radioFeet = (RadioButton) findViewById(R.id.radioFeet);
        System.out.println("Button clicked: " + view.getId());
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioCelsius:
                radioFahrenheit.setChecked(false);
                useCelsius = true;
                break;
            case R.id.radioFahrenheit:
                useCelsius = false;
                radioCelsius.setChecked(false);
                break;
            case R.id.radioFeet:
                useMeters = false;
                radioMeters.setChecked(false);
                break;
            case R.id.radioMeters:
                useMeters = true;
                radioFeet.setChecked(false);
                break;
            default:
                System.out.println("no valid button pressed??");
                break;
        }
    }
}
