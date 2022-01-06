package com.messenger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MailServerTest {

    @Spy
    ModeProviderService modeProviderService = new ModeProviderService();

    MailServer mailServer;


    @Test
    @DisplayName("Send message using Mail Server and check that Mode Provider Service's method was invoked 1 time")
    void sendMessageAndCheckModeProviderServiceMethodInvokedOnce() {
        mailServer = new MailServer(modeProviderService);
        String addresses = "ivan.ivanov@mail.com";
        String messageContent = "Message content";

        mailServer.send(addresses, messageContent);

        verify(modeProviderService).pullOutToOutput(messageContent);
    }
}
