package chat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This is a simple command-line based chat client. When it is run, it must be
 * run as either a server or a client, and only one server and one client can
 * connect. To run the program:
 * <p>
 * Server: <code>java chat/ChatClient -s &lt;port number&gt;</code><br>
 * Client:
 * <code>java chat/ChatClient -c &lt;host&gt;:%lt;port number&gt;</code></p>
 *
 * @author Robert Li, Evan Schoenbach
 * @version April 19, 2012
 */
public class ChatClient {

    private InetAddress host;
    private int port;
    private int publicExp;
    private int privateExp;
    private int modulus;
    private IReceive receiver;
    private WriterThread writer;

    /**
     * Creates a ChatClient with a null host and the specified port number, as
     * for a server.
     *
     * @param port the port number to connect on.
     */
    public ChatClient(int port, int publicExp, int privateExp, int modulus, IReceive receiver) {
        this(null, port, publicExp, privateExp, modulus, receiver);
    }

    /**
     * Creates a ChatClient with the specified host and port number, as for a
     * client.
     *
     * @param host the host to connect to.
     * @param port the port to connect to.
     */
    public ChatClient(String host, int port, int publicExp, int privateExp, int modulus, IReceive receiver) {
        this.port = port;
        try {
            this.host = InetAddress.getByName(host);
        } catch (Exception e) {
            System.err.println("Host error");
            e.printStackTrace();
            System.exit(-1);
        }

        this.publicExp = publicExp;
        this.privateExp = privateExp;
        this.modulus = modulus;
        this.receiver = receiver;
    }

    /**
     * Initializes the server by listening and accepting a connection on the
     * specified port.
     */
    public void startServer() {
        System.out.println("Server listening on port " + port + ".");
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
        } catch (Exception e) {
            System.err.println("Connection error");
            e.printStackTrace();
            System.exit(-1);
        }
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (Exception e) {
                System.err.println("Connection error");
                e.printStackTrace();
                System.exit(-1);
            }
        }
        System.out.println("Server connected.");
        createThreads(clientSocket);
    }

    /**
     * Initializes a client by connecting to the previously specified host and
     * port.
     */
    public void startClient() {
        System.out.println("Client attempting to connect to "
                + host + ":" + port + ".");
        Socket serverSocket = null;
        try {
            serverSocket = new Socket(host, port);
        } catch (Exception e) {
            System.err.println("Connection error");
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("Client connected.");
        createThreads(serverSocket);
    }

    /**
     * Creates the reader and writer threads.
     *
     * @param socket the connected socket to read and write from.
     */
    private void createThreads(Socket socket) {
        // Send public RSA key, receive partner's public RSA key
        int partnerExp = 0, partnerMod = 0;
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            out.println(publicExp + " " + modulus);
            String[] partnerKey = in.readLine().split(" ");
            partnerExp = Integer.parseInt(partnerKey[0]);
            partnerMod = Integer.parseInt(partnerKey[1]);
        } catch (Exception e) {
            System.err.println("IO error");
            e.printStackTrace();
            System.exit(-1);
        }

        Thread reader = new Thread(
                new ReaderThread(socket, privateExp, modulus, receiver));
        reader.start();

        writer = new WriterThread(socket, partnerExp, partnerMod);
    }

    public void onSend(String text) {
        writer.onSend(text);
    }

}
