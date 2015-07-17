package br.com.alura.loja.modelo;

import com.thoughtworks.xstream.XStream;

public class Projeto {
	
	private String nome;
	private Long id;
	private int anoDeInicio;
	
	public Projeto(Long id, String nome, int anoDeInicio) {
		this.nome = nome;
		this.id = id;
		this.anoDeInicio = anoDeInicio;
	}
	
	public String getNome() {
		return nome;
	}
	public Long getId() {
		return id;
	}
	public int getAnoDeInicio() {
		return anoDeInicio;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String toXML() {
		return new XStream().toXML(this);
	}

}
