<?xml version="1.0" encoding="UTF-8"?>
<!-- edited with XMLSpy v2005 rel. 3 U (http://www.altova.com) by  () -->
<page template="adminonerow" version="1" isdefault="false" skin="webclassic" level="0"  linkgroup="0000z010000000000002"  ordernum="15">
	<title>系统管理</title>
	<layout id="l1" name="simpleLayout" sizes="100%">
		<layout id="l2" name="paddingLayout" sizes="100%">
			<portlet id="p3" name="pserver:NavigationPortlet" theme="clean"  title="导航条" column="0" />
			<portlet id="p2" name="MgrContentPortlet" theme="clean"  title="管理内容" column="0" />
		</layout>
		<layout id="l3" name="simpleLayout" sizes="100%">
			<portlet id="p4" name="pserver:CopyRightPortlet" theme="clean"  title="版权" column="0" />
		</layout>
		
		<portlet id="p1" name="GrpMgrPortlet" theme="float" title="基础配置管理" column="0" />
	</layout>
</page>
