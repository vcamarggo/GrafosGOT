package grafos.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import grafos.arvore.Cor;
import grafos.arvore.No;

/**
 * @author V.Camargo
 * 
 * @Date 13 de mai de 2016
 */

public class Principal {
	// Hash de string e inteiro para armazenar nome e �ndice na matriz
	private static Map<String, Integer> nomesCSV = new HashMap<String, Integer>();

	private static List<String> nomes = new ArrayList<String>();

	private static List<No> listaAdjacencia;

	// Matriz de rela��o dos personagens
	private static int[][] matrizAdjacencia = new int[109][109]; // numero de
	// pessoas
	// diferentes
	// na lista

	public static void main(String[] args) {

		int enderecoElementoMatriz = 0;
		BufferedReader br = null;
		String linha = "";
		String csvDivisor = ",";

		try {
			// O(E)
			br = new BufferedReader(new FileReader(Principal.class.getResource("stormofswords.csv").getPath()));
			while ((linha = br.readLine()) != null) {

				// le a primeira parte (target) e adiciona em uma hash com nome
				// e posicao na matriz
				String personagem1 = linha.split(csvDivisor)[0];
				/*if (personagem1.equals("Source")) {
					continue;
				}*/
				
				//Se n�o houver chave com nome do personagem, ele ainda nao foi inserido na lista e na hash
				if (!nomesCSV.containsKey(personagem1) && !personagem1.equals("Source")) {
					nomesCSV.put(personagem1, enderecoElementoMatriz);
					nomes.add(personagem1);
					enderecoElementoMatriz++;

				}

				// le a segunda parte (target) e adiciona em uma hash com nome e
				// posicao na matriz
				String personagem2 = linha.split(csvDivisor)[1];
				if (!nomesCSV.containsKey(personagem2) && !personagem2.equals("Target")) {
					nomesCSV.put(personagem2, enderecoElementoMatriz);
					nomes.add(personagem2);
					enderecoElementoMatriz++;
					//System.out.println(personagem2);
				}

				// monta a matriz de adjacencia. Pega o �ndice dos personagens
				// no hashMap e essa posi��o da matriz receber� 1 que significa
				// que h� rela��o entre os personagens
				
				// O( V² )
				if (enderecoElementoMatriz > 0) {
					matrizAdjacencia[nomesCSV.get(personagem2)][nomesCSV.get(personagem1)] = 1;
					matrizAdjacencia[nomesCSV.get(personagem1)][nomesCSV.get(personagem2)] = 1;
				}

			}

			inicializaNos(matrizAdjacencia);
			listaAdjacencia = montaListaAdjacencia(matrizAdjacencia);
			// printaListaAdjacenciaNome(No.todosNos);
			// printaMatrizAdjacencia(matrizAdjacencia);

			System.out.println("Nome 1: ");
			Scanner scan = new Scanner(System.in);
			String pers = scan.nextLine();
			System.out.println("Nome 2: ");
			String pers2 = scan.nextLine();
			
			System.out.println("A dist�ncia �: "
					+ listaAdjacencia.get(nomesCSV.get(pers)).buscaLargura(nomesCSV.get(pers2)));
			
			// Linha de distancia entre 2 nomes
			/*System.out.println("A dist�ncia �: "
					+ listaAdjacencia.get(nomesCSV.get("Illyrio")).buscaLargura(nomesCSV.get("Jon")));*/

			No.buscaProfundidade();
			No.setTempo(0);
			No.reiniciaCores();
			No.todosNos.get(0).pontes(No.todosNos.get(0));
			No.setTempo(0);
			No.reiniciaPais();
			No.reiniciaCores();
			No.todosNos.get(0).pontosArticulacao(No.todosNos.get(0));
			
			

			// printaTempoDescobertaEFinal();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * O(V²)
	 * 
	 * @param matrizAdjacencia
	 */
	public static void printaMatrizAdjacencia(int matrizAdjacencia[][]) {
		for (int i = 0; i < matrizAdjacencia.length; i++) {
			for (int j = 0; j < matrizAdjacencia[i].length; j++) {
				System.out.print(matrizAdjacencia[i][j]);
			}
			System.out.println("");
		}
	}

	/**
	 * O(V²)
	 * 
	 * @param matrizAdjacencia
	 * @return
	 */

	public static List<No> montaListaAdjacencia(int matrizAdjacencia[][]) {
		List<No> listaAdjacencia = new ArrayList<No>();
		for (int i = 0; i < matrizAdjacencia.length; i++) {
			if (i < nomes.size()) {
				No n = No.todosNos.get(i);
				n.setCor(Cor.BRANCO);
				listaAdjacencia.add(n);
				for (int j = 0; j < matrizAdjacencia[i].length; j++) {
					if (matrizAdjacencia[i][j] > 0) {
						listaAdjacencia.get(i).addFilho(No.todosNos.get(j));
					}
				}
			}
		}
		return listaAdjacencia;
	}

	/**
	 * O( V )
	 * 
	 * @param matrizAdjacencia
	 */
	
	// Para cada personagem da lista 'nomes', cria-se um n�
	public static void inicializaNos(int matrizAdjacencia[][]) {
		for (int i = 0; i < nomes.size(); i++) {
			No.todosNos.add(new No(i, nomes.get(i)));
		}
	}

	/**
	 * O(V²)
	 * 
	 * @param list
	 */
	public static void printaListaAdjacencia(List<No> list) {
		for (int i = 0; i < nomes.size(); i++) {
			System.out.print(i);
			for (int j = 0; j < list.get(i).getAdjacentes().size(); j++) {
				System.out.print(" -> ");
				System.out.print(list.get(i).getAdjacentes().get(j).getId());
			}
			System.out.println();
		}
	}

	/**
	 * O(V²)
	 * 
	 * @param list
	 */
	public static void printaListaAdjacenciaNome(List<No> list) {
		for (int i = 0; i < nomes.size(); i++) {
			System.out.print(list.get(i).getNome());
			for (int j = 0; j < list.get(i).getAdjacentes().size(); j++) {
				System.out.print(" -> ");
				System.out.print(list.get(i).getAdjacentes().get(j).getNome());
			}
			System.out.println();
		}
	}

	/**
	 * O(V)
	 */

	private static void printaTempoDescobertaEFinal() {
		for (No n : No.todosNos) {
			System.out.println(n.getNome() + " - " + n.getTempoInicio() + "/" + n.getTempoTermino());
		}
	}

}
