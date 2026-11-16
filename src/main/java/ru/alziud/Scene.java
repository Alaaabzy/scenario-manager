package ru.alziud;

import java.util.List;

public class Scene {

    private final String id;
    private final String text;
    private final List<Choice> choices;

    public Scene(String id, String text, List<Choice> choices) {
        this.id = id;
        this.text = text;
        this.choices = List.copyOf(choices);
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public boolean isEnding() {
        return choices.isEmpty();
    }
}