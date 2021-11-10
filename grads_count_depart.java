package count_depart;

//import static org.junit.Assert.internalArrayEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.QueryParam;
import org.json.simple.*;

import com.mysql.cj.jdbc.result.ResultSetMetaData;
import com.sun.xml.txw2.Document;

import javax.json.*;
import org.json.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


@Path("/count_depart/grads") //set your service url path to <base_url>/hello
// the <base_url> is based on your application name, the servlet // and the URL pattern from the web.xml configuration file
public class grads_count_depart {
	
	/*public String convert_greek_words(String str_before) {
		String str_after="";
		ByteBuffer buffer = StandardCharsets.UTF_8.encode(str_before); 
		str_after= StandardCharsets.UTF_8.decode(buffer).toString();
		return str_after;
	}*/
	
	@GET
	@Produces(MediaType.APPLICATION_XML + ";charset=utf-8") //@Produces("application/json") //defines which MIME type is delivered by a method annotated with @GET
	public Grads_list sayHelloNameInXML(@QueryParam("depart") String depart) throws UnsupportedEncodingException, JAXBException {  //, @QueryParam("study_year") String study_year
		ResultSet results = null; 
		int count=0;
		ArrayList<Grads_cls> ar = new ArrayList<Grads_cls>();
		Grads_list gradsList = new Grads_list();
		StringWriter stringWriter = new StringWriter();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String DB_URL = "jdbc:mysql://localhost:3306/open_data_uoc?" + "autoReconnect=true&useSSL=false";
			String USER = "root";
			String PASS = "maria123"; 
			Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/open_data_uoc?useUnicode=true&characterEncoding=UTF8", "root","maria123");
			Statement statement = connection.createStatement();
			results = statement.executeQuery("SELECT * from graduates where department =\""+ depart +"\""); // where \"aca_year\" = aca_year AND \"study_yearment\"= study_year"
			
			while (results.next())
			{
				count++;
				Grads_cls newGrads= new Grads_cls();
				Double gender_M = results.getDouble("gender_M");
			    Double gender_F = results.getDouble("gender_F");
		        String etos_fitisis = results.getString("etos_fitisis");
		        String etos_anaforas = results.getString("etos_anaforas");
		        String department = results.getString("department");
		        newGrads.setEtos_anaforas(etos_anaforas);
		        newGrads.setEtos_fitisis(etos_fitisis);
		        newGrads.setGender_F(gender_F);
		        newGrads.setGender_M(gender_M);
		        newGrads.setDepartment(department);
		        ar.add(newGrads);
		        System.out.println(gender_M + " " + gender_F + " "+ etos_fitisis+ " "+ etos_anaforas + " "+ department);
	      }
		connection.close();
	} catch (ClassNotFoundException | SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		try {
			gradsList.setGrads_list(ar);
			gradsList.setGrads_counter(count);
			File file = new File(	//auto to arxeio this_is_my_path periexei to path sto opio tha dimiourgithei to kainourio xml arxeio mou
				"C:\\Users\\maria\\eclipse-workspace\\Rest_ptixiakis\\src\\main\\java\\open_data\\ws\\rest\\this_is_my_path.txt");
	        BufferedReader br = new BufferedReader(new FileReader(file));	//vrisko to txt arxeio
	        String path_in_file;
			path_in_file = br.readLine();	// kai diavazo to path
			File output = new File(path_in_file+"\\"+"graduates.xml");
						
	        JAXBContext jaxbContext = JAXBContext.newInstance(Grads_list.class);
	        javax.xml.bind.Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(count, output);
            jaxbMarshaller.marshal(gradsList, output);
            jaxbMarshaller.marshal(gradsList, System.out);
	      } catch (JAXBException | IOException e) {
	        e.printStackTrace();
	      }
	//return gradsList.toString();
		return gradsList;
}
	
