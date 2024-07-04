package com.magazzino.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Articolo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nome;
	private int dataDiScadenza;
	private double prezzo;
	private int qualita;
	private String categoria;

	public String getNome() {
		return nome;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getDataDiScadenza() {
		return dataDiScadenza;
	}

	public void setDataDiScadenza(int dataDiScadenza) {
		this.dataDiScadenza = dataDiScadenza;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}

	public int getQualita() {
		return qualita;
	}

	public void setQualita(int qualita) {
		this.qualita = qualita;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Integer getId() {
		return id;
	}

}
