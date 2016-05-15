package grafos.arvore;

import java.util.ArrayList;
import java.util.List;

public class Fila {
	
	private List<No> fila;
	
	public Fila(){
		this.fila = new ArrayList<>();
	}
	
	public void adiciona( No no){
		this.fila.add(no);
	}
	
	public No remove(){
		if( this.fila.size() > 0)
			return this.fila.remove(0);
		return null;
	}

}
