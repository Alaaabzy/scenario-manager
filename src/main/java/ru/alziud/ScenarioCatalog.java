package ru.alziud;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class ScenarioCatalog {

    public List<Path> findScenarios(Path directory) throws IOException {
        if (!Files.isDirectory(directory)) {
            return List.of();
        }

        try (Stream<Path> entries = Files.list(directory)) {
            return entries
                    .filter(Files::isDirectory)
                    .filter(folder ->
                            Files.isRegularFile(folder.resolve("story.txt"))
                    )
                    .sorted()
                    .toList();
        }
    }
}