import java.util.ArrayList;
public class Estado {
	int id;
	public boolean esFinal;
	public boolean esInicial;
	public ArrayList<Integer> estados; 

	public Estado(int id){
		this.id = id;
		estados = new ArrayList<Integer>();
	}

	public Estado(int id, ArrayList<Integer> e){
		this.id = id;
		estados = e;
	}

	public void agregarEstado(int n){
		estados.add(n);
	}

	public ArrayList<Integer> getEstados(){
		return estados;
	}
}