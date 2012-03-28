<?xml version="1.0" encoding='UTF-8'?>
<PageMeta caption="单点登陆配置" controllerClazz="nc.uap.portal.sso.setting.SSOConfPageController" id="ssoconf" sourcePackage="pint/src/public/" windowType="win">
    <Processor>nc.uap.lfw.core.event.AppRequestProcessor</Processor>
    <Widgets>
        <Widget id="main" refId="main">
        </Widget>
        <Widget id="setPropView" refId="setPropView">
        </Widget>
        <Widget id="ipMappingView" refId="ipMappingView">
        </Widget>
        <Widget id="ssoView" refId="ssoView">
        </Widget>
        <Widget id="simplequery" refId="../pubview_simplequery">
        </Widget>
        <Widget id="queryplan" refId="../pubview_queryplan">
        </Widget>
    </Widgets>
    <Attributes>
        <Attribute>
            <Key>$MODIFY_TS</Key>
            <Value>1330047433683</Value>
        </Attribute>
    </Attributes>
    <Events>
        <Event async="true" jsEventClaszz="nc.uap.lfw.core.event.conf.PageListener" methodName="afterPageInit_ssoconf" name="afterPageInit" onserver="true">
            <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
            </SubmitRule>
            <Params>
                <Param>
                    <Name>pageEvent</Name>
                    <Desc>                        <![CDATA[nc.uap.lfw.core.event.PageEvent]]>
                    </Desc>
                </Param>
            </Params>
            <Action>
            </Action>
        </Event>
    </Events>
    <Menus>
        <MenuBarComp id="ssomenu">
            <MenuItem displayHotKey="CTRL+SHIFT+" id="add" modifiers="3" text="新建">
                <Listeners>
                    <MouseListener id="menu_item_add_listener" serverClazz="nc.portal.sso.listener.SsoConfUifAddMouseListener">
                        <Events>
                            <Event async="true" name="onclick" onserver="true">
                                <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                                    <Widget cardSubmit="true" id="main" panelSubmit="false" tabSubmit="false">
                                        <Dataset id="ssoSystemsDs" type="ds_current_page">
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
                                </Action>
                            </Event>
                        </Events>
                    </MouseListener>
                </Listeners>
            </MenuItem>
            <MenuItem displayHotKey="CTRL+SHIFT+" id="save" modifiers="3" text="保存">
                <Listeners>
                    <MouseListener id="menu_item_save_listener" serverClazz="nc.portal.sso.listener.SsoConfUifSaveMouseListener">
                        <Events>
                            <Event async="true" name="onclick" onserver="true">
                                <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                                    <Widget cardSubmit="false" id="main" panelSubmit="false" tabSubmit="false">
                                        <Dataset id="ssoSystemsDs" type="ds_all_line">
                                        </Dataset>
                                        <Dataset id="ipMappingDs" type="ds_all_line">
                                        </Dataset>
                                        <Dataset id="referenceDs" type="ds_all_line">
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
                                </Action>
                            </Event>
                        </Events>
                    </MouseListener>
                </Listeners>
            </MenuItem>
            <MenuItem displayHotKey="CTRL+SHIFT+" id="delete" modifiers="3" text="删除">
                <Listeners>
                    <MouseListener id="menu_item_delete_listener" serverClazz="nc.portal.sso.listener.SsoConfUifDelMouseListener">
                        <Events>
                            <Event async="true" name="onclick" onserver="true">
                                <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                                    <Widget cardSubmit="false" id="main" panelSubmit="false" tabSubmit="false">
                                        <Dataset id="ssoSystemsDs" type="ds_all_line">
                                        </Dataset>
                                        <Dataset id="ipMappingDs" type="ds_all_line">
                                        </Dataset>
                                        <Dataset id="referenceDs" type="ds_all_line">
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
                                </Action>
                            </Event>
                        </Events>
                    </MouseListener>
                </Listeners>
            </MenuItem>
            <MenuItem displayHotKey="CTRL+SHIFT+" id="edit" modifiers="3" text="修改">
                <Listeners>
                    <MouseListener id="menu_item_edit_listener" serverClazz="nc.portal.sso.listener.SsoConfUifEditMouseListener">
                        <Events>
                            <Event async="true" name="onclick" onserver="true">
                                <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                                    <Widget cardSubmit="true" id="main" panelSubmit="false" tabSubmit="false">
                                        <Dataset id="ssoSystemsDs" type="ds_current_page">
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
                                </Action>
                            </Event>
                        </Events>
                    </MouseListener>
                </Listeners>
            </MenuItem>
            <MenuItem displayHotKey="CTRL+SHIFT+" id="cancel" modifiers="3" text="取消">
                <Listeners>
                    <MouseListener id="menu_item_cancel_listener" serverClazz="nc.portal.sso.listener.SsoConfUifCancelMouseListener">
                        <Events>
                            <Event async="true" name="onclick" onserver="true">
                                <SubmitRule cardSubmit="false" panelSubmit="false" tabSubmit="false">
                                    <Widget cardSubmit="false" id="main" panelSubmit="false" tabSubmit="false">
                                        <Dataset id="ssoSystemsDs" type="ds_current_page">
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
                                </Action>
                            </Event>
                        </Events>
                    </MouseListener>
                </Listeners>
            </MenuItem>
        </MenuBarComp>
    </Menus>
</PageMeta>
