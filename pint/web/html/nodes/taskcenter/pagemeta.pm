<?xml version="1.0" encoding='UTF-8'?>
<PageMeta caption="任务中心" controllerClazz="nc.uap.portal.task.ctrl.WinController" id="taskcenter" sourcePackage="pint/src/public/" windowType="win">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget id="main" refId="main">
        </Widget>
        <Widget id="category" refId="category">
        </Widget>
        <Widget id="qry" refId="../pubview_simplequery">
        </Widget>
        <Widget id="queryplan" refId="../pubview_queryplan">
        </Widget>
    </Widgets>
    <Attributes>
        <Attribute>
            <Key>$MODIFY_TS</Key>
            <Value>1331023239779</Value>
        </Attribute>
    </Attributes>
    <Events>
        <Event async="true" jsEventClaszz="nc.uap.lfw.core.event.conf.PageListener" methodName="sysWindowClosed" name="onClosed" onserver="true">
            <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
            </SubmitRule>
            <Params>
                <Param>
                    <Name>event</Name>
                    <Value>
                    </Value>
                    <Desc>                        <![CDATA[nc.uap.lfw.core.event.PageEvent]]>
                    </Desc>
                </Param>
            </Params>
            <Action>
            </Action>
        </Event>
    </Events>
    <Connectors>
        <Connector id="taskstatus_plugin" pluginId="doFilterTaskStatus" plugoutId="doFilterTaskStatus" source="category" target="main">
            <Maps>
                <Map inValue="taskstatus" outValue="taskstatus">
                    <outValue>taskstatus</outValue>
                    <inValue>taskstatus</inValue>
                </Map>
            </Maps>
        </Connector>
        <Connector id="simpleQuery_to_main" pluginId="simplequery_plugin" plugoutId="qryout" source="qry" target="main">
            <Maps>
                <Map inValue="simpleQueryRow" outValue="">
                    <outValue>
                    </outValue>
                    <inValue>simpleQueryRow</inValue>
                </Map>
            </Maps>
        </Connector>
    </Connectors>
</PageMeta>
