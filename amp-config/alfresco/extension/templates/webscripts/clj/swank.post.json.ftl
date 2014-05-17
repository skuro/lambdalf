[#ftl]
[#if port??]
{"port" : ${port}}
[#else]
{"error" : "Error starting swank server."}
[/#if]
