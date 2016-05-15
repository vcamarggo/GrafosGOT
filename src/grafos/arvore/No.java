package grafos.arvore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author V.Camargo
 * 
 * @Date 13 de mai de 2016
 */

public class No {
    public static List<No> todosNos = new ArrayList<No>();
    private String nome;
    private List<No> filhos;
    private Cor cor = Cor.BRANCO;
    private No pai;
    private int tempoInicio = 0;
    private int tempoTermino = 0;
    private int id;
    private int distancia;
    private int low;
    private static int tempo;

    public No(int id, String nome) {

	this.nome = nome;
	this.id = id;
	this.filhos = new ArrayList<No>();
    }

    public int getId() {
	return id;
    }

    public int getLow() {
	return low;
    }

    public void setLow(int low) {
	this.low = low;
    }
    public void addFilho(No no) {
	if (filhos == null) {
	    filhos = new ArrayList<No>();
	}
	filhos.add(no);
    }

    public String getNome() {
	return nome;
    }

    public List<No> getFilhos() {
	return filhos;
    }

    public Cor getCor() {
	return cor;
    }

    public void setCor(Cor cor) {
	this.cor = cor;
    }

    public No getPai() {
	return pai;
    }

    public void setPai(No pai) {
	this.pai = pai;
    }

    public int getTempoInicio() {
	return tempoInicio;
    }

    public void setTempoInicio(int tempoInicio) {
	this.tempoInicio = tempoInicio;
    }

    public int getTempoTermino() {
	return tempoTermino;
    }

    public void setTempoTermino(int tempoTermino) {
	this.tempoTermino = tempoTermino;
    }

    public int getDistancia() {
	return distancia;
    }

    public void setDistancia(int distancia) {
	this.distancia = distancia;
    }

    public int buscaLargura(int destino) {
	boolean achou = false;
	int distancia = -1;
	Fila fila = new Fila();

	this.setCor(Cor.CINZA);
	this.setDistancia(0);
	fila.adiciona(this);

	No n = fila.remove();
	while (n != null && !achou) {

	    for (No no : n.getFilhos()) {

		if (no.cor == Cor.BRANCO) {
		    no.setCor(Cor.CINZA);
		    no.setDistancia(n.getDistancia() + 1);
		    fila.adiciona(no);
		    if (no.id == destino) {
			achou = true;
			distancia = no.getDistancia();
		    }
		}
	    }

	    n.setCor(Cor.PRETO);
	    n = fila.remove();

	}

	return achou ? distancia : n.distancia;
    }

    public static void buscaProfundidade() {
	for (No n : todosNos) {
	    n.setCor(Cor.BRANCO);
	    n.setPai(null);
	}
	tempo = 0;
	for (No n : todosNos) {
	    if (n.getCor() == Cor.BRANCO) {
		distanciaVisita(n);
	    }
	}
    }

    private static void distanciaVisita(No n) {
	tempo++;
	n.setCor(Cor.CINZA);
	n.setTempoInicio(tempo);
	for (No no : n.getFilhos()) {
	    if (no.getCor() == Cor.BRANCO) {
		no.pai = n;
		distanciaVisita(no);
	    }
	}
	n.setCor(Cor.PRETO);
	tempo++;
	n.setTempoTermino(tempo);
    }

    public void pontes(No n) {
	tempo++;
	n.setCor(Cor.CINZA);
	n.setTempoInicio(tempo);
	n.setLow(n.getTempoInicio());
	for (No no : n.getFilhos()) {

	    if (no.getCor() == Cor.BRANCO) {
		no.setPai(n);
		pontes(no);
		n.setLow(Math.min(n.getLow(), no.getLow()));
		if (no.getLow() > n.getTempoInicio()) {
		    System.out.println("Entre " + no.getNome() + " e " + n.getNome() + " há uma ponte ");
		}
	    } else {
		if (no != n.getPai() && no.getTempoInicio() < n.getTempoInicio()) {
		    n.setLow(Math.min(n.getLow(), no.getTempoInicio()));
		}
	    }

	}
	n.setCor(Cor.PRETO);
	tempo++;
	n.setTempoTermino(tempo);

    }

    public void pontosArticulacao(No n) {
	tempo++;
	n.setCor(Cor.CINZA);
	n.setTempoInicio(tempo);
	n.setLow(n.getTempoInicio());
	for (No no : n.getFilhos()) {
	    if (no.getCor() == Cor.BRANCO) {
		no.setPai(n);
		pontosArticulacao(no);
		if (n.getPai() == null) {
		    System.out.println(n.getNome() + " é raiz");
		    if (no == n.getFilhos().get(1)) {
			System.out.println(n.getNome() + " é um ponto de articulacao ");
		    }
		} else {
		    n.setLow(Math.min(n.getLow(), no.getLow()));
		    if (no.getLow() >= n.getTempoInicio()) {
			System.out.println(n.getNome() + " é um ponto de articulacao ");
		    }
		}
	    } else {
		if (no != n.getPai() && no.getTempoInicio() < n.getTempoInicio()) {
		    n.setLow(Math.min(n.getLow(), no.getTempoInicio()));
		}
	    }
	}
	n.setCor(Cor.PRETO);
	tempo++;
	n.setTempoTermino(tempo);
    }

    public static void reiniciaCores() {
	for (No n : todosNos) {
	    n.setCor(Cor.BRANCO);
	}
    }

    public static void reiniciaPais() {
	for (No n : todosNos) {
	    n.setPai(null);
	}
    }

    public static void setTempo(int tempo) {
        No.tempo = tempo;
    }

}
