package com.messenger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mail server class.
 */
public class MailServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailServer.class);

    private final ModeProviderService modeProviderService;

    /**
     * Mail Server constructor
     *
     * @param modeProviderService Mode Provider service
     */
    public MailServer(ModeProviderService modeProviderService) {
        this.modeProviderService = modeProviderService;
    }

    /**
     * Send notification.
     *
     * @param addresses      the addresses
     * @param messageContent the message content
     */
    public void send(String addresses, String messageContent) {
        UtilityAssertions.assertInputStringsNotBlankOrNull(addresses, messageContent);
        LOGGER.trace("Sending email letter to {} with message content:\n{}", addresses, messageContent);
        modeProviderService.pullOutToOutput(messageContent);
    }
}
