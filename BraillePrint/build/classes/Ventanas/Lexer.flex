package Ventanas;
import static Ventanas.Token.*;
%%
%class Lexer
%type Token
%line
%column

L = [a-zA-ZáéíóúñÑ]+
D = [0-9]+
Palabra = {L}({L}_|{L}|{D}|{D}_)*({L}|{D})*
Sign = [+]
espacio = [ |\t|\r|\n]+
coment = ([^*]|\*+[^/*])*
docComent = "/*"{coment}"*/"

%{
    public String Lexeme;
    public int Line(){
        return (yyline+1);
    }
    public int Column(){
        return (yycolumn+1);
    }
%}

%%
/* Espacios en blanco */
{espacio} {/*Ignore*/}

/* Comentarios */
"##".* {/*Ignore*/}
{docComent} {/*Ignore*/}

/* Comillas */
 "\"" {Lexeme=yytext(); return Comillas;}

/* Texto */
 "\"" .* "\""  {Lexeme=yytext(); return Texto;}

/* Operadores logicos */
("&" | "||" | "!") {Lexeme=yytext(); return Op_Logico;}

/*Operadores Relacionales */
( ">" | "<" | "==" | "!=" | ">=" | "<=" ) {Lexeme = yytext(); return Op_Relacional;}

/*Operadores Booleanos*/
("true" | "TRUE" | "false" | "FALSE") {Lexeme = yytext(); return Op_Booleano;}

("\n") {Lexeme=yytext(); return Linea;}

(";") {Lexeme=yytext(); return Punto_Coma;}

/* Operador Suma */
( \+ ) {Lexeme=yytext(); return Adicion;}

/* Operador Resta */
( "-" ) {Lexeme=yytext(); return Sustraccion;}

/* Operador Multiplicacion */
( "*" ) {Lexeme=yytext(); return Producto;}

/* Operador Division */
( "/" ) {Lexeme=yytext(); return Cociente;}

/* Operador Igual */
( "=" ) {Lexeme=yytext(); return Asignacion;}

/* Operador Atribucion */
( "+=" | "=+" | "/=" | "=/" | "*=" | "=*" | "-=" | "=-") {Lexeme=yytext(); return Op_Atribucion;}

/* Parentesis de apertura */
( "(" ) {Lexeme=yytext(); return Parentesis_a;}

/* Parentesis de cierre */
( ")" ) {Lexeme=yytext(); return Parentesis_c;}

/* Llave de apertura */
( "{" ) {Lexeme=yytext(); return Llave_a;}

/* Llave de cierre */
( "}" ) {Lexeme=yytext(); return Llave_c;}

/* Corchete de apertura */
( "[" ) {Lexeme = yytext(); return Corchete_a;}

/* Corchete de cierre */
( "]" ) {Lexeme = yytext(); return Corchete_c;}

/* Numero entero */
({Sign}?)({D}+) { Lexeme=yytext(); return Num_Entero;}
(({Sign}?|"-"?)(("."|",")*{D}(("."|",")*{D}*("."|",")*)*)+) { Lexeme=yytext(); return ERR_NUM;}

("entero") {Lexeme=yytext();return Entero;} 
("varcar") {Lexeme=yytext();return VarCar;} 
("flot") {Lexeme=yytext();return Flotante;} 
("si") {Lexeme=yytext();return Si;} 
("sino") {Lexeme=yytext();return Sino;}
("select") {Lexeme=yytext();return Selector;}
("caso") {Lexeme=yytext();return Caso;} 
("ciclomientras") {Lexeme=yytext();return CicloMientras;}
("ciclofor") {Lexeme=yytext();return CicloFor;}
("declarar") {Lexeme=yytext();return Declarar;}

/*Funciones*/
("FLong") {Lexeme=yytext(); return func_Long;}
("FContCarac") {Lexeme=yytext(); return func_ContCarac;}
("FImprimir") {Lexeme=yytext(); return func_Imprimir;}
("FValCad") {Lexeme=yytext(); return func_ValCad;}

/* Identificadores */
({L}({L}_|{L}|{D}|{D}_)*({L}|{D})*) {Lexeme=yytext(); return Identificador;}
({D}{Palabra}) {Lexeme=yytext(); return ERR_ID;}

/* Error de analisis */
 . {return ERROR;}

