package com.blitz.adminpago.bo;

import com.blitz.adminpago.dto.PagoPisaIVRDTO;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.apache.log4j.Logger;



public class SocketPagoPisaBO {

    private Logger logger = Logger.getLogger(this.getClass());

    private int timeout = 35000;
    private int puerto;
    private String server;
    private String biblioObj;
    private String biblioDat;
    private String prog;
    private String servicio;
    private String trama;
    
    //datos en QAS para pruebas posibles cambios
    public SocketPagoPisaBO() {
    	puerto = 1680;
    	server = "10.192.2.130";
    	biblioObj = "TFSOBMX1   ";
    	biblioDat = "MEXV1";
    	prog = "CJ0099    ";
    	servicio = "09";
    }
	
	public SocketPagoPisaBO(int puerto, String server, String biblioObj,
			String biblioDat, String prog, String servicio) {
		super();
		this.puerto = puerto;
		this.server = server;
		this.biblioObj = biblioObj;
		this.biblioDat = biblioDat;
		this.prog = prog;
		this.servicio = servicio;
	}
	
	public String conectarSocket07(PagoPisaIVRDTO pagoIVR, String ipTel) {
		
		String res = "";
		
		byte datosRecibidos[] = new byte[512];
		byte datosEnviados[] = new byte[512];

		Socket socket = null;
		
		DataOutputStream out = null;
		DataInputStream in = null;
		
		if(pagoIVR.getBiblioteca() == null || pagoIVR.getBibliotecaObj() == null || pagoIVR.getPrograma() == null || 
				pagoIVR.getServicio() == null || pagoIVR.getClavePago() == null || pagoIVR.getTicket() == null || 
				pagoIVR.getTelefono() == null || pagoIVR.getFechaPago() == null ||
				pagoIVR.getOficinaComercial() == null || pagoIVR.getCaja() == null || 
				pagoIVR.getImportePago() == null || pagoIVR.getHoraPago() == null ||
				pagoIVR.getTipoPago() == null || pagoIVR.getMonedaCambio() == null || 
				pagoIVR.getImporteCambio() == null || pagoIVR.getTipoCambio() == null ||
				pagoIVR.getNumTarjeta() == null || pagoIVR.getAutorizacion() == null || 
				pagoIVR.getFechaVenc() == null) {
			
			res = "{"
					+ "\"estatus\": \"03\", "
					+ "\"descripcion\": \"El telefono esta vacio\""
					+ "}";
			return res;
			
		}
		
		//operaciones dentro del socket
		try {
			
			//abrir el socket
			try {
				socket = new Socket(ipTel, puerto);
			} catch (IOException e) {
				logger.info("no se pudo conectar al servidor");
				res = "{"
						+ "\"estatus\": \"01\", "
						+ "\"descripcion\": \"No se pudo abrir el socket\""
						+ "}";
				return res;			
			}
			
			//obtener buffers de entrada y salida del socket
			try {
				socket.setSoTimeout(timeout);
			} catch (SocketException e) {
				e.printStackTrace();
			} 
			
			try {
				out = new DataOutputStream (new BufferedOutputStream (socket.getOutputStream ()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				in = new DataInputStream (socket.getInputStream ());
			} catch(SocketTimeoutException e){
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			datosEnviados = formatoBuffer07(pagoIVR);
			
			//escribir los datos en el socket
			try {
				 out.write(datosEnviados,0,512);
			} catch(SocketTimeoutException e){
				logger.info("se presento un timeout");
				res = "{"
						+ "\"estatus\": \"08\", "
						+ "\"descripcion\": \"Se presento un timeout\""
						+ "}";
				
				try {
					logger.info("cerrando el socket en escritura");
					socket.close();
					
					if(socket.isClosed())
						logger.info("el socket se ha cerrado");
					else 
						logger.info("el socket no se ha cerrado");
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				return res;	
			} catch (IOException e) {
				logger.info("no se pudo escribir en el socket " + e.getMessage());
				res = "{"
						+ "\"estatus\": \"02\", "
						+ "\"descripcion\": \"Error no se pudo escribir en el socket\""
						+ "}";
				return res;	
			}
			
			try {
				out.flush();
			} catch(SocketTimeoutException e){
				logger.info("se presento un timeout");
				res = "{"
						+ "\"estatus\": \"08\", "
						+ "\"descripcion\": \"Se presento un timeout\""
						+ "}";
				
				try {
					logger.info("cerrando el socket en escritura");
					socket.close();
					
					if(socket.isClosed())
						logger.info("el socket se ha cerrado");
					else 
						logger.info("el socket no se ha cerrado");
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				return res;	
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//leer los datos de la respuesta del socket
			try {
				in.read(datosRecibidos, 0,512);
			} catch(SocketTimeoutException e){
				logger.info("se presento un timeout");
				res = "{"
						+ "\"estatus\": \"08\", "
						+ "\"descripcion\": \"Se presento un timeout\""
						+ "}";
				
				try {
					logger.info("cerrando el socket en escritura");
					socket.close();
					
					if(socket.isClosed())
						logger.info("el socket se ha cerrado");
					else 
						logger.info("el socket no se ha cerrado");
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				return res;	
			} catch (IOException e) {
				logger.info("error al leer los datos del socket");
				res = "{"
						+ "\"estatus\": \"03\", "
						+ "\"descripcion\": \"Error al leer los datos del socket\""
						+ "}";
				return res;	
			}
			
			 //obtener y formular la respuesta correcta
			 String respuesta = "";
			 for(int i = 0; i < 512; i++) {
				 respuesta += (char) EBCDICToASCII( (int) datosRecibidos[i]);
			 }
			 
			 //System.out.println(respuesta);
			 
			 String estatus = obtenerDatos(respuesta, 70, 71);
			 String descripcion = obtenerDatos(respuesta, 71, 100);
			 
                         if ( descripcion != null && descripcion.indexOf("TELEF. PROCES. CORRECTAMENTE") != -1) {
                            res = "{ "
                                                    + "\"codigo\": \"00\", "
                                                    + "\"estatus\": \""+estatus+"\", "
                                                    + "\"descripcion\": \""+descripcion+"\","
                                                    + "\"ticket\": \""+pagoIVR.getTicket()+"\" "
                                                    + "}";
                         }
                         else {
                            res = "{ "
                                                    + "\"codigo\": \"01\", "
                                                    + "\"estatus\": \""+estatus+"\", "
                                                    + "\"descripcion\": \""+descripcion+"\""
                                                    + "}";
                         
                         }
			
		} finally {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return res;
	}
	
	public String conectarSocket09(String telefono){
		
		String res = "";
		
		byte datosRecibidos[] = new byte[512];
		byte datosEnviados[] = new byte[512];

		Socket socket = null;
		
		DataOutputStream out = null;
		DataInputStream in = null;
		
		if(telefono == null || !(telefono.length() > 0) ) {
			res = "{"
					+ "\"estatus\": \"03\", "
					+ "\"descripcion\": \"El telefono esta vacio\""
					+ "}";
			return res;
		} 
		
		try {
			
			//abrir el socket
			try {
				socket = new Socket(server, puerto);
			} catch (IOException e) {
				logger.info("no se pudo conectar al servidor");
				res = "{"
						+ "\"estatus\": \"01\", "
						+ "\"descripcion\": \"No se pudo abrir el socket\""
						+ "}";
				return res;			
			}
			
			//obtener buffers de entrada y salida del socket
			try {
				socket.setSoTimeout(timeout);
			} catch (SocketException e) {
				e.printStackTrace();
			} 
			
			try {
				out = new DataOutputStream (new BufferedOutputStream (socket.getOutputStream ()));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			try {
				in = new DataInputStream (socket.getInputStream ());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			datosEnviados = formatoBuffer09(telefono);
			
			//escribir los datos en el socket
			try {
				out.write(datosEnviados,0,512);
			}  catch(SocketTimeoutException e){
				logger.info("se presento un timeout");
				res = "{"
						+ "\"estatus\": \"08\", "
						+ "\"descripcion\": \"Se presento un timeout\""
						+ "}";
				
				try {
					logger.info("cerrando el socket en escritura");
					socket.close();
					
					if(socket.isClosed())
						logger.info("el socket se ha cerrado");
					else 
						logger.info("el socket no se ha cerrado");
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				return res;	
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			 try {
				 out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			 
			 try {
				 in.read(datosRecibidos, 0,512);
			} catch(SocketTimeoutException e){
				logger.info("se presento un timeout");
				res = "{"
						+ "\"estatus\": \"08\", "
						+ "\"descripcion\": \"Se presento un timeout\""
						+ "}";
				
				try {
					logger.info("cerrando el socket en escritura");
					socket.close();
					
					if(socket.isClosed())
						logger.info("el socket se ha cerrado");
					else 
						logger.info("el socket no se ha cerrado");
					
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				
				return res;	
			} catch (IOException e) {
				e.printStackTrace();
			}

			 //obtener y formular la respuesta de retorno
			 String respuesta = "";
			 for(int i = 0; i < 512; i++) {
				 respuesta += (char) EBCDICToASCII( (int) datosRecibidos[i]);
			 }
			 
			 System.out.println(respuesta);
			 
			 String bibliotecaObjetos = obtenerDatos(respuesta, 110, 120);
			 String bibliotecaDatos = obtenerDatos(respuesta, 120, 130);
			 String ip = obtenerDatos(respuesta, 130, 145);
			 String programa = getProg();
			 String descripcion = obtenerDatos(respuesta, 70, 100);
			 
			 res = "{"
						+ "\"estatus\": \"00\", "
						+ "\"descripcion\": \""+descripcion+"\","
						+ "\"telefono\": \""+telefono+"\","
						+ "\"biblioteca_objetos\": \""+bibliotecaObjetos+"\","
						+ "\"biblioteca_datos\": \""+bibliotecaDatos+"\","
						+ "\"programa\": \""+programa+"\", "
						+ "\"ip\": " + ip +"\""
						+ "}";

		} finally {
			
			try {
				socket.close();
			} catch (IOException e) {
				logger.info("error al cerrar el socket");
			}
			
		}
		
		return res;
	}
	
	public String conectarSocket10(PagoPisaIVRDTO pagoIVR, String ipIVR) {
		String res = "";
		
		byte datosRecibidos[] = new byte[512];
		byte datosEnviados[] = new byte[512];

		Socket socket = null;
		
		DataOutputStream out = null;
		DataInputStream in = null;
		
		if(pagoIVR.getBiblioteca() == null || pagoIVR.getBibliotecaObj() == null || pagoIVR.getPrograma() == null || 
				pagoIVR.getServicio() == null || pagoIVR.getClavePago() == null || pagoIVR.getTicket() == null || 
				pagoIVR.getTelefono() == null || pagoIVR.getFechaPago() == null ||
				pagoIVR.getOficinaComercial() == null || pagoIVR.getCaja() == null || 
				pagoIVR.getImportePago() == null ) {
			
			res = "{"
					+ "\"estatus\": \"03\", "
					+ "\"descripcion\": \"El telefono esta vacio\""
					+ "}";
			return res;
			
		}
		
		//operaciones dentro del socket
		try {
					
			//abrir el socket
			try {
				socket = new Socket(ipIVR, puerto);
			} catch (IOException e) {
				logger.info("no se pudo conectar al servidor");
				res = "{"
						+ "\"estatus\": \"01\", "
						+ "\"descripcion\": \"No se pudo abrir el socket\""
						+ "}";
				return res;			
			}
			
			//obtener buffers de entrada y salida del socket
			try {
				socket.setSoTimeout(timeout);
			} catch (SocketException e) {
				e.printStackTrace();
			} 
					
			try {
				out = new DataOutputStream (new BufferedOutputStream (socket.getOutputStream ()));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				in = new DataInputStream (socket.getInputStream ());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
				
			//formar trama
			datosEnviados = formatoBuffer10(pagoIVR);
			
			//escribir los datos en el socket
			try {
				 out.write(datosEnviados,0,512);
			} catch (IOException e) {
				logger.info("no se pudo escribir en el socket " + e.getMessage());
				res = "{"
						+ "\"estatus\": \"02\", "
						+ "\"descripcion\": \"Error no se pudo escribir en el socket\""
						+ "}";
				return res;	
			}
					
			try {
				out.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			//leer los datos de la respuesta del socket
			try {
				in.read(datosRecibidos, 0,512);
			} catch(SocketTimeoutException e){
				logger.info("se presento un timeout");
				res = "{"
						+ "\"estatus\": \"08\", "
						+ "\"descripcion\": \"Se presento un timeout\""
						+ "}";
				return res;	
			} catch (IOException e) {
				logger.info("error al leer los datos del socket");
				res = "{"
						+ "\"estatus\": \"03\", "
						+ "\"descripcion\": \"Error al leer los datos del socket\""
						+ "}";
				return res;	
			}
				
			 //obtener y formular la respuesta correcta
			 String respuesta = "";
			 for(int i = 0; i < 512; i++) {
				 respuesta += (char) EBCDICToASCII( (int) datosRecibidos[i]);
			 }
			 
			 logger.info(respuesta);
			 
			 String estatus = obtenerDatos(respuesta, 70, 72);
			 String descripcion = "";
			 
			 if(estatus != null && estatus.equals("OK")) {
				 descripcion = "PAGO REGISTRADO EN PISA";
			 } else if (estatus != null && estatus.equals("NA")) {
				 descripcion = "NO APLICADO";
			 } else if(estatus != null && estatus.equals("NE")) {
				 descripcion = "NO EXISTE";
			 } else {
				 descripcion = "OCURRIO UN ERROR";
			 }
			 
			 res = "{ "
					 	+ "\"codigo\": \"00\", "
						+ "\"estatus\": \""+estatus+"\", "
						+ "\"descripcion\": \""+descripcion+"\","
						+ "\"ticket\": \""+pagoIVR.getTicket()+"\" "
					+ "}";
					
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
		return res;
		
	}
	
	private String obtenerDatos(String dato, int inicio, int fin) {
		
		String s = "";
		try {
                    s = dato.substring(inicio, fin);
                }
                catch(Exception e) {;}
		return s;
		
	}
	
	private byte[] formatoBuffer09(String telefono) {
		
		byte dataByte[] = new byte[512];
	    
   	    String bufferOut = llenarEspacios(getBiblioObj(),30) + llenarEspacios(getBiblioDat(),30) + 
   	    		llenarEspacios(getProg(),10) +  
   	    		llenarEspacios(getServicio(),10) + 
                telefono +  fillTo("",422," ");
   	    
	    char data2[]= bufferOut.toCharArray();
	    
	    for(int i=0; i < bufferOut.length() - 1; i++) {
	    
	    	dataByte[i]=(byte) ASCIIToEBCDIC(data2[i]);
	    	
	    }
            
	    return dataByte;
	}
	
        
        
        
	public byte[] formatoBuffer07(PagoPisaIVRDTO pagoIVR) {
		
		byte dataByte[] = new byte[512];
		
		String bufferOut = llenarEspacios(pagoIVR.getBibliotecaObj(), 30) + 
				llenarEspacios(pagoIVR.getBiblioteca(), 30) +
				llenarEspacios(pagoIVR.getPrograma(), 10) + llenarEspacios(pagoIVR.getServicio(), 2) +
				llenarEspacios(pagoIVR.getClavePago(), 3) + llenarEspacios(pagoIVR.getTicket(), 12) +
				llenarEspacios(pagoIVR.getTelefono(), 10) + llenarEspacios(pagoIVR.getFechaPago(), 8) +
				llenarEspacios(pagoIVR.getOficinaComercial(), 3) + llenarEspacios(pagoIVR.getCaja(), 10) +
				llenarEspacios(pagoIVR.getAdeudo(), 13) + llenarEspacios(pagoIVR.getImportePago(), 13) +
				llenarEspacios(pagoIVR.getHoraPago(), 6) + llenarEspacios(pagoIVR.getTipoPago(), 3) +
				llenarEspacios(pagoIVR.getMonedaCambio(), 1) + llenarEspacios(pagoIVR.getImporteCambio(), 9) +
				llenarEspacios(pagoIVR.getTipoCambio(), 9) + fillTo(pagoIVR.getNumTarjeta(), 16, "0") + 
				llenarEspacios(pagoIVR.getAutorizacion(), 6) + llenarEspacios(pagoIVR.getFechaVenc(), 4) +
				fillTo("",313," ");
		
		System.out.println(bufferOut);
		
		char data2[]= bufferOut.toCharArray();
	    
	    for(int i=0; i < bufferOut.length() - 1; i++) {
	    
	    	dataByte[i]=(byte) ASCIIToEBCDIC(data2[i]);
	    	
	    }
		
		return dataByte;
	}
        
	public String formatoBuffer07String(PagoPisaIVRDTO pagoIVR) {
		
		
		String bufferOut = llenarEspacios(pagoIVR.getBibliotecaObj(), 30) + 
				llenarEspacios(pagoIVR.getBiblioteca(), 30) +
				llenarEspacios(pagoIVR.getPrograma(), 10) + llenarEspacios(pagoIVR.getServicio(), 2) +
				llenarEspacios(pagoIVR.getClavePago(), 3) + llenarEspacios(pagoIVR.getTicket(), 12) +
				llenarEspacios(pagoIVR.getTelefono(), 10) + llenarEspacios(pagoIVR.getFechaPago(), 8) +
				llenarEspacios(pagoIVR.getOficinaComercial(), 3) + llenarEspacios(pagoIVR.getCaja(), 10) +
				llenarEspacios(pagoIVR.getAdeudo(), 13) + llenarEspacios(pagoIVR.getImportePago(), 13) +
				llenarEspacios(pagoIVR.getHoraPago(), 6) + llenarEspacios(pagoIVR.getTipoPago(), 3) +
				llenarEspacios(pagoIVR.getMonedaCambio(), 1) + llenarEspacios(pagoIVR.getImporteCambio(), 9) +
				llenarEspacios(pagoIVR.getTipoCambio(), 9) + fillTo(pagoIVR.getNumTarjeta(), 16, "0") + 
				llenarEspacios(pagoIVR.getAutorizacion(), 6) + llenarEspacios(pagoIVR.getFechaVenc(), 4) +
				fillTo("",313," ");
		
		System.out.println(bufferOut);
		
		
		return bufferOut;
	}
        
        
	
	private byte[] formatoBuffer10(PagoPisaIVRDTO pagoIVR) {
		
		byte dataByte[] = new byte[512];
		
		String bufferOut = llenarEspacios(pagoIVR.getBibliotecaObj(), 30) + 
				llenarEspacios(pagoIVR.getBiblioteca(), 30) +
				llenarEspacios(pagoIVR.getPrograma(), 10) + llenarEspacios(pagoIVR.getServicio(), 2) +
				llenarEspacios(pagoIVR.getClavePago(), 3) + llenarEspacios(pagoIVR.getTicket(), 12) +
				llenarEspacios(pagoIVR.getTelefono(), 10) + llenarEspacios(pagoIVR.getFechaPago(), 8) +
				llenarEspacios(pagoIVR.getOficinaComercial(), 3) + llenarEspacios(pagoIVR.getCaja(), 10) +
				llenarEspacios(pagoIVR.getImportePago(), 13) +
				fillTo("",380," ");
		
		logger.info("trama: "+bufferOut);
		
		char data2[]= bufferOut.toCharArray();
	    
	    for(int i=0; i < bufferOut.length() - 1; i++) {
	    
	    	dataByte[i]=(byte) ASCIIToEBCDIC(data2[i]);
	    	
	    }
		
		return dataByte;
	}
	
	private String fillTo(String dat,int num,String car) {
			String cadCar = "";
			for(int i = dat.length(); i < num; i++)
			    cadCar=cadCar + car;
			return new String(cadCar + dat);
	}
	
	private String llenarEspacios(String dato, int max) {
		String tmp = dato;
                if ( dato == null || dato.length() == 0)
                    tmp = "";

		for(int i = 0; i < max; i++) {
			if(tmp.length() < max) {
				tmp = tmp + " ";
			} else {
				return tmp;
			}
		}
		
		return tmp;
		
	}
	
    /**
     * translate a single byte 0 .. 255 from ASCII (Latin-1) to EBCDIC.
     * Also works with signed bytes -128 .. 127.
     */
    private int ASCIIToEBCDIC (int ascii ) {
    	 return AToE[ascii & 0xff] & 0xff;
    } 
	     
    private static byte[] AToE = {
         /* 0 */0, 1, 2, 3, 55, 45, 46, 47, 22, 5, 21, 11, 12, 13, 14, 15,
         /* 16 */  16, 17, 18, 63, 60, 61, 50, 38, 24, 25, 63, 39, 28, 29, 30, 31,
         /* 32 */  64, 90, 127, 123, 91, 108, 80, 125, 77, 93, 92, 78, 107, 96, 75, 97,
         /* 48 */  -16, -15, -14, -13, -12, -11, -10, -9, -8, -7, 122, 94, 76, 126, 110, 111,
         /* 64 */  124, -63, -62, -61, -60, -59, -58, -57, -56, -55, -47, -46, -45, -44, -43, -42,
         /* 80 */  -41, -40, -39, -30, -29, -28, -27, -26, -25, -24, -23, 63, 63, 63, 63, 109,
         /* 96 */  -71, -127, -126, -125, -124, -123, -122, -121, -120, -119, -111, -110, -109, -108, -107, -106,
         /* 112 */  -105, -104, -103, -94, -93, -92, -91, -90, -89, -88, -87, 63, 79, 63, 63, 7,
         /* 128 */  63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
         /* 144 */  63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
         /* 160 */  64, 63, 74, 123, 63, 63, 63, 63, 63, 63, 63, 63, 95, 96, 63, 63,
         /* 176 */  63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
         /* 192 */  63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
         /* 208 */  63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
         /* 224 */  63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63,
         /* 240 */  63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63, 63
      };
     
	private int EBCDICToASCII (int ebcdic ) {
		// 0xff ugliness required because Java bytes are signed.
		return EToA[ebcdic & 0xff] & 0xff;
    } 
	
	private byte[] EToA = {
        /* 0 */0, 1, 2, 3, 26, 9, 26, 127, 26, 26, 26, 11, 12, 13, 14, 15,
        /* 16 */  16, 17, 18, 26, 26, 10, 8, 26, 24, 25, 26, 26, 28, 29, 30, 31,
        /* 32 */  26, 26, 28, 26, 26, 10, 23, 27, 26, 26, 26, 26, 26, 5, 6, 7,
        /* 48 */  26, 26, 22, 26, 26, 30, 26, 4, 26, 26, 26, 26, 20, 21, 26, 26,
        /* 64 */  32, 26, 26, 26, 26, 26, 26, 26, 26, 26, -94, 46, 60, 40, 43, 124,
        /* 80 */  38, 26, 26, 26, 26, 26, 26, 26, 26, 26, 33, 36, 42, 41, 59, -84,
        /* 96 */  45, 47, 26, 26, 26, 26, 26, 26, 26, 26, 26, 44, 37, 95, 62, 63,
        /* 112 */  26, 26, 26, 26, 26, 26, 26, 26, 26, 26, 58, 35, 64, 39, 61, 34,
        /* 128 */  26, 97, 98, 99, 100, 101, 102, 103, 104, 105, 26, 26, 26, 26, 26, 26,
        /* 144 */  26, 106, 107, 108, 109, 110, 111, 112, 113, 114, 26, 26, 26, 26, 26, 26,
        /* 160 */  26, 26, 115, 116, 117, 118, 119, 120, 121, 122, 26, 26, 26, 26, 26, 26,
        /* 176 */  26, 26, 26, 26, 26, 26, 26, 26, 26, 96, 26, 26, 26, 26, 26, 26,
        /* 192 */  26, 65, 66, 67, 68, 69, 70, 71, 72, 73, 26, 26, 26, 26, 26, 26,
        /* 208 */  26, 74, 75, 76, 77, 78, 79, 80, 81, 82, 26, 26, 26, 26, 26, 26,
        /* 224 */  26, 26, 83, 84, 85, 86, 87, 88, 89, 90, 26, 26, 26, 26, 26, 26,
        /* 240 */  48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 26, 26, 26, 26, 26, 26
     };

	public int getPuerto() {
		return puerto;
	}

	public void setPuerto(int puerto) {
		this.puerto = puerto;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getBiblioObj() {
		return biblioObj;
	}

	public void setBiblioObj(String biblioObj) {
		this.biblioObj = biblioObj;
	}

	public String getBiblioDat() {
		return biblioDat;
	}

	public void setBiblioDat(String biblioDat) {
		this.biblioDat = biblioDat;
	}

	public String getProg() {
		return prog;
	}

	public void setProg(String prog) {
		this.prog = prog;
	}

	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public String getTrama() {
		return trama;
	}

	public void setTrama(String trama) {
		this.trama = trama;
	}

	public byte[] getEToA() {
		return EToA;
	}

	public void setEToA(byte[] eToA) {
		EToA = eToA;
	}

	public int getTimeout() {
		return timeout;
	}

    /**
     * @param timeout the timeout to set
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

}
