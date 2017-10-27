package Cliente;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPClientePpl {

	private String ipServer;
	private int puertoConexion;	
	private Secuencia sec = null;
	private String sourceFilePath = "";
	private String destinationPath = "docs\\";	

	//Establece la IP del Servidor que le usuario ingresa para la conexion
	public void cambiarIPServidor(String ipServidor) {
		ipServer=ipServidor;
		sec.setIp(ipServidor);
	}
	//Establece la puerto del Servidor que le usuario ingresa para la conexion
	public void cambiarPuertoServidor(int puertoServer) {
		puertoConexion=puertoServer;		
		sec.setPuerto(puertoConexion);
	}

	public void enviarObjetos(final String text3, String ipEstablecido, String puertoEstablecido){
		
		new Thread(new Runnable(){
			public void run(){
				// TODO Auto-generated method stub
				DatagramSocket socket;
				
				try{
					sec=new Secuencia();
					int t3=Integer.parseInt(text3);
					sec.setNumeroSecuencia(t3);
					sec.setIp(ipEstablecido);
					sec.setPuerto(Integer.parseInt(puertoEstablecido));
					System.out.println(t3+ipServer+"-"+puertoConexion);
					socket = new DatagramSocket();
					String datosObjeto = "OBJETO,";
					byte[] sendData = new byte[1024];
					sendData = datosObjeto.getBytes();
					InetAddress IPAddress = InetAddress.getByName(ipServer);
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, puertoConexion);
					socket.send(sendPacket);
					for (int i = 0; i<t3; i++)
					{
						Date fecha = new Date();
						DateFormat fechaFormato = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						datosObjeto = (i+1)+","+text3+","+fechaFormato.format(fecha);
						sendData = new byte[1024];
						sendData = datosObjeto.getBytes();
						IPAddress = InetAddress.getByName(ipServer);
						sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, puertoConexion);
						socket.send(sendPacket);						
					}
				} 
				catch (Exception e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}).start();
	}
	public void enviarArchivos(final String ruta){
		new Thread(new Runnable(){
			public void run() {
				// TODO Auto-generated method stub
				DatagramSocket socket;
				sec=new Secuencia();
				try{
					socket = new DatagramSocket();
					String hash = HashArchivo(ruta);
					String datosObjeto = "ARCHIVO,"+hash+",";
					byte[] sendData = new byte[1024];
					sendData = datosObjeto.getBytes();
					InetAddress IPAddress = InetAddress.getByName(ipServer);
					DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, puertoConexion);
					socket.send(sendPacket);
					sourceFilePath = ruta;
					byte[] incomingData = new byte[1024];
					sec = getFileEvent();
					System.out.println("1");
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					ObjectOutputStream os = new ObjectOutputStream(outputStream);
					os.writeObject(sec);
					System.out.println("2");
					byte[] data = outputStream.toByteArray();
					sendPacket = new DatagramPacket(data, data.length, IPAddress, puertoConexion);				      
					System.out.println("3");
					socket.send(sendPacket);
					System.out.println("Archivo enviado desde el Cliente");
				} 
				catch (Exception e){
					e.printStackTrace();
				}
			}
		}).start();
	}

	public Secuencia getFileEvent(){
		Secuencia fileEvent = new Secuencia();
		String fileName = sourceFilePath.substring(sourceFilePath.lastIndexOf("\\") + 1, sourceFilePath.length());
		String path = sourceFilePath.substring(0, sourceFilePath.lastIndexOf("\\") + 1);
		fileEvent.setDirectorioDestino(destinationPath);
		fileEvent.setNombreArchivo(fileName);
		fileEvent.setDirectorioFuente(sourceFilePath);
		File file = new File(sourceFilePath);
		if (file.isFile()) {
			try {
				DataInputStream diStream = new DataInputStream(new FileInputStream(file));
				long len = (int) file.length();
				byte[] fileBytes = new byte[(int) len];
				int read = 0;
				int numRead = 0;
				while (read < fileBytes.length && (numRead = diStream.read(fileBytes, read, fileBytes.length - read)) >= 0) {
					read = read + numRead;
				}
				fileEvent.setTamanioArchivo(len);
				fileEvent.setInfoArchivo(fileBytes);
				fileEvent.setEstado("Exitoso");
			} catch (Exception e) {
				e.printStackTrace();
				fileEvent.setEstado("Error");
			}
		} else {
			System.out.println("path specified is not pointing to a file");
			fileEvent.setEstado("Error");
		}
		return fileEvent;
	}
	public String HashArchivo(String ruta) throws Exception{
		//Escoge el tipo de hash
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		//llama el archivo
		FileInputStream fis = new FileInputStream(ruta);
		//crea el espacio
		byte[] infoBytes = new byte[1024];
		//lee el archivo
		int nread = 0;
		while ((nread = fis.read(infoBytes)) != -1) {
			md.update(infoBytes, 0, nread);
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

}
