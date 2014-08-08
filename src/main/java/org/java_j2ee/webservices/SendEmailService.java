package  org.java_j2ee.webservices;


import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import  org.java_j2ee.util.SendEmail;

@Path("")

public class SendEmailService {	

	private Logger logger = Logger.getLogger(SendEmailService.class);
	@Context private HttpServletRequest request;


	@POST
	@Path("/sendEmail")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Response sendEmail(
			@Context HttpServletRequest request,
			@FormParam("name") String name,
			@FormParam("email") String user_email,
			@FormParam("mobile") String mobile,
			@FormParam("textMsg") String textMsg
			//@FormParam("fileAttach") String fileAttach
			) throws JSONException, EmailException 
			{
		
		System.out.println("inside sendEmail service");
		String str=null;
		JSONObject json=new JSONObject();
		String strResp=null;		
		
			boolean flag=SendEmail.sendEmail(name,user_email,mobile,textMsg);

			if(flag==false){            	
				strResp="Email sending failed...";
				json.put("output", strResp);
				str=String.valueOf(json);

				return Response.status(200).entity(str).build();
			} 
			else
			{  			

				logger.info("name="+name);
				logger.info("email="+user_email);
				logger.info("mobile="+mobile);
				logger.info("textMsg="+textMsg);

				strResp="FeedBack sent...";
				json.put("output", strResp);
				str=String.valueOf(json);

				return Response.status(200).entity(str).build();
			}
		
			
	  }
}		




