package br.edu.utfpr.gabrielcau.fitcontrol.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import br.edu.utfpr.gabrielcau.fitcontrol.R;

public class AutoriaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoria);
        setupActionBar();
    }

    // Método para configurar o ActionBar
    private void setupActionBar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return isHomeItemSelected(item) || super.onOptionsItemSelected(item);
    }

    // Método para verificar se o item selecionado é o botão "about"
    private boolean isHomeItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return false;
    }
}
