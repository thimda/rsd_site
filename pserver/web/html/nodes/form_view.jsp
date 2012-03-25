<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="nc.uap.portal.constant.WebKeys"%>
<body onload="document.myform.submit();">
<%
	String pid = (String)request.getParameter("portletId");
	String formUrlKey = "form_url" + pid;
	String fieldsMapKey = "fieldsMap" + pid;
	String formUrl = (String)session.getAttribute(formUrlKey);
	String target = (String)session.getAttribute("target" + pid);
	String tar = (target == null) ? "" : "target='"+target+"'";
	java.util.HashMap fieldsMap = (java.util.HashMap)session.getAttribute(fieldsMapKey);
%>
<form name="myform" method="POST" action="<%=formUrl%>"  <%= tar %> >
	<c:forEach var="field" items="<%=fieldsMap%>">
		<input type="hidden" name="${field.key}" value="${field.value}"/>
	</c:forEach>
</form>
<%
	session.removeAttribute(formUrlKey);
	session.removeAttribute(fieldsMapKey);
%>
</body>
