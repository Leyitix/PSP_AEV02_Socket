package es.florida.evaluable;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SwingConstants;
import java.awt.Color;

@SuppressWarnings("serial")
public class Cliente extends JFrame {

	private JPanel contentPane;
	static JButton btn_00, btn_01, btn_02, btn_10, btn_11, btn_12, btn_20, btn_21, btn_22;
	static int fila, columna;
	static String valor = "X";
	static String turno;
	static ActionListener actionListenerButton_00, actionListenerButton_01, actionListenerButton_02,
			actionListenerButton_10, actionListenerButton_11, actionListenerButton_12, actionListenerButton_20,
			actionListenerButton_21, actionListenerButton_22;

	String[] columnas = { "1", "2", "3" };
	static String[][] tablero = { { "", "", "" }, { "", "", "" }, { "", "", "" } };

	/**
	 * Cuando se termina la partida y se llama a este metodo se muestra mediante un
	 * popup quien ha ganado la partida o si estan todas las casillas ocupadas y no
	 * se puede continuar. Una vez se ha terminado la partida se deshabilitan los
	 * botones para que el cliente no pueda seguir jugando y desencadene un error.
	 * 
	 * @param ganador Se recibe quien ha ganado la partida
	 */
	public static void ganador(String ganador) throws IOException {

		boolean finPartida = false;

		if (ganador.equals("matrizCompleta")) {
			JOptionPane.showMessageDialog(new JFrame(), "Todas las casillas están ocupadas", "Fin de partida",
					JOptionPane.INFORMATION_MESSAGE);
			finPartida = true;
		} else if (ganador.equals("maquinaGana")) {
			JOptionPane.showMessageDialog(new JFrame(), "Gana la máquina", "Fin de partida",
					JOptionPane.INFORMATION_MESSAGE);
			finPartida = true;
		} else if (ganador.equals("jugadorGana")) {
			JOptionPane.showMessageDialog(new JFrame(), "Gana el jugador", "Fin de partida",
					JOptionPane.INFORMATION_MESSAGE);
			finPartida = true;
		}

		if (finPartida) {
			getBtn_00().setEnabled(false);
			getBtn_01().setEnabled(false);
			getBtn_02().setEnabled(false);

			getBtn_10().setEnabled(false);
			getBtn_11().setEnabled(false);
			getBtn_12().setEnabled(false);

			getBtn_20().setEnabled(false);
			getBtn_21().setEnabled(false);
			getBtn_22().setEnabled(false);
		}

	}

