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

/* Cadena */
 "\"" .* "\""  {Lexeme=yytext(); return Cadena;}

/* Operadores logicos */
("&" | "||" | "!") {Lexeme=yytext(); return Op_Logico;}

/*Operadores Relacionales */
( ">" | "<" | "==" | "!=" | ">=" | "<=" ) {Lexeme = yytext(); return Op_Relacional;}

/*Operadores Booleanos*/
("true" | "TRUE" | "false" | "FALSE") {Lexeme = yytext(); return Op_Booleano;}

(";") {Lexeme=yytext(); return Punto_y_Coma;}

/* Operador Suma */
( \+ ) {Lexeme=yytext(); return Suma;}

/* Operador Resta */
( "-" ) {Lexeme=yytext(); return Resta;}

/* Operador Multiplicacion */
( "*" ) {Lexeme=yytext(); return Multiplicacion;}

/* Operador Division */
( "/" ) {Lexeme=yytext(); return Division;}

/* Operador Igual */
( "=" ) {Lexeme=yytext(); return Asignacion;}

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

("int") {Lexeme=yytext();return Int;} 
("char") {Lexeme=yytext();return Char;} 
("string") {Lexeme=yytext();return String;} 
("float") {Lexeme=yytext();return Float;} 
("if") {Lexeme=yytext();return If;} 
("else") {Lexeme=yytext();return Else;}
("switch") {Lexeme=yytext();return Switch;}
("case") {Lexeme=yytext();return Case;} 
("while") {Lexeme=yytext();return While;}
("for") {Lexeme=yytext();return For;}
("declare") {Lexeme=yytext();return Declare;}
("false") {Lexeme=yytext();return False;}
("true") {Lexeme=yytext();return True;}

/*Funciones*/
("Longitud") {Lexeme=yytext(); return func_Longitud;}
("CaracterEn") {Lexeme=yytext(); return func_CaracterEn;}
("View") {Lexeme=yytext(); return func_View;}
("ValidarCadena") {Lexeme=yytext(); return func_ValidarCadena;}

/* Identificadores */
({L}({L}_|{L}|{D}|{D}_)*({L}|{D})*) {Lexeme=yytext(); return ID;}
({D}{Palabra}) {Lexeme=yytext(); return ERR_ID;}

/* Error de analisis */
 . {return ERROR;}

