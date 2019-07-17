package com.example.todo;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.todo.Task.Priority;
import com.example.todo.Task.Status;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends BaseAdapter {

    // array de tarefas
    private final List<Task> mItems = new ArrayList<Task>();

    private final Context mContext;

    public TaskListAdapter(Context context) {

        mContext = context;
    }

    // Adicionar uma nova tarefa ao adaptador
    // O metodo notifyDataSetChanged() observa que os dados foram alterados e
    // as Views devem ser atualizadas
    // Documentacao: https://developer.android.com/reference/android/widget/BaseAdapter
    public void add(Task item) {

        mItems.add(item);
        notifyDataSetChanged();
    }

    // Limpa a lista de tarefas
    public void clear() {

        mItems.clear();
        notifyDataSetChanged();
    }

    /* TODO: Atualizar a lista de tarefas por meio do Intent recebido:
        Redefinir todos os atributos de uma tarefa.
    */
    public void update(int pos, Intent data) {
        Task item = (Task) getItem(pos);

        item.setTitle(data.getStringExtra(Task.TITLE));
        item.setDate(data.getStringExtra(Task.HOUR));
        item.setPriority(Priority.valueOf(data.getStringExtra(Task.PRIORITY)));
        item.setStatus(Status.valueOf(data.getStringExtra(Task.STATUS)));

        notifyDataSetChanged();
    }

    public void removerTaskAdapter(int posicao){
        mItems.remove(posicao);
    }


    // Retorna o numero de tarefas
    @Override
    public int getCount() {

        return mItems.size();
    }

    // Retrorna uma tarefa pela posicao
    @Override
    public Object getItem(int pos) {

        return mItems.get(pos);
    }

    // Retorna o ID de uma tarefa (igual a posicao)
    @Override
    public long getItemId(int pos) {

        return pos;
    }

    // Cria a View para uma tarefa e a adiciona em uma posicao da ListView
    // Esse metodo eh executado pelo Android quando o usuario submete/edita uma View para que
    // esta View seja adicionada na ListView.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO - pegar a Task corrente usando a posicao
        final Task task = (Task) getItem(position);

        // Infla o arquivo todo_item.xml para a nova View
        RelativeLayout itemLayout = (RelativeLayout) convertView.inflate(mContext, R.layout.todo_item, null);

        // Preencher os dados específicos de uma Tarefa.
        // Lembre-se que os dados que sao apresesentados nessa View,
        // correspondem aos elementos da IU definida no layout todo_item.xml

        // TODO - Mostrar o Titulo na TextView correspondente
        final TextView titleView = (TextView) itemLayout.findViewById(R.id.titleViewLabel);
        titleView.setText(task.getTitle());

        // TODO - Definir o Status na CheckBox
        final CheckBox statusView = (CheckBox) itemLayout.findViewById(R.id.statusCheckBox);
        if (Status.valueOf(task.getStatus().toString()) == Status.DONE)
            statusView.setChecked(true);
        else
            statusView.setChecked(false);

        // TODO - Definir a Prioridade na TextView correspondente
        final TextView priorityView = (TextView) itemLayout.findViewById(R.id.priorityView);
        priorityView.setText(task.getPriority().toString());

        // TODO - Mostrar a Hora na TextView correspondente
        final TextView dateView = (TextView) itemLayout.findViewById(R.id.TimeViewLabel);
        dateView.setText(task.getDate());

        // Retorna a View criada
        return itemLayout;
    }
}
