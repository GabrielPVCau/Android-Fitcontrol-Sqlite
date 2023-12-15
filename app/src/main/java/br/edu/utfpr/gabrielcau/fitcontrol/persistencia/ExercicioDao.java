package br.edu.utfpr.gabrielcau.fitcontrol.persistencia;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.edu.utfpr.gabrielcau.fitcontrol.entities.Exercicio;

@Dao
public interface ExercicioDao {

    @Insert
    long insert(Exercicio exercicio);

    @Delete
    void delete(Exercicio exercicio);

    @Update
    void update(Exercicio exercicio);

    @Query("SELECT * FROM Exercicio WHERE id = :id")
    Exercicio queryForId(long id);

    @Query("SELECT * FROM Exercicio ORDER BY nome ASC")
    List<Exercicio> queryAll();

    @Query("SELECT count(*) FROM Exercicio WHERE treinoId = :id LIMIT 1")
    int queryForTipoId(long id);
}
