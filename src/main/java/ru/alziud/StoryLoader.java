package ru.alziud;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StoryLoader {

    public Map<String, Scene> load(Path folder) throws IOException {
        Path storyFile = folder.resolve("story.txt");

        List<String> lines = Files.readAllLines(
                storyFile, StandardCharsets.UTF_8
        );

        Map<String, Scene> scenes = new LinkedHashMap<>();

        String currentId = null;
        String textFile = null;
        List<Choice> choices = new ArrayList<>();

        for (String rawLine : lines) {
            String line = rawLine.trim();

            if (line.isEmpty()) {
                continue;
            }

            if (line.startsWith("[") && line.endsWith("]")) {
                if (currentId != null) {
                    addScene(scenes, folder, currentId, textFile, choices);
                }

                currentId = line.substring(1, line.length() - 1).trim();
                textFile = null;
                choices = new ArrayList<>();
            } else if (currentId == null) {
                throw new IOException("Сначала укажите сцену в квадратных скобках.");
            } else if (line.startsWith("text=")) {
                textFile = line.substring(5).trim();
            } else if (line.startsWith("choice=")) {
                String[] parts = line.substring(7).split("\\|", -1);

                if (parts.length != 2
                        || parts[0].isBlank()
                        || parts[1].isBlank()) {
                    throw new IOException("Неверный формат выбора: " + line);
                }

                choices.add(new Choice(parts[0].trim(), parts[1].trim()));
            } else {
                throw new IOException("Неизвестная строка: " + line);
            }
        }

        if (currentId != null) {
            addScene(scenes, folder, currentId, textFile, choices);
        }

        if (!scenes.containsKey("start")) {
            throw new IOException("В истории отсутствует сцена start.");
        }

        for (Scene scene : scenes.values()) {
            for (Choice choice : scene.getChoices()) {
                if (!scenes.containsKey(choice.getNextSceneId())) {
                    throw new IOException(
                            "Не найдена сцена: " + choice.getNextSceneId()
                    );
                }
            }
        }

        return scenes;
    }

    private void addScene(Map<String, Scene> scenes, Path folder,
                          String id, String textFile,
                          List<Choice> choices) throws IOException {

        if (id.isBlank() || scenes.containsKey(id)) {
            throw new IOException("Пустой или повторяющийся ID сцены: " + id);
        }

        if (textFile == null || textFile.isBlank()) {
            throw new IOException("Не указан текст сцены: " + id);
        }

        Path base = folder.toAbsolutePath().normalize();
        Path textPath = base.resolve(textFile).normalize();

        if (!textPath.startsWith(base)) {
            throw new IOException("Файл текста должен находиться в папке истории.");
        }

        String text = Files.readString(
                textPath, StandardCharsets.UTF_8
        );

        scenes.put(id, new Scene(id, text, choices));
    }
}