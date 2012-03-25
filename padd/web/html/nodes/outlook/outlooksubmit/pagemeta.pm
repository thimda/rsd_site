<?xml version="1.0" encoding='UTF-8'?>
<PageMeta caption="流程数据台帐">
    <Processor>nc.uap.lfw.core.processor.EventRequestProcessor</Processor>
    <PageStates currentState="1">
        <PageState>
            <Key>1</Key>
            <Name>卡片显示</Name>
        </PageState>
        <PageState>
            <Key>2</Key>
            <Name>列表显示</Name>
        </PageState>
    </PageStates>
    <Widgets>
        <Widget id="main" refId="main">
        </Widget>
        <Widget id="sign" refId="sign">
        </Widget>
        <Widget id="users" refId="users">
        </Widget>
    </Widgets>
    <Listeners>
        <PageListener id="FlwDataPageServerListener" serverClazz="nc.portal.pwfm.listener.FlwDataPageServerListener">
            <Events>
            <!--
                <Event async="false" name="onClosed" onserver="true">
                    <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                    </SubmitRule>
                    <Params>
                        <Param>
                            <Name>
                            </Name>
                            <Value>
                            </Value>
                            <Desc>                                <![CDATA[]]>
                            </Desc>
                        </Param>
                    </Params>
                    <Action>
                    </Action>
                </Event>
                <Event async="true" name="afterPageInit" onserver="true">
                    <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                    </SubmitRule>
                    <Params>
                        <Param>
                            <Name>
                            </Name>
                            <Value>
                            </Value>
                            <Desc>                                <![CDATA[]]>
                            </Desc>
                        </Param>
                    </Params>
                    <Action>
                    </Action>
                </Event>
                <Event async="false" name="onClosing" onserver="false">
                    <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                    </SubmitRule>
                    <Params>
                        <Param>
                            <Name>
                            </Name>
                            <Value>
                            </Value>
                            <Desc>                                <![CDATA[]]>
                            </Desc>
                        </Param>
                    </Params>
                    <Action>                        <![CDATA[pageUI.showCloseConfirm();]]>
                    </Action>
                </Event>
                -->
            </Events>
        </PageListener>
    </Listeners>
    <Menus>
        <MenuBarComp id="menu_task">
            <Listeners>
                <MouseListener id="taskListener" serverClazz="nc.portal.pdbl.listener.PdblMouseListener">
                </MouseListener>
            </Listeners>
            <MenuItem displayHotKey="CTRL+SHIFT+" id="submitoraudit" modifiers="3" text="提交或审核">
                <Listeners>
                    <MouseListener id="auditListener">
                        <Events>
                            <Event async="true" name="onclick" onserver="false">
                                <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                                    <Widget cardSubmit="false" id="main" panelSubmit="false" tabSubmit="false">
                                        <Dataset id="ds_attachlist" type="ds_all_line">
                                        </Dataset>
                                    </Widget>
                                </SubmitRule>
                                <Params>
                                    <Param>
                                        <Name>mouseEvent</Name>
                                        <Value>
                                        </Value>
                                        <Desc>                                            <![CDATA[]]>
                                        </Desc>
                                    </Param>
                                </Params>
                                <Action>
                                	submit();
                                </Action>
                            </Event>
                        </Events>
                    </MouseListener>
                </Listeners>
            </MenuItem>
            <MenuItem id="reject" modifiers="2" text="退回">
                <Listeners>
                    <MouseListener id="pwfmlistener">
                        <Events>
                            <Event async="true" name="onclick" onserver="false">
                                <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                                    <Widget cardSubmit="false" id="main" panelSubmit="false" tabSubmit="false">
                                        <Dataset id="ds_attachlist" type="ds_all_line">
                                        </Dataset>
                                    </Widget>
                                </SubmitRule>
                                <Params>
                                    <Param>
                                        <Name>mouseEvent</Name>
                                        <Value>
                                        </Value>
                                        <Desc>                                            <![CDATA[]]>
                                        </Desc>
                                    </Param>
                                </Params>
                                <Action>
                                	recject();
                                </Action>
                            </Event>
                        </Events>
                    </MouseListener>
                </Listeners>
            </MenuItem>
            <MenuItem id="transmit" modifiers="2" text="转发">
                <Listeners>
                    <MouseListener id="assinListener" serverClazz="nc.portal.pdbl.listener.PdblMouseListener">
                        <Events>
                            <Event async="true" name="onclick" onserver="true">
                                <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                                </SubmitRule>
                                <Params>
                                    <Param>
                                        <Name>mouseEvent</Name>
                                        <Value>
                                        </Value>
                                        <Desc>                                            <![CDATA[]]>
                                        </Desc>
                                    </Param>
                                </Params>
                                <Action>
                                </Action>
                            </Event>
                        </Events>
                    </MouseListener>
                </Listeners>
            </MenuItem>
            <MenuItem displayHotKey="CTRL+SHIFT+" id="addsign" modifiers="3" text="加签">
                <Listeners>
                    <MouseListener id="addsateListener" serverClazz="nc.portal.pdbl.listener.PdblMouseListener">
                        <Events>
                            <Event async="true" name="onclick" onserver="true">
                                <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                                </SubmitRule>
                                <Params>
                                    <Param>
                                        <Name>mouseEvent</Name>
                                        <Value>
                                        </Value>
                                        <Desc>                                            <![CDATA[]]>
                                        </Desc>
                                    </Param>
                                </Params>
                                <Action>
                                </Action>
                            </Event>
                        </Events>
                    </MouseListener>
                </Listeners>
            </MenuItem>
            <MenuItem displayHotKey="CTRL+SHIFT+" id="sign" modifiers="3" text="签收">
                <Listeners>
                    <MouseListener id="assignopinionListener" serverClazz="nc.portal.pdbl.listener.PdblMouseListener">
                        <Events>
                            <Event async="true" name="onclick" onserver="true">
                                <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                                </SubmitRule>
                                <Params>
                                    <Param>
                                        <Name>mouseEvent</Name>
                                        <Value>
                                        </Value>
                                        <Desc>                                            <![CDATA[]]>
                                        </Desc>
                                    </Param>
                                </Params>
                                <Action>
                                </Action>
                            </Event>
                        </Events>
                    </MouseListener>
                </Listeners>
            </MenuItem>
            <!--
            <MenuItem id="print" modifiers="2" text="打印预览">
                <Listeners>
                    <MouseListener id="printListener" serverClazz="nc.portal.pdbl.listener.PdblMouseListener">
                        <Events>
                            <Event async="true" name="onclick" onserver="true">
                                <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                                </SubmitRule>
                                <Params>
                                    <Param>
                                        <Name>mouseEvent</Name>
                                        <Value>
                                        </Value>
                                        <Desc>                                            <![CDATA[]]>
                                        </Desc>
                                    </Param>
                                </Params>
                                <Action>
                                </Action>
                            </Event>
                        </Events>
                    </MouseListener>
                </Listeners>
            </MenuItem>
            -->
            <MenuItem id="back" modifiers="2" text="返回">
                <Listeners>
                    <MouseListener id="backListener">
                        <Events>
                            <Event async="true" name="onclick" onserver="false">
                                <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                                </SubmitRule>
                                <Params>
                                    <Param>
                                        <Name>mouseEvent</Name>
                                        <Value>
                                        </Value>
                                        <Desc>                                            <![CDATA[]]>
                                        </Desc>
                                    </Param>
                                </Params>
                                <Action>
                                	window.external.Close();
                                </Action>
                            </Event>
                        </Events>
                    </MouseListener>
                </Listeners>
            </MenuItem>
        </MenuBarComp>
    </Menus>
    <Containers>
    </Containers>
</PageMeta>
