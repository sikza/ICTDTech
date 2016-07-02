package infosource.routebuilders;

import infosource.aggregators.SearchAggregation;
import infosource.processors.ProcessSOAPOUT;
import infosource.processors.ProcessSearchResponse;
import infosource.processors.UserAccountSearchProcessor;
import infosource.remote.service.resources.BCServiceResource;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.model.dataformat.SoapJaxbDataFormat;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.springframework.stereotype.Component;
import org.apache.camel.component.http.*;

import webframeapp.interfaces.FindByCreator;

@Component
public class SearchRouteBuilder extends RouteBuilder {
	/*
	 * KEEP THIS from("direct:go") .beanRef("domainServiceClient",
	 * "createDomain") .setHeader("operationName", constant("createDomainsss"))
	 * .
	 * setHeader("operationNamespace",constant("http://interfaces.webFrameApp/")
	 * ).process(new ProcessSOAPOUT()) .to("cxf:bean:ws");
	 */
	@Override
	public void configure() throws Exception {

		from("direct:go")
				.beanRef("domainServiceClient", "createDomain")
				.setHeader("operationName", constant("createDomainsss"))
				.setHeader("operationNamespace",
						constant("http://interfaces.webFrameApp/"))
				.process(new ProcessSOAPOUT())
				.to("cxf:bean:ws");
		/*
	
		from("direct:start1")
				.beanRef("userAccountClient", "getPersons")
				.setHeader("operationName", constant("getPersons"))
				.setHeader("operationNamespace",
						constant("http://services.soap.useraccount/"))
				.process(new UserAccountSearchProcessor())
				.bean("cxf:bean:userAccount");
		
		 * from("direct:start1") .setHeader( Exchange.HTTP_METHOD,
		 * constant(org.apache.camel.component.http4.HttpMethods.GET))
		 * .to("http4://localhost:8080/BCManager/certificate/id/1")
		 * .beanRef("BCProcessor", "getCertificateResponse") .to("stream:out");
		 */
		from("activemq:queue:queue.birthIDs").to("stream:out");
	}
}
