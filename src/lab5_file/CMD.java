/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab5_file;
import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Calendar;

/**
 *
 * @author emyca
 */
public class CMD {
    private File dirActual;
    
    public CMD(){
        dirActual= new File("C:\\");
        if(!dirActual.exists()){
            dirActual = new File(System.getProperty("user.home"));
        }
    }
    
    public String getComando(){
        return "\n"+dirActual.getAbsolutePath()+">";
    }
    
    public String extraerParametro(String texto){
            int inicio=texto.indexOf("<");
            int fin=texto.indexOf(">");
            
            if(inicio!=-1 && fin!=-1 && fin>inicio){
                return texto.substring(inicio+1,fin);
            }else{
                throw new IllegalArgumentException();
            }
    }
    
    public String procesarComando(String linea, Component parent){
        if(linea.equals("<...>")){
            //regresar();
            return "";
        }
        
        int posicion=linea.indexOf("<");
        
        String comando;
        String parametro="";
        
        if (posicion!=-1){
            comando=linea.substring(0,posicion);
            try{
                parametro=extraerParametro(linea);
            }catch(Exception excepcion){
                return"Formato incorrecto, ingrese los parametros dentro de <>.\n";
            }
        }else{
            comando=linea;
        }
        
        switch(comando){
            case "Mkdir":
                return;
            case "Mfile":
                return;
            case "Rm":
                return;
            case "Cd":
                return;
            case "Dir":
                return Dir();
            case "Date":
                return Date();
            case "Time":
                return Time();
            case "Escribir":
                return Escribir(parametro);
            case "Leer":
                return;
            default:
                return "Comando no reconocido.\n";
        }
    }
    
    public String Dir(){
        File [] archivos= dirActual.listFiles();
        StringBuilder armarString= new StringBuilder();
        if(archivos!=null){
            armarString.append("Contenido de ").append(dirActual.getName()).append(":\n");
            for(File archivo: archivos){
                if(archivo.isFile()){
                    armarString.append("<FILE>");
                }else{
                    armarString.append("<DIR>");
                }
                armarString.append(archivo.getName()).append("\n");
            }
            return armarString.toString();
        }else{
            return"La carpeta esta vacia.\n";
        }
    }
    
    public String Date(){
        Calendar calendario= Calendar.getInstance();
        return calendario.get(Calendar.DAY_OF_MONTH)+"/"+calendario.get(Calendar.MONTH)+"/"+calendario.get(Calendar.YEAR);
    }
    
    public String Time(){
        Calendar horaActual= Calendar.getInstance();
        return horaActual.get(Calendar.HOUR_OF_DAY)+":"+horaActual.get(Calendar.MINUTE)+":"+horaActual.get(Calendar.SECOND);
    }
    
    public String Escribir (String nomArchivo) throws IOException{
        if(nomArchivo.isEmpty()){
            return "Debe ingresar el nombre del archivo.";
        }
        
        File archivo= new File(dirActual, nomArchivo);
        if(archivo.exists()){
            if(archivo.isFile()){
                FileWriter escribir= new FileWriter(archivo);
            }else{
                return"El archivo seleccionado es una carpeta.\n";
            }
        }else{
            return "El archivo seleccionado no existe.\n";
        }
    }

}
