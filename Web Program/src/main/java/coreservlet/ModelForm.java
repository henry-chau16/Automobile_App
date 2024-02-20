package coreservlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ModelForm
 */
@WebServlet("/ModelForm")
public class ModelForm extends ClientServlet{
	private static final long serialVersionUID = 1L;

	private String[] modelList;
	String select = "";

	//servlet handles selection form for model of car
    public ModelForm() {
        super();
        
    }
    //retrieves data from server and saves it to instance variables
    public void init(ServletConfig config)
    {
    	if(openConnection()) {
			try {
				in.readObject();
				send(2);
				String[] temp = in.readObject().toString().split("\n");
				modelList = Arrays.copyOfRange(temp, 1, temp.length);
				
				select = "<select name=\"model\" id=\"model\">\n";
				for(int i = 0; i<modelList.length; i++) {
					select+=" <option value=\""+modelList[i]+"\">"+modelList[i]+"</option>\n";
				}
				select+="</select>\n";
				
				send("exit");
				in.readObject();
				
				send(" ");
				in.readObject();
				send(0);
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
		
		response.setContentType("text/html");
	    PrintWriter write = response.getWriter();
	    String docType =
	      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
	      "Transitional//EN\">\n";
		write.println(docType +
                "<HTML>\n" +
                "<HEAD><TITLE>Test</TITLE></HEAD>\n" +
                "<BODY BGCOLOR=\"#FDF5E6\">\n" +
                "<form name=\"ModelSelection\" method=\"get\" action=\"ChooseOptions\">\n"+
                "Select a model:"+
                select+
                "<input type=\"submit\" value=\"Next\" />\n"+
                "</form>\n" +
                "</BODY></HTML>");	

	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		doGet(request, response);
	}

}
