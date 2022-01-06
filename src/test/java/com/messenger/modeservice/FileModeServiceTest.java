package com.messenger.modeservice;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

class FileModeServiceTest {

    FileModeService fileModeService;

    String inputFilePath = "inputfile.txt";

    String outputFilePath = "outputfile.txt";

    String expectedContent = "expected content";

    @Test
    @DisplayName("Create test file with content and read it using File Mode Service checking retrieved the content")
    void createTestFileAndReadItUsingFileModeService(@TempDir File temporaryDir) throws IOException {
        File inputTestFile = new File(temporaryDir, inputFilePath);
        Files.write(inputTestFile.toPath(), List.of(expectedContent), StandardCharsets.ISO_8859_1, CREATE, APPEND);
        fileModeService = new FileModeService(inputTestFile.toPath(), null);

        List<String> contentFromInput = fileModeService.getContentFromInput();

        Assertions.assertEquals(List.of(expectedContent), contentFromInput);
    }

    @Test
    @DisplayName("Write to output file using File Mode Service and check content of the file")
    void writeToOutputFileUsingFileModeServiceAndCheckTheFile(@TempDir File temporaryDir) throws IOException {
        File outputTestFile = new File(temporaryDir, outputFilePath);
        fileModeService = new FileModeService(null, outputTestFile.toPath());

        fileModeService.pullOutToOutput(expectedContent);

        List<String> contentFromOutput = Files.readAllLines(outputTestFile.toPath(), StandardCharsets.ISO_8859_1);
        Assertions.assertEquals(List.of(expectedContent), contentFromOutput);
    }
}