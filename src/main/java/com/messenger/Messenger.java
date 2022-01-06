package com.messenger;


import com.messenger.template.Template;
import com.messenger.template.TemplateEngine;

import static com.messenger.UtilityAssertions.assertInputObjectsNotNull;


/**
 * The type Messenger.
 */
public class Messenger {

    private final MailServer mailServer;

    private final TemplateEngine templateEngine;

    /**
     * Instantiates a new Messenger.
     *
     * @param mailServer     the mail server
     * @param templateEngine the template engine
     */
    public Messenger(MailServer mailServer,
                     TemplateEngine templateEngine) {
        assertInputObjectsNotNull(mailServer, templateEngine);
        this.mailServer = mailServer;
        this.templateEngine = templateEngine;
    }

    /**
     * Send message.
     *
     * @param client   the client
     * @param template the template
     */
    public void sendMessage(Client client, Template template) {
        assertInputObjectsNotNull(client, template);
        String messageContent =
                templateEngine.generateMessage(template, client);
        mailServer.send(client.getAddresses(), messageContent);
    }
}