package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import Cliente.Secuencia;


public class ComunicacionUDP extends Thread
{
	//private static FileEvent fileEvent = null;	
	private ArrayList<SecuenciaServidor> secuenciaS;
	private static int puerto;
	private static DatagramSocket socketServidor;	
	private int objetosRecibidos;	
	private int objetosPerdidos;
	private int objetosTotales;
	private static Secuencia secuenciaC;

	public static void main(String[] args) throws Exception{
		puerto =3210;
		conexiones();
	}


	//Retorna la fecha en la que se recibe el archivo
	public static long getFecha(Date date1, Date date2, TimeUnit timeUnit) {
		long diffInMillies = date2.getTime() - date1.getTime();
		return timeUnit.convert(diffInMillies,TimeUnit.MILLISECONDS);
	}
	//Hace un Hash para la recepciÛn de los archivos
	public static String hash(String ruta) throws Exception{
		//Escoge el tipo de hash
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		//llama el archivo
		FileInputStream fis = new FileInputStream(ruta);
		//crea el espacio
		byte[] dataBytes = new byte[1024];
		//lee el archivo
		int nread = 0;
		while ((nread = fis.read(dataBytes)) != -1) {
			md.update(dataBytes, 0, nread);
		};
		//crea el digest
		byte[] digestBytes = md.digest();
		//convertir a hash
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digestBytes.length; i++) {
			sb.append(Integer.toString((digestBytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		//print del hash
		return sb.toString();
	}

	// Crea conexion y modifica puerto, jar e IP para la recepciÛn de archivos desde el cliente
	public static void conexiones() throws ParseException{
		try {
			boolean objeto = false;
			int rec=0;
			int objs = 0;
			int arch= 0;
			String hash="";
			boolean archivo = false;
			socketServidor = new DatagramSocket(puerto);
			System.out.println("Recibiendo");
			byte[] receiveData = new byte[1024];
			BufferedWriter output = null;
			for(;;){
				try{
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					socketServidor.receive(receivePacket);
					String msg = new String( receivePacket.getData());
					String [] datos = msg.split(",");
					if (datos[0].equals("OBJETO")){
						objeto = true;
						archivo = false;
						objs = 0;
						rec++;
						File file = new File("docs\\peticion"+rec+".txt");
						output = new BufferedWriter(new FileWriter(file));
					}
					if (datos[0].equals("ARCHIVO")){
						objeto = false;
						archivo = true;
						hash = datos[1];
					}
					if (objeto && datos.length>2){
						objs++;
						datos = msg.split(",");
						String fecha = datos[2];
						SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						Date dateRecibido = (Date) dt.parse(fecha);
						Date dateReal = new Date();
						long tiempo = getFecha(dateRecibido,dateReal,TimeUnit.MILLISECONDS);
						output.write(datos[0]+" : "+tiempo +"ms" +"\n");
						System.out.println(datos[0]+" : "+tiempo +"ms");
						if(Integer.parseInt(datos[0]) == Integer.parseInt(datos[1])){
							output.write("Datos Recibidos: "+objs+"\n");
							output.write("Datos Perdidos: "+(Integer.parseInt(datos[1])-objs)+"\n");
							output.write("Datos Totales: "+datos[1]+"\n");
							output.close();
						}
					}
					if (archivo && arch > 0){
						byte[] data = msg.getBytes();
						ByteArrayInputStream in = new ByteArrayInputStream(data);
						ObjectInputStream is = new ObjectInputStream(in);
						secuenciaC= (Secuencia) is.readObject();
						if (secuenciaC.getEstado().equalsIgnoreCase("Error")) {
							System.out.println("Un problema se presentÛ en el lado del cliente");
							System.exit(0);
						}
						String hashN = almacenarArchivo();
						if (!hashN.equals(hash))
							System.out.println("Archivo incorrecto");
						else System.out.println("Archivo permitido");
					}
					else arch++;
				} 
				catch (Exception e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} 
		catch (SocketException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	//Guarda el archivo recibido desde el cliente
	public static String almacenarArchivo() throws Exception {
		String outputFile = secuenciaC.getDirectorioDestino() + secuenciaC.getNombreArchivo();
		if (!new File(secuenciaC.getDirectorioDestino()).exists()) {
			new File(secuenciaC.getDirectorioDestino()).mkdirs();
		}
		File file2 = new File(outputFile);
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file2);
			fileOutputStream.write(secuenciaC.getInfoArchivo());
			fileOutputStream.flush();
			fileOutputStream.close();
			System.out.println("El archivo : " + outputFile + " se ha guardado ");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return hash(outputFile);
	}

	/*public ComunicacionUDP(ArrayList<SecuenciaServidor> arreglo)
	{
		super();
		secuencia=arreglo;
	}

	public void run() {


		try {
			this.startUDPServer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startUDPServer() throws IOException { 


		socketServidor = new DatagramSocket(1008);
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];

		while(true)
		{
			System.out.println("UDP-Servidor Esperando Clientes");
			DatagramPacket paquete = new DatagramPacket(receiveData,receiveData.length);
			socketServidor.receive(paquete);
			System.out.println("UDP-Paquete recibido");

			String mensaje = new String(paquete.getData());
			String[] resp = mensaje.split(":");

			InetAddress ip = resp[0];
			int puerto = paquete.getPort();		

			try
			{
				synchronized(secuencia)
				{
					DateFormat format = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.ENGLISH);
					Date registro= format.parse(resp[2]);
					System.out.println(registro); // Sat Jan 02 00:00:00 GMT 2010
					secuencia.add(new Secuencia(resp[0], Integer.parseInt(resp[1]), registro));
					//Cambiar n√∫mero de puerto via linea de comando
					String input = System.console().readLine();
					 BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
					 System.out.print("Cambiar puerto:");
				     try{
				         int i = Integer.parseInt(br.readLine());
				         puerto=i;
				     }catch(NumberFormatException nfe){
				          System.err.println("Formato inv√°lido");
				     }
				}
			}
			catch(Exception e)
			{
				//Confirmacion Mensaje Perdido
				System.out.println("Un mensaje perdido");
				// Permite conocer el n√∫mero de objetos perdidos
				objetosPerdidos++;
			}

			System.out.println("UDP-Recibido: IP= "+ip+" Numero Secuencia= "+resp[0]+" marcaTiempo= "+resp[1]+ " Fecha= "+resp[2]);
			// Permite conocer el n√∫mero de objetos recibidos
			objetosRecibidos++;

			sendData = "OK".getBytes();

			DatagramPacket aEnviar = new DatagramPacket(sendData, sendData.length, ip, puerto);

			socketServidor.send(aEnviar);
			objetosTotales=objetosRecibidos+objetosPerdidos;
			System.out.println("UDP-Respuesta enviada");	
			System.out.println("Objetos Recibidos: "+ objetosRecibidos+ " Objetos Perdidos: "+ objetosPerdidos+ " Objetos Totales: "+objetosTotales);	

		}		*/
}
