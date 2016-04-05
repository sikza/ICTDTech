package infosource.routebuilders;

import infosource.aggregators.SearchAggregation;
import infosource.processors.ProcessSearchResponse;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.aggregate.AggregationStrategy;

public class SearchRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		from("direct:starts").process(new ProcessSearchResponse()).setExchangePattern(ExchangePattern.InOnly)
			.to("activemq:topic:topic/clientSearch?replyTo=queue/SearchResponse").to("activemq:respo");
		;

	}

}