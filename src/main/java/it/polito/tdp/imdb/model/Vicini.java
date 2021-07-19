package it.polito.tdp.imdb.model;

public class Vicini implements Comparable <Vicini> {

	private Director d;
	private Double peso;
	public Vicini(Director d, Double peso) {
		super();
		this.d = d;
		this.peso = peso;
	}
	public Director getD() {
		return d;
	}
	public void setD(Director d) {
		this.d = d;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	@Override
	public int compareTo(Vicini p) {
		return - (this.peso.compareTo(p.peso));
		
	}
	
	
	
	
}
