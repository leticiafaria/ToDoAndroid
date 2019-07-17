package com.example.todo;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

/*
 * Activity Principal:
 * Apresenta as Tarefas, caso existam, em uma lista de tarefas (ListView)
 */
public class TaskManagerActivity extends ListActivity {

    private static final int ADD_TODO_ITEM_REQUEST = 0;  // codigo de requisicao de nova Tarefa
    private static final int EDIT_TODO_ITEM_REQUEST = 1; // codigo de requisicao de edicao de uma Tarefa

    private static final String FILE_NAME = "TodoManagerActivityData.txt";
    private static final String TAG = "ToDoActivity"; // tag para os logs: https://developer.android.com/studio/debug/am-logcat
    private AlertDialog.Builder dialog;

    // Adaptador da ListView
    // Documentacao: https://developer.android.com/reference/android/widget/Adapter
    TaskListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Cria um novo TaskListAdapter (Adaptador) para a ListView
        mAdapter = new TaskListAdapter(getApplicationContext());

        // recebe a ListView dessa Activity
        ListView lv = getListView();

        // TODO - Anexar o adaptador a ListView dessa Activity
        // Dica: use o metodo setAdapter()
        lv.setAdapter(mAdapter);

        // Coloca um linha entre as tarefas
        lv.setFooterDividersEnabled(true);

        // Infla a textView de footer_view.xml
        TextView footerView = (TextView) getLayoutInflater().inflate(R.layout.footer_view, null);

        // Adicionar a footerView na ListView
        lv.addFooterView(footerView);


        // Anexa um Listener a FooterView
        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* TODO - Criar Intent explicito e executar AddTaskActivity
                    com a intencao de adicionar uma nova tarefa
                */
                Intent intent = new Intent(TaskManagerActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, ADD_TODO_ITEM_REQUEST);
            }
        });

        // referente ao ponto extra
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                removerTask(position);
                return true;
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Log.i(TAG,	"onItemClick()");
                // mostra uma mensagem indicando o item selecionado
                //String text = "id: " + id + " pos: " + position + " selected: " ;
                //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

                // TODO: pegar a referencia de cada View em todo_item.xml
                /* Obs.: A ListView podera conter varias tarefas, portanto eh necessario
                   saber qual das views foi clicada. Por isso foi usado o parametro 'view'.
                */
                TextView mViewTitle = (TextView) view.findViewById(R.id.titleViewLabel);
                TextView mViewTime = (TextView) view.findViewById(R.id.TimeViewLabel);
                TextView mViewPriority = (TextView) view.findViewById(R.id.priorityView);
                CheckBox mCheckbox = (CheckBox) view.findViewById(R.id.statusCheckBox);

                // TODO: Criar um intent explicito para abrir AddTaskActivity
                Intent intent = new Intent(TaskManagerActivity.this, AddTaskActivity.class);

                /* Todo: Adicionar dados extras ao intent criado. Os dados sao:
                     - O titulo da view
                     - O hoario da view
                     - A prioridade
                     - O status
                     - O ID: use a posicao da view
                     Dica: a chave de cada conteudo eh definida na classe Task. Ja
                     o conteudo lembre-se de usar o metodo toString() quando necessario.
                 */
                intent.putExtra(Task.TITLE, mViewTitle.getText().toString());
                intent.putExtra(Task.HOUR, mViewTime.getText().toString());
                intent.putExtra(Task.PRIORITY, mViewPriority.getText().toString());

                if (mCheckbox.isChecked())
                    intent.putExtra(Task.STATUS, String.valueOf(Task.Status.DONE));
                else
                    intent.putExtra(Task.STATUS, String.valueOf(Task.Status.NOTDONE));

                intent.putExtra(Task.ID, position); // acrescena um extra sobre a posicao da View clicada

                /* TODO: inicie a Activity AddTaskActivity pelo Intent criado,
                    com a intencao de editar uma tarefa
                */
                startActivityForResult(intent, EDIT_TODO_ITEM_REQUEST);
            }
        });

    }

    // referente ao ponto extra
    public void removerTask(final int posicao) {
        dialog = new AlertDialog.Builder(TaskManagerActivity.this);
        dialog.setTitle("Excluir tarefa");
        dialog.setMessage("Tem certeza que deseja excluir a tarefa?");

        dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAdapter.removerTaskAdapter(posicao);
                mAdapter.notifyDataSetChanged();
            }
        });
        dialog.create();
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        /* TODO: Checar o resultado por meio do codigo de requisicao e do codigo do resultado:
        //
        // Se o usuario submeteu uma nova tarefa (Task):
        //   Crie uma nova tarefa usando o Intent recebido e a adicione ao adaptador
        // Senao:
        //   Se o usuario esta editando uma tarefa:
        //     Verifique se a posicao da tarefa (ID) eh valida para atualiza-la no adaptador
        */
        if (ADD_TODO_ITEM_REQUEST == requestCode && RESULT_OK == resultCode) {
            Task task = new Task(data);
            task.getDate();
            mAdapter.add(task);

        } else if (EDIT_TODO_ITEM_REQUEST == requestCode && RESULT_OK == resultCode) {
            int item_at_position = data.getIntExtra(Task.ID, -1);

            if (item_at_position != -1) {
                mAdapter.update(item_at_position, data);

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Carrega tarefas salvas em disco
    private void loadTasks() {
        Log.i(TAG, "Loading task from " + FILE_NAME + ".");
    }

    // Salva tarefas em disco
    private void saveTasks() {
        Log.i(TAG, "Saving task to " + FILE_NAME + ".");
    }

}
