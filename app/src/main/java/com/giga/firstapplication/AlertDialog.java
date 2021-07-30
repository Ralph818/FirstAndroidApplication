package com.giga.firstapplication;

import androidx.fragment.app.DialogFragment;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class AlertDialog extends DialogFragment
{
    //metodo para devolver una instancia con parametros inyectados por medio de bundle
    static AlertDialog nuevaInstancia(String titulo, String name, String email)
    {
        //una instancia de la clase que soportará al diálogo

        AlertDialog aDialog = new AlertDialog();

        //Dicionario llave valor que inyectará los parámetros al dialog
        Bundle bundle = new Bundle();
        bundle.putString("titulo", titulo);
        bundle.putString("nombre", name);
        bundle.putString("email", email);

        // con esto inyectamos los parámetros
        aDialog.setArguments(bundle);

        return aDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        String titulo = getArguments().getString("titulo");
        String nombre = getArguments().getString("nombre");
        String email = getArguments().getString("email");

        Dialog dialog = new androidx.appcompat.app.AlertDialog.Builder(getActivity())
                .setIcon(R.drawable.microsoft_teams_image)
                .setTitle(titulo)
                .setMessage("Tus datos son: \n Nombre: " + nombre + "\n Email: " + email)
                .setPositiveButton("OK", (_dialog, which) ->
                {
                        ((SecondActivity) getActivity()).registrar();
                }
                )
                .setNegativeButton("CANCELAR", (_dialog, which) ->
                {
                        ((SecondActivity) getActivity()).Cancelar();
                }
                )
                .create();

        return dialog;
    }
}
