package com.messenger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientTest {

    String clientName = "Ivan Ivanov";

    String clientAddress = "ivan.ivanov@mail.com";

    @Test
    @DisplayName("Partially mock client class and check getFullInfoClient method's returned string " +
            "containing client name and address")
    void partiallyMockClientAndCheckGetFullClientInfoMethod() {
        Client client = mock(Client.class, CALLS_REAL_METHODS);
        when(client.getName()).thenReturn(clientName);
        when(client.getAddresses()).thenReturn(clientAddress);

        String fullClientInfo = client.getFullClientInfo();

        assertTrue(fullClientInfo.contains(clientName), "Full client info should contain client name");
        assertTrue(fullClientInfo.contains(clientAddress), "Full client info should contain client addresses");
    }
}