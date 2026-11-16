package ru.alziud;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("ТЕКСТОВЫЕ ПРИКЛЮЧЕНИЯ");
            System.out.println("1. Выбрать историю");
            System.out.println("0. Выход");
            System.out.print("Ваш выбор: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    selectScenario(scanner);
                    break;
                case "0":
                    System.out.println("До встречи, путешественник!");
                    running = false;
                    break;
                default:
                    System.out.println("Неверный выбор. Введите 1 или 0.");
            }
        }
    }

    private static void selectScenario(Scanner scanner) {
        ScenarioCatalog catalog = new ScenarioCatalog();

        while (true) {
            try {
                List<Path> folders = catalog.findScenarios(
                        Path.of("scenarios")
                );

                if (folders.isEmpty()) {
                    System.out.println(
                            "Истории не найдены в папке scenarios."
                    );
                    return;
                }

                System.out.println();
                System.out.println("ДОСТУПНЫЕ ИСТОРИИ");

                for (int i = 0; i < folders.size(); i++) {
                    System.out.println(
                            (i + 1) + ". " + folders.get(i).getFileName()
                    );
                }

                System.out.println("0. Вернуться в главное меню");
                System.out.print("Ваш выбор: ");

                String input = scanner.nextLine().trim();

                if (input.equals("0")) {
                    return;
                }

                try {
                    int number = Integer.parseInt(input);

                    if (number < 1 || number > folders.size()) {
                        System.out.println("Нет такой истории.");
                        continue;
                    }

                    Path selectedFolder = folders.get(number - 1);
                    startGame(scanner, selectedFolder);
                } catch (NumberFormatException e) {
                    System.out.println("Введите номер истории.");
                }
            } catch (IOException e) {
                System.out.println(
                        "Ошибка поиска историй: " + e.getMessage()
                );
                return;
            }
        }
    }

    private static void startGame(Scanner scanner, Path folder) {
        try {
            StoryLoader loader = new StoryLoader();
            Map<String, Scene> scenes = loader.load(folder);

            Scene currentScene = scenes.get("start");

            while (true) {
                System.out.println();
                System.out.println(currentScene.getText());

                if (currentScene.isEnding()) {
                    System.out.println();
                    System.out.println("Конец приключения.");
                    System.out.println(
                            "Нажмите Enter, чтобы вернуться к списку историй."
                    );
                    scanner.nextLine();
                    return;
                }

                List<Choice> choices = currentScene.getChoices();

                System.out.println();

                for (int i = 0; i < choices.size(); i++) {
                    System.out.println(
                            (i + 1) + ". " + choices.get(i).getText()
                    );
                }

                System.out.println("0. Вернуться к списку историй");
                System.out.print("Ваш выбор: ");

                String input = scanner.nextLine().trim();

                if (input.equals("0")) {
                    return;
                }

                try {
                    int number = Integer.parseInt(input);

                    if (number < 1 || number > choices.size()) {
                        System.out.println("Нет такого варианта.");
                        continue;
                    }

                    Choice selectedChoice = choices.get(number - 1);
                    currentScene = scenes.get(
                            selectedChoice.getNextSceneId()
                    );
                } catch (NumberFormatException e) {
                    System.out.println("Введите номер варианта.");
                }
            }
        } catch (IOException e) {
            System.out.println(
                    "Ошибка загрузки истории: " + e.getMessage()
            );
        }
    }
}