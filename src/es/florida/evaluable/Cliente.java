package es.florida.evaluable;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Cliente extends JFrame {

	private JPanel contentPane;
	static JTable table;
	static JButton btn_00, btn_01, btn_02, btn_10, btn_11, btn_12, btn_20, btn_21, btn_22;
	static int fila, columna;
	static String valor = "X";
	static String turno;
	static ActionListener actionListenerButton_00, actionListenerButton_01, actionListenerButton_02,
			actionListenerButton_10, actionListenerButton_11, actionListenerButton_12, actionListenerButton_20,
			actionListenerButton_21, actionListenerButton_22;

	String[] columnas = { "1", "2", "3" };
	static String[][] tablero = { { "", "", "" }, { "", "", "" }, { "", "", "" } };

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

			System.out.println("SERVIDOR>>> Posiciones enviadas a la maquina: Fila -> " + fila + " // Columna -> "
					+ columna + " // Valor -> " + valor + " // Turno -> " + getTurno());
			
			recibirObjeto(conexion);

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void recibirObjeto(Socket conexion) throws ClassNotFoundException, IOException {

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

	}

	public static void maquina(String boton, String valor, int fila, int columna) {
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
		table.setVisible(false);
		table.setVisible(true);
	}

	public static void jugada(int fila, int columna, String valor, Socket conexion) {

		if (tablero[fila][columna].equals("")) {
			tablero[fila][columna] = valor;
			table.setVisible(false);
			table.setVisible(true);
			enviarPosiciones(conexion, fila, columna, valor);
		} else {
			JOptionPane.showMessageDialog(new JFrame(), "Elige otra casilla para realizar tu jugada", "",
					JOptionPane.INFORMATION_MESSAGE);
		}

	}

	public static void jugador(Socket conexion) throws IOException {
		ActionListener actionListenerButton_00 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getBtn_00().setText(valor);
				jugada(0, 0, valor, conexion);
			}
		};

		ActionListener actionListenerButton_01 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getBtn_01().setText(valor);
				jugada(0, 1, valor, conexion);
			}
		};

		ActionListener actionListenerButton_02 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getBtn_02().setText(valor);
				jugada(0, 2, valor, conexion);
			}
		};

		ActionListener actionListenerButton_10 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getBtn_10().setText(valor);
				jugada(1, 0, valor, conexion);
			}
		};

		ActionListener actionListenerButton_11 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getBtn_11().setText(valor);
				jugada(1, 1, valor, conexion);
			}
		};

		ActionListener actionListenerButton_12 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getBtn_12().setText(valor);
				jugada(1, 2, valor, conexion);
			}
		};

		ActionListener actionListenerButton_20 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getBtn_20().setText(valor);
				jugada(2, 0, valor, conexion);
			}
		};

		ActionListener actionListenerButton_21 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getBtn_21().setText(valor);
				jugada(2, 1, valor, conexion);
			}
		};

		ActionListener actionListenerButton_22 = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getBtn_22().setText(valor);
				jugada(2, 2, valor, conexion);
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

	@SuppressWarnings("unused")
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
		int numero = Integer.parseInt(numStr);

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

			recibirObjeto(conexion);

		}

	}

	public Cliente() {
		// el metodo constructor contendrá la interfaz
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 327, 419);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		btn_00 = new JButton("");
		btn_00.setBounds(10, 35, 89, 68);
		contentPane.add(btn_00);

		btn_01 = new JButton("");
		btn_01.setBounds(109, 35, 89, 68);
		contentPane.add(btn_01);

		btn_02 = new JButton("");
		btn_02.setBounds(208, 35, 89, 68);
		contentPane.add(btn_02);

		btn_10 = new JButton("");
		btn_10.setBounds(10, 114, 89, 68);
		contentPane.add(btn_10);

		btn_11 = new JButton("");
		btn_11.setBounds(109, 114, 89, 68);
		contentPane.add(btn_11);

		btn_12 = new JButton("");
		btn_12.setBounds(208, 114, 89, 68);
		contentPane.add(btn_12);

		btn_20 = new JButton("");
		btn_20.setBounds(10, 193, 89, 68);
		contentPane.add(btn_20);

		btn_21 = new JButton("");
		btn_21.setBounds(109, 193, 89, 68);
		contentPane.add(btn_21);

		btn_22 = new JButton("");
		btn_22.setBounds(208, 193, 89, 68);
		contentPane.add(btn_22);

		table = new JTable(tablero, columnas);
		table.setBounds(76, 289, 152, 48);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		contentPane.add(table);
	}

	public static JButton getBtn_00() {
		return btn_00;
	}

	public static JButton getBtn_01() {
		return btn_01;
	}

	public static JButton getBtn_02() {
		return btn_02;
	}

	public static JButton getBtn_10() {
		return btn_10;
	}

	public static JButton getBtn_11() {
		return btn_11;
	}

	public static JButton getBtn_12() {
		return btn_12;
	}

	public static JButton getBtn_20() {
		return btn_20;
	}

	public static JButton getBtn_21() {
		return btn_21;
	}

	public static JButton getBtn_22() {
		return btn_22;
	}

	public static String getTurno() {
		return turno;
	}

	public static void setTurno(String turno) {
		Cliente.turno = turno;
	}

}
