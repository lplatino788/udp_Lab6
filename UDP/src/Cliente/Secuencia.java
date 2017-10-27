package Cliente;

import java.util.Date;
import java.io.Serializable;

public class Secuencia {

	private String ip;
	private int puerto;	
	private int numeroSecuencia;	
	private Date fechaRegistro;
	private static final long serialVersionUID = 1L;
	private String directorioFuente;
	private String nombreArchivo;
	private String directorioDestino;	
	private long tamanioArchivo;
	private byte[] infoArchivo;
	private String actualizacion;
	
	public Secuencia(){
		//ip = ipT;
		//puerto=port;
		//numeroSecuencia=secuencia;
		//fechaRegistro = registro;
	}

	//-------IP--------
	public String getIp() {
		return ip;
	}
	public void setIp(String ipServidor) {
		this.ip = ipServidor;
	}	
	//-------Puerto--------
	public int getPuerto() {
		return puerto;
	}
	public void setPuerto(int puertoServidor) {
		this.puerto = puertoServidor;
	}	
	//-------Número Secuencia--------
	public void setNumeroSecuencia(int sec) {
		numeroSecuencia=sec;
	}
	public int getNumeroSecuencia() {
		return numeroSecuencia;
	}
	//-------Directorio Fuente--------
	public String getSourceDirectory() {
	    return directorioFuente;
	}
	public void setDirectorioFuente(String dirFuente) {
	    this.directorioFuente = dirFuente;
	}
	//-------Fecha Secuencia--------
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	//-------Directorio Destino--------
	 public String getDirectorioDestino() {
        return directorioDestino;
	 }
	 public void setDirectorioDestino(String destDirectorio) {
	    this.directorioDestino = destDirectorio;
	 }
	//-------Nombre Archivo--------
	 public String getNombreArchivo() {
	    return nombreArchivo;
	 }
	 public void setNombreArchivo(String archivo) {
	    this.nombreArchivo = archivo;
	 }
	//-------Tamaño Archivo--------
	 public long getTamanioArchivo() {
		return tamanioArchivo;
     }
	 public void setTamanioArchivo(long tamanio) {
	    this.tamanioArchivo= tamanio;
	 }
	//-------Informacion Archivo--------
	 public byte[] getInfoArchivo() {
	    return infoArchivo;
	 }
	 public void setInfoArchivo(byte[] fileData) {
		this.infoArchivo = fileData;
	 }
	//-------Actualizacion--------
	 public String getEstado() {
		 return actualizacion;
	 }
	 public void setEstado(String estatus) {
		 this.actualizacion = estatus;
	 }
	
}

