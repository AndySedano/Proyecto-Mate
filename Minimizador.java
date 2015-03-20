 /**
*Clase minimizadora del Proyecto final: Primera Parte
*@author: Andrés Eugenio Sedano Casanova A00399842
*@author: Ulises Torner Campuzano A01333456
*@version: 18/03/2015/A
*/
import java.lang.StringBuilder;

public class Minimizador{
	//variable de la letra que será asignada al estado minimizado
	private static char laLetrosidadCambiante = 'A';
	
	/*
	*Método que minimiza el Automata Finito Determinista
	*@param: tablaAFD la tabla de transiciones del AFD
	*/
	public static void minimizar( String[][] tablaAFD ){
		int i=0;
		do{
			for(int j=0; j<tablaAFD[i].length; j++){
				if(i != j){
					if( tablaAFD[i][1].charAt(1) == tablaAFD[j][1].charAt(1) &&
						tablaAFD[i][2].charAt(1) == tablaAFD[j][2].charAt(1)  ){
							//Si ambos estados tienen las mismas transiciones
						if( tablaAFD[i][0].charAt(0) == '*' &&
							tablaAFD[j][0].charAt(0) == '*' ){
							//Si ambos estados son finales
							juntar(i,j,2,tablaAFD);
							
						}else if( tablaAFD[i][0].charAt(0) == 'q' &&
							 tablaAFD[j][0].charAt(0) == 'q' ){
							 //Si ambos estados no son finales
							 juntar(i,j,1,tablaAFD);
							 
						}else if( tablaAFD[i][0].charAt(0) == '-' ){
							//Para el estado inicial
							if( tablaAFD[i][0].charAt(2) == '*' ){
								//en caso de ser final también
								if( tablaAFD[j][0].charAt(0) == '*' ){
									//si su amigo también es final
									juntar(i,j,3,tablaAFD);
									
								}
							}else{
								//si sólo es inicial pero no final
								if( tablaAFD[j][0].charAt(0)== 'q' ){
									//sólo si su amigo no es final
									juntar(i,j,4,tablaAFD);
									
								}
							}
						}
					}
				}
			}
			i++;
		}while( i < tablaAFD.length );
		
	}
	
	/*
	*Método que junta un par de estados detectados como iguales
	*@param: i,j los índices de dichos estados
	*/
	private static void juntar( int i, int j, int tipo, String[][] tablaAFD ){
		switch( tipo ){
			case 1: //Si ambos son normalitos
				//Recorrer toda la tabla buscando a los q2 que serán qA
				nombreNuevo(tablaAFD[i][0].charAt(1),laLetrosidadCambiante,tablaAFD);
				
				//tablaAFD[i][0].charAt(1) = laLetrosidadCambiante;
				StringBuilder uli = new StringBuilder( tablaAFD[i][0] );
				uli.setCharAt(1,laLetrosidadCambiante);
				tablaAFD[i][0] = uli.toString();
				
				//tablaAFD[j][0].charAt(1) = laLetrosidadCambiante;
				StringBuilder uli2 = new StringBuilder( tablaAFD[j][0] );
				uli2.setCharAt(1,laLetrosidadCambiante);
				tablaAFD[j][0] = uli2.toString();
				
				quitarRepetidos(i,j,tablaAFD);
			break;
			
			case 2: //Si ambos son finales
				//Recorrer toda la tabla buscando a los q2 que serán qA
				nombreNuevo(tablaAFD[i][0].charAt(2),laLetrosidadCambiante,tablaAFD);
				
				//tablaAFD[i][0].charAt(2) = laLetrosidadCambiante;
				StringBuilder uli3 = new StringBuilder( tablaAFD[i][0] );
				uli3.setCharAt(2,laLetrosidadCambiante);
				tablaAFD[i][0] = uli3.toString();
				
				//tablaAFD[j][0].charAt(2) = laLetrosidadCambiante;
				StringBuilder uli4 = new StringBuilder( tablaAFD[j][0] );
				uli4.setCharAt(2,laLetrosidadCambiante);
				tablaAFD[j][0] = uli4.toString();
				
				quitarRepetidos(i,j,tablaAFD);
			break;
			
			case 3: //Si es el inicial y final
				//Recorrer toda la tabla buscando a los q2 que serán qA
				nombreNuevo(tablaAFD[i][0].charAt(4),laLetrosidadCambiante,tablaAFD);
				
				//tablaAFD[i][0].charAt(4) = laLetrosidadCambiante;
				StringBuilder uli5 = new StringBuilder( tablaAFD[i][0] );
				uli5.setCharAt(4,laLetrosidadCambiante);
				tablaAFD[i][0] = uli5.toString();
				
				//tablaAFD[j][0].charAt(2) = laLetrosidadCambiante;
				StringBuilder uli6 = new StringBuilder( tablaAFD[j][0] );
				uli6.setCharAt(2,laLetrosidadCambiante);
				tablaAFD[j][0] = uli6.toString();
				
				quitarRepetidos(i,j,tablaAFD);
			break;
			
			case 4: //Si es inicial y normalito
				//Recorrer toda la tabla buscando a los q2 que serán qA
				nombreNuevo(tablaAFD[i][0].charAt(3),laLetrosidadCambiante,tablaAFD);
				
				//tablaAFD[i][0].charAt(3) = laLetrosidadCambiante;
				StringBuilder uli7 = new StringBuilder( tablaAFD[i][0] );
				uli7.setCharAt(3,laLetrosidadCambiante);
				tablaAFD[i][0] = uli7.toString();
				
				//tablaAFD[j][0].charAt(1) = laLetrosidadCambiante;
				StringBuilder uli8 = new StringBuilder( tablaAFD[j][0] );
				uli8.setCharAt(1,laLetrosidadCambiante);
				tablaAFD[j][0] = uli8.toString();
				
				quitarRepetidos(i,j,tablaAFD);
			break;
		}
		laLetrosidadCambiante++;
	}//Fin de juntar
	
	/*
	*Método que cambia el nombre de los estados de las transiciones
	*@param: viejo,nuevo los nombres a reemplazar
	*@param: tablaAFD la tabla de transiciones del autómata
	*/
	private static void nombreNuevo( char viejo, char nuevo, String[][] tablaAFD ){
		for(int i=1; i<tablaAFD.length; i++){
			for(int j=1; j<tablaAFD[i].length; j++){
				if(tablaAFD[i][j].charAt(1) == viejo){
					tablaAFD[i][j] = "q" + nuevo;
				}
			}
		}
	}
	
	/*
	*Método que quita las líneas repetidas
	*@param: i,j los índices de dichos estados
	*/
	private static void quitarRepetidos( int i, int j, String[][] tablaAFD ){
		for(int k=0; k<tablaAFD[j].length;k++){
			tablaAFD[j][k] = "--";
		};
	}
	
}//Fin de la clase