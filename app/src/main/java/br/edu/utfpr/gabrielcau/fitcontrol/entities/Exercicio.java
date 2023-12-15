package br.edu.utfpr.gabrielcau.fitcontrol.entities;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

import br.edu.utfpr.gabrielcau.fitcontrol.utils.DateConverter;

@Entity(tableName = "exercicio",
        foreignKeys = @ForeignKey(entity = Treino.class,
                parentColumns = "id",
                childColumns  = "treinoId"))
@TypeConverters(DateConverter.class)
public class Exercicio {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String nome;

    private String tipo;
    private boolean concluido;

    @ColumnInfo(index = true)
    private int treinoId;

    private Date dataCadastro;

    public Exercicio(@NonNull String nome){
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public int getTreinoId() {
        return treinoId;
    }

    public void setTreinoId(int treinoId) {
        this.treinoId = treinoId;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    @Override
    public String toString(){
        return getNome();
    }
}
