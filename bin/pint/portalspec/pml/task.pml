<?xml version="1.0" encoding="UTF-8"?>
<page template="onerow" version="2" isdefault="false" skin="webclassic" level="1" ordernum="15" linkgroup="0000z010000000000004">
	<title>任务中心</title>
	<layout id="l1" name="simpleLayout" sizes="100%">
		<layout id="l11" name="paddingLayout" sizes="100%">
			<portlet id="p3" name="pserver:NavigationPortlet" theme="clean"  title="导航条" column="0" />
			<portlet id="p1" name="TaskCenterPortlet" theme="defaultround" title="任务中心" column="0" />
		</layout>
		<layout id="l7" name="simpleLayout" sizes="100%">
			<portlet id="p12" name="pserver:CopyRightPortlet" theme="clean"  title="版权" column="0" />
		</layout>
		<portlet id="p13" name="pmng:GrpMgrPortlet" theme="float" title="基础配置管理" column="0" />
	</layout>
</page>
