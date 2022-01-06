package com.messenger;

import com.messenger.template.Template;
import com.messenger.template.TemplateEngine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MessengerTest {

    @Mock
    TemplateEngine templateEngine;

    @Mock
    MailServer mailServer;

    Messenger messenger;

    Template template = new Template("template text");

    Client client = new Client("name", "address");


    @Test
    @DisplayName("Check messenger send message method calling mail server method once")
    void checkMessengerCallsMailServerMethodOnce() {
        messenger = new Messenger(mailServer, templateEngine);

        messenger.sendMessage(client, template);

        verify(mailServer).send(client.getAddresses(), null);
    }
}