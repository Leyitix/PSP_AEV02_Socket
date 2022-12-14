package es.florida.evaluable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Peticion implements Runnable {

	static ObjectInputStream inObject;
	static Socket conexion;
	static String valores[] = { "x", "o" };
	static String[][] tablero = { { "", "", "" }, { "", "", "" }, { "", "", "" } };
	static Integer fila, columna, indiceValor;
	static String valor;

	public Peticion(Socket conexion) {
		Peticion.conexion = conexion;
	}

	public static void enviarObjeto(Socket conexion, int fila, int columna) throws IOException {
		ObjectOutputStream outObjeto = new ObjectOutputStream(conexion.getOutputStream());
		Jugada jugada = new Jugada(fila, columna, valor);
		outObjeto.writeObject(jugada);
		outObjeto.close();
	}

	public static void recibirObjeto(Socket conexion) throws ClassNotFoundException, IOException {
		ObjectInputStream inObject = new ObjectInputStream(conexion.getInputStream());
		Jugada jugadaJugador = (Jugada) inObject.readObject();
		System.err.println("SERVIDOR>>> Objeto recibido del jugador: Fila -> " + jugadaJugador.getFila()
				+ " // Columna -> " + jugadaJugador.getColumna() + " // Valor -> " + jugadaJugador.getValor());

		tablero[jugadaJugador.getFila()][jugadaJugador.getColumna()] = jugadaJugador.getValor();
		inObject.close();

		// despues de recibir la tirada del jugador, la maquina continua la partida
//		maquina(conexion);
	}

	public static void maquina(Socket conexion) throws ClassNotFoundException, IOException {

		fila = (int) (Math.random() * 3);
		columna = (int) (Math.random() * 3);
		indiceValor = (int) (Math.random() * 2);
		valor = valores[indiceValor];

		while (tablero[fila][columna].equals("")) {

			System.err.println("SERVIDOR >>> Realiza jugada --> Fila: " + fila + " // Columna: " + columna
					+ " // Valor: " + valor);
			if (tablero[fila][columna].equals("")) {
				tablero[fila][columna] = valor.toUpperCase();
			} else {
				fila = (int) (Math.random() * 3);
				columna = (int) (Math.random() * 3);
			}

			enviarObjeto(conexion, fila, columna);

		}
	}

	@Override
	public void run() {

		String mensaje;
		String valor = null;
		String inicioPartida = null;

		OutputStream os;
		PrintWriter pw;

		try {

			// generamos un numero aleatorio entre 1 y 10 y lo enviamos al jugador
			Integer numero = (int) (Math.random() * 10 + 1);
			System.err.println("SERVIDOR >>> Genera un numero aleatorio: " + numero);

			os = conexion.getOutputStream();
			pw = new PrintWriter(os);
			pw.write(numero.toString() + "\n");
			pw.flush();

			// recibimos la respuesta del cliente ( par/impar )
			InputStream is = conexion.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader bf = new BufferedReader(isr);
			String respuesta = bf.readLine();
			System.err.println("SERVIDOR >>> Recibe respuesta de Cliente: " + respuesta);

			// dependiendo de la respuesta del cliente se elige quien empieza la partida
			if (respuesta.equals("par")) {
				if (numero % 2 == 0) {
					inicioPartida = "cliente";
				} else {
					inicioPartida = "maquina";
				}
			}
			if (respuesta.equals("impar")) {
				if (numero % 2 != 0) {
					inicioPartida = "cliente";
				} else {
					inicioPartida = "maquina";
				}
			}

			if (inicioPartida.equals("cliente")) {
				mensaje = "cliente";
				os = conexion.getOutputStream();
				pw = new PrintWriter(os);
				pw.write(mensaje + "\n");
				pw.flush();

				// la maquina recibe la jugada inicial del jugador
				recibirObjeto(conexion);

			}
			if (inicioPartida.equals("maquina")) {
				// envio de mensaje a cliente
				mensaje = "maquina";
				os = conexion.getOutputStream();
				pw = new PrintWriter(os);
				pw.write(mensaje + "\n");
				pw.flush();

				// maquina comienza partida
				maquina(conexion);

			}

			conexion.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("SERVIDOR >>> Error.");
		}

	}

}
