/*
 * @(#) Cifrado.java	1.0.0 31/10/2008
 *
 * Copyright 2008 Blitz Software S.A. de C.V. Todos los derechos reservados.
 * 
 */

package com.blitz.adminpago.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;


/**
 * 
 * Objetivo:
 * 
 * @author  aghelgue
 * @since 1.0.0
 * @version 1.0.0, 31/10/2008
 */

public class Cifrado {
    private static byte[] SALT_BYTES = {
  (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
  (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
      };
  private static int ITERATION_COUNT = 19;
  private static String passPhrase = "{V@fe9Hk";
     
  public String encriptar(String str) {
   Cipher ecipher = null;
   Cipher dcipher = null;
   try {
    // Crear la key
    KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), 
     SALT_BYTES, ITERATION_COUNT);
    SecretKey key = SecretKeyFactory.getInstance(
     "PBEWithMD5AndDES").generateSecret(keySpec);
    ecipher = Cipher.getInstance(key.getAlgorithm());
    dcipher = Cipher.getInstance(key.getAlgorithm());
     
    // Preparar los parametros para los ciphers
    AlgorithmParameterSpec paramSpec = new PBEParameterSpec(
     SALT_BYTES, ITERATION_COUNT);
     
    // Crear los ciphers
    ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
    dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
   } catch (javax.crypto.NoSuchPaddingException e) {
    e.printStackTrace();
   } catch (java.security.NoSuchAlgorithmException e) {
    e.printStackTrace();
   } catch (java.security.InvalidKeyException e) {
    e.printStackTrace();
   } catch (InvalidKeySpecException e) {
    e.printStackTrace();
   } catch (InvalidAlgorithmParameterException e) {
    e.printStackTrace();
   }
   
   try {
    // Encodear la cadena a bytes usando utf-8
    byte[] utf8 = str.getBytes("UTF8");
     
    // Encriptar
    byte[] enc = ecipher.doFinal(utf8);
     
    // Encodear bytes a base64 para obtener cadena
    return new String(Base64.getEncoder().encode(enc));
   } catch (javax.crypto.BadPaddingException e) {
   } catch (IllegalBlockSizeException e) {
   } catch (UnsupportedEncodingException e) {
   }
   
   return null;
  }
     
  public String desencriptar(String str) {
   Cipher ecipher = null;
   Cipher dcipher = null;
     
   try {
    // Crear la key
    KeySpec keySpec = new PBEKeySpec(passPhrase.toCharArray(), 
     SALT_BYTES, ITERATION_COUNT);
    SecretKey key = SecretKeyFactory.getInstance(
     "PBEWithMD5AndDES").generateSecret(keySpec);
    ecipher = Cipher.getInstance(key.getAlgorithm());
    dcipher = Cipher.getInstance(key.getAlgorithm());
     
    // Preparar los parametros para los ciphers
    AlgorithmParameterSpec paramSpec = new PBEParameterSpec(
     SALT_BYTES, ITERATION_COUNT);
     
    // Crear los ciphers
    ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
    dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);    
   } catch (javax.crypto.NoSuchPaddingException e) {
    e.printStackTrace();
   } catch (java.security.NoSuchAlgorithmException e) {
    e.printStackTrace();
   } catch (java.security.InvalidKeyException e) {
    e.printStackTrace();
   } catch (InvalidKeySpecException e) {
    e.printStackTrace();
   } catch (InvalidAlgorithmParameterException e) {
    e.printStackTrace();
   }
   
   try {
    // Decodear base64 y obtener bytes
    byte[] dec = Base64.getDecoder().decode(str.getBytes());
     
    // Desencriptar
    byte[] utf8 = dcipher.doFinal(dec);
     
    // Decodear usando utf-8
    return new String(utf8, "UTF8");
   } catch (javax.crypto.BadPaddingException e) {
    e.printStackTrace();
   } catch (IllegalBlockSizeException e) {
    e.printStackTrace();
   } catch (UnsupportedEncodingException e) {
    e.printStackTrace();
   } 

   return null;
  }
  
  public static void main(String[] args){
    Cifrado pb = new Cifrado();
    
    String cadenaEncriptada = pb.encriptar("EDELP92_");
    String[] cadenas = {"_osa","marte_","_urano",
                        "venus_","_tierra","_io_",
                        "_luna","pluton_","_sol",
                        "nebula_","_cometa","alfa_"};



    String cadenaDesencriptada = pb.desencriptar("LIwnzS7DRWIiHV2SJt9w/Q==");
    System.out.println("Cadena Desenncriptada: " + cadenaDesencriptada);
    
    System.out.println("Cadena Encriptada: " + cadenaEncriptada);
    System.out.println("Cadena Encriptada: " + pb.desencriptar("pT0QpyTGqZseT9mq8B3nZw=="));

    for (int i=0; i< cadenas.length; i++)
    {
        String lstC = pb.encriptar(cadenas[i]);
        System.out.println("Clave: [" + cadenas[i] +  "] Encirptada:" + lstC );

    }
  }
    
    
}
