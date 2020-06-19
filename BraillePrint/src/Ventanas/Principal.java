package Ventanas;

import java.io.File;

/**
 *
 * @author fenix
 */
public class Principal {
    public static void main(String args[]){
        String path = "C:\\Users\\cesar\\Downloads\\BraillePrint\\src\\Ventanas\\Lexer.flex";
        generarLexer(path);
    }    
    public static void generarLexer(String path){
        File file = new File(path);
        JFlex.Main.generate(file);
    }
}
