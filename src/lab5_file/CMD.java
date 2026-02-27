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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.Calendar;
/**
 *
 * @author emyca
 */
public class CMD {
    private File dirActual;
     boolean modoEscritura= false;
     File archivoEscritura= null;
    
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
                return Mkdir(parametro);
            case "Mfile":
                return Mfile(parametro);
            case "Rm":
                return Rm(parametro);
            case "Cd":
                return Cd(parametro);
            case "Dir":
                return Dir();
            case "Date":
                return Date();
            case "Time":
                return Time();
            case "Escribir":
                try{
                    return Escribir(parametro);
                }catch(IOException excepcion){
                    return"Error al escribir: "+excepcion.getMessage();
                }
            case "Leer":
                return Leer(parametro);
            default:
                return "Comando no reconocido.\n";
        }
    }
    
    String Mkdir(String nombre){
        if(nombre.isEmpty())
            return "Use: Mkdir nombre\n";
        File newFolder= new File(dirActual,nombre);
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
        File newFile= new File(dirActual,nombre);
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
        File file= new File(dirActual,nombre);
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
            File padre= dirActual.getParentFile();
            if(padre!=null && padre.exists()){
                dirActual=padre;
                return "";
            }else
                return "No hay carpeta padre\n";
        }
        File file= new File(dirActual,nombre);
        if(file.exists() && file.isDirectory()){
            dirActual=file;
            return "";
        }else
            return "La carpeta no existe";
        
         
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
                modoEscritura=true;
                archivoEscritura = archivo;
                return "Escribiendo en " + nomArchivo + "...\n"
                + "(Escribe una línea y presiona ENTER para guardar)\n";
            }else{
                return"El archivo seleccionado es una carpeta.\n";
            }
        }else{
            return "El archivo seleccionado no existe.\n";
        }
    }
    
    private String escribirLinea(String linea) {
        try (FileWriter fileWriter = new FileWriter(archivoEscritura, true)) {
            fileWriter.write(linea + System.lineSeparator());
        } catch (IOException e) {
            modoEscritura = false;
            archivoEscritura = null;
            return "Error al escribir: " + e.getMessage() + "\n";
        }
        modoEscritura = false;
        archivoEscritura = null;

        return "Texto guardado correctamente.\n";
    }

    public String Leer(String nomArchivo) {
        if (nomArchivo.isEmpty()) {
            return "Debe ingresar el nombre del archivo.\n";
        }
        File archivo = new File(dirActual, nomArchivo);
        if (!archivo.exists()) {
            return "El archivo no existe.\n";
        }
        if (archivo.isDirectory()) {
            return "El archivo es una carpeta.\n";
        }

        try {
            String contenido = Files.readString(archivo.toPath(), StandardCharsets.UTF_8);
            return "Contenido de " + nomArchivo + ":\n" + contenido + "\n";
        } catch (IOException e) {
            return "Error al leer: " + e.getMessage() + "\n";
        }
    }
}


    
