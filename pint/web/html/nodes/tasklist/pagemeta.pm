<?xml version="1.0" encoding='UTF-8'?>
<PageMeta caption="流程入口" controllerClazz="nc.uap.portal.task.ctrl.WinController" id="tasklist" sourcePackage="pint/src/public/" windowType="win">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget id="main" refId="main">
        </Widget>
         <Widget id="qry" refId="../pubview_simplequery">
        </Widget>
        <Widget id="queryplan" refId="../pubview_queryplan">
        </Widget>
    </Widgets>
    <Attributes>
        <Attribute>
            <Key>$MODIFY_TS</Key>
            <Value>-1</Value>
            <Desc>
            </Desc>
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
</PageMeta>
