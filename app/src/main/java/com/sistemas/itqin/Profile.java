package com.sistemas.itqin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Profile extends AppCompatActivity {

    TextView nombre;
    TextView carrera;
    TextView semestre;
    TextView email;
    TextView ncontrol;
    ImageView qrcodeImage;
    ImageView profileImage;

    private FirebaseAuth firebaseAuth;

    @Override
    public void onBackPressed() {
        firebaseAuth.signOut();
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_about:
                Intent i = new Intent(getApplicationContext(), About.class);
                startActivity(i);
                break;
            case R.id.sign_out:
                try{
                    firebaseAuth.signOut();
                    Intent im = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(im);
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            default:
                Toast.makeText(getApplicationContext(), "Escoge una opcion", Toast.LENGTH_SHORT).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent i = getIntent();
        firebaseAuth = FirebaseAuth.getInstance();

        String nombrev = i.getStringExtra("nombre");
        String sapellidoPv = i.getStringExtra("apellidoP");
        String sapellidoMv = i.getStringExtra("apellidoM");
        String ncontrolv = i.getStringExtra("ncontrol");
        String carrerav = i.getStringExtra("carrera");
        String semestrev = i.getStringExtra("semestre");
        String emailv = i.getStringExtra("email");
        String statusv = i.getStringExtra("status");
        String qrcode = i.getStringExtra("qrcode");
        String imgProfile = i.getStringExtra("imgProfile");

        nombre = (TextView) findViewById(R.id.profileName);
        carrera = (TextView) findViewById(R.id.profileCarrer);
        semestre = (TextView) findViewById(R.id.profileSemester);
        ncontrol = (TextView) findViewById(R.id.profileNoControl);
        email = (TextView) findViewById(R.id.profileEmail);
        qrcodeImage = (ImageView) findViewById(R.id.qrcodeView);
        profileImage = (ImageView) findViewById(R.id.profileImage);

        if (qrcode.equals("https://imgur.com/BZZ7i9Y")) {
            try {
                URL urlConnection = new URL(qrcode);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                qrcodeImage.setImageBitmap(myBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            qrcode = qrcode.replaceAll("data:image/png;base64,", "");
            byte[] decodedString = Base64.decode(qrcode, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            qrcodeImage.setImageBitmap(decodedByte);
        }

        try {
            URL urlConnection = new URL(imgProfile);
            HttpURLConnection connection = (HttpURLConnection) urlConnection
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            profileImage.setImageBitmap(myBitmap);
        }catch (Exception e){
            e.printStackTrace();
        }
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
