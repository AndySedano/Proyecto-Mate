/**
*Clase estado del Proyecto final: Primera Parte
*Esta clase almacena los estados del AFD, el id es el número del estado
*y la ArrayList "subEstados" almacena los sub-estados del AFND que forman
*el conjunto de estados que forma al estado actual.
*
*@author: Andrés Eugenio Sedano Casanova A00399842
*@author: Ulises Torner Campuzano A01333456
*@version: 17/03/2015/A
*/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Estado {
	//Campos de la clase
	public int id;
	private boolean esFinal;
	private boolean esInicial;
	private ArrayList<Integer> subEstados;
	private HashMap<Character, Estado> tabla;

	/**
	*Constructor de un Estado sin sub-estados definidos
	*éste es usado principalmente para q0, dado que los demás
	*estados se crean a partir de un conjunto de estados bien definido
	*@param: id - El nombre del Estado
	*@param: alfabeto - El alfabeto del Lenguaje que procesa el autómata
	*/
	public Estado(int id, ArrayList<Character> alfabeto){
		this.id = id;
		subEstados = new ArrayList<Integer>();
		crearLlaves(alfabeto);
	}
	
	/**
	*Constructor de un Estado cuando se conocen los sub-estados que conforman el conjunto
	*@param: subEds - se recibe una lista de enteros de los sub-estados en el conjunto
	*/
	public Estado(int id, ArrayList<Integer> subEds, ArrayList<Character> alfabeto){
		this.id = id;
		subEstados = subEds;
		crearLlaves(alfabeto);
	}

	//Métodos de la Clase  *************************
	
	/**
	*Getter de la lista de sub-estados
	*@return: ArrayList de Integers
	*/
	public ArrayList<Integer> getSubEstados(){
		return subEstados;
	}//Fin de getter
	
	/*
	*Método que coloca el valor final
	*/
	public void setFinal(boolean b){
		esFinal=b;
	}
	
	/*
	*Método que coloca el valor inicial
	*/
	public void setInicial(boolean b){
		esInicial=b;
	}
	
	/*
	*Getter de esFinal
	*@return: true si el estado es final
	*/
	public boolean seraFinal(){
		return esFinal;
	}
	
	/*
	*Getter de esInicial
	*@return: true si el estado es inicial
	*/
	public boolean seraInicial(){
		return esInicial;
	}
	
	/**
	*Método que crea las llaves(K) dentro del mapa hash por cada uno de los caracteres del
	*alfabeto, los valores asociados a las llaves se dejan en Null y en caso de que exista
	*una transición con dicho caracter se debe actualizar con el método insertarEstado()
	*@param: alfabeto - el alfabeto del Lenguaje que procesa en autómata
	*/
	private void crearLlaves(ArrayList<Character> alfabeto){
		tabla = new HashMap<Character, Estado>();
		for(Character o : alfabeto){
			tabla.put(o,null);
		}
	}//Fin de crearLLaves
	
	/**
	*Método que agrega un sub-estado al conjunto
	*@param: n - el id (número de estado) que posee este sub-estado en el AFND
	*/
	public void agregarSubEstado(int n){
		Integer temp = new Integer(n);
		if( ! subEstados.contains(temp) ){
			subEstados.add(temp);
		}
	}//Fin de agregarSubEstado
	
	/**
	*Método que agrega un nuevo sub-estado al conjunto
	*@param: u - el caracter con el cual se procesa la transición hacia el nuevo estado
	*@param: e - el estado(AFD) al cual se llega con la transición con "u"
	*/
	public void insertarEstado(Character u, Estado e){
		tabla.put(u,e);
	}//Fin de insertatEstado
	
	/**
	*Método comparar, revisa si un estado es igual a otro
	*para esta implementación "ser iguales" es si dos estados poseen los mismos sub-estados
	*@param: otro - el estado con el cual se compara
	*@return: true - si los estados son iguales
	*/
	public boolean comparar(Estado otro){
		if(otro.subEstados.size() == this.subEstados.size()){
			for(Integer uli : otro.subEstados){
				if( ! this.subEstados.contains( uli )){
					return false;
				}
			}
			return true;
		}else{
			return false;
		}
	}//Fin del método comparar

	/**
	*Método para obtener los estados a los que llegas con cada caracter
	*@return: un arreglo bidimensional donde arriba van los caracteres y abajo los id de los estados
	*/
	public String[] getTransiciones(){
		Set<Character> llaves = tabla.keySet();
		String[] tablis = new String[ llaves.size() ];
		
		int i=0;
		for( Character c : llaves ){
			if(tabla.get(c) != null){
				Estado e = (Estado)tabla.get(c);
				tablis[i] = "q" + Integer.toString(e.id);
			}else{
				tablis[i] = "/";
			}
			i++;
		}
		return tablis;
	}//Fin de getTransiciones
	
	/**
	*Método que imprime las conexiones entre estados
	*Este es un método temporal de uso exclusivo para los programadores.
	*/
	public void imprimir(){
		System.out.println("El estado " + id + "se conecta:");
		for (Character c: tabla.keySet()){
			System.out.println("Con " + c + "con:");
			for(int i : tabla.get(c).getSubEstados()){
				System.out.print(i + ", ");
			} 
        }
	}//Fin del método imprimir
	
}//Fin de la clase Estado