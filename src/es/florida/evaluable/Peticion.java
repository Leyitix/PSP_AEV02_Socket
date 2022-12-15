package es.florida.evaluable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Peticion implements Runnable {

	static Socket conexion;
	static String[][] tablero = { { "", "", "" }, { "", "", "" }, { "", "", "" } };
	static Integer fila, columna, indiceValor;
	static String valor = "O";
	static String turno;

	/**
	 * Metodo constructor de la Peticion
	 * 
	 * @param conexion Recibe la conexión lanzada por el servidor
	 */
	public Peticion(Socket conexion) {
		Peticion.conexion = conexion;
	}

	/**
	 * Metodo que se encarga de enviar las posiciones de la jugada aleatoria que
	 * realiza la maquina. Una vez enviadas las posiciones se llama al metodo
	 * recibirPosiciones que prepara la servidor para recibir la jugada del cliente.
	 * 
	 * @param conexion String que recibe la conexion entre cliente y servidor
	 * @param fila int que recibe la fila en la que se encuentra el valor
	 * @param columna int que en la que se encuentra el valor
	 * @param quienGana String que recibe el ganador de la partida
	 */
	public static void enviarPosiciones(Socket conexion, int fila, int columna, String quienGana) throws IOException {

		try {

			String filaStr = String.valueOf(fila);
			String columStr = String.valueOf(columna);

			if (quienGana != null) {
				setTurno(quienGana);
			} else {
				setTurno("jugador");
			}

			OutputStream os = conexion.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.write(filaStr.toString() + "\n");
			pw.write(columStr.toString() + "\n");
			pw.write(valor.toString() + "\n");
			pw.write(getTurno().toString() + "\n");
			pw.flush();

			System.err.println("SERVIDOR>>> Posiciones enviadas al jugador: Fila -> " + fila + " // Columna -> "
					+ columna + " // Valor -> " + valor + " // Turno -> " + getTurno());

			recibirPosiciones(conexion);

		} catch (IOException | ClassNotFoundException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Metodo que recibe la jugada del cliente y si la jugada no es nula la añade a
	 * la matriz que se encuentra en el servidor. Una vez añadida se llama al metodo
	 * maquina que da el turno de la partida a la maquina
	 * 
	 * @param conexion Recibe la conexion entre cliente y servidor
	 */
	public static void recibirPosiciones(Socket conexion) throws ClassNotFoundException, IOException {

		InputStream is = conexion.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader bf = new BufferedReader(isr);

		String fila = bf.readLine();
		String columna = bf.readLine();
		String valor = bf.readLine();
		String turno = bf.readLine();
		setTurno(turno);

		// despues de recibir la tirada del jugador, la maquina continua la partida
		if (fila != null && columna != null && valor != null && turno != null) {
			System.err.println("SERVIDOR>>> Posiciones recibidas del jugador: Fila -> " + fila + " // Columna -> "
					+ columna + " // Valor -> " + valor + " // Turno -> " + getTurno());

			int filaInt = Integer.parseInt(fila);
			int columnInt = Integer.parseInt(columna);

			// Asignar posiciones en el array
			tablero[filaInt][columnInt] = valor;
			maquina(conexion);
		}

	}

	/**
	 * Metodo que comprueba si la partida tiene ganador. Si se cumple alguna
	 * condicion, el metodo devuelve un string con el nombre del ganador
	 * 
	 * @return ganaPartida Retorna el String ganador de la partida
	 */
	public static String finPartida() throws IOException {

		String ganaPartida = null;

		// Cuando la matriz esta completa
		if (tablero[0][0] != "" && tablero[0][1] != "" && tablero[0][2] != "" && tablero[1][0] != ""
				&& tablero[1][1] != "" && tablero[0][2] != "" && tablero[2][0] != "" && tablero[2][1] != ""
				&& tablero[2][2] != "")
			ganaPartida = "matrizCompleta";

		// Cuando gana la máquina
		// Filas
		if (tablero[0][0].equals("O") && tablero[0][1].equals("O") && tablero[0][2].equals("O"))
			ganaPartida = "maquinaGana";

		if (tablero[1][0].equals("O") && tablero[1][1].equals("O") && tablero[1][2].equals("O"))
			ganaPartida = "maquinaGana";

		if (tablero[2][0].equals("O") && tablero[2][1].equals("O") && tablero[2][2].equals("O"))
			ganaPartida = "maquinaGana";

		// Columnas
		if (tablero[0][0].equals("O") && tablero[1][0].equals("O") && tablero[2][0].equals("O"))
			ganaPartida = "maquinaGana";

		if (tablero[0][1].equals("O") && tablero[1][1].equals("O") && tablero[2][1].equals("O"))
			ganaPartida = "maquinaGana";

		if (tablero[0][2].equals("O") && tablero[1][2].equals("O") && tablero[2][2].equals("O"))
			ganaPartida = "maquinaGana";

		// Diagonales
		if (tablero[0][0].equals("O") && tablero[1][1].equals("O") && tablero[2][2].equals("O"))
			ganaPartida = "maquinaGana";

		if (tablero[0][2].equals("O") && tablero[1][1].equals("O") && tablero[2][0].equals("O"))
			ganaPartida = "maquinaGana";

		// Cuando gana el jugador
		// Filas
		if (tablero[0][0].equals("X") && tablero[0][1].equals("X") && tablero[0][2].equals("X"))
			ganaPartida = "jugadorGana";

		if (tablero[1][0].equals("X") && tablero[1][1].equals("X") && tablero[1][2].equals("X"))
			ganaPartida = "jugadorGana";

		if (tablero[2][0].equals("X") && tablero[2][1].equals("X") && tablero[2][2].equals("X"))
			ganaPartida = "jugadorGana";

		// Columnas
		if (tablero[0][0].equals("X") && tablero[1][0].equals("X") && tablero[2][0].equals("X"))
			ganaPartida = "jugadorGana";

		if (tablero[0][1].equals("X") && tablero[1][1].equals("X") && tablero[2][1].equals("X"))

			if (tablero[0][2].equals("X") && tablero[1][2].equals("X") && tablero[2][2].equals("X"))
				ganaPartida = "jugadorGana";

		// diagonales
		if (tablero[0][0].equals("X") && tablero[1][1].equals("X") && tablero[2][2].equals("X"))
			ganaPartida = "jugadorGana";

		if (tablero[0][2].equals("X") && tablero[1][1].equals("X") && tablero[2][0].equals("X"))
			ganaPartida = "jugadorGana";

		return ganaPartida;

	}

	/**
	 * Metodo que realiza la jugada de la maquina. Para ello se generan dos numeros
	 * aleatorios, uno que es la fila en la que se encuentra el valor y el otro para
	 * la columna. Mientras no se encuentre una posicion valida en la que añadir el
	 * valor se generaran numeros aleatorios. Cuando haya una posicion valida la
	 * jugada se añadira a la matriz y se comprobara si hay o no ganador.
	 * Finalmente, se llamara al metodo enviarPosiciones y se cambiara el estado de
	 * posicionEncontrada para salir del bucle
	 * 
	 * @param conexion Se recibe la conexion entre cliente y servidor
	 */
	public static void maquina(Socket conexion) throws ClassNotFoundException, IOException {

		boolean posicionEncontrada = false;
		String finPartida = null;

		while (posicionEncontrada == false && finPartida == null) {

			fila = (int) (Math.random() * 3);
			columna = (int) (Math.random() * 3);

			if (tablero[fila][columna].equals("")) {
				System.err.println("SERVIDOR >>> Realiza jugada --> Fila: " + fila + " // Columna: " + columna);
				tablero[fila][columna] = valor.toUpperCase();

				String quienGana = finPartida();
				enviarPosiciones(conexion, fila, columna, quienGana);

				posicionEncontrada = true;
			}

		}

	}

	@SuppressWarnings("unused")
	@Override

	/**
	 * Metodo que se ejecuta gracias al hilo lanzalo por la aplicación Servidor. Al
	 * inicio del hilo se genera un numero aleatorio entre 1 y 10 que se envia al
	 * cliente. Una vez se recibe la respuesta del cliente, el servidor comunica al
	 * cliente quien comienza la partida. Si comienza el cliente se llama al metodo
	 * recibirPosiciones, si comienza la maquina se llama al metodo maquina.
	 */
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
				recibirPosiciones(conexion);

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
				maquina(conexion);

			}

			conexion.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("SERVIDOR >>> Error.");
		}

	}

	/**
	 * Getter del turno de juego
	 * 
	 * @return Retorna quien tiene el turno de juego
	 */
	public static String getTurno() {
		return turno;
	}

	/**
	 * Setter del turno de juego
	 * 
	 * @param turno recibe quien tiene el turno de juego
	 */
	public static void setTurno(String turno) {
		Peticion.turno = turno;
	}

}
