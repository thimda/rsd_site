<?xml version="1.0" encoding='UTF-8'?>
<page drivers="PC" isdefault="true" keepstate="false" level="1" ordernum="0" readonly="false" skin="webclassic" template="onerow" undercontrol="false" version="10">
    <title>协同办公</title>
    <layout id="l1" name="simpleLayout" sizes="100%">
		<layout id="l3" name="simpleLayout" sizes="76%,24%">
			<layout id="l11" name="paddingLayout" sizes="100%">
				<layout id="l10" name="vPaddingLayout" sizes="40%,60%">
					<layout id="l4" name="simpleLayout" sizes="100%">
						<portlet id="p2" name="pint:MyFunctionPortlet" theme="rimround"  title="我的功能" column="0" />
						<layout id="l10" name="vPaddingLayout" sizes="100%">
							<portlet id="p3" name="pint:MsgCenterMenuPortlet" theme="rimround"  title="消息中心" column="0" />
						</layout>
					</layout>
					<layout id="l5" name="paddingLayout" sizes="100%">
						<portlet id="p4" name="MockSlidePortlet" theme="clean"  title="幻灯片" column="0" />
						<layout id="l8" name="vPaddingLayout" sizes="100%">
							<portlet id="p5" name="MockCalendarPortlet" theme="vividround"  title="我的日程" column="0" />
						</layout>
						<portlet id="p6" name="pint:MyWflPortlet" theme="rimsquare"  title="代办流程" column="0" />
						<layout id="l9" name="vPaddingLayout" sizes="100%">
							<portlet id="p7" name="MockNewsPortlet" theme="bluerimsquare"  title="总部新闻" column="0" />
						</layout>
					</layout>
				</layout>
			</layout>
			
			<layout id="l6" name="vividLayout" sizes="100%">
				<portlet id="p8" name="MockTaskPortlet" theme="bluerimsquare2"  title="我的任务" column="0" />
				<portlet id="p9" name="MockNoticePortlet" theme="bluerimsquare2"  title="最新发文" column="0" />
				<portlet id="p10" name="MockAddrPortlet" theme="clean"  title="通讯录" column="0" />
				<portlet id="p11" name="MockMeetingPortlet" theme="bluerimsquare2"  title="我的会议" column="0" />
			</layout>
		</layout>
		
		<layout id="l7" name="simpleLayout" sizes="100%">
			<portlet id="p12" name="pserver:CopyRightPortlet" theme="clean"  title="版权" column="0" />
		</layout>
		<portlet id="p13" name="pmng:GrpMgrPortlet" theme="float" title="基础配置管理" column="0" />
	</layout>
</page>
