package ejemplo_CHAT_TCP;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.*;

import ejemplo_CHAT_TCP.LaminaMarcoCliente.Paquete;

public class Servidor {

	public static void main(String[] args) {

		MarcoServidor mimarco = new MarcoServidor();

		mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}

class MarcoServidor extends JFrame implements Runnable {

	private JTextArea areaTexto;

	public MarcoServidor() {

		setBounds(1200, 300, 280, 350);

		JPanel milamina = new JPanel();

		milamina.setLayout(new BorderLayout());

		areaTexto = new JTextArea();

		milamina.add(areaTexto, BorderLayout.CENTER);

		add(milamina);

		setVisible(true);
		
		Thread mihilo = new Thread(this);
		
		mihilo.start();

	}

	public void run() {
		
		try {
			
			ServerSocket servidor = new ServerSocket(9999); //ABRIMOS EL PUERTO DEL SOCKET CLIENTE PARA QUE LLEGUE EL FLUJO DE DATOS
			
			String nick, ip, mensaje; //VARIABLES PARA ALMACENAR LOS DATOS RECIBIDOS
			
			Paquete datosRecibidos;
				
			while(true) { //LO METEMOS TODO EN UN BUCLE PARA QUE RECOOGA MAS MENSAJES CADA VEZ QUE LE DEMOS AL BOTON
			
				Socket socketServidor = servidor.accept(); //ACEPTAMOS LA ENTRADA DE DATOS DESDE EL PUERTO 9999
			
				ObjectInputStream paquete_datosRecibidos = new ObjectInputStream(socketServidor.getInputStream()); //RECIBIMOS EL OBJETO DE DATOS PARA QUE LO RECOGA EL SOCKET DEL SERVIDOR
			
				datosRecibidos = (Paquete) paquete_datosRecibidos.readObject(); //LO LEEMOS PERO COMO SON DE DISTINTO TIPO LO CASTEAMOS PARA QUE FUNCIONE
				
				nick = datosRecibidos.getNick(); //RECOGEMOS EL NICK DEL PAQUETE Y LO ALMACENAMOS EN LA VARIABLE CREADA
				
				ip = datosRecibidos.getIp(); //RECOGEMOS LA IP DEL PAQUETE Y LO ALMACENAMOS EN LA VARIABLE CREADA
				
				mensaje = datosRecibidos.getMensaje(); //RECOGEMOS EL MENSAJE DEL PAQUETE Y LO ALMACENAMOS EN LA VARIABLE CREADA
				
				areaTexto.append("\n" + nick + "--> " + mensaje + " para: " + ip);
				
				//-----------------------------------------------------------------------------------------------------------------------------------------------//
				
				Socket enviarCliente2 = new Socket(ip, 9090); //SOCKET DEL SERVIDOR AL CLIENTE 2
				
				ObjectOutputStream reenvio = new ObjectOutputStream(enviarCliente2.getOutputStream()); //HE CREADO UN NUEVO PAQUETE DE DATOS EN EL SERVIDOR
				
				reenvio.writeObject(datosRecibidos); //Y DENTRO DE ESE NUEVO PAQUETE HE METIDO EL ANTERIOR ENVIADO POR EL PRIMER CLIENTE
				
				reenvio.close();
				
				enviarCliente2.close(); //Y CIERRO EL NUEVO SOCKET
					
				socketServidor.close();
			
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		
	}

}
