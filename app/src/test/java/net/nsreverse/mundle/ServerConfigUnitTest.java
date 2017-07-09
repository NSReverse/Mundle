package net.nsreverse.mundle;

import net.nsreverse.mundle.data.ServerConfiguration;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ServerConfigUnitTest -
 *
 * This class is meant to test the ServerConfiguration class in the data package. Log messages
 * are independent of MundleApplication's logging boolean.
 *
 * @author Robert
 * Created on 7/8/2017
 */
public class ServerConfigUnitTest {
    private static final String TAG = ServerConfigUnitTest.class.getSimpleName();

    @Test
    public void x10Server_sendIsCorrect() throws Exception {
        String address = ServerConfiguration.X10.getSendMessageAddress("rob", "Test Title",
                "Test Message", "testid");
        String address2 = ServerConfiguration.X10.getSendMessageAddress("rob", "Test",
                "Test", "testid");

        assertEquals("http://reverseeffectapps.x10.mx/send.php?u=rob&t=Test+Title&" +
                "m=Test+Message&id=testid",
                address);

        assertEquals("http://reverseeffectapps.x10.mx/send.php?u=rob&t=Test&" +
                "m=Test&id=testid", address2);


        logMessage("Send address: " + address);
        logMessage("Send address: " + address2);
    }

    @Test
    public void x10Server_viewIsCorrect() throws Exception {
        String address = ServerConfiguration.X10.getViewMessageAddress("rob", "testid");

        assertEquals("http://reverseeffectapps.x10.mx/view.php?u=rob&" +
                "id=testid",
                address);

        logMessage("View address: " + address);
    }

    private void logMessage(String message) {
        System.out.println(String.format("%s - %s", TAG, message));
    }
}