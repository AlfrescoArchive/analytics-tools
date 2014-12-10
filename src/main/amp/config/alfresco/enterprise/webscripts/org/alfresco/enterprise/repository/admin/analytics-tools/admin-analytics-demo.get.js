<import resource="classpath:alfresco/enterprise/webscripts/org/alfresco/enterprise/repository/admin/admin-common.lib.js">

/**
 * Repository Admin Console
 * 
 * Analytics tools
 */
model.tools = Admin.getConsoleTools("admin-analytics-demo");
model.sitesList = siteService.listSites(null, null);
model.people = groups.getGroup("ANALYTICS_DEMO_USERS").getAllUsers();