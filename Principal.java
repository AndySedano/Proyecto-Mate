import java.util.Scanner;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

public class Principal {
	static ArrayList<Estado> estadosAFD = new ArrayList<Estado>();
	static ArrayList<Character> alfabeto = new ArrayList<Character>();//ArrayList del alfabeto
	static LinkedList<Integer>[][] tablaEstados;//tabla de estados


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

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>INICIO

		Estado q0 = new Estado(0, alfabeto);
		q0.agregarSubEstado(0);

		/*
		Según uli esto ya no va por que al agregarlo a la
		pila después se va a revisar todo y panes
		
		
		for(Character c : alfabeto){
			Estado e = calcularConexion(c,q0);
			q0.insertarEstado(c,e);
		}
		*/
		
		Stack<Estado> pila = new Stack<Estado>();
		pila.push(q0);
		
		while(! pila.empty()){
			
			Estado temp = pila.pop();
			
			//Checar la función de transición por cada uno de los subesttados de temp
			for(Character c : alfabeto){
				Estado link = calcularConexion(c,temp);
				temp.insertarEstado(c,link);
			}
			
			//Esto aún no está listo en lugar de contains debe revisar si
			//tiene un estado con los mismos subestados que el otro
			//creo también faltan conexiones entre estados
			if( !(pila.contains(temp) && estadosAFD.contains(temp)) ){
				
				estadosAFD.add(temp);
			}
		}
		
		imprimir();
		
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>FIN

		//Falta agregar un método que reciba los estados y regrese o imprima la tabla del AFD
	
		//Falta un método que minimice al AFD
		
	}//Fin del main

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
	}

	public static void imprimir(){
		for (Estado e : estadosAFD){
			e.imprimir();
		}
	}
}//Fin de la clase