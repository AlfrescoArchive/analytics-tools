package org.alfresco.analytics.activiti;

import org.joda.time.DateTime;

/**
 * Pojo representation of basic activiti process demo data
 *
 * @author Gethin James
 */
public class DemoActivitiProcess
{
    
    private String processId;
    private String definitionKey;
    private DateTime dueTime;
    private DateTime startTime;    
    private DateTime endTime;
    private String user;

    public DemoActivitiProcess(String processId, String definitionKey, DateTime startTime,
                DateTime endTime, DateTime dueTime,  String user)
    {
        super();
        this.processId = processId;
        this.definitionKey = definitionKey;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dueTime = dueTime;
        this.user = user;
    }

    public String getProcessId()
    {
        return this.processId;
    }

    public String getDefinitionKey()
    {
        return this.definitionKey;
    }

    public DateTime getStartTime()
    {
        return this.startTime;
    }

    public DateTime getEndTime()
    {
        return this.endTime;
    }

    public String getUser()
    {
        return this.user;
    }

    public DateTime getDueTime()
    {
        return this.dueTime;
    }

    public void setProcessId(String processId)
    {
        this.processId = processId;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("Activiti Process [processId=").append(this.processId)
                    .append(", definitionKey=").append(this.definitionKey).append(", dueTime=")
                    .append(this.dueTime).append(", startTime=").append(this.startTime)
                    .append(", endTime=").append(this.endTime).append(", user=").append(this.user)
                    .append("]");
        return builder.toString();
    }

}
