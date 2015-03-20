/**
*Clase principal del Proyecto final: Primera Parte
*@author: Andrés Eugenio Sedano Casanova A00399842
*@author: Ulises Torner Campuzano A01333456
*@version: 18/03/2015/A
*/

//Imports
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.LinkedList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Principal extends JFrame{
	//Atributos públicos de la clase
	static Scanner leer;
	static LinkedList<Estado> pila;
	static ArrayList<Estado> estadosAFD;//Estados del AFD
	static ArrayList<Character> alfabeto;//ArrayList del alfabeto
	static LinkedList<Integer>[][] tablaEstados;//tablaGUI de estados del AFND
	
	//Atributos para la GUI
	static JTable tablaGUI;
	static JScrollPane jsp;
	static JLabel label;
	static JFrame frame;
	static JPanel panel;
	static JButton boton;
    static JFileChooser select;
    static FileNameExtensionFilter textFilter;

	/*
	*Método principal que inicializa todos los componetes y manda a llamar a actualizar GUI
	*/
    public static void main(String[] args){

        //Inicializacion de componentes de la GUI
        panel = new JPanel();
        jsp = new JScrollPane(panel);
        select = new JFileChooser();
        boton = new JButton("Nuevo");
        frame = new JFrame("Convertidor de AFND a AFD");
        textFilter = new FileNameExtensionFilter("Text Files","txt");
        
        //Asignacion de valores a componentes
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        frame.add(jsp, BorderLayout.CENTER);
        select.setFileFilter(textFilter);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setSize(400,400);
        frame.add(boton, BorderLayout.SOUTH);
        frame.setVisible(true);
        jsp.setVisible(true);

        actualizarGUI();

        boton.addActionListener(new ActionListener() {
        	//Para cada vez que se presiona el boton
            @Override
            public void actionPerformed(ActionEvent e) {
            	actualizarGUI();       
            }
        }); 
    }
	
	/*
	*Método que actualiza la GUI
	*/
    public static void actualizarGUI(){
    	panel.removeAll();

    	select.showOpenDialog(frame);
    	
    	boolean sePuedeMin = false;
    	String[][] tablaAFDTemp = sandias(select.getSelectedFile());
    	String[][] estadosAFDTemp = new String[estadosAFD.size()][2];
    	if(tablaAFDTemp.length>2){
    		String[][] tablaAFDMinTemp = new String[tablaAFDTemp.length][];
				for(int i = 0; i < tablaAFDTemp.length; i++){
	   		 		tablaAFDMinTemp[i] = tablaAFDTemp[i].clone();
	   			}
	   			Minimizador.minimizar(tablaAFDMinTemp);
	   			sePuedeMin = !Arrays.deepEquals(tablaAFDMinTemp, tablaAFDTemp);
	    	
    	}

    	int i=0;
    	for(Estado e : estadosAFD){
    		estadosAFDTemp[i][0] = "q" + e.id;
    		estadosAFDTemp[i][1] = e.getSubEstados().toString();
    		i++;
    	}

    	label = new JLabel("Tabla de estados", SwingConstants.CENTER);
    	panel.add(label);
    	tablaGUI = new JTable(estadosAFDTemp, estadosAFDTemp[0]);
    	panel.add(tablaGUI);
    	label = new JLabel("Tabla de transiciones", SwingConstants.CENTER);
    	panel.add(label);
    	tablaGUI = new JTable(tablaAFDTemp, tablaAFDTemp[0]);
    	panel.add(tablaGUI);

    	if(sePuedeMin){
	    	String[][] tablaAFDMinTemp = new String[tablaAFDTemp.length][];
				for(i = 0; i < tablaAFDTemp.length; i++){
	   		 		tablaAFDMinTemp[i] = tablaAFDTemp[i].clone();
	   			}
	    	Minimizador.minimizar(tablaAFDMinTemp);
	    	label = new JLabel("Tabla minimizada", SwingConstants.CENTER);
	    	panel.add(label);
	    	tablaGUI = new JTable(tablaAFDMinTemp, tablaAFDMinTemp[0]);
	    	panel.add(tablaGUI);
	    }else{
	    	label = new JLabel("No se puede minimizar el grafo", SwingConstants.CENTER);
	    	panel.add(label);
	    }

        frame.revalidate();
    	frame.repaint();
    }
	
	/*
	*Método "sandías" anteriormente era el main, pero con la llegada de la GUI
	*todo cambió, y el éste fue desplazado por otro main,
	*así fue como se renombro como sandías, esperando a ser llamado por el nuevo main
	*y así demostrar su verdadera habilidad en la transformación de autómatas
	*@param: f el archivo que fue seleccionado por el usuario
	*@return: la tabla del AFD que se crea
	*/
	public static String[][] sandias (File f){
		try{
			leer = new Scanner(f);
		}catch(IOException | NullPointerException n){
			JOptionPane.showMessageDialog(null, "Archivo no valido");
			System.exit(0);
		}

		estadosAFD = new ArrayList<Estado>();
		alfabeto = new ArrayList<Character>();

		String[] estados = leer.nextLine().split(",");//Lista de estados del automata
		String[] auxAlf = leer.nextLine().split(",");//alfabeto auxiliar para meterlo en un ArrayList
		
		//Para llenar el alfabeto
		for(String i : auxAlf){
			alfabeto.add(i.charAt(0));
		}

		int nEstados = estados.length;//numero de estados
		int nCaracteres = alfabeto.size();//numero de caracteres

		tablaEstados = new LinkedList[nEstados][nCaracteres];

		int estadoInicial = Character.getNumericValue(leer.nextLine().charAt(1));// El estado inicial
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
		q0.agregarSubEstado(estadoInicial);
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
		tablaAFDimprimir[0][0] = "  ";
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
			tablaAFDimprimir[0][i] = c.toString() + "  ";
			i++;
		}
		
		//Imprimir la Tabla del AFD en el terminal
		for(int u=0; u<=estadosAFD.size(); u++){
			for(int v=0; v<=alfabeto.size(); v++){
				System.out.print( tablaAFDimprimir[u][v] + "\t");
			}
			System.out.println("");
		}

		return tablaAFDimprimir;
		
	}//Fin del main sandias
		
	/*
	*Método calcularConexion, usa la función de transición para calcular los sub-estados a los cuales se conecta un estado dado
	*@param: c - el caracter del alfabeto por el cual se da la transición
	*@param: e - el estado desde el cual se calculará la función de transición
	*@return: regresa un estado del AFD con sus sub-estados del cual se conecta "e" con la transición por medio de "c"
	*/
	public static Estado calcularConexion(Character c, Estado e){
		Estado aux = new Estado(-1, alfabeto);//Nuevo estado auxiliar para saber si el estado(con todo y subestados) ya existe en la tablaGUI de estadosAFD

		for(int sub : e.getSubEstados()){//Para todos los subestados del estado
			for(int subEstado : tablaEstados[sub][alfabeto.indexOf(c)]){
				if (! aux.getSubEstados().contains(subEstado)){
					aux.agregarSubEstado(subEstado);
				}
			}
		}

		int bandera = 0;
		for (Estado eAFD : estadosAFD){//Por todos los estados en el afd
			if (eAFD.comparar(aux)){//Checa si alguno es igual a aux
				bandera = 1;
				aux=eAFD;
			}
		}
		for (Estado eAFD : pila){//Por todos los estados en el afd
			if (eAFD.comparar(aux)){//Checa si alguno es igual a aux
				bandera = 1;
				aux=eAFD;
			}
		}
		if(e.comparar(aux)){
			bandera = 1;
			aux=e;
		}

		if (bandera==0){
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
	
}//Fin de la clase