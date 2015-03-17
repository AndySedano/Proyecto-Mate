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

//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>INICIO DE LA TRANSFORMACIÓN AFND->AFD

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
			
			//Checar la función de transición por cada uno de los sub-estados de temp
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
		
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>FIN DE LA TRANSFORMACIÓN AFND->AFD

		//>>>>>>>>>>>>>>>>>Falta agregar un método que reciba los estados y regrese o imprima la tabla del AFD
		String[][] tabla = tablaAFD();
		for(){
			for(){
				System.out.print(tabla[i][j] + " ");
			}
			System.out.print("\n");
		}
		//>>>>>>>>>>>>>>>>>Falta un método que minimice al AFD
		
	}//Fin del main
	
	//Métodos de la Clase
	
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







//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>INICIO
	/**
	*Metodo que regresa la tabla de AFD a partir de los estados
	*@return: arreglo de strings
	*/
	public static String[][] tablaAFD(){
		String[][] tablita = new String[estadosAFD.size()][alfabeto.size()];
		int i,j;
		i = 1;
		for(Character chachacha : alfabeto ){
			tablita[0][i] = chachacha;
			i++;
		}
		i = 1;//reset el valor del índice
		for( Estado est : estadosAFD){
			tablita[i][0] = est.id;
			i++;
		}
		
	/*
	Hay que ordenar los estados por id en la lista estadoAFD para que los imprima bonito
	Si no en la tabla va a estar todo patatas :(

	Ejemplo:
				a	b	c	d
		q5		q2	/	/	/
		q2		q1	q2	q2	/
		q0		q0	q1	q2	q3
		q3		q4	q5	/	/
		q4		/	q0	/	/
		q1		/	q2	q1	q3
		
	*/
	
		//Esta parte está bien complicada :(
		for(i=1; i<estadosAFD.size()-1; i++){
			for(int j=1; j<alfabeto.size()-1; j++){
				if( buscarEstado(tablita[i][0]) != null ){//Este if es como de seguridad añadida, pero en la vida real siempre debería existir el estado

					buscarEstado(tablita[i][0]).getTransiciones();
					
					tablita[i][j] = 
				}
			}
		}
	}
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>FIN		
	
	
	
	
	
	
	
	
	
	
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