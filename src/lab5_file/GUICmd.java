/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lab5_file;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author emyca
 */
public class GUICmd extends JFrame {
    
    private final CMD cmd;
    private final JtextArea consola;
    private final JScrollPane scroll;
    private String promptConsola = "";
    
    public GUICmd(){
        super("CMD");
        
        cmd=new CMD();
        consola= crearConsola();
        scroll=new JScrollPane(consola);
        
        setSize(1000,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        setLayout(new BorderLayout());
        add(scroll,BorderLayout.CENTER);
        
        setVisible(true);
        
        imprimir("Minecrosoft Windows [Version 10.0.22621.521]\n");
        imprimir("(c) Microsoft Corporation. All rights reserved.\n\n");
        mostrarPrompt();
    }
    
    private JTextArea crearConsola(){
        JTextArea txt=new JtextArea();
        txt.setEditable(true);
        txt.setFont(new Font("Consolas", Font.BOLD, 16));
        txt.setBackground(Color.BLACK);
        txt.setForeground(Color.WHITE);
        txt.setMargin(new Insets(5, 5, 5, 5));
        
        txt.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {

                int caretPos = consola.getCaretPosition();
                int limite = consola.getText().lastIndexOf(promptConsola) + promptConsola.length();

                if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && caretPos <= limite) {
                    e.consume();
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_DELETE && caretPos < consola.getText().length()
                        && caretPos < limite) {
                    e.consume();
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_LEFT && caretPos <= limite) {
                    e.consume();
                    consola.setCaretPosition(consola.getText().length());
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();

                    String texto = consola.getText();
                    int posPrompt = texto.lastIndexOf(promptConsola);

                    String comando = texto.substring(posPrompt + promptConsola.length()).trim();

                    procesarEntrada(comando);
                    return;
                }

                if (caretPos < limite) {
                    consola.setCaretPosition(consola.getText().length());
                }
            }
        });
        return txt;
    }
    
    private void imprimir(String texto){
        consola.append(texto);
        consola.setCaretPosition(consola.getDocument().length());
    }
    
    private void mostrarPrompt(){
        promptConsola=cmd.getPrompt();
        imprimir(promptConsola);
    }
    
    private void procesarEntrada(String comando){
        imprimir("\n");
        
        if(!comando.isBlank()){
            String salida=cmd.procesarComando(comando,this);
            if(salida !=null){
                imprimir(salida);
            }
        }
        mostrarPrompt();
    }
}
