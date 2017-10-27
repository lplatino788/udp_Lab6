package Cliente;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.Color;

import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.DefaultComboBoxModel;

public class UDPClienteInterfaz extends JPanel {
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private UDPClientePpl udp;
	//private static final String ARCHIVO = "Archivo";
	//private static final String IP = "ip";
	//private static final String PUERTO = "Puerto";
	//private static final String OBJETOS = "Objetos";
	private JTextField textField_3;
	private String comboBoxText="";


	/**
	 * Create the panel.
	 */
	public UDPClienteInterfaz() {
		setBackground(Color.BLACK);
		setLayout(null);
		udp = new UDPClientePpl();

		//----------------------------Recibe IP del Servidor
		//Recibe IP del Servidor
		textField = new JTextField();
		textField.setBounds(255, 50, 112, 20);
		add(textField);
		textField.setColumns(10);
		String ipServidor="";
		//Boton Cambiar IP
		JButton btnIpServidor = new JButton("IP Servidor");
		btnIpServidor.setBounds(57, 47, 117, 29);
		btnIpServidor.setMnemonic(KeyEvent.VK_G);
		//btnIpServidor.setActionCommand( IP );
		btnIpServidor.setToolTipText("Cambiar IP del servidor");
		add(btnIpServidor);
		btnIpServidor.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String textArchivo = textField.getText();
				textField.selectAll();
				udp.cambiarIPServidor(textArchivo);		
			}
		});

		//-------------------------------------------------Puerto Servidor
		//Ingresa el numero de Puerto Servidor
		textField_1 = new JTextField();
		textField_1.setBounds(255, 87, 111, 20);
		add(textField_1);
		textField_1.setColumns(10);
		int puertoServer=0;
		/*try { 
			puertoServer=Integer.parseInt(textField_1.getText());
			udp.cambiarPuertoServidor(puertoServer);
		} catch(NumberFormatException e) { 
			JOptionPane.showMessageDialog(null, "El valor del puerto debe ser in entero");
		}*/
		//Boton Puerto
		JButton btnPuerto = new JButton("Puerto");
		btnPuerto.setBounds(57, 84, 117, 29);
		add(btnPuerto);
		btnPuerto.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				textField_1.selectAll();
				String textP=textField_1.getText();
				textField_1.selectAll();
				udp.cambiarPuertoServidor(Integer.parseInt(textP));			
			}
		});
		
		//--------------------------------------Objetos a Generar
		JLabel lblNumeroDe = new JLabel("Objetos a Generar");
		lblNumeroDe.setForeground(Color.WHITE);
		lblNumeroDe.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNumeroDe.setBounds(68, 159, 128, 14);
		add(lblNumeroDe);

		//Objetos a Generar
		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Select", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}));
		comboBox.setMaximumRowCount(10);
		comboBox.setBounds(255, 156, 119, 20);
		add(comboBox);		

		//----------------------------------Enviar Request al Servidor
		JButton btnEnviar = new JButton("Enviar");
		btnEnviar.setBounds(167, 217, 89, 23);
		btnEnviar.setMnemonic(KeyEvent.VK_M);
		//btnEnviar.setActionCommand( OBJETOS );
		btnEnviar.setToolTipText("Enviar "+ comboBox.getSelectedIndex() + " objetos");
		add(btnEnviar);
		btnEnviar.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				udp.enviarObjetos(comboBox.getSelectedItem().toString(), textField.getText(), textField_1.getText());				
			}
		});
		//------------------ Enviar Archivo por URL
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(255, 124, 111, 20);
		add(textField_3);
		
		JButton btnEnviarArchivo = new JButton("Enviar Archivo");
		btnEnviarArchivo.setBounds(57, 118, 117, 29);
		add(btnEnviarArchivo);
		btnEnviarArchivo.setMnemonic(KeyEvent.VK_P);
		//btnEnviarArchivo.setActionCommand( ARCHIVO );
		btnEnviarArchivo.setToolTipText("Enviar archivo con la ruta especificada");
		btnEnviarArchivo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String textArchivo = textField_3.getText();
				textField_3.selectAll();
				udp.enviarArchivos(textArchivo);				
			}
		});

		
	}
/*	public void actionPerformed (ActionEvent e){//(ActionEvent e) {

		String comando = e.getActionCommand( );
		String text = "";
		String text1 = "";
		String text2 = "";
		String text3= "";
		if( comando.equals( ARCHIVO ) ){
			text = textField_3.getText();
			textField_3.selectAll();
			udp.enviarArchivos(text);
		}
		else if( comando.equals( IP ) ) {
			text1 = textField.getText();
			textField.selectAll();
			udp.cambiarIPServidor(text1);
		}
		
		else if( comando.equals( OBJETOS ) ){
			text3 = comboBoxText;
			udp.enviarObjetos(text3);
		}
	}
*/
	/**
	 * Create the GUI and show it.  For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread.
	 */
	private static void crearMostrarGUI() {
		//Create and set up the window.
		JFrame frame = new JFrame("ButtonDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Create and set up the content pane.
		UDPClienteInterfaz newContentPane = new UDPClienteInterfaz();
		newContentPane.setOpaque(true); //content panes must be opaque
		frame.setContentPane(newContentPane);
		//Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				crearMostrarGUI();
			}
		});
	}
}