	/**
	 * Este metodo envia las posiciones elegidas por el cliente al servidor y ademas
	 * se comprueba si se cumple alguna de las condiciones para terminar la partida.
	 * Si se cumple alguna condicion se llamara al metodo ganador.
	 * 
	 * @param conexion Se recibe la conexion del socket
	 * @param fila     En la que se encuentra el valor
	 * @param columna  En la que se encuentra el valor
	 * @param valor    Que se dibuja en el boton correspondiente al cliente
	 */
	public static void enviarPosiciones(Socket conexion, int fila, int columna, String valor) {
		try {

			String filaStr = String.valueOf(fila);
			String columStr = String.valueOf(columna);
			String turno = "maquina";
			setTurno(turno);

			OutputStream os = conexion.getOutputStream();
			PrintWriter pw = new PrintWriter(os);
			pw.write(filaStr.toString() + "\n");
			pw.write(columStr.toString() + "\n");
			pw.write(valor.toString() + "\n");
			pw.write(turno.toString() + "\n");
			pw.flush();

			System.out.println("CLIENTE>>> Posiciones enviadas a la maquina: Fila -> " + fila + " // Columna -> "
					+ columna + " // Valor -> " + valor + " // Turno -> " + getTurno());

			// Cuando la matriz esta completa
			if (tablero[0][0] != "" && tablero[0][1] != "" && tablero[0][2] != "" && tablero[1][0] != ""
					&& tablero[1][1] != "" && tablero[0][2] != "" && tablero[2][0] != "" && tablero[2][1] != ""
					&& tablero[2][2] != "") {
				JOptionPane.showMessageDialog(new JFrame(), "Todas las casillas están ocupadas", "Fin de partida",
						JOptionPane.INFORMATION_MESSAGE);
			} else if (tablero[0][0].equals("O") && tablero[0][1].equals("O") && tablero[0][2].equals("O")) {
				ganador("maquinaGana");
			} else if (tablero[1][0].equals("O") && tablero[1][1].equals("O") && tablero[1][2].equals("O")) {
				ganador("maquinaGana");
			} else if (tablero[2][0].equals("O") && tablero[2][1].equals("O") && tablero[2][2].equals("O")) {
				ganador("maquinaGana");
			} else if (tablero[0][0].equals("O") && tablero[1][0].equals("O") && tablero[2][0].equals("O")) {
				ganador("maquinaGana");
			} else if (tablero[0][1].equals("O") && tablero[1][1].equals("O") && tablero[2][1].equals("O")) {
				ganador("maquinaGana");
			} else if (tablero[0][2].equals("O") && tablero[1][2].equals("O") && tablero[2][2].equals("O")) {
				ganador("maquinaGana");
			} else if (tablero[0][0].equals("O") && tablero[1][1].equals("O") && tablero[2][2].equals("O")) {
				ganador("maquinaGana");
			} else if (tablero[0][2].equals("O") && tablero[1][1].equals("O") && tablero[2][0].equals("O")) {
				ganador("maquinaGana");
			} else if (tablero[0][0].equals("X") && tablero[0][1].equals("X") && tablero[0][2].equals("X")) {
				ganador("jugadorGana");
			} else if (tablero[1][0].equals("X") && tablero[1][1].equals("X") && tablero[1][2].equals("X")) {
				ganador("jugadorGana");
			} else if (tablero[2][0].equals("X") && tablero[2][1].equals("X") && tablero[2][2].equals("X")) {
				ganador("jugadorGana");
			} else if (tablero[0][0].equals("X") && tablero[1][0].equals("X") && tablero[2][0].equals("X")) {
				ganador("jugadorGana");
			} else if (tablero[0][1].equals("X") && tablero[1][1].equals("X") && tablero[2][1].equals("X")) {
				ganador("jugadorGana");
			} else if (tablero[0][2].equals("X") && tablero[1][2].equals("X") && tablero[2][2].equals("X")) {
				ganador("jugadorGana");
			} else if (tablero[0][0].equals("X") && tablero[1][1].equals("X") && tablero[2][2].equals("X")) {
				ganador("jugadorGana");
			} else if (tablero[0][2].equals("X") && tablero[1][1].equals("X") && tablero[2][0].equals("X")) {
				ganador("jugadorGana");
			} else {
				recibirPosiciones(conexion);
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Este metodo se encarga de recibir las jugadas que hace la maquina. Estas
	 * posiciones se asignan en la matriz y se llama a dos metodos primero al metodo
	 * maquina para mostrar en la interfaz la jugada de la maquina y despues al
	 * metodo ganador.
	 * 
	 * @param conexion Se recibe el socket
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

		System.out.println("CLIENTE>>> Posiciones rebibidas del servidor: Fila -> " + fila + " // Columna -> " + columna
				+ " // Valor -> " + valor + " // Turno -> " + getTurno());

		int filaInt = Integer.parseInt(fila);
		int columnInt = Integer.parseInt(columna);

		// Asignar posiciones en el array
		tablero[filaInt][columnInt] = valor;

		String boton = fila + columna;

		maquina(boton, valor, filaInt, columnInt);

		ganador(turno);

	}

	/**
	 * En este metodo se pinta en los diferentes botones la jugada que hace la
	 * maquina. Tambien se añade a la matriz la jugada de la maquina.
	 * 
	 * @param boton   Se recibe el nombre del boton
	 * @param valor   Que se va a pintar en la interfaz
	 * @param fila    En la que se encuentra el valor
	 * @param columna En la que se encuentra el valor
	 */
	public static void maquina(String boton, String valor, int fila, int columna) throws IOException {
		// set del valor obtenido desde la maquina en el boton
		if (boton.equals("00"))
			getBtn_00().setText(valor);
		if (boton.equals("01"))
			getBtn_01().setText(valor);
		if (boton.equals("02"))
			getBtn_02().setText(valor);
		if (boton.equals("10"))
			getBtn_10().setText(valor);
		if (boton.equals("11"))
			getBtn_11().setText(valor);
		if (boton.equals("12"))
			getBtn_12().setText(valor);
		if (boton.equals("20"))
			getBtn_20().setText(valor);
		if (boton.equals("21"))
			getBtn_21().setText(valor);
		if (boton.equals("22"))
			getBtn_22().setText(valor);

		tablero[fila][columna] = valor;

	}

	/**
	 * En este metodo se recibe la jugada que ha hecho el cliente y se añade a la
	 * matriz. Por otro lado se llama al metodo enviarPosiciones.
	 * 
	 * @param valor    Valor que se va a pintar en la interfaz
	 * @param fila     En la que se encuentra el valor
	 * @param columna  En la que se encuentra el valor
	 * @param conexion Recibe la conexion entre cliente y servidor
	 */
	public static void jugada(int fila, int columna, String valor, Socket conexion) {
		tablero[fila][columna] = valor;
		enviarPosiciones(conexion, fila, columna, valor);
	}

	/**
	 * Este metodo lanza un popup si se intenta pulsar en un boton que ya esta
	 * completo.
	 */
	public static void posicionOcupada() {
		JOptionPane.showMessageDialog(new JFrame(), "Elige otra casilla para realizar tu jugada", "",
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Desde este metodo el jugador puede realizar su jugada. Este contendrá todos
	 * los ActionListeners y los botones con los que el jugador puede interactuar.
	 * Cuando se pulsa un boton en el se pinta la "X" que es la figura con la que
	 * juega el cliente y se llama al metodo jugada. Si ese boton ya tiene una
	 * jugada se llamara al metodo posicionOcupada
	 * 
	 * @param conexion Se recibe la conexion entre cliente y servidor
	 */
	public static void jugador(Socket conexion) throws IOException {
		ActionListener actionListenerButton_00 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tablero[0][0].equals("")) {
					getBtn_00().setText(valor);
					jugada(0, 0, valor, conexion);
				} else {
					posicionOcupada();
				}

			}
		};

		ActionListener actionListenerButton_01 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tablero[0][1].equals("")) {
					getBtn_01().setText(valor);
					jugada(0, 1, valor, conexion);
				} else {
					posicionOcupada();
				}
			}
		};

		ActionListener actionListenerButton_02 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tablero[0][2].equals("")) {
					getBtn_02().setText(valor);
					jugada(0, 2, valor, conexion);
				} else {
					posicionOcupada();
				}
			}
		};

		ActionListener actionListenerButton_10 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tablero[1][0].equals("")) {
					getBtn_10().setText(valor);
					jugada(1, 0, valor, conexion);
				} else {
					posicionOcupada();
				}
			}
		};

		ActionListener actionListenerButton_11 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tablero[1][1].equals("")) {
					getBtn_11().setText(valor);
					jugada(1, 1, valor, conexion);
				} else {
					posicionOcupada();
				}
			}
		};

		ActionListener actionListenerButton_12 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tablero[1][2].equals("")) {
					getBtn_12().setText(valor);
					jugada(1, 2, valor, conexion);
				} else {
					posicionOcupada();
				}
			}
		};

		ActionListener actionListenerButton_20 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tablero[2][0].equals("")) {
					getBtn_20().setText(valor);
					jugada(2, 0, valor, conexion);
				} else {
					posicionOcupada();
				}
			}
		};

		ActionListener actionListenerButton_21 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tablero[2][1].equals("")) {
					getBtn_21().setText(valor);
					jugada(2, 1, valor, conexion);
				} else {
					posicionOcupada();
				}
			}
		};

		ActionListener actionListenerButton_22 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tablero[2][2].equals("")) {
					getBtn_22().setText(valor);
					jugada(2, 2, valor, conexion);
				} else {
					posicionOcupada();
				}
			}
		};

		getBtn_00().addActionListener(actionListenerButton_00);
		getBtn_01().addActionListener(actionListenerButton_01);
		getBtn_02().addActionListener(actionListenerButton_02);
		getBtn_10().addActionListener(actionListenerButton_10);
		getBtn_11().addActionListener(actionListenerButton_11);
		getBtn_12().addActionListener(actionListenerButton_12);
		getBtn_20().addActionListener(actionListenerButton_20);
		getBtn_21().addActionListener(actionListenerButton_21);
		getBtn_22().addActionListener(actionListenerButton_22);

	}

	/**
	 * Metodo main de la aplicacion Cliente. En este metodo se realizan las
	 * siguientes acciones: 1. Se construye la interfaz del juego 2. Se realiza la
	 * conexion con el servidor 3. El jugador recibe un numero aleatorio por parte
	 * del servidor 4. Jugador intenta adivinar si es par o impar y envia su
	 * respuesta al servidor 5. Dependiendo de la respuesta anterior servidor decide
	 * quien inicia el juego y envia la respuesta a jugador 6. Si el jugador
	 * comienza la partida, llamara al metodo jugador 7. Si la maquina inica la
	 * partida llamara al metodo recibirPosiciones
	 * 
	 * @param args[] es un arreglo con los parámetros que reciba por consola
	 */
	public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {

		// Cliente construye la interfaz
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Cliente frame = new Cliente();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		System.out.println("CLIENTE >>> Preparado para conectarse con el servidor...");

		String host = "localhost";
		int puerto = 1234;
		Socket conexion = new Socket(host, puerto);

		// jugador recibe un numero aleatorio entre 1 y 10 desde el servidor
		InputStream is = conexion.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader bf = new BufferedReader(isr);

		String numStr = bf.readLine();
		System.out.println("CLIENTE>>> Recibe numero aleatorio: " + numStr);
//		int numero = Integer.parseInt(numStr);

		// jugador intenta adivinar si el número obtenido es par
		int option = JOptionPane.showConfirmDialog(null, "Servidor ha generado un número aleatorio, ¿Crees que es par?",
				"Par o Impar", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		String tipoNum;

		if (option == 0) {
			System.out.println("CLIENTE>>> El numero es par");
			tipoNum = "par";
		} else {
			tipoNum = "impar";
		}

		// jugador envia su respuesta al servidor
		OutputStream os = conexion.getOutputStream();
		PrintWriter pw = new PrintWriter(os);
		pw.write(tipoNum.toString() + "\n");
		pw.flush();

		// se recibe la información de quien inicia la partida
		is = conexion.getInputStream();
		isr = new InputStreamReader(is);
		bf = new BufferedReader(isr);
		String mensaje = bf.readLine();
		System.out.println("CLIENTE>>> Envio respuesta a servidor: " + mensaje);

		// cliente comienza partida
		if (mensaje.equals("cliente")) {
			JOptionPane.showMessageDialog(new JFrame(), "Jugador inicia la partida", "INICIO DE PARTIDA",
					JOptionPane.INFORMATION_MESSAGE);

			setTurno("jugador");
			jugador(conexion);

		}

		// maquina comienza partida
		if (mensaje.equals("maquina")) {
			JOptionPane.showMessageDialog(new JFrame(), "La máquina comienza la partida", "INICIO DE PARTIDA",
					JOptionPane.INFORMATION_MESSAGE);

			setTurno("maquina");
			recibirPosiciones(conexion);

			setTurno("jugador");
			jugador(conexion);

		}

	}

	/**
	 * Metodo constructor de la aplicacion Cliente que contendra la interfaz
	 */
	public Cliente() {
		setBackground(new Color(255, 255, 255));
		// el metodo constructor contendrá la interfaz
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 337, 383);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(219, 207, 254));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		btn_00 = new JButton("");
		btn_00.setBounds(10, 74, 89, 68);
		contentPane.add(btn_00);

		btn_01 = new JButton("");
		btn_01.setBounds(119, 74, 89, 68);
		contentPane.add(btn_01);

		btn_02 = new JButton("");
		btn_02.setBounds(222, 74, 89, 68);
		contentPane.add(btn_02);

		btn_10 = new JButton("");
		btn_10.setBounds(10, 153, 89, 68);
		contentPane.add(btn_10);

		btn_11 = new JButton("");
		btn_11.setBounds(119, 153, 89, 68);
		contentPane.add(btn_11);

		btn_12 = new JButton("");
		btn_12.setBounds(222, 153, 89, 68);
		contentPane.add(btn_12);

		btn_20 = new JButton("");
		btn_20.setBounds(10, 232, 89, 68);
		contentPane.add(btn_20);

		btn_21 = new JButton("");
		btn_21.setBounds(119, 232, 89, 68);
		contentPane.add(btn_21);

		btn_22 = new JButton("");
		btn_22.setBounds(222, 232, 89, 68);
		contentPane.add(btn_22);

		JLabel lblNewLabel = new JLabel("Tres en raya");
		lblNewLabel.setFont(new Font("Gill Sans MT", Font.PLAIN, 17));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(59, 35, 210, 28);
		contentPane.add(lblNewLabel);
	}

	/**
	 * Getter del btn_00
	 * 
	 * @return retorna el btn_00
	 */
	public static JButton getBtn_00() {
		return btn_00;
	}

	/**
	 * Getter del btn_01
	 * 
	 * @return retorna el btn_01
	 */
	public static JButton getBtn_01() {
		return btn_01;
	}

	/**
	 * Getter del btn_02
	 * 
	 * @return retorna el btn_02
	 */
	public static JButton getBtn_02() {
		return btn_02;
	}

	/**
	 * Getter del btn_10
	 * 
	 * @return retorna el btn_10
	 */
	public static JButton getBtn_10() {
		return btn_10;
	}

	/**
	 * Getter del btn_11
	 * 
	 * @return retorna el btn_11
	 */
	public static JButton getBtn_11() {
		return btn_11;
	}

	/**
	 * Getter del btn_12
	 * 
	 * @return retorna el btn_12
	 */
	public static JButton getBtn_12() {
		return btn_12;
	}

	/**
	 * Getter del btn_20
	 * 
	 * @return retorna el btn_20
	 */
	public static JButton getBtn_20() {
		return btn_20;
	}

	/**
	 * Getter del btn_21
	 * 
	 * @return retorna el btn_21
	 */
	public static JButton getBtn_21() {
		return btn_21;
	}

	/**
	 * Getter del btn_22
	 * 
	 * @return retorna el btn_22
	 */
	public static JButton getBtn_22() {
		return btn_22;
	}

	/**
	 * Getter del turno de juego
	 * 
	 * @return retorna quien tiene el turno de juego
	 */
	public static String getTurno() {
		return turno;
	}

	/**
	 * Setter del turno de juego
	 * 
	 * @param turno Recibe quien tiene el turno de juego
	 */
	public static void setTurno(String turno) {
		Cliente.turno = turno;
	}
}
