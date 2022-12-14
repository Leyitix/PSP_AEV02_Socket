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
	static String[][] tablero = { { "", "", "" }, { "", "", "" }, { "", "", "" } };
	static Integer fila, columna, indiceValor;
	static String valor = "O";
	static String turno;

	public Peticion(Socket conexion) {
		Peticion.conexion = conexion;
	}

	public static void enviarObjeto(Socket conexion, int fila, int columna) throws IOException {

		try {

			String filaStr = String.valueOf(fila);
			String columStr = String.valueOf(columna);
			String turno = "jugador";
			setTurno(turno);

			OutputStream os = conexion.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.write(filaStr.toString() + "\n");
			pw.write(columStr.toString() + "\n");
			pw.write(valor.toString() + "\n");
			pw.write(getTurno().toString() + "\n");
			pw.flush();

			System.err.println("SERVIDOR>>> Posiciones enviadas al jugador: Fila -> " + fila + " // Columna -> "
					+ columna + " // Valor -> " + valor + " // Turno -> " + getTurno());

		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public static void recibirPosiciones(Socket conexion) throws ClassNotFoundException, IOException {

		InputStream is = conexion.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader bf = new BufferedReader(isr);

		String fila = bf.readLine();
		String columna = bf.readLine();
		String valor = bf.readLine();
		String turno = bf.readLine();
		setTurno(turno);

		System.err.println("SERVIDOR>>> Posiciones recibidas del jugador: Fila -> " + fila + " // Columna -> " + columna
				+ " // Valor -> " + valor + " // Turno -> " + getTurno());

		int filaInt = Integer.parseInt(fila);
		int columnInt = Integer.parseInt(columna);

		// Asignar posiciones en el array
		tablero[filaInt][columnInt] = valor;

		// despues de recibir la tirada del jugador, la maquina continua la partida
		maquina(conexion);

	}

	public static void maquina(Socket conexion) throws ClassNotFoundException, IOException {

		boolean posicionEncontrada = false;
		
		while (posicionEncontrada == false) {
			
			fila = (int) (Math.random() * 3);
			columna = (int) (Math.random() * 3);
			
			if (tablero[fila][columna].equals("")) {
				System.out.println("SE HA ENCONTRADO UNA POSICION VALIDA!!!!!!");
				System.err.println("SERVIDOR >>> Realiza jugada --> Fila: " + fila + " // Columna: " + columna);
				tablero[fila][columna] = valor.toUpperCase();
				enviarObjeto(conexion, fila, columna);
				
				posicionEncontrada = true;
			} else {
				System.out.println("NO SE HA ENCONTRADO UNA POSICION VALIDA!!!!!!");
			}
			// TODO: AÑADIR OTRA CONDICION!!!
			
		}
			


//		while (tablero[fila][columna].equals("")) {
//
//			System.err.println("SERVIDOR >>> Realiza jugada --> Fila: " + fila + " // Columna: " + columna);
//			if (tablero[fila][columna].equals("")) {
//
//				tablero[fila][columna] = valor.toUpperCase();
//				enviarObjeto(conexion, fila, columna);
//
//			} else {
//				fila = (int) (Math.random() * 3);
//				columna = (int) (Math.random() * 3);
//			}
//
//		}

		// TODO: SI NO SE ENCUENTRA NINGUNA COINCIDENCIA // EVITAR QUE LA APP EXPLOTE :(
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
				setTurno("jugador");
				while (getTurno().equals("jugador")) {
					recibirPosiciones(conexion);
				}

			}
			if (inicioPartida.equals("maquina")) {
				// envio de mensaje a cliente
				mensaje = "maquina";
				os = conexion.getOutputStream();
				pw = new PrintWriter(os);
				pw.write(mensaje + "\n");
				pw.flush();

				// maquina comienza partida
				setTurno("maquina");
				while (getTurno().equals("maquina")) {
					maquina(conexion);
				}

			}

			conexion.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("SERVIDOR >>> Error.");
		}

	}

	public static String getTurno() {
		return turno;
	}

	public static void setTurno(String turno) {
		Peticion.turno = turno;
	}

}
