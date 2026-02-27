/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab5_file;

import java.io.*;

/**
 *
 * @author adria
 */
public class TemporalFunciones {
    File actual;
    boolean modoEscritura= false;
    File archivoEscritura= null;
    
    String Mkdir(String nombre){
        if(nombre.isEmpty())
            return "Use: Mkdir nombre\n";
        File newFolder= new File(actual,nombre);
        if(newFolder.exists())
            return "El archivo ya existe\n";
        if(newFolder.mkdir())
            return "Se creo la carpeta "+ newFolder.getName()+"\n";
        else
            return "No se pudo crear la carpeta\n";
    }
    String Mfile(String nombre){
        if(nombre.isEmpty() || !nombre.contains("."))
            return "Use: Mfile nombre.extension\n";
        File newFile= new File(actual,nombre);
        try{
            if(newFile.createNewFile())
                return "Se creo el archivo"+ newFile.getName()+"\n";
            else
                return "No se pudo crear el archivo\n";
        }catch(IOException e){
            return "Error: "+ e.getMessage();
        }
        
    }
    boolean borrar(File file){
        if(file.isDirectory() && file.listFiles().length >0){
            File[] files= file.listFiles();
            
            if(files!=null){
            for(File f: files){
                borrar(f);
            }
        }
        }
        return file.delete();
            
    }
    String Rm(String nombre){
        if(nombre.isEmpty())
            return "Use: Rm nombreDelArchivo/Carpeta\n";
        File file= new File(actual,nombre);
        if(!file.exists())
            return "El archivo/carpeta no exite\n";
        
        Boolean seBorro= borrar(file);
        if(seBorro)
            return "Eliminado"+ nombre+"\n";
        else
            return "No se pudo elminar\n";
        
    }
    String Cd(String nombre){
        if(nombre.isEmpty() || nombre.contains("."))
            return "Use: Cd nombreCarpeta o Cd ;;\n";
        if(nombre.equals(";;")){
            File padre= actual.getParentFile();
            if(padre!=null && padre.exists()){
                actual=padre;
                return "";
            }else
                return "No hay carpeta padre\n";
        }
        File file= new File(actual,nombre);
        if(file.exists() && file.isDirectory()){
            actual=file;
            return "";
        }else
            return "La carpeta no existe";
        
         
    }
}
