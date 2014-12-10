<#if resultSize??>
{"status":"SUCCESS","requested": "${requested?c}","totalRows": "${resultSize?c}","from": "${from}","to": "${to}"}
<#else>
{"status":"FAIL"}
</#if>