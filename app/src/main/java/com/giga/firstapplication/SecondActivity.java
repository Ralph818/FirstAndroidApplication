package com.giga.firstapplication;

import android.app.Notification;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

public class SecondActivity extends AppCompatActivity {


    private static String nombre;
    private static String email;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
    }

    public void confirmacion(View v)
    {
        EditText txtname = findViewById(R.id.txtname);
        nombre = txtname.getText().toString();

        EditText txtmail = findViewById(R.id.txtmail);
        email = txtmail.getText().toString();

        AlertDialog dialog = AlertDialog.nuevaInstancia("Confirme por favor los datos", nombre, email);
        dialog.show(getSupportFragmentManager(),"dialogo");
    }

    public void registrar()
    {
        Toast.makeText(getBaseContext(),"Diste click en OK",Toast.LENGTH_LONG).show();

        Intent i = new Intent();

        i.putExtra("name",nombre);
        i.putExtra("mail",email);

        setResult(RESULT_OK,i);
        finish();
    }

    public void Cancelar(){
        Log.d("giga-rafa", "Accediste a cancelar");
        Toast.makeText(getBaseContext(),"Diste click en cancelar",Toast.LENGTH_LONG).show();
    }


}