	// This method is called if application/json is requested
	@GET
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8") //@Produces("application/json") //defines which MIME type is delivered by a method annotated with @GET
	public String sayHelloNameInJson(@QueryParam("depart") String depart) throws UnsupportedEncodingException {  //, @QueryParam("depart") String depart
		 Grads_cls grad_obj= new Grads_cls();
		 JSONArray array=new JSONArray();
		 JSONObject jsonObject = new JSONObject();
		 int count=0;
		 ResultSet results = null; //ArrayList<String> ar = new ArrayList<String>();
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				String DB_URL = "jdbc:mysql://localhost:3306/open_data_uoc?" + "autoReconnect=true&useSSL=false";
				String USER = "root";
				String PASS = "maria123"; 
				Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/open_data_uoc?useUnicode=true&characterEncoding=UTF8", "root","maria123");
				Statement statement = connection.createStatement();
				results = statement.executeQuery("SELECT * from graduates where department =\""+ depart +"\""); // where \"aca_year\" = aca_year AND \"department\"= depart"
				//System.out.println("To etos anaforas einai ->"+aca_year+"<-");
				while (results.next())
				{
					count++;
					JSONObject json = new JSONObject();
					json.put("gender_M",results.getDouble("gender_M"));
					json.put("gender_F", results.getDouble("gender_F"));
					json.put("etos_fitisis", results.getString("etos_fitisis"));
					json.put("etos_anaforas", results.getString("etos_anaforas"));
					json.put("department", results.getString("department"));
			       
			        array.add(json);
			        //System.out.println(gender_M + " " + gender_F + " "+ etos_apofitisis+ " "+ etos_anaforas + " "+ department);
		      }
			connection.close();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			jsonObject.put("Graduates_counter", count);
		    jsonObject.put("Graduates", array);
		    
		    System.out.println(jsonObject.toJSONString());


		    return jsonObject.toJSONString();
	}
	
	// This method is called if HTML is requested
	@GET
	@Produces(MediaType.TEXT_HTML + ";charset=utf-8")
	public String sayHelloNameInHtml(@QueryParam("depart") String depart) {
		ResultSet results = null; ArrayList<String> ar = new ArrayList<String>();
		int count=0;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String DB_URL = "jdbc:mysql://localhost:3306/open_data_uoc?" + "autoReconnect=true&useSSL=false";
			String USER = "root";
			String PASS = "maria123"; 
			Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/open_data_uoc?useUnicode=true&characterEncoding=UTF8", "root","maria123");
			Statement statement = connection.createStatement();
			results = statement.executeQuery("SELECT * from graduates where department =\""+ depart +"\""); // where \"aca_year\" = aca_year AND \"department\"= depart"
			//System.out.println("To etos anaforas einai ->"+aca_year+"<-");
			//System.out.println("To etos etos_fitisis einai ->"+study_year+"<-");
			while (results.next())
			{
				count++;
				Double gender_M = results.getDouble("gender_M");
			    Double gender_F = results.getDouble("gender_F");
		        String etos_fitisis = results.getString("etos_fitisis");
		        String etos_anaforas = results.getString("etos_anaforas");
		        String department = results.getString("department");
			    
			    ar.add(Double.toString(gender_M));
		        ar.add(Double.toString(gender_F));
				ar.add(etos_fitisis);
				ar.add(etos_anaforas);
				ar.add(department);
		        System.out.println(gender_M + " " + gender_F + " "+ etos_fitisis+ " "+ etos_anaforas + " "+ department);
	      }
		connection.close();
	} catch (ClassNotFoundException | SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		String output="<html>  <meta charset=\"utf-8\">" + "<br>"
			     + "<body>" + "<p>The counter of Graduates of department " + depart+ " is " +count + "</p>" + "<table>	<tbody>	<tr>"
		     + "<th>Male</th><th>Female</th><th>Year_of_study</th><th>Academic_year</th><th>Department</th></tr>";
			
		for (int i=0;i<ar.size();i=i+5) {
			output=output+"<tr>";
			for (int j=0;j<5;j++)
				 output=output + "<th>" + ar.get(i+j).toString() +"</th>";
			//output= output+ "<br>" + ar.get(i).toString() + "<br>";
			output=output+"</tr>";
			//System.out.println(output);
		}
		output=output+ "</body>" + "</html> ";
		return output;
	}
}
