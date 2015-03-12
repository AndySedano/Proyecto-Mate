import java.util.ArrayList;
import java.util.HashMap;

public class Estado {
	int id;
	public boolean esFinal;
	public boolean esInicial;
	public ArrayList<Integer> subEstados;
	
	private HashMap<Character, Estado> tabla;

	public Estado(int id, ArrayList<Character> alfabeto){
		this.id = id;
		subEstados = new ArrayList<Integer>();
		
		crearLlaves(alfabeto);
	}

	public Estado(int id, ArrayList<Integer> subEds, ArrayList<Character> alfabeto){
		this.id = id;
		subEstados = subEds;
		
		crearLlaves(alfabeto);
	}

	public void agregarSubEstado(int n){
		subEstados.add(n);
	}

	public ArrayList<Integer> getSubEstados(){
		return subEstados;
	}
	
	private void crearLlaves(ArrayList<Character> alfabeto){
		tabla = new HashMap<Character, Estado>();
		for(Character o : alfabeto){
			tabla.put(o,null);
		}
	}
	
	private void insertarEstado(Character u, Estado e){
		tabla.put(u,e);
		//si la llave u a existe entonces reemplaza la vie en rose
	}
	
	public boolean comparar(Estado otro){
		if(otro.subEstados.size() == this.subEstados.size()){
			for(Integer uli : otro.subEstados){
				if( ! this.subEstados.contains( uli )){
					return false;
				}
			}
			
			return true;//true si iguales
			
		}else{
			return false;
		}
	}

	public void imprimir(){
		System.out.println("El estado " + id + "se conecta:");
		for (Character c: tabla.keySet()){
			System.out.println("Con " + c + "con:");
			for(int i : tabla.get(c).getSubEstados()){
				System.out.print(i + ", ");
			} 
        }
	}
}