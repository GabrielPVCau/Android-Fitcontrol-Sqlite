package br.edu.utfpr.gabrielcau.fitcontrol.activities;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import br.edu.utfpr.gabrielcau.fitcontrol.R;
import br.edu.utfpr.gabrielcau.fitcontrol.adapters.ExercicioAdapter;
import br.edu.utfpr.gabrielcau.fitcontrol.entities.Exercicio;
import br.edu.utfpr.gabrielcau.fitcontrol.persistencia.ExerciciosDatabase;
import br.edu.utfpr.gabrielcau.fitcontrol.utils.UtilsGUI;

public class ListagemExerciciosActivity extends AppCompatActivity {

    private static final int REQUEST_NOVO_EXERCICIO = 1;
    private static final int REQUEST_ALTERAR_EXERCICIO = 2;

    private ListView listViewExercicio;
    private ArrayAdapter<Exercicio> listaAdapter;
    private List<Exercicio> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        listViewExercicio = findViewById(R.id.listViewItens);

        listViewExercicio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Exercicio exercicio = (Exercicio) parent.getItemAtPosition(position);

                CadastroExercicioActivity.alterar(ListagemExerciciosActivity.this,
                        REQUEST_ALTERAR_EXERCICIO,
                        exercicio);
            }
        });

        carregaExercicios();

        registerForContextMenu(listViewExercicio);
    }

    private void carregaExercicios() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ExerciciosDatabase database = ExerciciosDatabase.getDatabase(ListagemExerciciosActivity.this);

                lista = database.exercicioDao().queryAll();

                ListagemExerciciosActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listaAdapter = new ExercicioAdapter(ListagemExerciciosActivity.this,
                                new ArrayList<>(lista));

                        listViewExercicio.setAdapter(listaAdapter);
                    }
                });
            }
        });
    }


    private void excluirExercicio(final Exercicio exercicio) {

        String mensagem = getString(R.string.deseja_realmente_apagar)
                + "\n" + exercicio.getNome();

        DialogInterface.OnClickListener listener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {

                                        ExerciciosDatabase database =
                                                ExerciciosDatabase.getDatabase(ListagemExerciciosActivity.this);

                                        database.exercicioDao().delete(exercicio);

                                        ListagemExerciciosActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                listaAdapter.remove(exercicio);
                                            }
                                        });
                                    }
                                });

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

        UtilsGUI.confirmaAcao(this, mensagem, listener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_NOVO_EXERCICIO || requestCode == REQUEST_ALTERAR_EXERCICIO)
                && resultCode == Activity.RESULT_OK) {

            carregaExercicios();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_para_listagem_exercicios, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_adicionar:
                CadastroExercicioActivity.nova(ListagemExerciciosActivity.this, REQUEST_NOVO_EXERCICIO);
                return true;
            case R.id.action_sobre:
                Intent sobreIntent = new Intent(this, AutoriaActivity.class);
                startActivity(sobreIntent);
                return true;
            case R.id.menuItemTreinos:
                Intent treinosIntent = new Intent(this, ListagemTreinosActivity.class);
                startActivity(treinosIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.item_selecionado, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info;

        info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Exercicio exercicio = (Exercicio) listViewExercicio.getItemAtPosition(info.position);

        switch (item.getItemId()) {

            case R.id.menuItemAbrir:
                CadastroExercicioActivity.alterar(this,
                        REQUEST_ALTERAR_EXERCICIO,
                        exercicio);
                return true;

            case R.id.menuItemApagar:
                excluirExercicio(exercicio);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}
