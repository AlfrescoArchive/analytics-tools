<#include "../admin-template.ftl" />
<@page title="Analytics Demo Events" readonly=true>
   <link href="${url.context}/jquery/jquery-ui.min.css" rel="stylesheet">
   <div class="column-full">
      <p class="intro-tall">${msg("tool.analytics.demo.intro-text")?html}</p>
   </div>
   <div class="column-full">
      <@section label=msg("tool.analytics.demo") />
   </div>
   <div class="column-left">
      <div class="control options">
         <span class="label">${msg("Sites")?html}:</span>
         <span class="value">
            <select id="sites" name="sites" tabindex="0" multiple size="10">
              <#list sitesList as s>
                  <option value="${s.shortName?html}">${s.title?html}</option>
               </#list>
            </select>
         </span>
         <span class="description">Choose Multiple Sites (about 5)</span>         
      </div>      
   </div>
   <div class="column-right">
      <div class="control options">
         <span class="label">${msg("People")?html}:</span>
         <span class="value">
            <select id="people" name="people" tabindex="1" multiple size="10">
              <#list people as p>
                  <option value="${p.userName?html}" selected="true">${p.fullName?html}</option>
               </#list>
            </select>
         </span>
         <span class="description">Choose Multiple People (about 10). This list shows all the people in the "ANALYTICS_DEMO_USERS" group.
            Normally, users don't need to be in a specific group to record analytics (only for this demo data).</span>         
      </div>      
   </div>
   <div class="column-full">
      <div class="control field">
         <span class="label">Content source</span>
         <input id="content" tabindex="0" value="workspace://SpacesStore/880a0f47-31b1-4101-b20b-4d325e54e8b1" name="content" style='width:50em'>
         <span class="description">Files to use as demo data. It uses FileFolderServices.listFiles() to find files that are children of the noderef. 
         The files don't have to actually be in the same Site as the Sites selected (above).</span>
      </div>
   </div>   
   <div class="column-left">
      <div class="control field">
         <span class="label">Date From</span>
         <input type="text" id="startDate" name="startDate">
         <span class="description">When should events start?</span>
      </div>
   </div>
   <div class="column-right">
      <div class="control field">
         <span class="label">Date To</span>
         <input type="text" id="endDate" name="endDate">
         <span class="description">When should events end?</span>
      </div>
   </div>
   <div class="column-left">
      <div class="control field">
         <span class="label">Number of Events</span>
         <input id="numberOfValues" tabindex="0" maxlength="1000" value="100" name="numberOfValues" style='width:4em'>
         <span class="description">How many events do you want?</span>
      </div>
   </div>     
   <div class="column-right">
      <@button id="upload-events" label=msg("tool.analytics.generate") description=msg("tool.analytics.generate") />
   </div>
   <div class="column-full">
   </div>
   <div class="column-left">
      <div class="control options">
         <span class="label">${msg("Event types")?html}:</span>
         <span class="value">
            <select id="events" name="events" tabindex="0" multiple size="10">
            </select>
         </span>
         <span class="description">The event types to use.</span>         
      </div>      
   </div>
   <div class="column-right">
      <div class="control options">
         <span class="label">${msg("Client Types")?html}:</span>
         <span class="value">
            <select id="clients" name="clients" tabindex="1" multiple size="4">
            </select>
         </span>
         <span class="description">The type of client</span>         
   </div> 
   <div class="column-full label">
      <@section label=msg("Results") />
         <div class="ui-widget">
           <div id="result" class="ui-state-highlight ui-corner-all" style="margin-top: 20px; padding: 0 .7em;" />
         </div>       
   </div>  
   
  
   
       
   <script type="text/javascript" src="${url.context}/jquery/jquery-2.1.1.min.js"></script>
   <script type="text/javascript" src="${url.context}/jquery/jquery-ui.min.js"></script>   
   <script type="text/javascript">//<![CDATA[
      $.noConflict();
      
      var eventTypes = ["org.alfresco.comments.comment-created",
                    "org.alfresco.comments.comment-updated",
                    "org.alfresco.comments.comment-deleted",
                    "org.alfresco.documentlibrary.file-added",
                    "org.alfresco.documentlibrary.file-created",
                    "org.alfresco.documentlibrary.file-deleted",
                    "org.alfresco.documentlibrary.file-updated",
                    "org.alfresco.documentlibrary.inline-edit",
                    "org.alfresco.documentlibrary.file-previewed",
                    "org.alfresco.documentlibrary.file-downloaded",
                    "org.alfresco.documentlibrary.file-liked"];
      
      var clientTypes = ["webclient","cmis", "webdav"];
      
      jQuery( document ).ready(function( $ ) {
      
        $("#startDate").datepicker({
           dateFormat: "yy-mm-dd"
        });
        $("#endDate").datepicker({
           dateFormat: "yy-mm-dd"
        });
        $("#startDate").datepicker( "setDate", "-6m" );
        $("#endDate").datepicker( "setDate", "-1d" );
        $("#sites option:first").attr('selected','selected');
        
        for (i = 0; i < eventTypes.length; i++) {
          $("#events").append(new Option(eventTypes[i], eventTypes[i]));
        }
        $("#events option").attr('selected','selected');
        
        for (i = 0; i < clientTypes.length; i++) {
          $("#clients").append(new Option(clientTypes[i], clientTypes[i]));
        }
        $("#clients option").attr('selected','selected');
                
        $( "#upload-events" ).click(function( event ) {
           $("#confirmMessage").text("Would you like to create "+$("#numberOfValues").val()+ " events for "+$("#sites option:selected").size()+ " sites and "+$("#people option:selected").size()
                                      + " people. The events will be distributed uniformly from "+$("#startDate").val()+ " to "+$("#endDate").val()+" using "+$("#events option:selected").size()+ " event types and "+$("#clients option:selected").size()+ " client types.");
           $("#dialog-confirm").dialog({
               resizable: false,
               height:250,
               buttons: {
                 "OK": function() {
                    $( this ).dialog( "close" );
                    $.getJSON("${url.serviceContext}/enterprise/admin/admin-analytics-bulkevent",
                              $("#admin-jmx-form").serializeArray(), 
                              function( data ) {
                                   $("#result").append("<br/>Successfully created "+data.totalRows +" events from "+data.from +" to "+data.to +" <br/>");
                             })
                             .fail(function() {
                               $("#result").text("Something went wrong.<br/>");
                             })
                             .always(function() {
                               event.preventDefault();
                             });
                              
                              
                                                 
                 },
                 Cancel: function() {
                   $( this ).dialog( "close" );
                 }
               }
             });
          

        });
      });
//]]></script>
<div id="dialog-confirm" title="Create Demo Events?">
  <p><span style="float:left; margin:0 1px 20px 0;"></span>
  <span id="confirmMessage">&nbsp;</span></p>
</div>
</@page>