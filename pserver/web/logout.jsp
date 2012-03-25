<% 
	String force = request.getParameter("force");
	if(force != null){
		nc.bs.portal.util.SecurityUtil.addSession(session);
	}
	else{
		nc.bs.portal.util.SecurityUtil.clearLicenseInfo(session);
		session.invalidate();	
		response.sendRedirect("c");
	}
%>