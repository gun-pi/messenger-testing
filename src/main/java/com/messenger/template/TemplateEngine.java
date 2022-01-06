package com.messenger.template;

import com.messenger.Client;
import com.messenger.ModeProviderService;
import com.messenger.UtilityAssertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Template engine.
 */
public class TemplateEngine {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateEngine.class);

    private static final String TEMPLATE_VARIABLE_SEPARATOR = "=";
    private static final String TEMPLATE_VARIABLE_FORMAT = ".+=.+";

    private final ModeProviderService modeProviderService;

    /**
     * Template Engine constructor
     *
     * @param modeProviderService Mode provider service
     */
    public TemplateEngine(ModeProviderService modeProviderService) {
        this.modeProviderService = modeProviderService;
    }

    /**
     * Generate message string.
     *
     * @param template the template
     * @param client   the client
     * @return the string
     */
    public String generateMessage(Template template, Client client) {
        UtilityAssertions.assertInputObjectsNotNull(template, client);

        String templateText = template.getTemplateText();
        LOGGER.info("Generating message for client {} from template:\n{}", client.getName(), templateText);

        Map<String, String> templateVariables = getTemplateVariables(client);
        templateText = replacePlaceholdersInTemplateText(templateText, templateVariables);

        LOGGER.info("Message is generated for client {}:\n{}", client.getName(), templateText);
        return templateText;
    }

    private Map<String, String> getTemplateVariables(Client client) {
        Map<String, String> templateVariables = new HashMap<>();

        List<String> contentFromInput = modeProviderService.getContentFromInput();
        validateInputContent(contentFromInput);
        for (String expression : contentFromInput) {
            String[] variable = expression.strip().split(TEMPLATE_VARIABLE_SEPARATOR, 2);
            templateVariables.put(variable[0], variable[1]);
        }

        templateVariables.put("client", client.getName());
        LOGGER.trace("Template variables map is retrieved. The map size: {}", templateVariables.size());
        return templateVariables;
    }

    private void validateInputContent(List<String> inputFileContent) {
        if (inputFileContent.isEmpty()) {
            throw new IllegalArgumentException("Input should contain at least one placeholder value!");
        }
        for (int i = 0; i < inputFileContent.size(); i++) {
            if (!inputFileContent.get(i).matches(TEMPLATE_VARIABLE_FORMAT)) {
                String msg = String.format("Line #%d has incorrect format!", i + 1);
                throw new IllegalArgumentException(msg);
            }
        }
    }

    private String replacePlaceholdersInTemplateText(String templateText, Map<String, String> templateVariables) {
        for (Map.Entry<String, String> variable : templateVariables.entrySet()) {
            if (templateText.contains(variable.getKey())) {
                templateText = templateText.replaceAll("#\\{" + variable.getKey() + "}", variable.getValue());
            }
        }
        return templateText;
    }
}
