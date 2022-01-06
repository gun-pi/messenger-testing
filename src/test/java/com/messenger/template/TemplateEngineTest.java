package com.messenger.template;

import com.messenger.Client;
import com.messenger.ModeProviderService;
import com.messenger.TestReportExtension;
import com.messenger.UnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.Mockito.when;

@ExtendWith({TestReportExtension.class, MockitoExtension.class})
class TemplateEngineTest {

    @Mock
    ModeProviderService modeProviderService;

    TemplateEngine templateEngine;

    Client client;

    @BeforeEach
    void initialize() {
        client = new Client("Ivan Ivanov", "ivan.ivanov@mail.com");
        templateEngine = new TemplateEngine(modeProviderService);
    }

    @UnitTest
    @DisplayName("Generate message for client from template text and check that generated message does not contain " +
            "placeholders and contains input variables")
    void generateMessage() {
        Template template = new Template("Hi #{client}!\n " +
                "There is #{value} new articles about #{subject}. " +
                "Do you want to check them?\n " +
                "Just click on the link: https://news.com/#{subject}\n");
        when(modeProviderService.getContentFromInput()).thenReturn(List.of(
                "subject=SPORT",
                "value=3"
        ));

        String generatedMessage = templateEngine.generateMessage(template, client);

        Set<String> placeholders = Set.of("#{client}", "#{value}", "#{subject}");
        placeholders.forEach(placeholder -> assertFalse(generatedMessage.contains(placeholder), "Generated message should not contain placeholder " + placeholder));
        Set<String> variables = Set.of("Ivan Ivanov", "3", "SPORT");
        variables.forEach(variable -> assertTrue(generatedMessage.contains(variable), "Generated message should contain variable's value " + variable));
    }

    @UnitTest
    @DisplayName("Check throwing exception in case template variables not provided")
    void getExceptionIfTemplateVariablesNotProvided() {
        Template template = new Template("Hi #{client}!\n " +
                "There is #{value} new articles about #{subject}. " +
                "Do you want to check them?\n " +
                "Just click on the link: https://news.com/#{subject}\n");
        when(modeProviderService.getContentFromInput()).thenReturn(List.of());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> templateEngine.generateMessage(template, client));
        assertTrue(exception.getMessage().contains("has incorrect format"), "Illegal argument exception message should be about incorrect format");
    }

    @UnitTest
    @DisplayName("Generate message with template variable containing equals sign")
    void generateMessageWithTemplateVariableContainingEqualsSign() {
        Template template = new Template("Hi #{client}!\n " +
                "Do you know that Earth's free fall acceleration #{equation}?\n" +
                "Do you want to know more? " +
                "Just click on the link: https://news.com/#{subject}\n");
        when(modeProviderService.getContentFromInput()).thenReturn(List.of(
                "equation=g = 9.8 m/s",
                "subject=SCIENCE"
        ));

        String generatedMessage = templateEngine.generateMessage(template, client);

        Set<String> placeholders = Set.of("#{client}", "#{equation}", "#{subject}");
        placeholders.forEach(placeholder -> assertFalse(generatedMessage.contains(placeholder), "Generated message should not contain placeholder " + placeholder));
        Set<String> variables = Set.of("Ivan Ivanov", "g = 9.8 m/s", "SCIENCE");
        variables.forEach(variable -> assertTrue(generatedMessage.contains(variable), "Generated message should contain variable's value " + variable));
    }

    @UnitTest
    @DisplayName("Generate message with wrong variables ignoring them")
    void generateMessageWithWrongVariables() {
        Template template = new Template("Hi #{client}!\n " +
                "There is #{value} new articles about #{subject}. " +
                "Do you want to check them?\n " +
                "Just click on the link: https://news.com/#{subject}\n");
        when(modeProviderService.getContentFromInput()).thenReturn(List.of(
                "score=5:0",
                "team=BOSTON RED SOX",
                "subject=SPORT",
                "value=3"
        ));

        String generatedMessage = templateEngine.generateMessage(template, client);

        Set<String> placeholders = Set.of("#{client}", "#{value}", "#{subject}");
        placeholders.forEach(placeholder -> assertFalse(generatedMessage.contains(placeholder), "Generated message should not contain placeholder " + placeholder));
        Set<String> variables = Set.of("Ivan Ivanov", "3", "SPORT");
        variables.forEach(variable -> assertTrue(generatedMessage.contains(variable), "Generated message should contain variable's value " + variable));
    }

    @UnitTest
    @DisplayName("Generate message with a variables whose syntax looks like a placeholder")
    void generateMessageWithVariableSyntaxLikePlaceholder() {
        Template template = new Template("Hi #{client}!\n " +
                "There is #{value} new articles about #{subject}. " +
                "Do you want to check them?\n " +
                "Just click on the link: https://news.com/#{subject}\n");
        when(modeProviderService.getContentFromInput()).thenReturn(List.of(
                "subject=SPORT",
                "value=#{value}"
        ));

        String generatedMessage = templateEngine.generateMessage(template, client);

        Set<String> placeholders = Set.of("#{client}", "#{subject}");
        placeholders.forEach(placeholder -> assertFalse(generatedMessage.contains(placeholder), "Generated message should not contain placeholder " + placeholder));
        Set<String> variables = Set.of("Ivan Ivanov", "#{value}", "SPORT");
        variables.forEach(variable -> assertTrue(generatedMessage.contains(variable), "Generated message should contain variable's value " + variable));
    }

    @TestFactory
    @DisplayName("Generate message for client from template text and check that generated message " +
            "does not contain placeholders")
    @EnabledIfEnvironmentVariable(named = "additional_tests", matches = "enable")
    Stream<DynamicTest> generateMessageAndCheckTextNotContainPlaceholders() {
        Template template = new Template("Hi #{client}!\n " +
                "There is #{value} new articles about #{subject}. " +
                "Do you want to check them?\n " +
                "Just click on the link: https://news.com/#{subject}\n");
        when(modeProviderService.getContentFromInput()).thenReturn(List.of(
                "subject=SPORT",
                "value=3"
        ));

        String generatedMessage = templateEngine.generateMessage(template, client);

        return Stream.of("#{client}", "#{value}", "#{subject}")
                .map(placeholder ->
                        dynamicTest("Generated message does not contain placeholder " + placeholder,
                                () -> assertFalse(generatedMessage.contains(placeholder), "Generated message should not contain placeholder " + placeholder))
                );
    }

    @ParameterizedTest
    @CsvSource({
            "#{subject},SPORT",
            "#{value},3",
            "#{client},Ivan Ivanov",
            "#{tag}, #{tag}"})
    @DisplayName("Generate messages with different placeholders")
    @EnabledIfEnvironmentVariable(named = "additional_tests", matches = "enable")
    void generateMessagesWithDifferentPlaceholders(String templateText, String expectedText) {
        Template template = new Template(templateText);
        when(modeProviderService.getContentFromInput()).thenReturn(List.of(
                "subject=SPORT",
                "value=3"
        ));

        String generatedMessage = templateEngine.generateMessage(template, client);

        assertEquals(generatedMessage, expectedText);
    }
}
