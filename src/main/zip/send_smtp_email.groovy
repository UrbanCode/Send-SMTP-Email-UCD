/**
 * (c) Copyright IBM Corporation 2014, 2017.
 * This is licensed under the following license.
 * The Eclipse Public 1.0 License (http://www.eclipse.org/legal/epl-v10.html)
 * U.S. Government Users Restricted Rights:  Use, duplication or disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */
import javax.mail.internet.*;
import javax.mail.*
import javax.activation.*

import com.urbancode.air.AirPluginTool;
import com.urbancode.ud.client.UDRestClient;

def apTool = new AirPluginTool(this.args[0], this.args[1]);

// get the step properties
def props = apTool.getStepProperties();

// get the user, password, and weburl needed to create a rest client
def udUser = apTool.getAuthTokenUsername();
def udPass = apTool.getAuthToken();
def weburl = apTool.getWebUrl();

// get the properties from the step definition
def toAddress = props['toList'];
def subject = props['subject'];
def message = props['message'];

// define the properties we will get from the system configuration
def host;
def port;
def fromAddress;

// create the rest client and get the system configuration
com.urbancode.air.XTrustProvider.install();
def client = new UDRestClient(new URI(weburl), udUser, udPass);
def values = client.getSystemConfiguration();

// find the system configuration properties we are interested in
values.keys().each() { key ->
	if (key == "deployMailHost") {
		host = values.optString(key);
	}
	if (key == "deployMailPort") {
		port = values.optString(key);
	}
	if (key == "deployMailSender") {
		fromAddress = values.optString(key);
	}
}

//tokenize out the recipients in case they came in as a list
StringTokenizer tok = new StringTokenizer(toAddress,",");
ArrayList emailTos = new ArrayList();
while(tok.hasMoreElements()){
  emailTos.add(new InternetAddress(tok.nextElement().toString()));
}

// create a new mail session and message
Properties mprops = new Properties();
mprops.setProperty("mail.transport.protocol","smtp");
mprops.setProperty("mail.host",host);
mprops.setProperty("mail.smtp.port",port);
Session lSession = Session.getDefaultInstance(mprops,null);
MimeMessage msg = new MimeMessage(lSession);

// populate the to, from, subject, and text of the message
InternetAddress[] to = new InternetAddress[emailTos.size()];
to = (InternetAddress[]) emailTos.toArray(to);
msg.setRecipients(MimeMessage.RecipientType.TO,to);
msg.setFrom(new InternetAddress(fromAddress));
msg.setSubject(subject);
msg.setText(message)

// send the message
Transport transporter = lSession.getTransport("smtp");
transporter.connect();
transporter.send(msg);
