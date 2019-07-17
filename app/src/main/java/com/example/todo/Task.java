package com.example.todo;

import android.content.Intent;

/*
 Classe que define o que eh uma tarefa
 --> Atencao: as chaves sao usadas pelas outras Activities.
*/
public class Task {
    public static final String ITEM_SEP = System.getProperty("line.separator");

    public enum Priority {
        LOW, MED, HIGH
    }

    public enum Status {
        NOTDONE, DONE
    }

    // chaves para os conteudos dos intents
    public final static String TITLE = "title";
    public final static String HOUR = "date";
    public final static String STATUS = "status";
    public final static String PRIORITY = "priority";

    public final static String ID = "id";

    private String mTitle;
    private String mHour;
    private Priority mPriority;
    private Status mStatus;

    // Cria uma nova Tarefa

    Task(String title, Priority priority, Status status, String date) {
        this.mTitle = title;
        this.mPriority = priority;
        this.mStatus = status;
        this.mHour = date;
    }

    // Cria uma nova Tarefa por meio de um Itent
    Task(Intent intent) {
        mTitle = intent.getStringExtra(TITLE);
        mPriority = Priority.valueOf(intent.getStringExtra(PRIORITY));
        mStatus = Status.valueOf(intent.getStringExtra(STATUS));
        mHour = intent.getStringExtra(HOUR);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Priority getPriority() {
        return mPriority;
    }

    public void setPriority(Priority priority) {
        mPriority = priority;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        mStatus = status;
    }

    public String getDate() {
        return mHour;
    }

    public void setDate(String date) {
        mHour = date;
    }

    public String toString() {
        return mTitle + ITEM_SEP + mPriority + ITEM_SEP + mStatus + ITEM_SEP + mHour;
    }
}
