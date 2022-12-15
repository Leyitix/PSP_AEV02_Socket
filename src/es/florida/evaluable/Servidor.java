package es.florida.evaluable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

	public static void main(String[] args) throws IOException {

		int puerto = 1234;
		System.err.println("SERVIDOR >>> Escuchando...");
		ServerSocket server = new ServerSocket(puerto); 
		System.err.println("SERVIDOR >>> Conexion con exito!!!");
		
		while (true) {
			Socket conexion = server.accept();
            System.err.println("SERVIDOR >>> Lanza hilo clase Peticion");
            Peticion p = new Peticion(conexion);
            Thread hilo = new Thread(p);
            hilo.start();
		}
	}
}
