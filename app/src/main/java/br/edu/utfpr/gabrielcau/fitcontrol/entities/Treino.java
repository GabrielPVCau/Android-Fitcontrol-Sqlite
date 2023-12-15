package br.edu.utfpr.gabrielcau.fitcontrol.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "treino",
        indices = @Index(value = {"nome"}, unique = true))
public class Treino {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String nome;

    public Treino(@NonNull String nome){
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getNome() {
        return nome;
    }

    public void setNome(@NonNull String nome) {
        this.nome = nome;
    }

    @Override
    public String toString(){
        return getNome();
    }
}