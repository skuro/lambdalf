[#ftl]
[#if swank??]
{"result" : ${swank}}
[#else]
{"error" : "Error stopping the swank server."}
[/#if]
