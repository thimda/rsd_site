<html>
	<head>
		<link rel="stylesheet" type="text/css" href="solr-admin.css">
		<link rel="icon" href="favicon.ico" type="image/ico"></link>
		<link rel="shortcut icon" href="favicon.ico" type="image/ico"></link>
		<title>Welcome to Index Server</title>
	</head>

	<body>
		<h1>
			Welcome to Index Server!
		</h1>

		<%
			org.apache.solr.core.CoreContainer cores = (org.apache.solr.core.CoreContainer) request
					.getAttribute("org.apache.solr.CoreContainer");
			if (cores != null
					&& cores.getCores().size() > 0){
					//&& cores.getCores().iterator().next().getName().length() != 0) {
					org.apache.solr.core.SolrCore[] subcores =  cores.getCoresArray();
					for(int i=0;i<subcores.length;i++){
					org.apache.solr.core.SolrCore core = subcores[i];
		%>
		<a href="/portal/solr<%=core.getName()%>/admin/">Admin <%=core.getName()%></a>
		<br />
		<%
			}
			} else {
		%>
		<a href="admin/">Index Server Admin</a>
		<%
			}
		%>

	</body>
</html>
