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
    private List<No> adjacentes;
    private Cor cor = Cor.BRANCO;
    private No pai;
    private int tempoInicio = 0;
    private int tempoTermino = 0;
    private int id;
    private int distancia;
    private int low;
    private static int tempo;
    private List<No> filhos;

    public No(int id, String nome) {

        this.nome = nome;
        this.id = id;
        this.adjacentes = new ArrayList<No>();
        this.filhos = new ArrayList<No>();
    }

    public List<No> getFilhos() {
        return filhos;
    }

    public void setFilhos(List<No> filhos) {
        this.filhos = filhos;
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
        if (adjacentes == null) {
            adjacentes = new ArrayList<No>();
        }
        adjacentes.add(no);
    }

    public String getNome() {
        return nome;
    }

    public List<No> getAdjacentes() {
        return adjacentes;
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

    /**
     * O( V + E )
     * @param destino
     * @return 
     */
    public int buscaLargura(int destino) {
        boolean achou = false;
        int distancia = -1;
        Fila fila = new Fila();

        this.setCor(Cor.CINZA);
        this.setDistancia(0);
        fila.adiciona(this);

        No n = fila.remove();
        //O(V)
        while (n != null && !achou) {

            //E
            for (No no : n.getAdjacentes()) {

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

    
    /**
     * O( V + E )
     */
    public static void buscaProfundidade() {
        //O(V)
        for (No n : todosNos) {
            n.setCor(Cor.BRANCO);
            n.setPai(null);
        }
        tempo = 0;
        
        //O ( V )
        for (No n : todosNos) {
            if (n.getCor() == Cor.BRANCO) {
                //O( E) 
                distanciaVisita(n);
            }
        }
    }

    /**
     * O(E)
     * @param n 
     */
    private static void distanciaVisita(No n) {
        tempo++;
        n.setCor(Cor.CINZA);
        n.setTempoInicio(tempo);
        //O(E)
        for (No no : n.getAdjacentes()) {
            if (no.getCor() == Cor.BRANCO) {
                no.pai = n;
                n.getFilhos().add(no);
                distanciaVisita(no);
            }
        }
        n.setCor(Cor.PRETO);
        tempo++;
        n.setTempoTermino(tempo);
    }

    /**
     * O( V + E )
     * @param n 
     */
    public void pontes(No n) {
        tempo++;
        n.setCor(Cor.CINZA);
        n.setTempoInicio(tempo);
        n.setLow(n.getTempoInicio());
        
        //O(E)
        for (No no : n.getAdjacentes()) {

            if (no.getCor() == Cor.BRANCO) {
                no.setPai(n);
                pontes(no);
                n.setLow(Math.min(n.getLow(), no.getLow()));
                if (no.getLow() > n.getTempoInicio()) {
                    System.out.println("Entre " + no.getNome() + " e " + n.getNome() + " h� uma ponte ");
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

    //O( V + E )
    public void pontosArticulacao(No n) {
        tempo++;
        n.setCor(Cor.CINZA);
        n.setTempoInicio(tempo);
        n.setLow(n.getTempoInicio());
        //O(E)
        for (No no : n.getAdjacentes()) {
            if (no.getCor() == Cor.BRANCO) {
                no.setPai(n);
                pontosArticulacao(no);
                if (n.getPai() == null) {
                    System.out.println(n.getNome() + " � raiz");
                    if (n.getFilhos().size() >= 2) {
                        System.out.println(n.getNome() + " � um ponto de articulacao propriedade 1");
                    }
                } else {
                    n.setLow(Math.min(n.getLow(), no.getLow()));
                    if (no.getLow() >= n.getTempoInicio()) {
                        System.out.println(n.getNome() + " � um ponto de articulacao propriedade 2 ");
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

    
    /**
     * O ( V )
     */
    public static void reiniciaCores() {
        for (No n : todosNos) {
            n.setCor(Cor.BRANCO);
        }
    }

    /**
     * O( V ) 
     */
    public static void reiniciaPais() {
        for (No n : todosNos) {
            n.setPai(null);
        }
    }

    public static void setTempo(int tempo) {
        No.tempo = tempo;
    }

}
