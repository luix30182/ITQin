package com.sistemas.itqin;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Profile extends AppCompatActivity {

    TextView nombre;
    TextView carrera;
    TextView semestre;
    TextView email;
    TextView ncontrol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent i = getIntent();
        String nombrev = i.getStringExtra("nombre");
        String sapellidoPv = i.getStringExtra("apellidoP");
        String sapellidoMv = i.getStringExtra("apellidoM");
        String ncontrolv = i.getStringExtra("ncontrol");
        String carrerav = i.getStringExtra("carrera");
        String semestrev = i.getStringExtra("semestre");
        String emailv = i.getStringExtra("email");
        String statusv = i.getStringExtra("status");

        nombre = (TextView) findViewById(R.id.profileName);
        carrera = (TextView) findViewById(R.id.profileCarrer);
        semestre = (TextView) findViewById(R.id.profileSemester);
        ncontrol = (TextView) findViewById(R.id.profileNoControl);
        email = (TextView) findViewById(R.id.profileEmail);


        nombre.setText(nombrev + " " + sapellidoPv + " " + sapellidoMv);
        nombre.setTypeface(null, Typeface.BOLD);
        carrera.setText(carrerav);
        carrera.setTypeface(null, Typeface.BOLD);
        semestre.setText(semestrev + "° Semestre");
        semestre.setTypeface(null, Typeface.BOLD);
        ncontrol.setText("Número de control: " + ncontrolv);
        ncontrol.setTypeface(null, Typeface.BOLD);
        email.setText(emailv);
        email.setTypeface(null, Typeface.BOLD);

    }


}
