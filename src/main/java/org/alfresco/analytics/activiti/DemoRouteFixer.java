package org.alfresco.analytics.activiti;

import java.util.EventObject;

import org.alfresco.events.activiti.ProcessEvent;
import org.alfresco.events.activiti.TaskEvent;
import org.alfresco.messaging.camel.routes.IsEventTypePredicate;
import org.alfresco.messaging.camel.routes.IsTypePredicate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.support.EventNotifierSupport;
import org.springframework.beans.factory.InitializingBean;

/**
 * DemoRouteFixer, changes the route definitions whilst Camel
 * is starting up and adds in some special enrichers
 *
 * @author Gethin James
 */
public class DemoRouteFixer implements InitializingBean
{
    private ModelCamelContext camelContext;    
    private static IsTypePredicate isProcessEvent = new IsEventTypePredicate(ProcessEvent.class);
    private static IsTypePredicate isTaskEvent = new IsEventTypePredicate(TaskEvent.class);
    private static String routeId = "Activiti SourceEvents -> Enrichers";
    
    @Override
    public void afterPropertiesSet() throws Exception
    {
        camelContext.getManagementStrategy().addEventNotifier(new DemoSetupEventNotifier()); 
    }

    public RouteDefinition weave(String routeId, AdviceWithRouteBuilder builder) throws Exception 
    {
        return camelContext.getRouteDefinition(routeId).adviceWith(camelContext, builder);
    }
    
    public void modifyRouteDefinition() throws Exception
    {   
        weave(routeId, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
               weaveAddFirst()
               .choice().when(isProcessEvent).beanRef("demoProcessEnricher").end()
               .choice().when(isTaskEvent).beanRef("demoTaskEnricher").end();
            }
        });
    }

    public void setCamelContext(ModelCamelContext camelContext)
    {
        this.camelContext = camelContext;
    }

    public class DemoSetupEventNotifier extends EventNotifierSupport
    {

        @Override
        public void notify(EventObject event) throws Exception
        {
            //No Op       
        }

        @Override
        public boolean isEnabled(EventObject event)
        {
            return true;
        }

        @Override
        protected void doStart() throws Exception
        {
            //At this point in the lifecycle change the route definition
            modifyRouteDefinition();
        }
        
    }
}
