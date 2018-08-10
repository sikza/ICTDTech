package messaging;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.spring.ActiveMQConnectionFactory;
import org.jboss.ejb3.annotation.ResourceAdapter;

import docman.services.FileService;
import marshallers.UniversalMarshaller;
import entities.FileInfo;

;

@MessageDriven(name = "Messeger", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "topic/clientSearch")

})
// activemq
@ResourceAdapter(value="activemq")
public class UFinder implements MessageListener {
	
	@JMSConnectionFactory("java:/AMQConnectionFactory")//
	private ActiveMQConnectionFactory amqCon;
	@Inject
	private JMSContext jcontext;
	@Resource(mappedName = "java:/jms/queue/SearchRequest")
	private Queue findQueue;
	@Resource(mappedName = "java:/queue/SearchResponse")
	private Queue response;
	@EJB
	private FileService fs;


/*@Inject
    @JMSConnectionFactory("java:/jms/remoteCF")
*/
	public UFinder() {

	}

	// this is called by the "onMessage" method to send a response
	public void findRequest(String username, Message msg) {
		if (!username.equals(null) || username != null) {
			try {
				 
				 
				List<FileInfo> userfiles = null;
				userfiles = fs.getFilesByOwner(username);
				if (userfiles != null) {

					String responseMsg = new UniversalMarshaller()
							.toXML(userfiles.get(0));
					TextMessage reply = jcontext.createTextMessage();
					 reply.setJMSCorrelationID(msg.getJMSCorrelationID());
					reply.setJMSMessageID(msg.getJMSMessageID());
					reply.setText(responseMsg);
					 
				 new  MQjmsContext().getJMSContextAMQ(msg.getJMSReplyTo(), reply);
				// new  MQjmsContext().getJMSContextAMQ(findQueue, reply.getJMSCorrelationID()+":"+msg.getJMSCorrelationID()+"\n "+ reply.getJMSMessageID()+":"+msg.getJMSMessageID());
				System.out.println(" \n  \n Ufinder Message Key::: "+msg.getStringProperty("tests")+"  :  "+msg.getJMSCorrelationID());
				System.out.println(" \n  \n Ufinder Message Key::: "+msg.getStringProperty("tests")+"  :  "+msg.getJMSCorrelationID());

				//	jcontext.createProducer().send(msg.getJMSReplyTo(), reply);
				//	System.out.println("Message Sent to "
				//			+ response.getQueueName() + " : @Body| \n"
					//		+ responseMsg);
				
				 
				}
			} catch (JMSException  e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
	}

	public void onMessage(Message message) {

		System.out.println("Search Request Recieved!!!");
		if (message instanceof TextMessage) {
			try {
				String username = ((TextMessage) message).getText();
				// Call a method to send a response
				findRequest(username, message);
				 
			} catch (JMSException e) {
				e.printStackTrace();
			}
		} else {
			System.out
					.println("\n Response Recieved, but its an instance of TextMessage");
		}

	}

}