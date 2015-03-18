/**
*Clase principal del Proyecto final: Primera Parte
*@author: Andrés Eugenio Sedano Casanova A00399842
*@author: Ulises Torner Campuzano A01333456
*@version: 17/03/2015/A
*/

//Imports
import java.util.Scanner;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

public class Principal {
	//Atributos públicos de la clase
	static ArrayList<Estado> estadosAFD = new ArrayList<Estado>();//Estados del AFD
	static ArrayList<Character> alfabeto = new ArrayList<Character>();//ArrayList del alfabeto
	static LinkedList<Integer>[][] tablaEstados;//tabla de estados del AFND

	public static void main (String[] args){
		Scanner leer = new Scanner(System.in);

		String[] estados = leer.nextLine().split(",");//Lista de estados del automata
		String[] auxAlf = leer.nextLine().split(",");//alfabeto auxiliar para meterlo en un ArrayList
		
		//Para llenar el alfabeto
		for(String i : auxAlf){
			alfabeto.add(i.charAt(0));
		}

		int nEstados = estados.length;//numero de estados
		int nCaracteres = alfabeto.size();//numero de caracteres

		tablaEstados = new LinkedList[nEstados][nCaracteres];

		String estadoInicial = leer.nextLine();// El estado inicial
		String[] estadosFinales = leer.nextLine().split(",");//Los estados finales

		String[] aux;
		int estadoActual;
		int charActual;
		while(leer.hasNext()){
			aux = leer.nextLine().split(",");
			estadoActual = Character.getNumericValue(aux[0].charAt(1));
			charActual = alfabeto.indexOf(aux[1].charAt(0));
			int uli = Character.getNumericValue(aux[1].charAt(4));
			
			if(tablaEstados[estadoActual][charActual]==null){
				tablaEstados[estadoActual][charActual]=new LinkedList<Integer>();
			}
			
			tablaEstados[estadoActual][charActual].add(uli);
			for(int i=2; i<aux.length;i++){
				tablaEstados[estadoActual][charActual].add(Character.getNumericValue(aux[i].charAt(1)));
			}
		}

		Estado q0 = new Estado(0, alfabeto);
		q0.agregarSubEstado(0);
		
		Stack<Estado> pila = new Stack<Estado>();
		pila.push(q0);
		
		while(! pila.empty() ){
			Estado temp = pila.pop();
			
			//Checar la función de transición por cada uno de los sub-estados de temp
			for(Character c : alfabeto){
				Estado link = calcularConexion(c,temp);
				temp.insertarEstado(c,link);
			}
			
			//Esto aún no está listo en lugar de contains debe revisar si
			//tiene un estado con los mismos subestados que el otro
			//creo también faltan conexiones entre estados
			int gil=0;
			for (Estado eAFD : estadosAFD){//Por todos los estados en el afd
				if (eAFD.comparar(temp)){//Checa si alguno es igual a aux
					gil=-1;
				}
			}
			for (Estado eAFD : pila){//Por todos los estados en el afd
				if (eAFD.comparar(temp)){//Checa si alguno es igual a aux
					gil=-1;
				}
			}
			
			if( gil==0 ){
				estadosAFD.add(temp);
			}
		}
		
		imprimir();
		
		String[][] tablaAFDimprimir = new String[estadosAFD.size()+1][alfabeto.size()+1];
		int i=1;
		int j=1;
		for(Estado e : estadosAFD){
			if(e.seraInicial() && e.seraFinal()){
				tablaAFDimprimir[0][i] = "->*q" + e.id.toString();
			}else if(e.seraInicial()){
				tablaAFDimprimir[0][i] = "->q" + e.id.toString();
			}else if(e.seraFinal()){
				tablaAFDimprimir[0][i] = "*q" + e.id.toString();
			}else{
				tablaAFDimprimir[0][i] = "q" + e.id.toString();
			}
			i++;
			
			String[] pan = e.getTransiciones();
			for(String s : pan){
				tablaAFDimprimir[j][i] = s;
				j++;
			}
		}
		i=1;
		for(Character c : alfabeto){
			tablaAFDimprimir[i][0] = c;
			i++;
		}
	}//Fin del main
		
	/*
	*Método calcularConexion, usa la función de transición para calcular los sub-estados a los cuales se conecta un estado dado
	*@param: c - el caracter del alfabeto por el cual se da la transición
	*@param: e - el estado desde el cual se calculará la función de transición
	*@return: regresa un estado del AFD con sus sub-estados del cual se conecta "e" con la transición por medio de "c"
	*/
	public static Estado calcularConexion(Character c, Estado e){
		Estado aux = new Estado(-1, alfabeto);//Nuevo estado auxiliar para saber si el estado(con todo y subestados) ya existe en la tabla de estadosAFD

		for(int sub : e.getSubEstados()){//Para todos los subestados del estado
			for(int subEstado : tablaEstados[sub][alfabeto.indexOf(c)]){
				if (! aux.getSubEstados().contains(subEstado)){
					aux.agregarSubEstado(subEstado);
				}
			}
		}

		for (Estado eAFD : estadosAFD){//Por todos los estados en el afd
			if (eAFD.comparar(aux)){//Checa si alguno es igual a aux
				return eAFD;// si si regresa ese estado
			}
		}
		Estado nuevoE = new Estado((estadosAFD.size()-1), aux.getSubEstados() , alfabeto);
		return nuevoE;//si no regresa un nuevo estado con los subestados de aux
	}//Fin de calcularConexion
	
	/**
	*Método para buscar un Estado con base a su id (nombre)
	*@return: el estado con el id o null en caso de no existir
	*/
	public static Estado buscarEstado(int id){
		for(Estado estadito : estadosAFD){
			if(estadito.id == id){
				return estadito;
			}
		}
		return null;
	}
	
	/*
	*Método imprimir, manda a llamar a Estado.imprimir() por cada Estado del AFD
	*/
	public static void imprimir(){
		for (Estado e : estadosAFD){
			e.imprimir();
		}
	}//Fin de imprimir
	
}//Fin de la clase