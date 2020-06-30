package com.blitz.adminpago.util;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import org.springframework.validation.Validator;


/**
 *
 * @author 
 */
public class GeneralValidator  {
    
    public static boolean campoVacio(String campo){
        return campo == null || campo.trim().equals("");
    }
    
    public static boolean numeroDeCaracteres(String cadena, int numero){
        if(cadena.trim().length() == numero){
            return true;
        }else{
            return false;
        }
    }
    
    public static boolean numeroDeCaracteres( String cadena, int min, int max){
        if(cadena.trim().length() >= min && cadena.trim().length() <= max){
            return true;
        }else{
            return false;
        }
    }
    
    public static boolean soloCaracteresNumericos(String cadena){
        CharacterIterator it = new StringCharacterIterator(cadena);
        
        for (char ch=it.first(); ch != CharacterIterator.DONE; ch=it.next()) {
            if(!Character.isDigit(ch)){
                return false;
            }
        }
        return true;
    }
    
    public static boolean soloCaracteresNumericosOPunto(String cadena){
        CharacterIterator it = new StringCharacterIterator(cadena);
        
        for (char ch=it.first(); ch != CharacterIterator.DONE; ch=it.next()) {
            if(!( Character.isDigit(ch) || ch == '.')){
                return false;
            }
        }
        return true;
    }

    public static boolean soloCaracteresNumericosOPuntoOComa(String cadena){
        CharacterIterator it = new StringCharacterIterator(cadena);

        for (char ch=it.first(); ch != CharacterIterator.DONE; ch=it.next()) {
            if(!( Character.isDigit(ch) || ch == '.' || ch == ',' )){
                return false;
            }
        }
        return true;
    }

    public static boolean soloCaracteresAlfabeticos(String cadena){
        CharacterIterator it = new StringCharacterIterator(cadena);
        
        for (char ch=it.first(); ch != CharacterIterator.DONE; ch=it.next()) {
            if(!Character.isLetter(ch)){
                return false;
            }
        }
        return true;
    }           
    
    public static boolean soloCaracteresAlfaNumericos(String cadena){
        CharacterIterator it = new StringCharacterIterator(cadena);
        
        for (char ch=it.first(); ch != CharacterIterator.DONE; ch=it.next()) {
            if(!(Character.isLetter(ch) || Character.isDigit(ch))){
                return false;
            }
        }
        return true;
    }

    public static boolean soloCaracteresAlfaNumericosEsp(String cadena){
        CharacterIterator it = new StringCharacterIterator(cadena);

        for (char ch=it.first(); ch != CharacterIterator.DONE; ch=it.next()) {
            if(!( Character.isLetter(ch) || Character.isDigit(ch)
                    || ch == '@' || ch == '#' || ch == '$' || ch == '%' )){
                return false;
            }
        }
        return true;
    }



    public static boolean soloCaracteresAlfabeticosOBlancos(String cadena){
        CharacterIterator it = new StringCharacterIterator(cadena);
        
        for (char ch=it.first(); ch != CharacterIterator.DONE; ch=it.next()) {
            if(!(Character.isLetter(ch) || Character.isWhitespace(ch))){
                return false;
            }
        }
        return true;
    }
    
    public static boolean contieneSubCadena(String cadena, String subCadena){
        return cadena.contains(subCadena);
    }
    
    public static boolean contieneSubCadena(String cadena, String subCadena, int posicion){
        int tamanio = subCadena.length();
        
        if(cadena==null || cadena.trim().equals("") || cadena.trim().length()<tamanio){
            return false;
        }else{
            if (subCadena.trim().equals(cadena.trim().substring(posicion ,posicion + tamanio)) ){
                return true;
            }else{
                return false;
            }
        }
    }
    
    public static boolean formatoCorreo(String correo){
        Matcher mat=null;
        Pattern pat=null;

        pat=Pattern.compile("(\\s|>|^)(?!(:|www\\.|\\.))[A-Za-z0-9_.-]+@([A-Za-z0-9_-]+\\.)+[A-Za-z]{2,4}(\\s|\\W|_|<|$)");
        mat=pat.matcher(correo);

        return mat.find();
    }
    
}