/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab5_file;


import java.io.File;
import java.awt.Component;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MiCmd {

    private File currentDir;
    private boolean modoEscritura = false;
    private File archivoEscritura = null;

    public MiCmd() {

        currentDir = new File("C:\\");

        if (!currentDir.exists()) {
            currentDir = new File(System.getProperty("user.home"));
        }
    }

    public String getPrompt() {
        return "\n" + currentDir.getAbsolutePath() + ">";
    }

    public String procesarComando(String linea, Component parent) {

        linea = linea.trim();

        if (modoEscritura) {
            return escribirLinea(linea);
        }

        if (linea.isEmpty()) {
            return "";
        }

        String[] partes = linea.split("\\s+");
        String comando = partes[0];
        String argumentos = (linea.length() > comando.length())
                ? linea.substring(comando.length()).trim()
                : "";

        StringBuilder sb = new StringBuilder();

        switch (comando) {
            case "Mkdir":
                sb.append(cmdMkdir(argumentos));
                break;
            case "Mfile":
                sb.append(cmdMfile(argumentos));
                break;
            case "Rm":
                sb.append(cmdRm(argumentos));
                break;
            case "Cd":
                sb.append(cmdCd(argumentos));
                break;
            case "Dir":
                sb.append(cmdDir());
                break;
            case "Date":
                sb.append(cmdDate());
                break;
            case "Time":
                sb.append(cmdTime());
                break;
            case "wr":
                sb.append(cmdWrite(argumentos));
                break;
            case "rd":
                sb.append(cmdRead(argumentos));
                break;
            default:
                sb.append("Comando no reconocido.\n");
        }

        return sb.toString();
    }

    public String cmdMkdir(String nombre) {

        if (nombre.isEmpty()) {
            return "Uso: Mkdir <nombre>\n";
        }

        File nueva = new File(currentDir, nombre);

        if (nueva.exists()) {
            return "La carpeta ya existe.\n";
        }

        if (nueva.mkdir()) {
            return "Carpeta creada: " + nueva.getName() + "\n";
        } else {
            return "No se pudo crear la carpeta.\n";
        }

    }

    public String cmdMfile(String nombre) {

        if (nombre.isEmpty()) {
            return "Uso: Mfile <nombre.ext>\n";
        }

        File archivo = new File(currentDir, nombre);

        if (archivo.exists()) {
            return "El archivo ya existe.\n";
        }

        try {

            if (archivo.createNewFile()) {
                return "Archivo creado: " + archivo.getName() + "\n";
            } else {
                return "No se pudo crear el archivo.\n";
            }

        } catch (IOException e) {
            return "Error al crear archivo: " + e.getMessage() + "\n";
        }
    }

    public boolean borrarRecursivo(File f) {

        if (f.isDirectory()) {

            File[] hijos = f.listFiles();

            if (hijos != null) {

                for (File h : hijos) {
                    borrarRecursivo(h);
                }
            }

        }

        return f.delete();

    }

    public String cmdRm(String nombre) {

        if (nombre.isEmpty()) {
            return "Uso: Rm <archivo/carpeta>\n";
        }

        File objetivo = new File(currentDir, nombre);

        if (!objetivo.exists()) {
            return "No existe el archivo/carpeta.\n";
        }

        boolean ok = borrarRecursivo(objetivo);

        if (ok) {
            return "Eliminado: " + nombre + "\n";
        } else {
            return "No se pudo eliminar: " + nombre + "\n";
        }

    }

    public String cmdCd(String argumento) {
        if (argumento.isEmpty()) {
            return "Uso: Cd <carpeta> o Cd ..\n";
        }

        if (argumento.equals("..")) {
            File padre = currentDir.getParentFile();
            if (padre != null && padre.exists()) {
                currentDir = padre;
                return "";
            } else {
                return "No hay carpeta padre.\n";
            }
        }

        if (argumento.contains("..")) {
            return "Comando no reconocido.\n";
        }

        File nueva = new File(currentDir, argumento);
        if (nueva.exists() && nueva.isDirectory()) {
            currentDir = nueva;
            return "";
        } else {
            return "La carpeta no existe: " + argumento + "\n";
        }
    }

    public String cmdDir() {
        StringBuilder sb = new StringBuilder();
        File[] archivos = currentDir.listFiles();
        if (archivos == null || archivos.length == 0) {
            sb.append("La carpeta está vacía.\n");
            return sb.toString();
        }

        sb.append("Contenido de ").append(currentDir.getAbsolutePath()).append(":\n");
        for (File f : archivos) {
            if (f.isDirectory()) {
                sb.append("<DIR>  ").append(f.getName()).append("\n");
            } else {
                sb.append("       ").append(f.getName()).append("\n");
            }
        }
        return sb.toString();
    }

    public String cmdDate() {
        LocalDate hoy = LocalDate.now();
        return "Fecha actual: " + hoy.toString() + "\n";
    }

    public String cmdTime() {
        LocalTime ahora = LocalTime.now();
        String hora = ahora.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return "Hora actual: " + hora + "\n";
    }

    public String cmdWrite(String nombre) {
        if (nombre.isEmpty()) {
            return "Uso: wr <archivo.ext>\n";
        }

        File archivo = new File(currentDir, nombre);

        if (!archivo.exists() || archivo.isDirectory()) {
            return "El archivo no existe o es una carpeta.\n";
        }

        modoEscritura = true;
        archivoEscritura = archivo;

        return "Escribiendo en " + nombre + "...\n"
                + "(Escribe una línea y presiona ENTER para guardar)\n";
    }

    private String escribirLinea(String linea) {
        try (FileWriter fw = new FileWriter(archivoEscritura, true)) {
            fw.write(linea + System.lineSeparator());
        } catch (IOException e) {
            modoEscritura = false;
            archivoEscritura = null;
            return "Error al escribir: " + e.getMessage() + "\n";
        }

        modoEscritura = false;
        archivoEscritura = null;

        return "Texto guardado correctamente.\n";
    }

    public String cmdRead(String nombre) {
        if (nombre.isEmpty()) {
            return "Uso: rd <archivo.ext>\n";
        }
        File archivo = new File(currentDir, nombre);
        if (!archivo.exists() || archivo.isDirectory()) {
            return "El archivo no existe o es una carpeta.\n";
        }

        try {
            String contenido = Files.readString(archivo.toPath(), StandardCharsets.UTF_8);
            return "Contenido de " + nombre + ":\n" + contenido + "\n";
        } catch (IOException e) {
            return "Error al leer: " + e.getMessage() + "\n";
        }
    }}

