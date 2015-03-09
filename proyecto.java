import java.util.Scanner;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Arrays;

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
			System.out.println(aux[1].charAt(4));
			int uli = Character.getNumericValue(aux[1].charAt(4));
			tablaEstados[estadoActual][charActual].add(uli);
			for(int i=2; i<aux.length;i++){
				System.out.println(aux[i].charAt(4));
				//tablaEstados[estadoActual][charActual].add(Character.getNumericValue(aux[i].charAt(1)));
			}
		}

		for (int i=0; i<nEstados;i++){
			for (int j=0; i<nCaracteres;j++){
				System.out.println(tablaEstados[i][j].toArray().toString());
			}
		}
	}
}