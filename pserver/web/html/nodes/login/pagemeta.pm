<?xml version="1.0" encoding='UTF-8'?>
<PageMeta>
    <Processor>nc.uap.lfw.core.processor.EventRequestProcessor</Processor>
    <PageStates>
    </PageStates>
    <Widgets>
        <Widget id="main" refId="main">
        </Widget>
    </Widgets>
    <Listeners>
        <PageListener id="pagelistener" serverClazz="nc.uap.portal.login.PortalLoginPageListener">
            <Events>
                <Event async="true" name="afterPageInit" onserver="true">
                    <Params>
                        <Param>
                            <Name>
                            </Name>
                            <Value></Value>
                            <Desc>                                <![CDATA[]]>
                            </Desc>
                        </Param>
                    </Params>
                    <Action>                        <![CDATA[]]>
                    </Action>
                </Event>
                
                <Event async="false" name="onClosed" onserver="true">
                    <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                    </SubmitRule>
                    <Params>
                        <Param>
                            <Name>
                            </Name>
                            <Value></Value>
                            <Desc>                                <![CDATA[]]>
                            </Desc>
                        </Param>
                    </Params>
                    <Action>
                    </Action>
                </Event>
            </Events>
        </PageListener>
    </Listeners>
    <Containers>
    </Containers>
</PageMeta>
