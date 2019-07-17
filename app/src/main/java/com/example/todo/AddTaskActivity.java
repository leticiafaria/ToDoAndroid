package com.example.todo;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.example.todo.Task.Priority;
import com.example.todo.Task.Status;

import java.util.Calendar;
import java.util.Date;

/*
 * Cria uma nova Tarefa
 */
public class AddTaskActivity extends Activity {

    private static String timeString; // variavel para controle da formatacao do horario
    private static TextView mTimeView;

    private Date mHour; // variavel para controle da classe Calendario do Java
    private RadioGroup mPriorityRadioGroup;
    private RadioGroup mStatusRadioGroup;
    private EditText mTitleText;

    private Integer mItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // define o layout
        setContentView(R.layout.add_todo);

        // TODO: encontrar os elementos necessarios da IU no layout definido
        mTitleText = (EditText) findViewById(R.id.title);
        mPriorityRadioGroup = (RadioGroup) findViewById(R.id.priorityGroup);
        mStatusRadioGroup = (RadioGroup) findViewById(R.id.statusGroup);
        mTimeView = (TextView) findViewById(R.id.time);

        /* TODO: receber um Intent que "pode" ter sido passado para essa Activity.
            Verificar se esse Intent possui extras. (i) Caso nao possua, entao
            uma nova Tarefa esta sendo criada. Apenas defina um valor default
            de horario. (ii) Caso possua, uma Tarefa esta sendo editada. O Intent
            deve ser vasculhado por meio de seus extras. Então:
             - definir o titulo
             - definir o horario
             - definir a prioridade
             - definir o status

            Dica: use intent.getStringExtra(<chave>), sendo a <chave> usada para
            buscar o conteudo relacionado a esta chave.
         */

        Intent intent = getIntent();
        if (intent.getExtras() != null) {

            mTitleText.setText(intent.getStringExtra(Task.TITLE));
            mTimeView.setText(intent.getStringExtra(Task.HOUR));

            Priority mPriority = Priority.valueOf(intent.getStringExtra(Task.PRIORITY));
            setPriorityInView(mPriority);

            Status mStatus = Status.valueOf(intent.getStringExtra(Task.STATUS));
            setStatusInView(mStatus);

            mItemId = intent.getIntExtra(Task.ID, -1);
        } else {
            // definie horario corrente como default
            setDefaultDateTime();
        }

        // OnClickListener para o botao de horario chama showTimePickerDialog() para
        // mostrar o 'Dialog' em que o usuario definie a hora
        final Button timePickerButton = (Button) findViewById(R.id.time_picker_button);
        timePickerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        // OnClickListener para o botao de Cancelar
        final Button cancelButton = (Button) findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO - Indicar o codigo de resultado como Cancelado
                AddTaskActivity.this.setResult(RESULT_CANCELED);
                finish();
            }
        });

        // OnClickListener para o botao de submeter
        final Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO: Criar um intent e adicionar os extras
                // Dica: use putExtra(<chave>, valor)
                // Para a prioridade e status use os metodos:
                // getPriorityInView() e getStatusInView() desta Activity.
                Intent data = new Intent();
                data.putExtra(Task.TITLE, mTitleText.getText().toString());
                data.putExtra(Task.HOUR, mTimeView.getText().toString());
                data.putExtra(Task.PRIORITY, getPriorityInView());
                data.putExtra(Task.STATUS, getStatusInView());

                data.putExtra(Task.ID, mItemId);

                // TODO - defininr o resultado como OK e retorar o Intent
                AddTaskActivity.this.setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    // NAO modifique os metodos abaixo
    private void setDefaultDateTime() {

        mHour = new Date();
        mHour = new Date(mHour.getTime());

        Calendar c = Calendar.getInstance();
        c.setTime(mHour);

        setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
        mTimeView.setText(timeString);
    }

    private static void setTimeString(int hourOfDay, int minute) {
        String hour = "" + hourOfDay;
        String min = "" + minute;

        if (hourOfDay < 10)
            hour = "0" + hourOfDay; // acrecenta o segundo digito a esquerda
        if (minute < 10)
            min = "0" + minute; // acrecenta o segundo digito a esquerda

        timeString = hour + ":" + min + ":00";
    }

    private String getPriorityInView() {

        switch (mPriorityRadioGroup.getCheckedRadioButtonId()) {
            case R.id.lowPriority: {
                return "LOW";
            }
            case R.id.highPriority: {
                return "HIGH";
            }
            default: {
                return "MED";
            }
        }
    }

    private void setPriorityInView(Priority priority) {
        if (priority == Priority.LOW) {
            mPriorityRadioGroup.check(R.id.lowPriority);
        } else if (priority == Priority.HIGH) {
            mPriorityRadioGroup.check(R.id.highPriority);
        } else {
            mPriorityRadioGroup.check(R.id.medPriority);
        }
    }

    private String getStatusInView() {

        switch (mStatusRadioGroup.getCheckedRadioButtonId()) {
            case R.id.statusDone: {
                return "DONE";
            }
            default: {
                return "NOTDONE";
            }
        }
    }

    private void setStatusInView(Status status) {
        if (status == Status.DONE) {
            mStatusRadioGroup.check(R.id.statusDone);
        } else {
            mStatusRadioGroup.check(R.id.statusNotDone);
        }
    }

    // DialogFragment usado para escolher um horario para uma Task
    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            setTimeString(hourOfDay, minute);
            mTimeView.setText(timeString);
        }
    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }
}