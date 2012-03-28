<?xml version="1.0" encoding='UTF-8'?>
<PageMeta caption="消息中心" controllerClazz="nc.uap.portal.msg.ctrl.CenterController" id="center" sourcePackage="pint/src/public/" windowType="win">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget id="main" refId="main">
        </Widget>
        <Widget id="cate" refId="cate">
        </Widget>
        <Widget id="qry" refId="../pubview_simplequery">
        </Widget>
        <Widget id="queryplan" refId="../pubview_queryplan">
        </Widget>
    </Widgets>
    <Attributes>
        <Attribute>
            <Key>$MODIFY_TS</Key>
            <Value>1329100806700</Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    
    <Connectors>
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
