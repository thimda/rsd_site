<?xml version="1.0" encoding='UTF-8'?>
<PageMeta caption="页面管理" controllerClazz="nc.portal.page.PageManagerWindowController" id="pagemgr" sourcePackage="pmng/src/public/" windowType="win">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget id="main" refId="main">
        </Widget>
        <Widget id="simplequery" refId="../pubview_simplequery">
        </Widget>
        <Widget id="queryplan" refId="../pubview_queryplan">
        </Widget>
        <Widget id="navorg" refId="../pubview_org">
        </Widget>
        <Widget id="popup" refId="popup">
        </Widget>
        <Widget id="relationView" refId="relationView">
        </Widget>
    </Widgets>
    <Attributes>
        <Attribute>
            <Key>$QueryTemplate</Key>
            <Value>
            </Value>
            <Desc>
            </Desc>
        </Attribute>
    </Attributes>
    <Connectors>
        <Connector id="mainToRelation" pluginId="main_plugin" plugoutId="main_plugout" source="main" target="relationView">
            <Maps>
                <Map inValue="gridRow" outValue="gridRow">
                    <outValue>gridRow</outValue>
                    <inValue>gridRow</inValue>
                </Map>
            </Maps>
        </Connector>
        <Connector id="simpleQuery_to_main" pluginId="simplequery_plugin" plugoutId="qryout" source="simplequery" target="main">
            <Maps>
                <Map inValue="simpleQueryRow" outValue="">
                    <outValue>
                    </outValue>
                    <inValue>simpleQueryRow</inValue>
                </Map>
            </Maps>
        </Connector>
        <Connector id="puborg_to_main" pluginId="org_plugin" plugoutId="orgout" source="navorg" target="main">
            <Maps>
                <Map inValue="orgValue" outValue="">
                    <outValue>
                    </outValue>
                    <inValue>orgValue</inValue>
                </Map>
            </Maps>
        </Connector>
    </Connectors>
</PageMeta>
