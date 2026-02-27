/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab5_file;

import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Calendar;

/**
 *
 * @author emyca
 */
public class CMD {

    private File dirActual;
    boolean modoEscritura = false;
    File archivoEscritura = null;

    public CMD() {
        // Carpeta del proyecto (directorio de ejecución)
        dirActual = new File(System.getProperty("user.dir"));

        // Si por alguna razón rara no existe, cae al home
        if (!dirActual.exists()) {
            dirActual = new File(System.getProperty("user.home"));
        }
    }

    public String getComando() {
        return "\n" + dirActual.getAbsolutePath() + ">";
    }

    public String procesarComando(String linea, Component parent) {

        if (linea == null) {
            return "";
        }
        linea = linea.trim();

        // MODO ESCRITURA: acepta muchas líneas hasta EXIT
        if (modoEscritura) {
            if (linea.equals("EXIT")) {
                modoEscritura = false;
                archivoEscritura = null;
                return "Modo escritura finalizado.\n";
            }
            return escribirLinea(linea);
        }

        if (linea.isEmpty()) {
            return "";
        }

        String[] partes = linea.split(" ", 2);
        String comando = partes[0];
        String parametro = (partes.length > 1) ? partes[1].trim() : "";

        switch (comando) {
            case "Mkdir":
                return Mkdir(parametro);
            case "Mfile":
                return Mfile(parametro);
            case "Rm":
                return Rm(parametro);
            case "Cd":
                return Cd(parametro);
            case "...":
                return Cd("..");
            case "Dir":
                return Dir();
            case "Date":
                return Date();
            case "Time":
                return Time();
            case "Wr":
                try {
                    return Wr(parametro);
                } catch (IOException excepcion) {
                    return "Error al escribir: " + excepcion.getMessage() + "\n";
                }
            case "Rd":
                return Rd(parametro);
            default:
                return "Comando no reconocido.\n";
        }
    }

    String Mkdir(String nombre) {
        if (nombre.isEmpty()) {
            return "Use: Mkdir nombre\n";
        }

        File newFolder = new File(dirActual, nombre);
        if (newFolder.exists()) {
            return "La carpeta ya existe\n";
        }

        if (newFolder.mkdir()) {
            return "Se creo la carpeta " + newFolder.getName() + "\n";
        } else {
            return "No se pudo crear la carpeta\n";
        }
    }

    String Mfile(String nombre) {
        if (nombre.isEmpty() || !nombre.contains(".")) {
            return "Use: Mfile nombre.extension\n";
        }

        File newFile = new File(dirActual, nombre);
        try {
            if (newFile.createNewFile()) {
                return "Se creo el archivo " + newFile.getName() + "\n";
            } else {
                return "No se pudo crear el archivo\n";
            }
        } catch (IOException e) {
            return "Error: " + e.getMessage() + "\n";
        }
    }

    boolean borrar(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File f : files) {
                    borrar(f);
                }
            }
        }
        return file.delete();
    }

    String Rm(String nombre) {
        if (nombre.isEmpty()) {
            return "Use: Rm nombreDelArchivo/Carpeta\n";
        }

        File file = new File(dirActual, nombre);
        if (!file.exists()) {
            return "El archivo/carpeta no exite\n";
        }

        Boolean seBorro = borrar(file);
        if (seBorro) {
            return "Eliminado " + nombre + "\n";
        } else {
            return "No se pudo elminar\n";
        }
    }

    public String Cd(String argumento) {
        if (argumento.isEmpty()) {
            return "Uso: Cd <carpeta> o Cd ..\n";
        }

        if (argumento.equals("..")) {
            File padre = dirActual.getParentFile();
            if (padre != null && padre.exists()) {
                dirActual = padre;
                return "";
            } else {
                return "No hay carpeta padre.\n";
            }
        }

        if (argumento.contains("..")) {
            return "Comando no reconocido.\n";
        }

        File nueva = new File(dirActual, argumento);

        if (nueva.exists() && nueva.isDirectory()) {
            dirActual = nueva;
            return "";
        } else {
            return "La carpeta no existe: " + argumento + "\n";
        }
    }

    public String Dir() {
        File[] archivos = dirActual.listFiles();
        StringBuilder armarString = new StringBuilder();
        if (archivos == null || archivos.length == 0) {
            return "La carpeta está vacía.\n";
        }
        armarString.append("Contenido de ").append(dirActual.getAbsolutePath()).append(":\n");
        for (File archivo : archivos) {
            if (archivo.isFile()) {
                armarString.append("<FILE>   ");
            } else {
                armarString.append("<DIR>    ");
            }
            armarString.append("Bytes: ").append(archivo.length()).append("   ").append(archivo.getName()).append("\n");
        }
        return armarString.toString();
    }

    public String Date() {
        Calendar calendario = Calendar.getInstance();
        return calendario.get(Calendar.DAY_OF_MONTH) + "/"
                + (calendario.get(Calendar.MONTH) + 1) + "/"
                + calendario.get(Calendar.YEAR) + "\n";
    }

    public String Time() {
        Calendar horaActual = Calendar.getInstance();
        return horaActual.get(Calendar.HOUR_OF_DAY) + ":"
                + horaActual.get(Calendar.MINUTE) + ":"
                + horaActual.get(Calendar.SECOND) + "\n";
    }

    public String Wr(String nomArchivo) throws IOException {
        if (nomArchivo.isEmpty()) {
            return "Debe ingresar el nombre del archivo.\n";
        }

        File archivo = new File(dirActual, nomArchivo);

        if (archivo.exists()) {
            if (archivo.isFile()) {
                modoEscritura = true;
                archivoEscritura = archivo;
                return "Escribiendo en " + nomArchivo + "...\n"
                        + "(Escribe líneas y presiona ENTER. Para terminar escribe EXIT)\n";
            } else {
                return "El archivo seleccionado es una carpeta.\n";
            }
        } else {
            return "El archivo seleccionado no existe.\n";
        }
    }

    private String escribirLinea(String linea) {
        try (FileWriter fileWriter = new FileWriter(archivoEscritura, true)) {
            fileWriter.write(linea + System.lineSeparator());
            return ""; // no mostrar "guardado" cada línea
        } catch (IOException e) {
            modoEscritura = false;
            archivoEscritura = null;
            return "Error al escribir: " + e.getMessage() + "\n";
        }
    }

    public String Rd(String nomArchivo) {
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
