package br.edu.utfpr.gabrielcau.fitcontrol.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.edu.utfpr.gabrielcau.fitcontrol.R;
import br.edu.utfpr.gabrielcau.fitcontrol.entities.Exercicio;
import br.edu.utfpr.gabrielcau.fitcontrol.entities.Treino;
import br.edu.utfpr.gabrielcau.fitcontrol.persistencia.ExerciciosDatabase;
import br.edu.utfpr.gabrielcau.fitcontrol.utils.UtilsGUI;

public class CadastroExercicioActivity extends AppCompatActivity {

    public static final String MODO = "MODO";
    public static final String ID = "ID";
    public static final int NOVO = 1;
    public static final int ALTERAR = 2;

    private EditText editTextExerciseName;
    private RadioGroup radioGroupExerciseType;
    private CheckBox checkBoxExerciseCompleted;
    private Spinner spinnerTrainingExercise;
    private EditText editTextDataCadastro;

    private int modo;
    private Exercicio exercicio;

    private List<Treino> listaTreinos;

    public static void nova(Activity activity, int requestCode){
        Intent intent = new Intent(activity, CadastroExercicioActivity.class);
        intent.putExtra(MODO, NOVO);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void alterar(Activity activity, int requestCode, Exercicio exercicio){
        Intent intent = new Intent(activity, CadastroExercicioActivity.class);
        intent.putExtra(MODO, ALTERAR);
        intent.putExtra(ID, exercicio.getId());
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_cadastro_exercicio);

        editTextExerciseName = findViewById(R.id.editTextExerciseName);
        radioGroupExerciseType = findViewById(R.id.radioGroupExerciseType);
        checkBoxExerciseCompleted = findViewById(R.id.checkBoxExerciseCompleted);
        spinnerTrainingExercise = findViewById(R.id.spinnerTrainingExercise);
        editTextDataCadastro = findViewById(R.id.editTextDataCadastro);

        editTextDataCadastro.setFocusable(false);

        editTextDataCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(CadastroExercicioActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                editTextDataCadastro.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        modo = bundle.getInt(MODO, NOVO);

        carregaTreinos();

        if (modo == ALTERAR){
            setTitle(R.string.alterar_exercicio);
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    int id = bundle.getInt(ID);
                    ExerciciosDatabase database = ExerciciosDatabase.getDatabase(CadastroExercicioActivity.this);
                    exercicio = database.exercicioDao().queryForId(id);

                    CadastroExercicioActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            editTextExerciseName.setText(exercicio.getNome());

                            String tipo = exercicio.getTipo();

                            RadioButton radioButtonAerobic = findViewById(R.id.radioButtonAerobic);
                            RadioButton radioButtonAnaerobic = findViewById(R.id.radioButtonAnaerobic);

                            if (tipo.equals(radioButtonAerobic.getText().toString())) {
                                radioGroupExerciseType.check(R.id.radioButtonAerobic);
                            } else if (tipo.equals(radioButtonAnaerobic.getText().toString())) {
                                radioGroupExerciseType.check(R.id.radioButtonAnaerobic);
                            }

                            checkBoxExerciseCompleted.setChecked(exercicio.isConcluido());

                            editTextDataCadastro.setText(UtilsGUI.dateToString(exercicio.getDataCadastro()));

                            int posicao = posicaoTreino(exercicio.getTreinoId());
                            spinnerTrainingExercise.setSelection(posicao);
                        }
                    });
                }
            });

        } else {
            setTitle(R.string.novo_exercicio);
            exercicio = new Exercicio("");
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edicao_detalhes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_limpar) {
            limpar();
            return true;
        } else if (id == R.id.menuItemSalvar) {
            salvar();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void limpar(){

        editTextExerciseName.setText("");
        editTextDataCadastro.setText("");

        radioGroupExerciseType.clearCheck();
        checkBoxExerciseCompleted.setChecked(false);


        if (spinnerTrainingExercise.getAdapter() != null) {
            spinnerTrainingExercise.setSelection(0);
        }
        
        Toast.makeText(this, R.string.campo_vazio, Toast.LENGTH_SHORT).show();
    }

    private void carregaTreinos(){
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ExerciciosDatabase database = ExerciciosDatabase.getDatabase(CadastroExercicioActivity.this);
                listaTreinos = database.treinoDao().queryAll();

                CadastroExercicioActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<Treino> spinnerAdapter =
                                new ArrayAdapter<>(CadastroExercicioActivity.this,
                                        android.R.layout.simple_list_item_1,
                                        listaTreinos);
                        spinnerTrainingExercise.setAdapter(spinnerAdapter);
                    }
                });
            }
        });
    }

    private int posicaoTreino(int idTreino){
        for (int posicao = 0; posicao < listaTreinos.size(); posicao++){
            if (listaTreinos.get(posicao).getId() == idTreino){
                return posicao;
            }
        }
        return -1;
    }

    private void salvar() {
        String nome = editTextExerciseName.getText().toString();
        if (nome.isEmpty()) {
            Toast.makeText(this, R.string.nome_vazio, Toast.LENGTH_LONG).show();
            return;
        }

        int tipoSelecionadoId = radioGroupExerciseType.getCheckedRadioButtonId();
        if (tipoSelecionadoId == -1) {
            Toast.makeText(this, R.string.tipo_vazio, Toast.LENGTH_LONG).show();
            return;
        }

        Date dataCadastro;
        try {
            dataCadastro = UtilsGUI.stringToDate(editTextDataCadastro.getText().toString());
        } catch (Exception e) {
            Toast.makeText(this, R.string.data_invalida, Toast.LENGTH_LONG).show();
            return;
        }

        exercicio.setNome(nome);

        RadioButton radioButtonSelecionado = findViewById(tipoSelecionadoId);
        exercicio.setTipo(radioButtonSelecionado.getText().toString());

        exercicio.setConcluido(checkBoxExerciseCompleted.isChecked());

        exercicio.setDataCadastro(dataCadastro);

        int posicao = spinnerTrainingExercise.getSelectedItemPosition();
        if (posicao != Spinner.INVALID_POSITION){
            exercicio.setTreinoId(listaTreinos.get(posicao).getId());
        }

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ExerciciosDatabase database = ExerciciosDatabase.getDatabase(CadastroExercicioActivity.this);

                if (modo == NOVO) {
                    database.exercicioDao().insert(exercicio);
                } else {
                    database.exercicioDao().update(exercicio);
                }

                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }
}
