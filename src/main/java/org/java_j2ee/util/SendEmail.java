package org.java_j2ee.util;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.log4j.Logger;

public class SendEmail {
	private static final Logger logger = Logger.getLogger(SendEmail.class); 
	public static boolean sendEmail(String name,String user_email,String mobile,String textMsg) throws EmailException{
		logger.info("inside send email...");	
		//name=name+":"+user_email+":"+mobile;		
		textMsg="name="+name+"<br/> mobile="+mobile+"<br/>email="+user_email+"<br/> message="+textMsg;		
		
		String subject="feedBack";
		String from="contact.java.j2ee.org@gmail.com";
		String password = "antaratma";
		String SendIds=from;
		String email_to_send=""; 		
		boolean flag=true;
		//Boolean smtp_auth =Boolean.valueOf(PrjProperties.getProperty("USE_SMTP_AUTHENTICATION"));	
		Boolean smtp_auth = true;
		HtmlEmail email = new HtmlEmail();
		if(smtp_auth==true){
			//String SMTP_HOST = PrjProperties.getProperty("SMTP_HOST");
			String SMTP_HOST="smtp.gmail.com";
			//String smtp_port = PrjProperties.getProperty("SMTP_PORT");
			String smtp_port="587";
			int SMTP_PORT=Integer.parseInt(smtp_port);
			email.setHostName(SMTP_HOST);
			
			String authUser = from;
			String authPassword = password;
			email.setSmtpPort(SMTP_PORT);
			email.setAuthenticator(new DefaultAuthenticator(authUser, authPassword)); 
		}else{
			email.setHostName("localhost");
		}
		email.setFrom(from,name);
		email.setDebug(false);
		email.setSubject(subject);
		email.setHtmlMsg(textMsg);
		ArrayList bccList = new ArrayList();
		if(SendIds.indexOf(",")!= -1)
		{
			String[] bccListtEmailIdsAry =SendIds.split(",");
			for(int k=0;k<bccListtEmailIdsAry.length;k++)
			{
				javax.mail.internet.InternetAddress internetEmailId = new javax.mail.internet.InternetAddress();
				internetEmailId.setAddress(bccListtEmailIdsAry[k].trim());				
				bccList.add(internetEmailId);
				if(k==0)
					email_to_send=bccListtEmailIdsAry[k];
			} 
		}
		else
		{
			javax.mail.internet.InternetAddress singleInternetEmailId = new javax.mail.internet.InternetAddress();
			singleInternetEmailId.setAddress(SendIds);
			bccList.add(singleInternetEmailId);
			email_to_send=SendIds;
		}
		System.out.println("bccList:"+bccList.size());            
		email.addTo(email_to_send,"");	
		/*System.out.println("fileAttach.length()="+fileAttach.length());
		if(fileAttach!=null && fileAttach.length()!=0){
			EmailAttachment attachment = new EmailAttachment();
			String folderPath=PrjProperties.getProperty("FOLDER_PATH");
			folderPath=folderPath+File.separator+fileAttach;			
			attachment.setPath(folderPath);
			attachment.setDisposition(EmailAttachment.ATTACHMENT);
			// attachment.setDescription("Picture of John");
			attachment.setName("Java-J2ee.org");	
			email.attach(attachment);			
		}
		*/
		email.setBcc(bccList);	
		email.setTLS(true);  
		email.send();
		return flag;

	}
}