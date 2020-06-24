package Ventanas;
import java_cup.runtime.Symbol;
%%
%class LexerCup
%type java_cup.runtime.Symbol
%cup
%full
%line
%column
%char

L = [a-zA-ZáéíóúñÑ]+
D = [0-9]+
Palabra = {L}({L}_|{L}|{D}|{D}_)*({L}|{D})*
Sign = [+]
espacio = [ |\t|\r| \n]+
coment = ([^*]|\*+[^/*])*
docComent = "/*"{coment}"*/"

%{
    private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline, yycolumn, value);
    }
    private Symbol symbol(int type){
        return new Symbol(type, yyline, yycolumn);
    }
    
%}

%%
/* Espacios en blanco */
{espacio} {/*Ignore*/}

/* Comentarios */
"#".* {/*Ignore*/}
{docComent} {/*Ignore*/}

/* Comillas */
 "\"" {return new Symbol(sym.Comillas, yychar, yyline, yytext());}

/* Comas */
 "," {return new Symbol(sym.Coma, yychar, yyline, yytext());}

/* Texto */
("\"" .* "\"") {return new Symbol(sym.Texto, yychar, yyline, yytext());}

/* Operadores logicos */
("&" | "||" | "!") {return new Symbol(sym.Op_Logico, yychar, yyline, yytext());}

/*Operadores Relacionales */
( ">" | "<" | "==" | "!=" | ">=" | "<=" ) {return new Symbol(sym.Op_Relacional, yychar, yyline, yytext());}

/*Operadores Booleanos*/
("true" | "TRUE" | "false" | "FALSE") {return new Symbol(sym.Op_Booleano, yychar, yyline, yytext());}

/*Punto y coma*/
(";") {return new Symbol(sym.Punto_Coma, yychar, yyline, yytext());}

/* Operador Suma */
( \+ ) {return new Symbol(sym.Adicion, yychar, yyline, yytext());}

/* Operador Resta */
( "-" ) {return new Symbol(sym.Sustraccion, yychar, yyline, yytext());}

/* Operador Multiplicacion */
( "*" ) {return new Symbol(sym.Producto, yychar, yyline, yytext());}

/* Operador Division */
( "/" ) {return new Symbol(sym.Cociente, yychar, yyline, yytext());}

/* Operador Igual */
( "=" ) {return new Symbol(sym.Asignacion, yychar, yyline, yytext());}

/* Operador Atribucion */
( "+=" | "=+" | "/=" | "=/" | "*=" | "=*" | "-=" | "=-") {return new Symbol(sym.Op_Atribucion, yychar, yyline, yytext());}

/* Parentesis de apertura */
( "(" ) {return new Symbol(sym.Parentesis_a, yychar, yyline, yytext());}

/* Parentesis de cierre */
( ")" ) {return new Symbol(sym.Parentesis_c, yychar, yyline, yytext());}

/* Llave de apertura */
( "{" ) {return new Symbol(sym.Llave_a, yychar, yyline, yytext());}

/* Llave de cierre */
( "}" ) {return new Symbol(sym.Llave_c, yychar, yyline, yytext());}

/* Corchete de apertura */
( "[" ) {return new Symbol(sym.Corchete_a, yychar, yyline, yytext());}

/* Corchete de cierre */
( "]" ) {return new Symbol(sym.Corchete_c, yychar, yyline, yytext());}

/* Numero entero */
({Sign}?)({D}+) {return new Symbol(sym.Num_Entero, yychar, yyline, yytext());}
(({Sign}?|"-"?)(("."|",")*{D}(("."|",")*{D}*("."|",")*)*)+) {return new Symbol(sym.ERR_NUM, yychar, yyline, yytext());}

("entero") {return new Symbol(sym.Entero, yychar, yyline, yytext());} 
("varcar") {return new Symbol(sym.VarCar, yychar, yyline, yytext());} 
("flot") {return new Symbol(sym.Flotante, yychar, yyline, yytext());} 
("si") {return new Symbol(sym.Si, yychar, yyline, yytext());} 
("alter") {return new Symbol(sym.Alter, yychar, yyline, yytext());}
("ciclomientras") {return new Symbol(sym.CicloMientras, yychar, yyline, yytext());}
("ciclofor") {return new Symbol(sym.CicloFor, yychar, yyline, yytext());}
("declarar") {return new Symbol(sym.Declarar, yychar, yyline, yytext());}
("camptrab") {return new Symbol(sym.Campotrabajo, yychar, yyline, yytext());}

/*Funciones*/
("FVer") {return new Symbol(sym.func_Ver, yychar, yyline, yytext());}
("FContCarac") {return new Symbol(sym.func_ContCarac, yychar, yyline, yytext());}
("FImprimir") {return new Symbol(sym.func_Imprimir, yychar, yyline, yytext());}
("FValCad") {return new Symbol(sym.func_ValCad, yychar, yyline, yytext());}

/* Identificadores */
({L}({L}_|{L}|{D}|{D}_)*({L}|{D})*) {return new Symbol(sym.Identificador, yychar, yyline, yytext());}
({D}{Palabra}) {return new Symbol(sym.ERR_ID, yychar, yyline, yytext());}

/* Error de analisis */
 . {return new Symbol(sym.ERROR, yychar, yyline, yytext());}
