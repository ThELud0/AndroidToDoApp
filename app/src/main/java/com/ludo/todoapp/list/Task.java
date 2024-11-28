package com.ludo.todoapp.list;

import org.jetbrains.annotations.NotNull;

public class Task {
    public String id;
    public String title;
    public String description = "default";

    public Task(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public Task(String id, String title) {
        this.id = id;
        this.title = title;
    }

}
