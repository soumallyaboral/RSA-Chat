package minirsa;

/**
 * An RSA key cracker. Given a public key, uses a brute force algorithm to find 
 * the corresponding private key.
 * 
 * @author Robert Li, Evan Schoenbach
 * @version April 20, 2012
 */
public class Cracker {

    /**
     * Runs the RSA key cracker. Takes two command line arguments, e and m, 
     * which are the exponent and modulus of the public key, respectively.
     * 
     * @param args e and m
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            printUsage();
        }
        
        long e = 0, m = 0;
        try {
            e = Integer.parseInt(args[0]);
            m = Integer.parseInt(args[1]);
        } catch (Exception ex) {
            printUsage();
        }
        
        long d = MiniRSA.crackKey(e, m);
        System.out.println("Private key is (" + d + ", " + m + ")");
    }
    
    /**
     * Prints directions on how to use the program.
     */
    private static void printUsage() {
        System.err.println("USAGE: java minirsa/Cracker e m\n");
        System.err.println("e and m are integers specifying the exponent " +
        		"and modulus, respectively, of the private key.");
        System.exit(-1);
    }

}
