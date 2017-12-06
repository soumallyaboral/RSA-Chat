package minirsa;

/**
 * An RSA key generator. Generates a random RSA key with the nth and mth prime 
 * numbers.
 * 
 * @author Robert Li, Evan Schoenbach
 * @version April 20, 2012
 */
public class Keygen {

    /**
     * Runs the RSA key generator. Takes two command line arguments n and m, 
     * which are integers specifying which prime numbers to use. Generates an 
     * RSA key pair with the nth and mth primes.
     * 
     * @param args n and m.
     */
    public static void main(String[] args) {
       
        int n = 0, m = 0;
        try {
            n = 13;//Integer.parseInt(args[0]);
            m = 11;//Integer.parseInt(args[1]);
        } catch (Exception e) {
            
        }
        long p = getPrime(n);
        long q = getPrime(m);
        long[] key = MiniRSA.generateKey(p, q);
        System.out.println("Public: (" + key[0] + ", " + key[2] + 
                "); Private: (" + key[1] + ", " + key[2] + ").");
    }
    
    /**
     * Calculates the nth prime.
     * 
     * @param n
     * @return the nth prime.
     */
    private static long getPrime(int n) {
        long p = 1;
        while (n > 0) {
            p++;
            if (isPrime(p)) {
                n--;
            }
        }
        return p;
    }
    
    /**
     * Determines if p is prime.
     * 
     * @param p
     * @return true if p is prime, false otherwise.
     */
    private static boolean isPrime(long p) {
        if (p == 2 || p == 3) {
            return true;
        }
        if (divides(2, p) || divides(3, p)) {
            return false;
        }
        long n = 1;
        while (6 * n - 1 <= Math.sqrt(p)) {
            if (divides(6 * n - 1, p) || 
                (6 * n + 1 <= Math.sqrt(p) && divides(6 * n + 1, p))) {
                return false;
            }
            n++;
        }
        return true;
    }
    
    /**
     * Determines if n divides p, that is, p / n is a whole number.
     * 
     * @param n
     * @param p
     * @return true if n divides p, false otherwise.
     */
    private static boolean divides(long n, long p) {
        return (p / n) * n == p;
    }

}
