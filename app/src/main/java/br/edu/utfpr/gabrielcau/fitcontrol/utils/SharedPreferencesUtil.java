package br.edu.utfpr.gabrielcau.fitcontrol.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtil {

    private static final String APP_PREFERENCES = "FitControlPreferences";
    private static final String KEY_UNIDADE_MEDIDA = "unidadeMedida";
    private static final String KEY_IDIOMA = "idioma";

    public static void salvarUnidadeMedida(Context context, String unidadeMedida) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_UNIDADE_MEDIDA, unidadeMedida);
        editor.apply();
    }

    public static String obterUnidadeMedida(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_UNIDADE_MEDIDA, "minutos");
    }

    public static void salvarIdioma(Context context, String idioma) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_IDIOMA, idioma);
        editor.apply();
    }

    public static String obterIdioma(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_IDIOMA, "en");
    }
}
