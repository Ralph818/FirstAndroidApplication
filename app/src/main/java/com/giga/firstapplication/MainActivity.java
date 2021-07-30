package com.giga.firstapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.channels.Channel;

public class MainActivity extends AppCompatActivity
{

    static final int requestCode = 1;
    static final String channelID = "giga";
    static final String tag = "giga-talks";
    static String number;
    static String textoSMS;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Aqui verificar los parámetros de la notificación para que se muestren en un toast.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getIntent().getExtras() != null)
        {
            Toast.makeText(getBaseContext(),"Respuesta recibida: " + getIntent().getStringExtra("respuesta"), Toast.LENGTH_LONG).show();
        }
    }
   //LLAMADA EXPLICITA
    public void lanzar(View v)
    {
        Intent i = new Intent( this,SecondActivity.class);
        startActivityForResult(i,requestCode);
    }

    //LLAMADA IMPLICITA [Intent]
    public void abrirWeb(View v)
    {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.grupo-giga.com"));
        startActivity(i);
    }

    public void localizar(View v)
    {
        Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("geo:19.168591407217832,-96.2269772258987"));
        startActivity(i);
    }

    public void marcar(View v)
    {
        Intent i = new Intent(Intent.ACTION_DIAL,Uri.parse("content://contacts/people/1"));
        startActivity(i);
    }

    public void contacto(View v)
    {
        Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("content://contacts/people/1"));
        startActivity(i);
    }

    //Llamada implícita, esto nos va a cachar la respuesta de la segunda actividad.
    @Override
    public void onActivityResult(int request_code, int result_code, Intent data)
    {
        super.onActivityResult(request_code,result_code,data);

        Log.d("giga_talks","Request_code => " + request_code + " - result code => " + result_code);

        if (request_code == requestCode)
        {
            if(result_code == RESULT_OK)
            {
                Toast.makeText(getBaseContext(),"Ya regresó la activity" + " nombre: " + data.getStringExtra("name") + " mail: " + data.getStringExtra("mail"), Toast.LENGTH_LONG).show();
                Log.d("giga-rafa"," nombre: " + data.getStringExtra("name") + " mail: " + data.getStringExtra("mail"));
            }

        }

    }

    public void lanzar_notificacion(View v)
    {
        //se genera el intent que se va a mandar a llamar con la notificación
        Intent i = new Intent(this,Notification_Giga.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        i.putExtra("saludar","hola que tal");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i,0);

        Log.d("giga-rafa","LA versión de Android actual es: " + Build.VERSION.SDK_INT);

        //Comparación de versión para definir si tenemos que crear canal de notificación o no.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            create_notification_channel("Giga channel","Channel of Giga", NotificationManager.IMPORTANCE_DEFAULT);
        }

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        Notification notification = generar_notificacion("Confirmación de la activación", "Entra para activar tu cuenta", pendingIntent);
        //detona la notificación
        manager.notify(10,notification);

    }

    //este metodo nos sirve para generar la notificación
    private Notification generar_notificacion(String title, String content, PendingIntent pendingIntent)
    {
        return new NotificationCompat.Builder(this, channelID)
                .setSmallIcon(R.drawable.microsoft_teams_image)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)  //  para que cuando el usuario entre a la notificacion, ya se quite
                .build();
    }

    private void create_notification_channel(String name, String description, int importance)
    {
        //preparar nuestro canal de notificación
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelID, name, importance);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setDescription(description);
        }

        // Registrar canal de notificación
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
    }


    //Método para enviar SMS
    private void sendSMS(String numero, String texto)
    {
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(numero, null, texto, null, null);
            Log.d(tag, "SMS send message" + " number: " + numero + " texto: " + texto);
        }catch(Exception ex){
            Log.d(tag,"SMS fail => " + ex.getMessage());
        }
        EditText txtnumero = findViewById(R.id.txtnumero);
        EditText txttextoSMS = findViewById(R.id.txttextoSMS);

        txtnumero.setText("");
        txttextoSMS.setText("");

        Toast.makeText(getBaseContext(), "Message has been sent", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions, grantResults);
        Log.d(tag,"permissions => " + permissions);
        switch(requestCode){
            case 0:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED)
                {
                    EditText txtNumber = findViewById(R.id.txtnumero);
                    EditText txttextoSMS = findViewById(R.id.txttextoSMS);

                    number = txtNumber.getText().toString();
                    textoSMS = txttextoSMS.getText().toString();

                    sendSMS(number, textoSMS);
                }
                else
                {
                    Toast.makeText(getBaseContext(),"Debes activar los permisos", Toast.LENGTH_SHORT).show();
                    requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, 0 );
                }
                break;
        }
    }

    public void sendingSMS(View v){
        try{
            if((ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) &&
            (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED))
            {
                Log.d(tag,"permissions granted!");

                EditText txtNumber = findViewById(R.id.txtnumero);
                EditText txttextoSMS = findViewById(R.id.txttextoSMS);

                number = txtNumber.getText().toString();
                textoSMS = txttextoSMS.getText().toString();

                Log.d(tag,"Sending message, number: " + number + " texto: " + textoSMS);

                sendSMS(number, textoSMS);
            }
            else
            {
                Log.d(tag,"no permissions detected!");
                requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE}, 0 );
            }
        }catch(Exception ex){
            Log.d(tag,"ocurrió un error al revisar permisos ==> " + ex.getMessage());
        }
    }
}