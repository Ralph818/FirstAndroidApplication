package com.giga.firstapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Notification_Giga extends AppCompatActivity // extends appcompactactivity para que la reconozca como una activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //esperar parametros
        if(getIntent().getExtras()!=null)
        {
            Toast.makeText(getBaseContext(),"Parámetros: " + getIntent().getStringExtra("saludar"),Toast.LENGTH_SHORT).show();
        }

        setContentView(R.layout.activity_giga_notification);
    }

    public void lanzar_main_si(View v)
    {
        Intent i = new Intent(this, MainActivity.class);

        // aqui poner un put extra con unos parámetros
        i.putExtra("respuesta", "SI");
        startActivity(i);
    }

    public void lanzar_main_no(View v)
    {
        Intent i = new Intent(this, MainActivity.class);

        // aqui poner un put extra con unos parámetros
        i.putExtra("respuesta","NO");

        startActivity(i);
    }
}
