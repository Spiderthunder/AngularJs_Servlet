import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;


public class ScriviSuUnFile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ScriviSuUnFile() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		//RECUPERO I PARAMETRI PASSATI DA ANGULAR
		String regione = request.getParameter("regione");
		String nome = request.getParameter("nome");
		
		//STRINGHE UTILI
		String nomeFile = "C:/Users/salvatore.venditti/eclipse-workspace/AngularJs_Servlet/src/main/webapp/elenco_citta.json";
		String cost = "";
		String jsonFinale = "";
		
		//CREO UN FILE READER A CUI PASSO LA LOCAZIONE DEL FILE DA SCRIVERE/LEGGERE
		FileReader reader = new FileReader(nomeFile);
		
		//TRAMITE LO SCANNER SCORRO L'INTERO FILE E COPIO IL CONTENUTO IN UNA STRINGA "COST"
		Scanner sc = new Scanner(reader);
		while(sc.hasNext()){	
			cost = cost + sc.nextLine();
		}
		
		try{
			
			//CREO UN FORMATTATORE ATTRAVERSO IL QUALE POSSO CREARE UN OGGETTO JSON DALLA STRINGA CREATA PRIMA 
			JSONParser parser = new JSONParser();
			JSONObject parsed = (JSONObject) parser.parse(cost);
			
			//PRENDO SOLAMENTE IL VALORE JSON ATTRIBUITO ALLA CHIAVE CITTA'
			JSONArray cittaArray = (JSONArray) parsed.get("citta");
			
			//CREO UN NUOVO OGGETTO ATTRIBUENDOGLI UN NOME E UNA REGIONE
			JSONObject newCitta = new JSONObject();
			newCitta.put("nome", nome);
			newCitta.put("regione",regione);
			//AGGIUNGO L'OGGETTO ALL'ARRAY
			cittaArray.add(newCitta);
			
			//CREO UN OGGETTO FINALE JSON CHE CONTIENE LE INFORMAZIONI DA TRASCRIVERE NEL FILE (PRECEDENTI E NUOVE, INSERITE DALL'UTENTE)
			JSONObject finale = (JSONObject) parser.parse("{ \"citta\": " + cittaArray.toJSONString() + "}");
			jsonFinale = finale.toJSONString();
			
			//SCRIVO SU FILE
			FileWriter writer = new FileWriter(nomeFile,false);
			writer.write(jsonFinale);
			writer.close();
			
			//FORNISCO IL FILE JSON ANCHE IN RESPONSE
			response.setContentType("application/txt");
            PrintWriter out = response.getWriter();  
            out.print(jsonFinale);
            out.flush();        
            
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
