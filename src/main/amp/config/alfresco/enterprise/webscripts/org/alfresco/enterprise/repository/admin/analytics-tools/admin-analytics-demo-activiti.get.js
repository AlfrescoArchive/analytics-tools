<import resource="classpath:alfresco/enterprise/webscripts/org/alfresco/enterprise/repository/admin/admin-common.lib.js">

/**
 * Repository Admin Console
 * 
 * Analytics tools
 */
model.tools = Admin.getConsoleTools("admin-analytics-demo-activiti");
model.people = groups.getGroup("ANALYTICS_DEMO_USERS").getAllUsers();