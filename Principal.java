/**
*Clase principal del Proyecto final: Primera Parte
*@author: Andrés Eugenio Sedano Casanova A00399842
*@author: Ulises Torner Campuzano A01333456
*@version: 18/03/2015/A
*/

//Imports
import java.util.Scanner;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Principal {
	//Atributos públicos de la clase
	static ArrayList<Estado> estadosAFD = new ArrayList<Estado>();//Estados del AFD
	static ArrayList<Character> alfabeto = new ArrayList<Character>();//ArrayList del alfabeto
	static LinkedList<Integer>[][] tablaEstados;//tabla de estados del AFND
	static LinkedList<Estado> pila;

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

		for(int i = 0; i<nEstados;i++){
			for(int j=0; j<nCaracteres;j++ ){
				tablaEstados[i][j] = new LinkedList();
			}
		}

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
		q0.setInicial(true);
		
		pila = new LinkedList<Estado>();
		pila.add(q0);
		int contador = 1;
		
		while(pila.size() != 0 ){
			Estado temp = pila.pop();
			
			//Checar la función de transición por cada uno de los sub-estados de temp
			for(Character c : alfabeto){
				Estado link = calcularConexion(c,temp);
				if (link.id == -1){
					link.id = contador;
					contador++;
					pila.addLast(link); 
				}
				temp.insertarEstado(c,link);
			}
			
			//Revisa si el estado es final
			for(int uli=0; uli<estadosFinales.length; uli++){
				for( Integer i : temp.getSubEstados() ){
					if( i.intValue() == Character.getNumericValue(estadosFinales[uli].charAt(1))){
						temp.setFinal(true);
					}	
				}
			}
			estadosAFD.add(temp);
		}
		
		//Tabla AFD en formato String[][]
		String[][] tablaAFDimprimir = new String[estadosAFD.size()+1][alfabeto.size()+1];
		tablaAFDimprimir[0][0] = "";
		
		int i=1;
		for(Estado e : estadosAFD){
			if(e.seraInicial() && e.seraFinal()){
				tablaAFDimprimir[i][0] = "->*q" + e.id;
			}else if(e.seraInicial()){
				tablaAFDimprimir[i][0] = "->q" + e.id;
			}else if(e.seraFinal()){
				tablaAFDimprimir[i][0] = "*q" + e.id;
			}else{
				tablaAFDimprimir[i][0] = "q" + e.id;
			}
			
			String[] pan = e.getTransiciones();
			int j=1;
			while(j < alfabeto.size()+1){
				tablaAFDimprimir[i][j] = pan[j-1];
				j++;
			}
			i++;
		}
		i=1;
		for(Character c : alfabeto){
			tablaAFDimprimir[0][i] = c.toString();
			i++;
		}
		
		//Imprimir la Tabla del AFD en el terminal
		for(int u=0; u<=estadosAFD.size(); u++){
			for(int v=0; v<=alfabeto.size(); v++){
				System.out.print( tablaAFDimprimir[u][v] + "\t");
			}
			System.out.println("");
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

		int gil = 0;
		for (Estado eAFD : estadosAFD){//Por todos los estados en el afd
			if (eAFD.comparar(aux)){//Checa si alguno es igual a aux
				gil = 1;
				aux=eAFD;
			}
		}
		for (Estado eAFD : pila){//Por todos los estados en el afd
			if (eAFD.comparar(aux)){//Checa si alguno es igual a aux
				gil = 1;
				aux=eAFD;
			}
		}
		if(e.comparar(aux)){
			gil = 1;
			aux=e;
		}

		if (gil==0){
			Estado nuevoE = new Estado(-1, aux.getSubEstados() , alfabeto);
			return nuevoE;

		}

		return aux;

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
	*Sólo es para debugging
	*/
	public static void imprimir(){
		System.out.println(estadosAFD.size());
		for (Estado e : estadosAFD){
			e.imprimir();
		}
	}//Fin de imprimir
	
}//Fin de la clase