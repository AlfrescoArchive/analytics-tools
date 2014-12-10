package org.alfresco.analytics.activiti;

import org.alfresco.events.activiti.ProcessEvent;
import org.alfresco.messaging.camel.routes.IsEventTypePredicate;
import org.alfresco.messaging.camel.routes.IsTypePredicate;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.StartupListener;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.springframework.beans.factory.InitializingBean;

/**
 * DemoRouteFixer
 *
 * @author Gethin James
 */
public class DemoRouteFixer implements InitializingBean, StartupListener
{

    private ModelCamelContext camelContext;    
    private static IsTypePredicate isProcessEvent = new IsEventTypePredicate(ProcessEvent.class);
    
    @Override
    public void afterPropertiesSet() throws Exception
    {
       // camelContext.addStartupListener(this);
    }

    public void weave(String routeId, AdviceWithRouteBuilder builder) throws Exception 
    {
        camelContext.getRouteDefinition(routeId).adviceWith(camelContext, builder);
    }
    
    @Override
    public void onCamelContextStarted(CamelContext context, boolean alreadyStarted)
                throws Exception
    {   String routeId = "Activiti SourceEvents -> Enrichers";
        weave(routeId, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
               weaveAddFirst()
               .choice().when(isProcessEvent).beanRef("demoProcessEnricher").end();
            }
        });
        RouteDefinition def = camelContext.getRouteDefinition(routeId);
        def.markPrepared();
        camelContext.addRouteDefinition(def);
        Route route = camelContext.getRoute(routeId);
        System.out.println(route);
        System.out.println("startable: "+def.isStartable(context));
        camelContext.startRoute(routeId);
        route = camelContext.getRoute(routeId);
        System.out.println(def.getStatus(context));
    }

    public void setCamelContext(ModelCamelContext camelContext)
    {
        this.camelContext = camelContext;
    }

}
