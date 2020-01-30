/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.edu.javeriana.gestorcontrasenias.presentacion;

import java.io.*;
import static java.util.Optional.empty;
import java.util.Timer;
import java.util.TimerTask;

import co.edu.javeriana.gestorcontrasenias.persistencia.ManejoArchivos;

/**
 *
 * @author Miguel
 */
public class PruebaProyecto {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Registro nuevoR = new Registro();
        Login nuevoLog = new Login();
        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;
        int i = 0;
        Registro nuevoRegistro = new Registro();
                
        
        if( !ManejoArchivos.verificarExistenciaArchivo() )
        {
           nuevoRegistro.setVisible( true );
        }
        else
        {
            nuevoLog.setVisible(true);
        }

    }

}
