package br.edu.utfpr.gabrielcau.fitcontrol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.edu.utfpr.gabrielcau.fitcontrol.R;
import br.edu.utfpr.gabrielcau.fitcontrol.entities.Exercicio;

public class ExercicioAdapter extends ArrayAdapter<Exercicio> {

    private final Context context;
    private final ArrayList<Exercicio> exercicios;

    public ExercicioAdapter(Context context, ArrayList<Exercicio> exercicios) {
        super(context, R.layout.item_exercicio, exercicios);
        this.context = context;
        this.exercicios = exercicios;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_exercicio, parent, false);

        TextView tvNome = rowView.findViewById(R.id.tvNome);
        TextView tvTipo = rowView.findViewById(R.id.tvTipo);
        TextView tvConcluido = rowView.findViewById(R.id.tvConcluido);
        TextView tvDataCadastro = rowView.findViewById(R.id.tvDataCadastro);

        Exercicio exercicio = exercicios.get(position);
        tvNome.setText(exercicio.getNome());
        tvTipo.setText(exercicio.getTipo());
        tvConcluido.setText(exercicio.isConcluido() ? context.getString(R.string.concluido) : context.getString(R.string.nao_concluido));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        tvDataCadastro.setText(sdf.format(exercicio.getDataCadastro()));

        return rowView;
    }
}