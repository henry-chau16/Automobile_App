package coreservlet;


import java.io.IOException;
import java.io.PrintWriter;



import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exception.AutoException;
import model.Automobile;

/**
 * Servlet implementation class ChooseOptions
 */
@WebServlet("/ChooseOptions")
public class ChooseOptions extends ClientServlet{
	private static final long serialVersionUID = 1L;
	private String[] setList;
	String select = "";
	//servlet handles selection form for options of selected car model
    public ChooseOptions() {
        super();
        
    }
    //retrieves data from server and saves it to instance variables
    public void init(ServletConfig config)
    {
    	if(openConnection()) {
			try {
				in.readObject();
				send(2);
				sop(in.readObject());
				
			}
			catch(ClassNotFoundException e) {
				sop("Transmission error");
				sop(e);
				
				
			}
			catch(IOException e) {
				sop("Error receiving input");
				sop(e);
				
			}
			
    	}
    }
    
    //uses get; post calls get
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String selection = request.getParameter("model");
		
		try {
			
			send(selection);
			Object o =in.readObject();
			sop(o);
			Automobile a = (Automobile)o;
			String setNames = a.getAllSets();
			setList = setNames.split("\n");
			for(int i = 0; i<setList.length;i++) {
				String [] optList = a.getAllOptions(setList[i]).split("\n");
				sop(setList[i]);
				select+= setList[i]+": <select name=\""+setList[i]+"\" id=\""+setList[i]+"\">\n";
				for(int j = 0; j<optList.length;j++) {
					select+=" <option value=\""+optList[j]+"\">"+optList[j]+"</option>\n";
				}
				select+="</select>\n <br>";
			}
			send(" ");
			in.readObject();
			send(0);
			
			response.setContentType("text/html");
		    PrintWriter write = response.getWriter();
		    String docType =
		      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
		      "Transitional//EN\">\n";
			write.println(docType +
	                "<HTML>\n" +
	                "<HEAD><TITLE>Test</TITLE></HEAD>\n" +
	                "<BODY BGCOLOR=\"#FDF5E6\">\n" +
	                "<form name=\"OptionSelection\" method=\"get\" action=\"showQuote.jsp\">\n"+
	                "Model: "+ "<select name=\"select\" id=\"select\">\n"+" <option value=\""+selection+"\">"+selection+"</option>\n"
	                +"</select>\n<br>"+
	                "Select Options:"+
	                select+
	                "<input type=\"submit\" value=\"Submit\" />\n"+
	                "</form>\n" +
	                "</BODY></HTML>");
			
			
		}
		catch(ClassNotFoundException e) {
			sop("Transmission error");
			sop(e);
			
			
		}
		catch(IOException e) {
			sop("Error receiving input");
			sop(e);
			
		}
		catch(AutoException e) {
			sop("Error handling automobile object");
			sop(e);
			
		}
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
