<#include "../admin-template.ftl" />
<@page title="Analytics Admin Tools" readonly=true>
   <div class="column-full">
      <p class="intro-tall">${msg("tool.analytics.demo.intro-text")?html}</p>
   </div>
   <div class="column-full">
      <@section label=msg("tool.analytics.demo") />
   </div>
   <div class="column-left">
       <@options id="sites-select" name="sites" label=msg("sites")?html value=selectedTLS>
         <@option label=msg("inboundemail.transport-layer-security.tls-support.disabled")?html value="" />
         <@option label=msg("inboundemail.transport-layer-security.tls-support.hidden")?html value="email.server.hideTLS" />
         <@option label=msg("inboundemail.transport-layer-security.tls-support.enabled")?html value="email.server.enableTLS" />
         <@option label=msg("inboundemail.transport-layer-security.tls-support.required")?html value="email.server.requireTLS" />
      </@options>
   </div>
   <div class="column-full">
      <@section label=msg("tool.analytics.generate") />
   </div>   
   <div class="column-left">
      <@button id="upload-license" label=msg("tool.analytics.generate") description=msg("tool.analytics.generate") onclick="Admin.showDialog('${url.serviceContext}/enterprise/admin/admin-analytics-bulkevent?startDate=2014-09-01&count=200');" />
   </div>
</@page>