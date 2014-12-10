package org.alfresco.analytics.event;

import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.model.ModelCamelContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Helps when using Camel
 *
 * @author Gethin James
 */
@Component
public class CamelHelper
{
    private static Log logger = LogFactory.getLog(CamelHelper.class);
    
    @Autowired(required=false)
    private ModelCamelContext alfrescoCamelContext;

    public boolean replaceRoute(String routeId, RoutesBuilder builder)
    {
        boolean removed = false;
        
        try
        {
            removed = alfrescoCamelContext.removeRoute(routeId);
            if (removed)
            {
                alfrescoCamelContext.addRoutes(builder);
            }            
        }
        catch (Exception error)
        {
            logger.error("Failed to remove the route.", error);
        }

        return removed ;
    }
    
    public void weave(String routeId, AdviceWithRouteBuilder builder) throws Exception 
    {
        alfrescoCamelContext.getRouteDefinition(routeId).adviceWith(alfrescoCamelContext, builder);
    }
}
