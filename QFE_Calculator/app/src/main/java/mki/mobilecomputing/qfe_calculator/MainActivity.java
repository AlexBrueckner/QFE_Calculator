package mki.mobilecomputing.qfe_calculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Declare EditTexts for input parsing
        final EditText input_runwayQfe, input_runwayElevation, input_targetElevation, input_temperature;
        input_runwayQfe = (EditText) findViewById(R.id.input_runwayQfe);
        input_runwayElevation = (EditText) findViewById(R.id.input_runwayElevation);
        input_targetElevation = (EditText) findViewById(R.id.input_targetElevation);
        input_temperature = (EditText) findViewById(R.id.input_temperature);
        //Declare output field
        final EditText output_Qfe = (EditText) findViewById(R.id.output_targetQfe);
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
                    sTemperature = input_targetElevation.getText().toString();

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
                    fRunwayElevation = Double.parseDouble(sRunwayElevation);
                    fTargetElevation = Double.parseDouble(sTargetElevation);
                    fTemperature = Double.parseDouble(sTemperature);
                    //Everything okay? Lets do this!

                    double temp0, temp1, temp2, temp3;
                    temp0 = 0.0065 * (fTargetElevation - fRunwayElevation);
                    temp1 = 237.15 + fTemperature;
                    temp2 = 1 - (temp0 / temp1);
                    temp3 = Math.pow(temp2,5.225);

                    double fTargetQfe = fRunwayQfe * temp3;
                    output_Qfe.setText(Double.toString(Math.round(fTargetQfe)));
                }

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
}
