package co.edu.javeriana.gestorcontrasenias.negocio;


import java.util.ArrayList;
import java.util.List;


/**
 *
 * Esta clase contiene todos los metodos relacionado con la
 */
public class GestorContrasenias 
{
    String contraseniaMaestra;
    List< Sitio > listaSitios = new ArrayList<>();

    public GestorContrasenias( String contraseniaMaestra ) 
    {
        this.contraseniaMaestra = contraseniaMaestra;
    }

    public String getContraseniaMaestra() 
    {
        return contraseniaMaestra;
    }

    public List< Sitio > getListaSitios() 
    {
        return listaSitios;
    }

    public void setContraseniaMaestra( String contraseniaMaestra )
    {
        this.contraseniaMaestra = contraseniaMaestra;
    }

    public void setListaSitios( List< Sitio > listaSitios )
    {
        this.listaSitios = listaSitios;
    }
    
}
