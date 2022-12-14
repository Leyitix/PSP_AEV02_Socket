package es.florida.evaluable;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Jugada implements Serializable {
	int columna, fila;
	String valor;

	public Jugada() {
	}

	public Jugada(int fila, int columna, String valor) {
		this.fila = fila;
		this.columna = columna;
		this.valor = valor;
	}

	public int getColumna() {
		return columna;
	}

	public void setColumna(int columna) {
		this.columna = columna;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public int getFila() {
		return fila;
	}

	public void setFila(int fila) {
		this.fila = fila;
	}
}
