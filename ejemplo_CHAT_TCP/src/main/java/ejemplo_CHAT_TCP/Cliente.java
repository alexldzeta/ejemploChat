package ejemplo_CHAT_TCP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.*;

import javax.swing.*;

public class Cliente {

	public static void main(String[] args) {

		MarcoCliente mimarco = new MarcoCliente(); // CREAMOS UN NUEVO MARCO CON JAVA SWING

		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}

class MarcoCliente extends JFrame { // LE AÑADIMOS UNAS PROPIEDADES

	public MarcoCliente() {

		setBounds(600, 300, 280, 350);

		LaminaMarcoCliente milamina = new LaminaMarcoCliente();

		add(milamina);

		setVisible(true);

	}

}

class LaminaMarcoCliente extends JPanel implements Runnable { // LE AÑADIMOS UN CAMPO DE TEXTO Y UN BOTON

	private JTextField campo1, nick, ip;
	private JButton miboton;
	private JTextArea mensajesRecibidos;

	public LaminaMarcoCliente() {

		nick = new JTextField(5);
		
		add(nick);
		
		JLabel texto = new JLabel("-CHAT-");

		add(texto);
		
		ip =new JTextField(8);
		
		add(ip);

		mensajesRecibidos = new JTextArea(12, 20);

		add(mensajesRecibidos);

		campo1 = new JTextField(20);

		add(campo1);

		miboton = new JButton("Enviar");

		EnviaTexto mievento = new EnviaTexto();

		miboton.addActionListener(mievento);

		add(miboton);
		
		Thread mihilo = new Thread(this);
		
		mihilo.start();

	}

	private class EnviaTexto implements ActionListener { // LE PONEMOS UN ACTION LISTENER AL BOTON PARA QUE SAQUE EL MENSAJE POR PANTALLA

		private static final String IP = "192.168.1.64";
		private static final int PUERTO = 9999;

		public void actionPerformed(ActionEvent e) {

			try {

				Socket socketCliente = new Socket(IP, PUERTO); // CREAMOS UN NUEVO SOCKET PARA COMUNICARNOS CON NUESTRO SERVIDOR
				
				Paquete paqueteDatos = new Paquete(); //INSTANCIAMOS LA CLASE PAQUETE
				
				paqueteDatos.setNick(nick.getText()); //Y ESTABLECEMOS EL NICK CON NUESTRO JTEXTFIELD EN LA VARIABLE NICK
				
				paqueteDatos.setIp(ip.getText()); //HACEMOS LO MISMO CON LA IP
				
				paqueteDatos.setMensaje(campo1.getText()); //AQUI COGEMOS LO QUE ESCRIBIMOS EN EL TEXTAREA CAMPO1
				
				ObjectOutputStream paquete_datos = new ObjectOutputStream(socketCliente.getOutputStream()); //EL PAQUETE DE DATOS ES UN OBJETO, CON LO CUAL TENEMOS QUE MANDAR UN OBJETO AL SERVIDOR

				paquete_datos.writeObject(paqueteDatos); //ESCRIBIMOS EL PAQUETE DE DATOS DE LA CLASE PAQUETE
				
				socketCliente.close();

			} catch (UnknownHostException e1) {

				e1.printStackTrace();

			} catch (IOException e1) {

				System.out.println(e1.getMessage());
			}

		}

	}
	
	class Paquete implements Serializable { //CREAMOS UNA CLASE QUE ENVIE UN PAQUETE DE DATOS CON NUESTRO NICK LA IP Y EL MENSAJE QUE MANDEMOS
											//CONVERTIMOS LA CLASE PAQUETE EN SERIALIZABLE PARA QUE EL PAQUETE DE DATOS LO TRANSFORME EN BITES Y PUEDA SER ENVIADO POR LA RED
		private String nick, ip, mensaje;

		public String getNick() {
			return nick;
		}

		public void setNick(String nick) {
			this.nick = nick;
		}

		public String getIp() {
			return ip;
		}

		public void setIp(String ip) {
			this.ip = ip;
		}

		public String getMensaje() {
			return mensaje;
		}

		public void setMensaje(String mensaje) {
			this.mensaje = mensaje;
		}
		
	}

	public void run() { //METODO RUN DE LA INTERFAZ RUNNABLE PARA QUE ESTE LA CLASE A LA ESCUCHA DEL NUEVO PUERTO
		
		
		
		try {
			
			ServerSocket servidorCliente = new ServerSocket(9090); //CREAMOS EL SERVIDOR EN EL CLIENTE 1 PARA QUE ABRA EL PUERTO DEL CLIENTE 2
			
			Socket cliente1; //UN NUEVO SOCKET QUE ESTE SIEMPRE A LA ESCUCHA
			
			Paquete paqueteRecibidoCliente2; //INSTANCIAMOS LA CLASE PAQUETE
			
			while(true) {
				
				cliente1 = servidorCliente.accept(); //ACEPTAMOS EL SOCKET EN EL SERVIDOR
				
				ObjectInputStream flujoentrada = new ObjectInputStream(cliente1.getInputStream()); //CREO UN NUEVO OBJETO DE DATOS PARA QUE LLEGUE EL PAQUETE DE DATOS DEL CLIENTE 2
				
			    paqueteRecibidoCliente2 = (Paquete) flujoentrada.readObject(); //LEO EL OBJETO DE DATOS QUE ENTRA
			    
			    mensajesRecibidos.append("\n" + paqueteRecibidoCliente2.getNick() + "--> " + paqueteRecibidoCliente2.getMensaje()); //RECOJO LOS DATOS DEL PAQUETE Y LO ESCRIBO EN EL AREA DE TEXTO CREADA EN LA LÁMINA
			
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}  catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} 
		
		
	}

}
