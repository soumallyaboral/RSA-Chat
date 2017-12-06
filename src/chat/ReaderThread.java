package chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import minirsa.MiniRSA;

/**
 * A Runnable class that reads and decrypts RSA-encrypted data from a socket.
 *
 * @author Robert Li, Evan Schoenbach
 * @version April 20, 2012
 */
public class ReaderThread implements Runnable {

    private Socket socket;
    private long exponent;
    private long modulus;
    private IReceive receiver;

    /**
     * Creates a ReaderThread.
     *
     * @param socket the socket to listen on.
     * @param exponent the exponent of the private RSA key.
     * @param modulus the modulus of the private RSA key.
     */
    public ReaderThread(Socket socket, long exponent, long modulus, IReceive receiver) {
        this.socket = socket;
        this.exponent = exponent;
        this.modulus = modulus;
        System.out.println("Reader decrypting with " + exponent + " " + modulus);
        this.receiver = receiver;
    }

    @Override
    public void run() {
        BufferedReader in;
        try {
            in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            while (true) {
                String line = in.readLine();
                receiver.OnReceive(line);

                if (line.length() > 0) {
                    String[] chars = line.split(" ");
//                    System.out.println(">> " + decrypt(chars));
                    receiver.OnReceive(">> " + decrypt(chars));
                }
            }
        } catch (Exception e) {
            System.err.println("IO error in Reader");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private String decrypt(String[] chars) {
        String ret = "";
        for (String s : chars) {
            long decrypt = MiniRSA.endecrypt(
                    Integer.parseInt(s), exponent, modulus);
            int ascii = (int) (decrypt % 128); // 128 chars in ASCII
            ret += (char) ascii;
        }
        return ret;
    }

}
