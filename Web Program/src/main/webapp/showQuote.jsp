<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.IOException" %>
<%@ page import="java.io.ObjectInputStream" %>
<%@ page import="java.io.ObjectOutputStream" %>
<%@ page import="java.net.Socket" %>
<%@ page import="model.Automobile" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="exception.AutoException" %>


<!DOCTYPE html>
<HTML>
<HEAD>
<TITLE>Options Printout</TITLE>
<LINK REL=STYLESHEET
      HREF="JSP-Styles.css"
      TYPE="text/css">
</HEAD>


<BODY>


<% //option to dl as excel file
String format = request.getParameter("format");
if ((format != null) && (format.equals("excel"))) { 
  response.setContentType("application/vnd.ms-excel");
}

String selection = request.getParameter("select");

Socket sock;
String host = "localhost";
int port = 9998;
ObjectOutputStream output;
ObjectInputStream input;
float total=0;
float baseCost=0;
String[] setList = {};
float[] priceList = {};

//Connects to server and retrieves selected automobile
//Uses auto object to select the appropriate options with the 
//parameters passed in from the previous servlet form and
//saves data in parallel arrays

try {
	sock = new Socket(host, port);
	output = new ObjectOutputStream(sock.getOutputStream());
	input = new ObjectInputStream(sock.getInputStream());
	input.readObject();
	output.writeObject(2);
	input.readObject();
	output.writeObject(selection);
	Object o = input.readObject();
	System.out.println(o);
	output.writeObject(" ");
	input.readObject();
	output.writeObject(0);
	Automobile car = (Automobile)o;
	String setNames = car.getAllSets();
	setList = setNames.split("\n");
	priceList = new float[setList.length];
	
	for(int i = 0; i<setList.length;i++){
		System.out.println(setList[i]);
		System.out.println(request.getParameter(setList[i]));
		car.setOptionChoice(setList[i], request.getParameter(setList[i]));
		priceList[i] = car.getOptionChoicePrice(setList[i]);
		System.out.println(priceList[i]);
	}
	total = car.getTotalCost();
	baseCost = car.getBaseCost();
	System.out.println("here");
	car.printChoice();
}
catch (IOException e) {
	System.out.println(e);
}
catch (ClassNotFoundException e){
	System.out.println(e);
}
catch (AutoException e){
	System.out.println(e.getErrMsg());
}
finally{
%>
<%//print the table %>
<H2>Printout for <%=selection%></H2>
<TABLE BORDER=1>
  <TR><TH><%=selection%><TH>Base Cost<TH><%=baseCost%>
  <%for(int i = 0; i < setList.length; i++) { %>
  <TR><TH><%=setList[i]%><TH><%=request.getParameter(setList[i])%><TH><%=priceList[i]%>
  <%} %>
  <TR><TH>Total Price<TH><TH><%=total%>
  <%} %>
</TABLE>



</BODY>
</HTML>