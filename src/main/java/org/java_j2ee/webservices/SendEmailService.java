package com.travelmarg.webservices;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.travelmarg.database.DBUtils;
import com.travelmarg.util.SendEmail;

@Path("")

public class SendEmailService {	

	private Logger logger = Logger.getLogger(SendEmailService.class);
	@Context private HttpServletRequest request;


	@POST
	@Path("/sendEmail")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Response sendEmail(
			@Context HttpServletRequest request,
			@FormParam("template") String template,
			@FormParam("from") String from,
			@FormParam("name") String name,
			@FormParam("to") String to,
			@FormParam("bcc") String bcc,
			@FormParam("subject") String subject,
			@FormParam("textMsg") String textMsg,
			@FormParam("fileAttach") String fileAttach,
			@FormParam("email_limit") int email_limit,
			@FormParam("password") String password	
			) throws JSONException, EmailException 
			{
		String str=null;
		JSONObject json=new JSONObject();
		String strResp=null;

		if(template==null || template=="" || (template.length()==0)){

			strResp="Please select a template..!";	
			json.put("output", strResp);
			str=String.valueOf(json);

			return Response.status(200).entity(str).build();

		}else{

			// boolean flag=EmailVerification.sendEmail(from,name,password,to,bcc,subject,textMsg,fileAttach);
			String SendIds=to+","+bcc;
			boolean flag=SendEmail.sendEmail(from,password,name,subject,textMsg,SendIds,fileAttach);


			if(flag==false){            	
				strResp="Email sending failed...";
				json.put("output", strResp);
				str=String.valueOf(json);

				return Response.status(200).entity(str).build();
			} 
			else
			{           	

				//HttpSession session = request.getSession(true);
				//String user=(String) session.getAttribute("user");
				//String pwd=(String) session.getAttribute("pwd");

				String user="user1";
				String pwd="pass1";	

				logger.info("user:"+user);
				logger.info("pwd:"+pwd);

				/*Database interaction*/

				DBUtils dt=new DBUtils();		
				template = template.toUpperCase();
				int sent_count = DBUtils.getEmailsSentCount(template);
				if(sent_count<email_limit){
					dt.insertRecord(user,from,name,password,to,bcc,subject,textMsg,fileAttach,template);
					DBUtils.updateEmailSentCounter(template);
				}else{
					//str="<!DOCTYPE html><html><body><p>Email not sent..! </p><p> Daily Limit got exceeded..</p></body></html>";
					strResp="Daily Limit got exceeded..";
					json.put("output", strResp);
					str=String.valueOf(json);

					return Response.status(200).entity(str).build();
				}


				logger.info("from="+from);
				logger.info("name="+name);
				logger.info("password="+password);
				logger.info("to="+to);
				logger.info("bcc="+bcc);
				logger.info("subject="+subject);
				logger.info("textMsg="+textMsg);
				logger.info("fileAttach="+fileAttach);	
				logger.info("email_limit="+email_limit);	

				//str="<!DOCTYPE html><html><body><p>Email sent..!</p></body></html>";

				strResp="Email sent to "+to+"";
				json.put("output", strResp);
				str=String.valueOf(json);

				return Response.status(200).entity(str).build();
			}
		}
			}		


	@GET
	@Path("/getFormParams")
	@Produces({MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML})
	public Response getFormParams(
			@QueryParam("template") String template			
			) throws JSONException, SQLException 
			{
		JSONObject json=new JSONObject();

		String temp=template;
		String from=null;
		String name=null;
		String to=null;
		String bcc=null;
		String subject=null;
		String textMsg=null;
		int email_limit = 0;
		String password=null;


		template = temp.toUpperCase();
		Connection conn = DBUtils.getConnection();
		String sql = "select * from email_templates where template_name='"+template+"'";
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		while(rs.next()){
			from=rs.getString("from_email");
			name=rs.getString("from_name");
			to=rs.getString("to_email");
			bcc=rs.getString("bcc_email");
			subject=rs.getString("subject");
			textMsg=rs.getString("message");
			email_limit = rs.getInt("email_limit");
			password = rs.getString("from_password");
		} 
		conn.close();

		json.put("from",from);
		json.put("name",name);		
		json.put("to",to);
		json.put("bcc",bcc);
		json.put("subject",subject);
		json.put("textMsg",textMsg);
		json.put("email_limit",email_limit);
		json.put("password",password);

		String str=String.valueOf(json);		
		return Response.status(200).entity(str).build();

			}


}
