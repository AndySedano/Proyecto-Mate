import java.util.Scanner;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class proyecto {
	public static void main (String[] args){
		Scanner leer = new Scanner(System.in);

		String[] estados = leer.nextLine().split(",");//Lista de estados del automata
		String[] auxAlf = leer.nextLine().split(",");//alfabeto auxiliar para meterlo en un ArrayList

		ArrayList<Character> alfabeto = new ArrayList<Character>();//ArrayList del alfabeto
		//Para llenar el alfabeto
		for(String i : auxAlf){
			alfabeto.add(i.charAt(0));
		}

		int nEstados = estados.length;//numero de estados
		int nCaracteres = alfabeto.size();//numero de caracteres

		LinkedList<Integer>[][] tablaEstados = new LinkedList[nEstados][nCaracteres];//tabla de estados

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

		
		/* Imprimir antiguo
		for (int i=0; i<nEstados;i++){
			System.out.println("Estado q" + i);
			for (int j=0; j<nCaracteres;j++){
				System.out.println("Con caracter en: " + j);
				System.out.println(Arrays.toString(tablaEstados[i][j].toArray()));
			}
		}
		*/
		
		
		HashMap<Estado,Estado[]> tablaAFD = crearGrafo(tablaEstados, alfabeto);
		for(Estado e : tablaAFD.keySet()){
			int key = e.id;
			System.out.println(e.id);
			Estado[] eds = tablaAFD.get(e);
			for(Estado e2 : eds){
				if(e2 != null)
					System.out.println(e2.id);
			}
		}
		
	}
	
	public static HashMap<Estado,Estado[]> crearGrafo( LinkedList<Integer>[][] tablaEstados , ArrayList<Character> alfabeto ){

		//la llave son los estados y los valores es un arreglo de estados a los que llegas con
		//cada caracter
		HashMap<Estado,Estado[]> tablaAFD = new HashMap<Estado,Estado[]>();



		Estado q0 = new Estado(0);
		q0.agregarEstado(0);
		tablaAFD.put(q0, new Estado[alfabeto.size()] );
		
		int i=1;
		for( Character c : alfabeto ){
			ArrayList<Integer> temp = funcionTransicion(q0,alfabeto.indexOf(c), tablaEstados);
			Estado e = new Estado(i, temp);
			
			Estado[] uli = tablaAFD.get(q0);
			uli[alfabeto.indexOf(c)] = e;
			
			tablaAFD.put(e, new Estado[alfabeto.size()] );
			
			i++;
		}
		
		return tablaAFD;
	}
	
	public static ArrayList<Integer> funcionTransicion(Estado e, int caracter , LinkedList<Integer>[][] tablaEstados){
		ArrayList<Integer> estadosDondeLlega = new ArrayList<Integer>();
		
		for(Integer i : e.getEstados()){
				estadosDondeLlega.addAll(tablaEstados[i][caracter]);
		}
		
		return estadosDondeLlega;
	}
}