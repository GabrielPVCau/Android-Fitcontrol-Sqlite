package br.edu.utfpr.gabrielcau.fitcontrol.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import br.edu.utfpr.gabrielcau.fitcontrol.R;
import br.edu.utfpr.gabrielcau.fitcontrol.entities.Treino;
import br.edu.utfpr.gabrielcau.fitcontrol.persistencia.ExerciciosDatabase;
import br.edu.utfpr.gabrielcau.fitcontrol.utils.UtilsGUI;

public class ListagemTreinosActivity extends AppCompatActivity {

    private static final int REQUEST_NOVO_TREINO = 1;
    private static final int REQUEST_ALTERAR_TREINO = 2;

    private ListView listViewTreinos;
    private ArrayAdapter<Treino> listaAdapter;
    private List<Treino> lista;

    public static void abrir(Activity activity) {

        Intent intent = new Intent(activity, ListagemTreinosActivity.class);

        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        listViewTreinos = findViewById(R.id.listViewItens);

        listViewTreinos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listViewTreinos.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            int position = -1;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                this.position = position;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.item_selecionado, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Handle menu actions here
                switch (item.getItemId()) {
                    case R.id.menuItemAbrir:
                        Treino treinoAbrir = (Treino) listViewTreinos.getItemAtPosition(position);
                        CadastroTreinoActivity.alterar(ListagemTreinosActivity.this,
                                REQUEST_ALTERAR_TREINO,
                                treinoAbrir);
                        mode.finish();
                        return true;
                    case R.id.menuItemApagar:
                        Treino treinoApagar = (Treino) listViewTreinos.getItemAtPosition(position);
                        excluirTreino(treinoApagar);
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                position = -1;
            }
        });

        carregaTreinos();

        setTitle(R.string.treinos);
    }


    private void carregaTreinos() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                ExerciciosDatabase database = ExerciciosDatabase.getDatabase(ListagemTreinosActivity.this);

                lista = database.treinoDao().queryAll();

                ListagemTreinosActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listaAdapter = new ArrayAdapter<>(ListagemTreinosActivity.this,
                                android.R.layout.simple_list_item_1,
                                lista);

                        listViewTreinos.setAdapter(listaAdapter);
                    }
                });
            }
        });
    }

    private void excluirTreino(final Treino treino) {

        String mensagem = getString(R.string.deseja_realmente_apagar) + "\n" + treino.getNome();

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
                                                ExerciciosDatabase.getDatabase(ListagemTreinosActivity.this);

                                        int dependentRecords = database.exercicioDao().queryForTipoId(treino.getId());

                                        if(dependentRecords > 0) {
                                            ListagemTreinosActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(ListagemTreinosActivity.this,
                                                            getString(R.string.apague_antes),
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        } else {
                                            database.treinoDao().delete(treino);

                                            ListagemTreinosActivity.this.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    listaAdapter.remove(treino);
                                                }
                                            });
                                        }
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
        if ((requestCode == REQUEST_NOVO_TREINO || requestCode == REQUEST_ALTERAR_TREINO)
                && resultCode == Activity.RESULT_OK) {

            carregaTreinos();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lista_tipos, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menuItemNovo:
                CadastroTreinoActivity.novo(this, REQUEST_NOVO_TREINO);
                return true;

            case R.id.action_limpar:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
