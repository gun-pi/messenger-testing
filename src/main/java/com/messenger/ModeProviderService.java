package com.messenger;

import com.messenger.modeservice.ConsoleModeService;
import com.messenger.modeservice.FileModeService;
import com.messenger.modeservice.ModeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import static com.messenger.UtilityAssertions.assertInputStringsNotBlankOrNull;

/**
 * Mode Provider service provides methods to get content from input and pull out content to output.
 * Mode Provider Service defines what input / output mode will be used (File or Console mode)
 * when is created. If "input" and "output" environment variables are provided and input path is valid,
 * File mode is used.
 */
public class ModeProviderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModeProviderService.class);

    private final ModeService modeService;

    /**
     * Mode Provider service constructor
     */
    public ModeProviderService() {
        String inputPath = System.getenv("input");
        String outputPath = System.getenv("output");
        if (inputPath != null && outputPath != null && Files.exists(Path.of(inputPath))) {
            this.modeService = new FileModeService(Path.of(inputPath), Path.of(outputPath));
        } else {
            Scanner scanner = new Scanner(System.in, StandardCharsets.ISO_8859_1);
            this.modeService = new ConsoleModeService(scanner);
        }
        LOGGER.info("ModeProviderService is created");
    }

    /**
     * Get content from input (file or console)
     *
     * @return content
     */
    public List<String> getContentFromInput() {
        return modeService.getContentFromInput();
    }

    /**
     * Pull out content to output (file or console)
     *
     * @param content content
     */
    public void pullOutToOutput(String content) {
        assertInputStringsNotBlankOrNull(content);
        modeService.pullOutToOutput(content);
    }
}
