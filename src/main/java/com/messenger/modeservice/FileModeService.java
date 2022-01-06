package com.messenger.modeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static com.messenger.UtilityAssertions.assertInputStringsNotBlankOrNull;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

/**
 * File Mode service provides methods to work with file input and output
 */
public class FileModeService implements ModeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileModeService.class);

    private final Path inputFilePath;

    private final Path outputFilePath;

    /**
     * File Mode service constructor
     *
     * @param inputFilePath  input file path
     * @param outputFilePath output file path
     */
    public FileModeService(Path inputFilePath, Path outputFilePath) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
    }

    /**
     * Get content from file input
     *
     * @return content
     */
    @Override
    public List<String> getContentFromInput() {
        LOGGER.trace("Getting content from file {}", inputFilePath);

        List<String> inputFileContent;
        try {
            inputFileContent = Files.readAllLines(inputFilePath, StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            throw new UncheckedIOException("Exception occurred during reading input file!", e);
        }

        return inputFileContent;
    }

    /**
     * Pull out content to file output
     *
     * @param content content
     */
    @Override
    public void pullOutToOutput(String content) {
        assertInputStringsNotBlankOrNull(content);

        LOGGER.trace("Writing content to file {}. Content:\n{}", outputFilePath, content);
        try {
            Files.writeString(outputFilePath, content, StandardCharsets.ISO_8859_1, CREATE, APPEND);
        } catch (IOException e) {
            throw new UncheckedIOException("Exception occurred during writing to output file!", e);
        }
    }
}
