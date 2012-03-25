<?xml version="1.0" encoding='UTF-8'?>
<PageMeta caption="流程退回步">
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
    </Widgets>
    <Listeners>
        <PageListener id="workflownext_defaultListener" serverClazz="nc.uap.lfw.core.event.deft.DefaultPageServerListener">
            <Events>
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
                <Event async="true" name="afterPageInit" onserver="false">
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
<![CDATA[
var widget=pageUI.getWidget("main");
var opinionObj=widget.getComponent("text_opinion");
var common0=widget.getComponent("text_opinion_common")
var listener0 = new TextListener();
listener0.valueChanged = function(mouseEvent) {
   if(common0.getValue()!=null){
      opinionObj.setValue(opinionObj.getValue()+common0.getValue());
   }
}
common0.addListener(listener0);	


var padObj=widget.getComponent("text_pad");
var common1=widget.getComponent("text_pad_common")
var listener1 = new TextListener();
listener1.valueChanged = function(mouseEvent) {
   if(common1.getValue()!=null){
      padObj.setValue(padObj.getValue()+common1.getValue());
   }
}
common1.addListener(listener1);

var channel=widget.getComponent("text_channel");
if(channel!=null){
   var index=channel.getValueIndex('startup');
   var listener2 = new TextListener();
   listener2.valueChanged = function(mouseEvent) {
       var dsNextHumAct=widget.getDataset("ds_nexthumact");
       var currentKey=dsNextHumAct.currentKey;
       dsNextHumAct.removeRowSet(currentKey);
       dsNextHumAct.setCurrentPage(currentKey);
   }  
   channel.checkboxs[index].addListener(listener2);	
}
]]>
                    </Action>
                </Event>
            </Events>
        </PageListener>
    </Listeners>
    <Containers>
    </Containers>
</PageMeta>
