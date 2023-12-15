package br.edu.utfpr.gabrielcau.fitcontrol.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import br.edu.utfpr.gabrielcau.fitcontrol.R;
import br.edu.utfpr.gabrielcau.fitcontrol.entities.Treino;
import br.edu.utfpr.gabrielcau.fitcontrol.persistencia.ExerciciosDatabase;
import br.edu.utfpr.gabrielcau.fitcontrol.utils.UtilsGUI;

public class CadastroTreinoActivity extends AppCompatActivity {

    public static final String MODO    = "MODO";
    public static final String ID      = "ID";
    public static final int    NOVO    = 1;
    public static final int    ALTERAR = 2;

    private EditText editTexDescricao;

    private int  modo;
    private Treino treino;

    public static void novo(Activity activity, int requestCode) {

        Intent intent = new Intent(activity, CadastroTreinoActivity.class);

        intent.putExtra(MODO, NOVO);

        activity.startActivityForResult(intent, requestCode);
    }

    public static void alterar(Activity activity, int requestCode, Treino treino){

        Intent intent = new Intent(activity, CadastroTreinoActivity.class);

        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(ID, treino.getId());

        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro_treino);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        editTexDescricao = findViewById(R.id.editTextDescricao);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        if (bundle != null){
            modo = bundle.getInt(MODO, NOVO);
        }else{
            modo = NOVO;
        }

        if (modo == ALTERAR){

            setTitle(R.string.alterar_treino);

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {

                    int id = bundle.getInt(ID);

                    ExerciciosDatabase database = ExerciciosDatabase.getDatabase(CadastroTreinoActivity.this);

                    treino = database.treinoDao().queryForId(id);

                    CadastroTreinoActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            editTexDescricao.setText(treino.getNome());
                        }
                    });
                }
            });

        }else{

            setTitle(R.string.novo_treino);

            treino = new Treino("");
        }
    }

    private void salvar(){

        final String descricao  = UtilsGUI.validaCampoTexto(this,
                editTexDescricao,
                R.string.descricao_vazia);
        if (descricao == null){
            return;
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                ExerciciosDatabase database = ExerciciosDatabase.getDatabase(CadastroTreinoActivity.this);

                List<Treino> lista = database.treinoDao().queryForDescricao(descricao);

                if (modo == NOVO) {

                    if (lista.size() > 0){

                        CadastroTreinoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                UtilsGUI.avisoErro(CadastroTreinoActivity.this, R.string.descricao_usada);
                            }
                        });

                        return;
                    }

                    treino.setNome(descricao);

                    database.treinoDao().insert(treino);

                } else {

                    if (!descricao.equals(treino.getNome())){

                        if (lista.size() >= 1){

                            CadastroTreinoActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    UtilsGUI.avisoErro(CadastroTreinoActivity.this, R.string.descricao_usada);
                                }
                            });

                            return;
                        }

                        treino.setNome(descricao);

                        database.treinoDao().update(treino);
                    }
                }

                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edicao_detalhes, menu);
        return true;
    }
    private void limpar(){

        editTexDescricao.setText("");
        Toast.makeText(this, R.string.campo_vazio, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.menuItemSalvar:
                salvar();
                return true;
            case R.id.action_limpar:
                limpar();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
