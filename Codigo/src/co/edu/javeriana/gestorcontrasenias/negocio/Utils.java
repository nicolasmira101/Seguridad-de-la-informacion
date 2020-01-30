package co.edu.javeriana.gestorcontrasenias.negocio;

import javax.crypto.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;

/*
 * Clase con los métodos principales de criptografía disponibles en Java.
 * Adaptado de: https://github.com/kdenhartog/Password-Manager/blob/master/SecurityFunction.java
*/
public class Utils 
{
    /*      
     * Método que transforma un mensaje en una nueva serie de caracteres con 
     * una longitud fija, en este caso se utiliza la función de hash SHA-512
    */
    public static byte[] hash( byte[] mensaje ) throws NoSuchAlgorithmException, NoSuchProviderException
    {
        Security.addProvider( new BouncyCastleProvider() );
        MessageDigest mda = MessageDigest.getInstance( "SHA-512", "BC" );
        
        return mda.digest(mensaje);
    }
    
    /*
     * Método que cifra un archivo usando el algoritmo  AES mediante CTR
     * (Counter)que convierte el cifrado de bloque en un cifrado de flujo
    */
    public static byte[] cifrarAES( byte[] entrada, SecretKey key ) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        Security.addProvider( new BouncyCastleProvider() );
        Cipher aes = Cipher.getInstance( "AES/CTR/NoPadding", "BC" );
        
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[ aes.getBlockSize() ];
        random.nextBytes( iv );
        IvParameterSpec ivParametro = new IvParameterSpec( iv );
        
        aes.init( Cipher.ENCRYPT_MODE, key, ivParametro );
        byte[] cifrado = aes.doFinal( entrada );
        
        byte[] ivCifrado = Arrays.concatenate( iv, cifrado );
        
        return ivCifrado;
    }
    
    /*
     * Método que cifra un archivo usando el algoritmo  AES mediante CTR
     * (Counter)que convierte el cifrado de bloque en un cifrado de flujo
    */
    public static byte[] descifrarAES( byte[] entrada, SecretKey key ) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        Security.addProvider( new BouncyCastleProvider() );
        Cipher aes = Cipher.getInstance( "AES/CTR/NoPadding", "BC" );
        
        byte[] iv = new byte[ aes.getBlockSize() ];
        iv = Arrays.copyOf( entrada, iv.length );
        IvParameterSpec ivParametro = new IvParameterSpec( iv );
        
        byte[] cifrado = Arrays.copyOfRange( entrada, iv.length, entrada.length );
        
        aes.init( Cipher.DECRYPT_MODE, key, ivParametro);
        byte[] descifrado = aes.doFinal( cifrado);
        
        return descifrado;
    }
    
    /*
     * Método calcula el código de autenticación de mensaje (MAC) usando la
     * función de hash criptográfica SHA-512
    */
    public static byte[] hmac( byte[] entrada, SecretKey key ) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException 
    {
        Security.addProvider( new BouncyCastleProvider() );
        
        Mac mac = Mac.getInstance( "HmacSHA512", "BC" );
        mac.init( key );
        
        return mac.doFinal( entrada );
    }
    
    /*
     * Método que genera un número pseudoaleatorio
    */
    public static byte[] generarNumeroAleatorio( int tamanio ) 
    {
        Security.addProvider( new BouncyCastleProvider() );
        
        SecureRandom random = new SecureRandom();
        byte[] datos = new byte[ tamanio ];
        random.nextBytes(datos);
        
        return datos;
    }
    
    /*
     * Método que genera la llave secreta
    */
    public static SecretKey generarLlave( String contrasenia, byte[] salt ) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException
    {
        Security.addProvider( new BouncyCastleProvider() );
        
        SecretKeyFactory fabrica = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA256", "BC" );
        
        KeySpec spec = new PBEKeySpec( contrasenia.toCharArray(), salt, 65536, 128 );
        SecretKey llaveTemporal = fabrica.generateSecret( spec );
        SecretKey llave = new SecretKeySpec( llaveTemporal.getEncoded(), "AES" );
        
        return llave;
    }
    
    
}
