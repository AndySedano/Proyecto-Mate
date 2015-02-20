import java.util.Scanner;
import java.util.ArrayList;

public class proyecto {
	public static void main (String[] args){
		Scanner leer = new Scanner(System.in);
		ArrayList<String> evaluaciones = new ArrayList<String>();
		ArrayList<Estado> estados = new ArrayList<Estado>();
		String[] estadosL = leer.nextLine().split(",");
		for (int i = 0; i<estadosL.length; i++){
			Integer.parseInt(estadosL[i]);
		}
		String alfabeto = leer.nextLine();
		String estadoInicial = leer.nextLine();
		String estadosFinales = leer.nextLine();
		while(leer.hasNext()){
			evaluaciones.add(leer.nextLine());
		}
		System.out.println("estados: " + estados);
		System.out.println("alfabeto: " + alfabeto);
		System.out.println("estadosFinales: " + estadosFinales);
		System.out.println("estadoInicial: " + estadoInicial);
		for(int i = 0; i<evaluaciones.size(); i++){
			System.out.println("funcion de transicion: " + i + evaluaciones.get(i));
		}
	}
}