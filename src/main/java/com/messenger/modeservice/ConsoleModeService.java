package com.messenger.modeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Console Mode service provides methods to work with console input and output
 */
public class ConsoleModeService implements ModeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleModeService.class);

    private final Scanner scanner;

    /**
     * Console Mode service constructor
     *
     * @param scanner scanner
     */
    public ConsoleModeService(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Get content from console input
     *
     * @return content
     */
    @Override
    public List<String> getContentFromInput() {
        LOGGER.trace("Getting content from console");

        List<String> inputContent = new ArrayList<>();
        int inputLinesNumber = scanner.nextInt();
        for (int i = 0; i < inputLinesNumber; i++) {
            String line = scanner.nextLine();
            inputContent.add(line);
        }

        return inputContent;
    }

    /**
     * Pull out content to console output
     *
     * @param content content
     */
    @Override
    public void pullOutToOutput(String content) {
        LOGGER.trace("Pulling out content to info logs");
        LOGGER.info(content);
    }
}
