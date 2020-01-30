/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.javeriana.gestorcontrasenias.negocio;

/**
 * Esta Clase contiene todos los atributos necesarios para caracterizar un sitio 
 */
public class Sitio 
{
    String nombre;
    String url;
    String user;
    String contrasenia;

    public Sitio( String nombre, String url, String user, String contrasenia ) 
    {
        this.nombre = nombre;
        this.url = url;
        this.user = user;
        this.contrasenia = contrasenia;
    }
    
    public String getNombre() 
    {
        return nombre;
    }

    public void setNombre( String nombre ) 
    {
        this.nombre = nombre;
    }

    public String getUrl() 
    {
        return url;
    }

    public void setUrl( String url ) 
    {
        this.url = url;
    }

    public String getUser() 
    {
        return user;
    }

    public void setUser( String user ) 
    {
        this.user = user;
    }

    public String getContrasenia()
    {
        return contrasenia;
    }

    public void setContrasenia( String contrasenia ) 
    {
        this.contrasenia = contrasenia;
    }
    
    
}
