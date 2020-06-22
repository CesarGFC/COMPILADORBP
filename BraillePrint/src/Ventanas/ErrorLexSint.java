package Ventanas;
/**
 * @author Cesar
 */

public class ErrorLexSint {
    public String lexema, tipo, desc;
    int nError, linea, columna;
    
    public ErrorLexSint(){
        lexema = null;
        tipo = null;
        desc = null;
        linea = columna = 0;
    }

    public ErrorLexSint(int nError, String lexema, String tipo, int linea, int columna, String desc) {
        this.nError = nError;
        this.lexema = lexema;
        this.tipo = tipo;
        this.linea = linea;
        this.columna = columna;
        this.desc = desc;
    }
    
    public ErrorLexSint(String tipo, int linea, String desc) {
        this.tipo = tipo;
        this.linea = linea;
        this.desc = desc;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }
    
    public String getLexema() {
        return lexema;
    }
    
    @Override
    public String toString(){
        if (lexema != null)
            return tipo + " " + nError +" en la línea "+linea+" columna "+columna+": "+desc+" => "+lexema;
        else
            return tipo+" en la línea "+linea+" "+desc;
    }
    
}
