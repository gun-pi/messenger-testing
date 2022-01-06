package com.messenger;

/**
 * The type Client.
 */
public class Client {

    private final String name;

    private final String addresses;

    /**
     * Client constructor
     *
     * @param name      client name
     * @param addresses client addresses
     */
    public Client(String name, String addresses) {
        UtilityAssertions.assertInputStringsNotBlankOrNull(name, addresses);
        this.name = name;
        this.addresses = addresses;
    }

    /**
     * Get name
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets addresses.
     *
     * @return the addresses
     */
    public String getAddresses() {
        return addresses;
    }

    /**
     * Get full information about client.
     *
     * @return full information about client
     */
    public String getFullClientInfo() {
        return String.format("Client name: %s%nClient addresses: %s%n", getName(), getAddresses());
    }
}
