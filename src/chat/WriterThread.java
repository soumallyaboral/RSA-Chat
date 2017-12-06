package chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import minirsa.MiniRSA;

/**
 * A Runnable class that RSA-encrypts and writes data to a socket.
 *
 * @author Robert Li, Evan Schoenbach
 * @version April 20, 2012
 */
public class WriterThread {

    private long exponent;
    private long modulus;
    BufferedReader in;
    PrintWriter out;

    /**
     * Creates a WriterThread.
     *
     * @param socket the socket to write to.
     * @param exponent the exponent of the public RSA key.
     * @param modulus the modulus of the public RSA key.
     */
    public WriterThread(Socket socket, long exponent, long modulus) {
        this.exponent = exponent;
        this.modulus = modulus;
        this.in = new BufferedReader(
                new InputStreamReader(System.in));
        System.out.println("Writer encrypting with " + exponent + " " + modulus);
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (Exception e) {
            System.err.println("IO error in Writer");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void onSend(String input) {
        try {
            String encryptedInput = encrypt(input);
            out.println(encryptedInput);
            System.out.println("Sent: " + encryptedInput);
        } catch (Exception e) {
            System.err.println("IO error in Writer");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private String encrypt(String input) {
        String ret = "";
        for (int i = 0; i < input.length(); i++) {
            ret += MiniRSA.endecrypt(input.charAt(i), exponent, modulus) + " ";
        }
        return ret;
    }
}
