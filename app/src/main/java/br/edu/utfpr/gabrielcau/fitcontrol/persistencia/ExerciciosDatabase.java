package br.edu.utfpr.gabrielcau.fitcontrol.persistencia;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

import br.edu.utfpr.gabrielcau.fitcontrol.R;
import br.edu.utfpr.gabrielcau.fitcontrol.entities.Exercicio;
import br.edu.utfpr.gabrielcau.fitcontrol.entities.Treino;

@Database(entities = {Exercicio.class, Treino.class}, version = 1)
public abstract class ExerciciosDatabase extends RoomDatabase {

    public abstract ExercicioDao exercicioDao();

    public abstract TreinoDao treinoDao();

    private static ExerciciosDatabase instance;

    public static ExerciciosDatabase getDatabase(final Context context) {

        if (instance == null) {

            synchronized (ExerciciosDatabase.class) {
                if (instance == null) {
                    RoomDatabase.Builder builder =  Room.databaseBuilder(context,
                            ExerciciosDatabase.class,
                            "exercicios.db");

                    builder.addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    carregaTreinosIniciais(context);
                                }
                            });
                        }
                    });

                    instance = (ExerciciosDatabase) builder.build();
                }
            }
        }

        return instance;
    }

    private static void carregaTreinosIniciais(final Context context){
        String[] descricoes = context.getResources().getStringArray(R.array.tipos_iniciais);

        for (String descricao : descricoes) {
            Treino treino = new Treino(descricao);
            instance.treinoDao().insert(treino);
        }
    }
}
