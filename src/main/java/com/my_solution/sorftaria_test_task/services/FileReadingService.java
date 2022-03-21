package com.my_solution.sorftaria_test_task.services;

import com.my_solution.sorftaria_test_task.configurations.PathsConfiguration;
import com.my_solution.sorftaria_test_task.readers.FileWorker;
import com.my_solution.sorftaria_test_task.readers.enums.TimeConstraint;
import com.my_solution.sorftaria_test_task.utils.Pair;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileReadingService {
    private final FileWorker reader;
    private final PathsConfiguration configuration;

    public FileReadingService(FileWorker reader, PathsConfiguration configuration) {
        this.reader = reader;
        this.configuration = configuration;
    }

    public Map<String, String> fillMap(TimeConstraint timeConstraint) {
        Pair<List<String>, List<Path>> urlsAndPaths = switch (timeConstraint) {
            case YESTERDAY -> new Pair<>(
                    reader.readFileLines(configuration.getYesterdayUrlsPath()),
                    reader.walkDirectory(configuration.getYesterdayDirectory())
            );
            case TODAY -> new Pair<>(
                    reader.readFileLines(configuration.getTodayUrlsPath()),
                    reader.walkDirectory(configuration.getTodayDirectory())
            );
        };

        Set<String> urls = urlsAndPaths
                .getKey()
                .stream()
                .flatMap(s -> Stream.of(s.split("/")))
                .filter(s -> s.startsWith("www."))
                .collect(Collectors.toCollection(HashSet::new));

        List<Path> paths = urlsAndPaths.getValue();

        HashMap<String, String> results = new HashMap<>();
        for (Path path : paths) {
            String pathString = getNameWithoutExtension(path);
            if (urls.contains(pathString)) {
                results.put(pathString, reader.read(path));
            }
        }

        return results;
    }

    public static String getNameWithoutExtension(Path path) {
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }
}
