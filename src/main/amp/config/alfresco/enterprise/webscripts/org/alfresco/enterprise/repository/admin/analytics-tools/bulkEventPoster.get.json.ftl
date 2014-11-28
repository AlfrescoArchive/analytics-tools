<#if resultSize??>
{"status":"SUCCESS","totalRows": "${resultSize?c}","from": "${from}","to": "${to}"}
<#else>
{"status":"FAIL"}
</#if>