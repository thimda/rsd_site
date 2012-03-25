<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"><%@ page contentType="text/html;charset=UTF-8" %><%@ taglib uri="http://www.ufida.com/lfw" prefix="lfw" %><%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %><html xmlns="http://www.w3.org/1999/xhtml">	<head>		<title>UFIDA NC</title>		<lfw:base/>		<lfw:head/>		<lfw:import />	</head>	<body>		<lfw:pageModel>			<lfw:borderLayout>
<lfw:borderPanel position="center">
</lfw:borderPanel>
<lfw:widget id="main">
<lfw:form id="credential_form" widget='main'/>

<lfw:flowhLayout>
<lfw:flowhPanel>
</lfw:flowhPanel>
<lfw:flowhPanel width="80">
<lfw:button id="submit" widget='main'/>
</lfw:flowhPanel>
<lfw:flowhPanel width="80">
<lfw:button id="cancel" widget='main'/>
</lfw:flowhPanel>
<lfw:flowhPanel width="80">
<lfw:button id="giveup" widget='main'/>
</lfw:flowhPanel>
<lfw:flowhPanel>
</lfw:flowhPanel>
</lfw:flowhLayout>
</lfw:widget>
</lfw:borderLayout>
		</lfw:pageModel>	</body></html>