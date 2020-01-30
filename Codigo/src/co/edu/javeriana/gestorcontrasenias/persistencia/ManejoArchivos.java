package co.edu.javeriana.gestorcontrasenias.persistencia;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.SecretKey;

import co.edu.javeriana.gestorcontrasenias.negocio.Sitio;
import co.edu.javeriana.gestorcontrasenias.negocio.Utils;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.bouncycastle.util.Arrays;

/*
 * Esta clase contiene todos los metodos relacionado con la lectura y escritura
 * de archivos
 */
public class ManejoArchivos
{  
    /*
     * Variables globales que permiten realizarlas funciones criptográficas que
     * se van a utilizar en cada uno de los métodos
    */
    public static SecretKey cifLlave;
    public static SecretKey macLlave;
    public static byte[] macSalt;
    public static byte[] cifSalt;

    public ManejoArchivos()
    { }
    
    /*
     * Método que valida si el archivo de contraseña maestra y el archivo de  
     * sitios existe o no existe
    */
    public static boolean verificarExistenciaArchivo()
    {
        String archivoContraseniaMaestraPath = System.getProperty( "user.dir" );
        archivoContraseniaMaestraPath += "/contraseniaMaestra";
        
        String archivoSitiosPath = System.getProperty( "user.dir" );
        archivoSitiosPath += "/sitios";
        
        File archivoContraseniaMaestra = new File( archivoContraseniaMaestraPath );
        File archivoSitios = new File( archivoSitiosPath );
        
        return ( archivoContraseniaMaestra.exists() && archivoSitios.exists() );
    }
    
    /*
     * Método que valida si el archivo de contraseña maestra y el archivo de  
     * sitios existe o no existe
    */
    public static boolean verificarContraseniaMaestra( String contraseniaMaestra ) throws IOException, NoSuchAlgorithmException, NoSuchProviderException
    {
        String archivoContraseniaMaestraString = System.getProperty( "user.dir" );
        archivoContraseniaMaestraString += "/contraseniaMaestra"; 
        Path archivoContraseniaMaestraRuta = Paths.get( archivoContraseniaMaestraString );
        
        byte[] contenido = Files.readAllBytes( archivoContraseniaMaestraRuta );
        byte[] salt = Arrays.copyOf( contenido, 256 );
        byte[] contraseniaMaestraBytes = contraseniaMaestra.getBytes();
        
        byte[] contraseniaMaestraSalted = Arrays.concatenate( salt, contraseniaMaestraBytes );
        byte[] hash = Utils.hash( contraseniaMaestraSalted );
        
        return ( Arrays.areEqual( contenido, Arrays.concatenate(salt, hash ) ) );
    }
    
    /*
     * Método crear los archivos de contraseña maestra y sitios que el usuario
     * vaya agregando
    */
    public static void crearArchivo( String contraseniaMaestra ) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        String archivoContraseniaMaestraString = System.getProperty( "user.dir" );
        archivoContraseniaMaestraString += "/contraseniaMaestra"; 
        
        String archivoSitiosString = System.getProperty( "user.dir" );
        archivoSitiosString += "/sitios"; 
        
        Path archivoContraseniaMaestraRuta = Paths.get( archivoContraseniaMaestraString );
        Path archivoSitiosRuta = Paths.get( archivoSitiosString );
        
        File archivoContraseniaMaestra = new File( archivoContraseniaMaestraString );
        File archivoSitios = new File( archivoSitiosString );
                
        Files.deleteIfExists( archivoContraseniaMaestraRuta );
        Files.deleteIfExists( archivoSitiosRuta );
        
        archivoContraseniaMaestra.createNewFile();
        archivoSitios.createNewFile();
        
        byte[] contraseniaMaestraBytes = contraseniaMaestra.getBytes();
        
        cifSalt = Utils.generarNumeroAleatorio( 256 );
        macSalt = Utils.generarNumeroAleatorio( 256 );
        byte[] contraseniaSalted = Arrays.concatenate( cifSalt, contraseniaMaestraBytes );
        
        byte[] hash = Utils.hash( contraseniaSalted );
        byte[] saltHash = Arrays.concatenate( cifSalt, hash );
        
        try ( FileOutputStream salida = new FileOutputStream( "contraseniaMaestra" ) )
        {
            salida.write( saltHash );
            salida.close();
            
        }
        catch ( Exception e )
        {
            System.out.println( "Algo fallo: " + e );
        }
        
        cifLlave = Utils.generarLlave( contraseniaMaestra, cifSalt );
        macLlave = Utils.generarLlave( contraseniaMaestra, macSalt );
        
        byte[] informacionArchivoSitios = Files.readAllBytes( archivoSitiosRuta );
        byte[] cifrado = Utils.cifrarAES( informacionArchivoSitios, cifLlave );
        byte[] hmac = Utils.hmac( cifrado, macLlave );
        byte[] hmacSaltCifrado = Arrays.concatenate( macSalt, hmac, cifrado );
        
        try ( FileOutputStream salida = new FileOutputStream( "sitios" ) )
        {
            salida.write( hmacSaltCifrado );
            salida.close();
            
        }
        catch ( Exception e )
        {
            System.out.println( "Algo fallo: " + e );
        }
    }
    
    /*
     * Método que verifica si un sitio existe o no existe
    */
    public static String buscarSitio( Sitio nuevoSitio, byte[] informacion ) throws UnsupportedEncodingException
    {
        String informacionString = new String( informacion, "UTF-8" );
        String[] sitios = informacionString.split( "!" );
        String nombreSitio = nuevoSitio.getNombre();
        String usuarioSitio = nuevoSitio.getUser();
        String id = nombreSitio + " " + usuarioSitio;
        
        for( String sitio : sitios )
        {
            if( sitio.contains( id ) )
            {
                return sitio;
            }
        }
                
        return null;
    }
    /*
     * Método que agrega un sitio en caso de no existir
    */
    public static void agregarSitio( Sitio nuevoSitio ) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException
    {
        String archivoSitiosString = System.getProperty( "user.dir" );
        archivoSitiosString += "/sitios";  
        Path archivoSitiosRuta = Paths.get( archivoSitiosString );
        byte[] informacion = Files.readAllBytes( archivoSitiosRuta );
        
        byte[] informacionCifrada = Arrays.copyOfRange( informacion, 320, informacion.length );
        byte[] descifrado = Utils.descifrarAES( informacionCifrada, cifLlave);
        
        String sitioExiste = buscarSitio( nuevoSitio, informacion );
        if( sitioExiste == null )
        {
            String nombre = nuevoSitio.getNombre();
            String url = nuevoSitio.getUrl();
            String user = nuevoSitio.getUser();
            String contrasenia = nuevoSitio.getContrasenia();
            
            String sitio = nombre + " " + url + " " + user + " " + contrasenia + "!";
            byte[] informacionBytes = sitio.getBytes( "UTF-8" );
            byte[] nuevaInformacion = Arrays.concatenate( descifrado, informacionBytes );
            byte[] cifrado = Utils.cifrarAES( nuevaInformacion, cifLlave );
            
            byte[] hmac = Utils.hmac( cifrado, macLlave );
            byte[] hmacSaltCifrado = Arrays.concatenate( macSalt, hmac, cifrado );
            
            try ( FileOutputStream salida = new FileOutputStream( "sitios" ) )
            {
                salida.write( hmacSaltCifrado );
                salida.close();
                System.out.println( "Sitio creado exitosamente" );

            }
            catch ( Exception e )
            {
                System.out.println( "Algo fallo: " + e );
            }
        }
        else
        {
            System.out.println( "Sitio ya existe" );
        }
    }
}
