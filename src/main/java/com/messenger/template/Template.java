package com.messenger.template;

import com.messenger.UtilityAssertions;

/**
 * The type Template.
 */
public class Template {

    private final String templateText;

    /**
     * Template constructor
     *
     * @param templateText Template text
     */
    public Template(String templateText) {
        UtilityAssertions.assertInputStringsNotBlankOrNull(templateText);
        this.templateText = templateText;
    }

    /**
     * Get template text
     *
     * @return template text
     */
    public String getTemplateText() {
        return templateText;
    }
}
