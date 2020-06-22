package Ventanas;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 *
 * @author fenix
 */
public class Principal {
    public static void main(String args[]) throws Exception{
        String path1 = "/home/larios/Desktop/BraillePrint/COMPILADORBP/BraillePrint/src/Ventanas/Lexer.flex";
        String path2 = "/home/larios/Desktop/BraillePrint/COMPILADORBP/BraillePrint/src/Ventanas/LexerCup.flex";
        String[] rutaS = {"-parser", "Sintax", "/home/larios/Desktop/BraillePrint/COMPILADORBP/BraillePrint/src/Ventanas/Sintax.cup"};
        generar(path1, path2, rutaS);
    }    
    public static void generar(String path1, String path2, String[] rutaS)throws IOException, Exception{
        File file;
        file = new File(path1);
        JFlex.Main.generate(file);
        file = new File(path2);
        JFlex.Main.generate(file);
        java_cup.Main.main(rutaS);

        Path rutaSym = Paths.get("/home/larios/Desktop/BraillePrint/COMPILADORBP/BraillePrint/src/Ventanas/sym.java");
        if (Files.exists(rutaSym)) {
            Files.delete(rutaSym);
        }
        Files.move(
                Paths.get("/home/larios/Desktop/BraillePrint/COMPILADORBP/BraillePrint/sym.java"), 
                Paths.get("/home/larios/Desktop/BraillePrint/COMPILADORBP/BraillePrint/src/Ventanas/sym.java")
        );
        Path rutaSin = Paths.get("/home/larios/Desktop/BraillePrint/COMPILADORBP/BraillePrint/src/Ventanas/Sintax.java");
        if (Files.exists(rutaSin)) {
            Files.delete(rutaSin);
        }
        Files.move(
                Paths.get("/home/larios/Desktop/BraillePrint/COMPILADORBP/BraillePrint/Sintax.java"), 
                Paths.get("/home/larios/Desktop/BraillePrint/COMPILADORBP/BraillePrint/src/Ventanas/Sintax.java")
        );
    }

}
