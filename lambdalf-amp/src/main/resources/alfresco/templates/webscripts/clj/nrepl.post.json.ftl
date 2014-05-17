[#ftl]
[#if port??]
{"port" : ${port}}
[#else]
{"error" : "Error starting nREPL server."}
[/#if]
