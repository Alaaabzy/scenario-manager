package ru.alziud;

public class Choice {

    private final String text;
    private final String nextSceneId;

    public Choice(String text, String nextSceneId) {
        this.text = text;
        this.nextSceneId = nextSceneId;
    }

    public String getText() {
        return text;
    }

    public String getNextSceneId() {
        return nextSceneId;
    }
}