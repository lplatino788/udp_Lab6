package Server;

import java.util.Date;

public class SecuenciaServidor {
		
		private int numeroSecuencia;		
		private Date fechaRegistro;
		public int totalObjetos;
		
		public SecuenciaServidor(int total, int secuencia, Date registro ){
			numeroSecuencia=secuencia;
			fechaRegistro = registro;
			totalObjetos = total;
		}
		
		//-------Número Secuencia--------
		public int getNumeroSecuencia() {
			return numeroSecuencia;
		}
		//-------Fecha Secuencia--------
		public Date getFechaRegistro() {
			return fechaRegistro;
		}
		//-------Fecha Secuencia--------
	    public int getTotalObjetos() {
			return totalObjetos;
		}
}
