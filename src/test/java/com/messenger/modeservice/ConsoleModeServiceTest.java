package com.messenger.modeservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ConsoleModeServiceTest {

    @Mock
    Scanner scanner;

    ConsoleModeService consoleModeService;

    String expectedContentLine1 = "expected content line 1";

    String expectedContentLine2 = "expected content line 2";


    @Test
    @DisplayName("Mock scanner and test Console Mode Service's get content from input method")
    void testConsoleModeServiceGetContentMethod() {
        Mockito.when(scanner.nextInt()).thenReturn(2);
        Mockito.when(scanner.nextLine())
                .thenReturn(expectedContentLine1)
                .thenReturn(expectedContentLine2);
        consoleModeService = new ConsoleModeService(scanner);

        List<String> contentFromInput = consoleModeService.getContentFromInput();

        assertEquals(List.of(expectedContentLine1, expectedContentLine2), contentFromInput);
    }
}