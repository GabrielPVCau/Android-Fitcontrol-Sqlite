package br.edu.utfpr.gabrielcau.fitcontrol.persistencia;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.edu.utfpr.gabrielcau.fitcontrol.entities.Treino;

@Dao
public interface TreinoDao {

    @Insert
    long insert(Treino treino);

    @Delete
    void delete(Treino treino);

    @Update
    void update(Treino treino);

    @Query("SELECT * FROM Treino WHERE id = :id")
    Treino queryForId(long id);

    @Query("SELECT * FROM Treino ORDER BY nome ASC")
    List<Treino> queryAll();

    @Query("SELECT * FROM Treino WHERE nome = :nome ORDER BY nome ASC")
    List<Treino> queryForDescricao(String nome);

    @Query("SELECT count(*) FROM Treino")
    int total();
}
