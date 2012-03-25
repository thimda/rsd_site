<?xml version="1.0" encoding='UTF-8'?>
<Application TagName="Application" caption="页面管理app" controllerClazz="nc.uap.portal.mng.page.PageManagerWindowController" defaultWindowId="pagemgr" id="pagemgrApp" sourcePackage="pmng/src/public/">
    <PageMetas>
        <PageMeta caption="页面管理" id="pagemgr">
        </PageMeta>
        <PageMeta caption="模板分配" id="cp_templateassign">
        </PageMeta>
    </PageMetas>
    <Connectors>
     	<Connector id="assinToRelateion" pluginId="Assign_plugin" plugoutId="plugout_gridrow" source="main" target="relationView" sourceWindow="cp_templateassign"  targetWindow="pagemgr">
        	<Maps>
        		<Map inValue="assignRow" outValue="plugoutRow">
        			<outValue>plugoutRow</outValue>
        			<inValue>assignRow</inValue>
        		</Map>
        	</Maps>
        </Connector>
    </Connectors>
</Application>
