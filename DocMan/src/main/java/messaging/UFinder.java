package messaging;

import java.io.File;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSConnectionFactoryDefinition;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import marshallers.UniversalMarshaller;
import entities.FileInfo;
import sessionBeans.FileService;
 ;
 

@MessageDriven(name = "Messeger", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "test1")

})
public class UFinder implements MessageListener {
	@Inject
	private JMSContext jcontext;
	@Resource(mappedName = "java:/jms/queue/SearchRequest")
	private Queue findQueue;
	
	@Resource(mappedName = "java:/queue/SearchResponse")
	private Queue response;
	
	@EJB
	private FileService fs;
	public UFinder() {
	
	}

	public void findRequest(String username) {
		 
		try {
			System.out.println("what wthat");
			TextMessage request = jcontext.createTextMessage();
			request.setText(username);
			List<FileInfo> userfiles=null;
			userfiles=fs.getFilesByOwner(username);
			 
			System.out.println(userfiles.get(0).getFileName()+" is about to be sent");
			request.setJMSReplyTo(response);
			request.setJMSCorrelationID(UUID.randomUUID().toString());
			jcontext.createProducer().send(response, new UniversalMarshaller().toXML(userfiles.get(0)));
			System.out.println("rEsponse Sent"+new UniversalMarshaller().toXML(userfiles.get(0)));

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// jcontext.createProducer().send(findQueue,
		// "makazi ka lizo unomtwana ongu milisa");

	}

	public void onMessage(Message message) {
		
		System.out.println("Messege Recieved on Finder...!!!");
		if(message instanceof TextMessage){
			try {
			 String username=((TextMessage) message).getText();
			 findRequest(username);
				
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println("\n Response Recieved, but its an instance of TextMessage");
		}

	}

}
