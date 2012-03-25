<%@ page contentType="text/html; charset=UTF-8"%>
<!-- 
	提供基于Iframe的跨域访问机制 
	异域中新建一个iframe指向本页面,传入eval参数,执行Portal中提供的js方法.
-->
<%
	String eval = request.getParameter("eval");
%>

<script>
	parent.parent.<%= eval%>;
</script>

