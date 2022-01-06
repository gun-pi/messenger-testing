package com.messenger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class TestReportExtension implements InvocationInterceptor {

    private final String testReportFilePath;

    public TestReportExtension() {
        String testReportFilePath = System.getenv("test_report_filepath");
        if (testReportFilePath == null || testReportFilePath.isBlank()) {
            testReportFilePath = "testreport.txt";
        }
        this.testReportFilePath = testReportFilePath;
    }

    @Override
    public void interceptTestMethod(Invocation<Void> invocation,
                                    ReflectiveInvocationContext<Method> invocationContext,
                                    ExtensionContext extensionContext) throws Throwable {

        long beforeTest = System.currentTimeMillis();
        try {
            invocation.proceed();
        } finally {
            long afterTest = System.currentTimeMillis();
            long testDuration = afterTest - beforeTest;
            outputTestExecutionInformationToFile(invocationContext, testDuration);
        }
    }

    @Override
    public <T> T interceptTestFactoryMethod(Invocation<T> invocation,
                                            ReflectiveInvocationContext<Method> invocationContext,
                                            ExtensionContext extensionContext) throws Throwable {
        T proceedResult;
        long beforeTest = System.currentTimeMillis();
        try {
            proceedResult = invocation.proceed();
        } finally {
            long afterTest = System.currentTimeMillis();
            long testDuration = afterTest - beforeTest;
            outputTestExecutionInformationToFile(invocationContext, testDuration);
        }
        return proceedResult;
    }


    @Override
    public void interceptTestTemplateMethod(Invocation<Void> invocation,
                                            ReflectiveInvocationContext<Method> invocationContext,
                                            ExtensionContext extensionContext) throws Throwable {
        long beforeTest = System.currentTimeMillis();
        try {
            invocation.proceed();
        } finally {
            long afterTest = System.currentTimeMillis();
            long testDuration = afterTest - beforeTest;
            outputTestExecutionInformationToFile(invocationContext, testDuration);
        }
    }

    private void outputTestExecutionInformationToFile(ReflectiveInvocationContext<Method> invocationContext, long testDuration) {
        String testClassName = invocationContext.getTargetClass().getSimpleName();
        String testMethodName = invocationContext.getExecutable().getName();
        String testDescription = invocationContext.getExecutable().getAnnotation(DisplayName.class).value();
        String testArguments = invocationContext.getArguments().stream().map(Object::toString).collect(Collectors.joining(", "));
        if (testArguments.isBlank()) {
            testArguments = "no arguments";
        }
        String executionTime = ZonedDateTime.now(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        String testExecutionInformation = String.format(
                        "Execution time: %s\n" +
                        "Test name: %s.%s\n" +
                        "Test description: %s\n" +
                        "Test arguments: %s\n" +
                        "Test duration: %s ms\n\n",
                executionTime,
                testClassName, testMethodName,
                testDescription,
                testArguments,
                testDuration
        );
        try {
            Files.writeString(Path.of(testReportFilePath), testExecutionInformation, StandardCharsets.ISO_8859_1, CREATE, APPEND);
        } catch (IOException e) {
            throw new UncheckedIOException("Exception occurred during writing to output file!", e);
        }
    }
}
