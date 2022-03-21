package com.my_solution.sorftaria_test_task.readers;

import com.my_solution.sorftaria_test_task.readers.exceptions.FileReadingException;
import com.my_solution.sorftaria_test_task.readers.exceptions.FileWalkingNotProperlyEndedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

@Component
public class FileWorker implements IReader<Path> {

    private final Charset charset;

    @Autowired
    public FileWorker(Charset charSet) {
        this.charset = charSet;
    }

    public String read(Path path) {
        try {
            return Files.readString(path, charset);
        }
        catch (IOException ioException) {
            throw new FileReadingException("read: An error while reading file", ioException);
        }
    }

    public List<Path> walkDirectory(String pathStr) {
        try (Stream<Path> stream = Files.walk(Paths.get(pathStr))) {
            return stream.filter(Files::isRegularFile).toList();
        }
        catch (IOException ioException) {
            throw new FileWalkingNotProperlyEndedException("Can not walk directory in a proper way",
                    ioException);
        }
    }

    public List<String> readFileLines(String pathStr) {
        try (Stream<String> stream = Files.lines(Path.of(pathStr))) {
            return stream.toList();
        }
        catch (IOException ioException) {
            throw new FileReadingException("readLines: An error while reading file", ioException);
        }
    }
}
