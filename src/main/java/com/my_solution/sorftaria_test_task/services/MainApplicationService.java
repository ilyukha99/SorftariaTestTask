package com.my_solution.sorftaria_test_task.services;

import com.my_solution.sorftaria_test_task.configurations.SecretaryNameConfiguration;
import com.my_solution.sorftaria_test_task.encoders.IEncoder;
import com.my_solution.sorftaria_test_task.readers.enums.TimeConstraint;
import com.my_solution.sorftaria_test_task.readers.exceptions.AbstractReadingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MainApplicationService {
    private static final Logger LOGGER = LoggerFactory.getLogger("MainApplicationService");

    private Map<String, String> yesterdaySiteStates;
    private Map<String, String> todaySiteStates;

    private final IEncoder<String> sourceEncoder;
    private final FileReadingService fileReadingService;
    private final SecretaryNameConfiguration nameConfiguration;

    @Autowired
    public MainApplicationService(IEncoder<String> encoder, FileReadingService fileReadingService,
                                  SecretaryNameConfiguration nameConfiguration) {
        sourceEncoder = encoder;
        this.fileReadingService = fileReadingService;
        this.nameConfiguration = nameConfiguration;
    }

    public void init() {
        try {
            yesterdaySiteStates = fileReadingService.fillMap(TimeConstraint.YESTERDAY);
            todaySiteStates = fileReadingService.fillMap(TimeConstraint.TODAY);
        } catch (AbstractReadingException exception) {
            LOGGER.error(exception.getMessage(), exception);
        }
    }

    public Set<String> getMissingUrls(Map<String, String> first, Map<String, String> second) {
        return first
                .keySet()
                .stream()
                .filter(s -> !second.containsKey(s))
                .collect(Collectors.toCollection(HashSet::new));
    }

    public long compareUsingStringCompare(List<String> modifiedUrlSources, String url) {
        String oldHtmlSource = yesterdaySiteStates.get(url);
        String newHtmlSource = todaySiteStates.get(url);

        long start = System.nanoTime();
        if (oldHtmlSource.compareTo(newHtmlSource) != 0) {
            modifiedUrlSources.add(url);
        }

        return System.nanoTime() - start;
    }

    public long compareUsingEncoding(List<String> modifiedUrlSources, String url) {
        String oldHtmlSource = yesterdaySiteStates.get(url);
        String newHtmlSource = todaySiteStates.get(url);

        long start = System.nanoTime();
        byte[] oldBytes = sourceEncoder.encode(oldHtmlSource);
        byte[] newBytes = sourceEncoder.encode(newHtmlSource);
        for (int it = 0; it < newBytes.length; ++it) {
            if (oldBytes[it] != newBytes[it]) {
                modifiedUrlSources.add(url);
                break;
            }
        }

        return System.nanoTime() - start;
    }

    public List<String> getModifiedUrlSourcesList(Set<String> newUrls) {
        List<String> modifiedUrlSources = new ArrayList<>(
                todaySiteStates.size() - newUrls.size());

        long res = 0;
        for (Map.Entry<String, String> entry : todaySiteStates.entrySet()) {
            String url = entry.getKey();
            if (!newUrls.contains(url)) {
                res += compareUsingStringCompare(modifiedUrlSources, url);
                //res += compareUsingEncoding(modifiedUrlSources, url);
            }
        }

        LOGGER.info(String.format("Execution duration: %f sec.", res / 1_000_000_000d));
        return modifiedUrlSources;
    }

    public String makeEmail() {
        Set<String> newUrls = getMissingUrls(todaySiteStates, yesterdaySiteStates);
        Set<String> removedUrls = getMissingUrls(yesterdaySiteStates, todaySiteStates);
        List<String> modifiedUrlSources = getModifiedUrlSourcesList(newUrls);

        return new StringBuilder().append("Здравствуйте, дорогая ")
                .append(nameConfiguration.getFirst())
                .append(" ")
                .append(nameConfiguration.getMiddle())
                .append("\n\nЗа последние сутки во вверенных Вам сайтах произошли следующие изменения:\n\n")
                .append("Исчезли следующие страницы: ")
                .append(String.join(", ", removedUrls))
                .append("\nПоявились следующие новые страницы: ")
                .append(String.join(", ", newUrls))
                .append("\nИзменились следующие страницы: ")
                .append(String.join(", ", modifiedUrlSources))
                .toString();
    }

    public void startExecution() {
        init();
        System.out.println(makeEmail());
    }
}
