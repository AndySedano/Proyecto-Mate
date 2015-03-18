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

		System.out.println("Tamano del alfabeto: " + alfabeto.size());
		System.out.println(Arrays.deepToString(tablaEstados));

		Estado q0 = new Estado(0, alfabeto);
		q0.agregarSubEstado(0);
		
		pila = new LinkedList<Estado>();
		pila.add(q0);
		int contador = 1;
		
		while(pila.size() != 0 ){
			System.out.println("En la pila hay " + pila.size());
			Estado temp = pila.pop();
			System.out.println("De la pila se saco el estado " + temp.id);
			
			//Checar la función de transición por cada uno de los sub-estados de temp
			for(Character c : alfabeto){
				Estado link = calcularConexion(c,temp);
				System.out.println("El estado prueba en " + c + " tiene como subestados:");

				for(Integer i : link.getSubEstados()){
					System.out.print(i + ", ");
				}
				System.out.println("");

				if (link.id == -1){
					link.id = contador;
					contador++;
					//temp.insertarEstado(c,link);//esto va despues de revisar siexiste o n
					pila.addLast(link); 
				}
				///EEEEESSSSTTTTOOOO EEESSS IIIIMMMMPPPOOORRRTTAAAANNNTTEEEE
				//pase lo que pase le agrega una conexion con el estado, no solo si es nuevo el estado
				temp.insertarEstado(c,link);//esto va despues de revisar siexiste o no      


			}	
			estadosAFD.add(temp);

		}


		for(Estado e : estadosAFD){
			System.out.println("El estado " + e.id + " tiene:");
			for(Integer i : e.getSubEstados()){
				System.out.print(i+", ");
			}
			System.out.println("");
		}
		
		imprimir();
		
		String[][] tablaAFDimprimir = new String[estadosAFD.size()+1][alfabeto.size()+1];
		int i=1;
		int j=1;
		for(Estado e : estadosAFD){
			if(e.seraInicial() && e.seraFinal()){
				tablaAFDimprimir[0][i] = "->*q" + e.id;
			}else if(e.seraInicial()){
				tablaAFDimprimir[0][i] = "->q" + e.id;
			}else if(e.seraFinal()){
				tablaAFDimprimir[0][i] = "*q" + e.id;
			}else{
				tablaAFDimprimir[0][i] = "q" + e.id;
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
			tablaAFDimprimir[i][0] = c.toString();
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
	*/
	public static void imprimir(){
		System.out.println(estadosAFD.size());
		for (Estado e : estadosAFD){
			e.imprimir();
		}
	}//Fin de imprimir
	
}//Fin de la clase