package messaging;

import java.util.Properties;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
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
import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import org.hamcrest.core.IsInstanceOf;

@MessageDriven(name = "Messeger", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/ExpiryQueue")

})
public class UFinder implements MessageListener {
	@Inject
	private JMSContext jcontext;
	@Resource(mappedName = "java:/jms/queue/SearchRequest")
	private Queue findQueue;
	
	@Resource(mappedName = "java:/jms/queue/SearchRequest")
	private Queue responseQ;

	public UFinder() {
	
	}

	public void findRequest(String username) {
		username = "admin";
		try {

			TextMessage request = jcontext.createTextMessage();
			request.setText(username);
			request.setJMSReplyTo(responseQ);
			request.setJMSCorrelationID(UUID.randomUUID().toString());
			jcontext.createProducer().send(findQueue, request);
			System.out.println("Message Sent to: " + findQueue.getQueueName());

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
				System.out.println("\n Response:  "+ ( (TextMessage) message).getText() );
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println("\n Response Recieved, but its an instance of TextMessage");
		}

	}

}
