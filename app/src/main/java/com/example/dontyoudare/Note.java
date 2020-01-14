package com.example.dontyoudare;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    //Einträge für SGL_Lite Einträge (Aufgaben ME und Friends Bereiche)
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private int priority;

    //Constructor für die Einträge
    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    //Setter für ID
    public void setId(int id) {
        this.id = id;
    }

    //Getter für Variablen
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
