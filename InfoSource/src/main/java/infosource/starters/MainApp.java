package infosource.starters;

import infosource.routebuilders.SearchRouteBuilder;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.main.Main;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
 
public class MainApp {

	private ApplicationContext context;
	private CamelContext camelContext;

	public MainApp() {

		try {
			this.context = new ClassPathXmlApplicationContext(
					"META-INF/camel-conf.xml");
			this.camelContext = SpringCamelContext.springCamelContext(context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendMessage(String msg) {

		try {
			ProducerTemplate template = this.camelContext
					.createProducerTemplate();
			template.start();
		 
			for (int i = 0; i < 10; i++) {
			
			    template.sendBody("direct:starts", "admin");
			}
			template.stop();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		System.out.println("Main App Started...");
	      
		new MainApp().sendMessage("admin");
	 ;
		 
	}

}