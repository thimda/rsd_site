package portlet
{
	import exception.RuntimeError;
	
	import flash.display.Bitmap;
	import flash.display.BitmapData;
	import flash.display.Graphics;
	import flash.display.Shape;
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.net.FileReference;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.system.ApplicationDomain;
	
	import mx.containers.Canvas;
	import mx.containers.HBox;
	import mx.containers.VBox;
	import mx.controls.Alert;
	import mx.controls.Image;
	import mx.controls.Label;
	import mx.core.Container;
	import mx.core.UIComponent;
	import mx.events.DragEvent;
	import mx.events.ResizeEvent;
	import mx.formatters.DateFormatter;
	import mx.graphics.codec.JPEGEncoder;
	import mx.graphics.codec.PNGEncoder;
	import mx.managers.CursorManager;
	import mx.managers.DragManager;
	import mx.managers.PopUpManager;
	
	import portlet.comp.LayoutElement;
	import portlet.comp.PortletElement;
	
	import utils.IDGenerator;
	
	
	/**
	 * portlet设计界面器容器类,可以在该容器中画出各种元素
	 * */
	//[Event(name="myStatusChanged", type="flash.events.Event")]
	public class Layout extends Node
	{
		protected var _status:String="";
		public var description:String;
		
		public var selectedElement:Element;
		public var selectedElements:Array = new Array();
		
		public var page:Page = new Page();
		
		//layout分割比例
		private var _sizes:String = "";
		public var skin:String = "simpleLayout";
		
		public static const PREFIX:String = "L_";
		
		public var undoXML:XML=new XML("<UndoList><WorkFlows/></UndoList>");
		public var redoXML:XML=new XML("<RedoList/>");
		
		public function Layout(iDrawBoard:Container=null,iXML:XML=null)
		{
			super(iDrawBoard,iXML);
			this.Name = "主布局";			
			this.addEventListener(MouseEvent.CLICK,onClick);
			this.addEventListener(Event.RESIZE,setBackgroud);
			this.addEventListener(DragEvent.DRAG_ENTER,dragEnterHandler);
			this.addEventListener(DragEvent.DRAG_DROP,dragDropHandler);
			
			//this.addEventListener(Event.RESIZE,onResizeHandler);
			init();
		}
		public function init():void{
			//this.setStyle("verticalAlign","middle");
			this.setStyle("horizontalAlign","center");
			this.setStyle("verticalGap",LocalConst.UP);
			
		}
		//[Bindable(event=Event.RESIZE)]
		public function setBackgroud(event:Event):void{
			//只有顶级容器才画网格背景
			if(myDrawBoard!=null)return;
			this.setStyle("backgroundColor", "ffffff");
			var gc:Graphics = this.graphics;
			gc.clear();
			gc.lineStyle(1, 0xe8e8e8);
			
			this.validateNow();
			var width:int = this.width;
			var height:int = this.height;
			var spa:int = 40;
			var hnum:int = width/spa;
			var vnum:int = height/spa;
			//			for(var i:int = 0; i <=vnum; i ++) {
			//				gc.moveTo(0, i  * spa);
			//				gc.lineTo(width, i  * spa);
			//			}
			//			for(var j:int = 0; j <=hnum; j ++) {
			//				gc.moveTo(j * spa,0);
			//				gc.lineTo(j * spa,height);
			//			}
			//设置背景图片
			//this.setStyle("backgroundImage","images/pic/lv.gif");
			
			event.stopPropagation();
		}
		public function onResizeHandler(event:Event):void{
			var children:Array = this.getChildren();
			
			for (var i:int=0;i<children.length;i++) {
				if (!(children[i] is LayoutPanel))continue;
				var panel:LayoutPanel = children[i];
				setLayoutLocV(panel);
				this.adjustLayouttSize(panel);
			}
		}
		public function onClick(event: MouseEvent):void {
			if(myDrawBoard==null){
				this.UnSelectedAllElement();
				selectedElement = this;
				var evt:ElementEvent=new ElementEvent(ElementEvent.ELEMENT_SELECT_CHANGED);
				evt.srcElement=this;
				this._selected = true;
				this.dispatchEvent(evt);				
			}
			event.stopPropagation();
		}
		
		public function get Status(): String{
			return _status;
		}
		//设置当前界面的状态
		public function set Status(value: String): void{
			this._status = value;
		}
		
		public function AddUndo() :void {
			this.getTopLayout().undoXML.appendChild(new XML(this.getTopLayout().BuildXml().toXMLString()));
			//trace("AddUndo:\n"+this.getTopLayout().undoXML.length()+"\n"+this.getTopLayout().undoXML.toString());
		}
		
		//撤销
		public function Undo() :void {
			var workflows:XMLList= this.getTopLayout().undoXML..Definitions;
			if (workflows.length()>1) {
				var index:int=workflows.length()-2;
				var xml:XML=new XML(workflows[index+1].toString());
				//trace(xml);
				this.getTopLayout().redoXML.appendChild(xml);
				delete workflows[index+1];
				//trace(workflows[index].toString());	
				this.getTopLayout().ParseFromXml(workflows[index]);
			}
		}
		
		//重做
		public function Redo() :void {
			var workflows:XMLList= this.getTopLayout().redoXML..Definitions;
			//trace("redoXML:\n"+redoXML);
			if (workflows.length()>0) {
				var index:int=workflows.length()-1;
				var xml:XML=new XML(workflows[index].toString());
				//trace(xml.toString());
				this.getTopLayout().undoXML.appendChild(xml);
				this.ParseFromXml(workflows[index]);
				delete workflows[index];
			}
		}
		//删除元素节点
		public function DeleteElement(iElement:Element):void{
			if(!this.contains(iElement))return;
			this.removeChild(iElement);				
		}
		
		//快捷键处理
		public function onKeyDown(event:KeyboardEvent):void {			
			var keyCode:int = event.keyCode;
			//Alert.show( event.keyCode.toString());
			if (keyCode==46){//Delete
				var children:Array = this.getChildren();				
				for (var i:int=0;i<children.length;i++) {
					if (!(children[i] is Element))continue;
					var ele:Element = children[i] as Element;
					if(ele.Selected==true){
						this.DeleteElement(ele);
					}
				}				
				AddUndo();
			}
			else if ((keyCode==90) && event.ctrlKey){//Ctrl+Z Undo
				this.Undo();
			}
			else if ((keyCode==89) && event.ctrlKey){//Ctrl+Y Redo
				this.Redo();
			}			
			this.setFocus();            //只有得到焦点的组件才能监听键盘事件
		}
		
		//转化为xml
		override public function BuildXml(): XML{	
			var root:XML=page.BuildXml();
			var title:XML = new XML("<title/>");
			title.appendChild(this.page.title);
			root.appendChild(title);
			root.appendChild(BuildLayoutXml(this,-1));
			return root;
		}
		
		//Layout节点信息转化为xml
		public function BuildLayoutXml(layout:Layout,column:int): XML{
			var xml:XML=new XML("<layout/>");
			xml.@id = IDGenerator.addPrefix(PREFIX,layout.ID);
			xml.@name = layout.skin;
			//xml.@skin = layout.skin;
			//if()xml.@sizes = layout.Sizes;
			if(column>-1)xml.@column = column;
			
			var childs:Array = layout.getChildren();			
			for(var i:int=0;i<childs.length;i++){
				if(!(childs[i] is Element)&&!(childs[i] is LayoutPanel))continue;
				if(childs[i] is LayoutPanel){
					var panel:LayoutPanel = childs[i];
					var layouts:Array = panel.getChildren();
					if(layouts.length>=1)xml.@sizes = layout.Sizes;
					for(var k:int=0;k<layouts.length;k++){
						xml.appendChild(BuildLayoutXml(layouts[k] as Layout,k));
					}
				}
				else if(childs[i] is Portlet)
					xml.appendChild((childs[i] as Portlet).BuildXml());
			}
			return xml;
		}
		//验证sizes的正确性(格式,逗号数量)
		public function validateSizes(layout:Layout):String{
			var childs:Array = layout.getChildren();	
			var errorStr:String = "";			
			for(var i:int=0;i<childs.length;i++){
				if(!(childs[i] is LayoutPanel))continue;
				var panel:LayoutPanel = childs[i];
				var layouts:Array = panel.getChildren();
				var layoutNum:int = 0;
				for(var k:int=0;k<layouts.length;k++){
					if(layouts[k] is Layout){
						++layoutNum;
					}
				}
				var regStr:String = "(\\d+%?\\,){"+(layoutNum-1)+"}\\d+%?";
				var pattern:RegExp = new RegExp(regStr,"g");
				var result:Object = pattern.exec(layout.Sizes);
				if(result==null||result[0]!=layout.Sizes){
					errorStr+="id="+layout.ID+"布局的  宽度比例  设置错误";
				}
				//				else if(layout.Sizes.split(",").length!=layoutNum){
				//					errorStr+="id="+layout.ID+" 的 sizes 属性错误\n";
				//					//throw new RuntimeError(errorStr);
				//				}
				//对子递归
				for(var m:int=0;m<layouts.length;m++){
					if(layouts[m] is Layout){
						errorStr += validateSizes(layouts[m]);
					}
				}
			}
			return errorStr;
		}
		
		//清空当前界面
		public function Clear():void{
			this.removeAllChildren();			
		}	
		
		//从xml文件中解析节点到界面显示
		override public function ParseFromXml(iXML:XML): int{
			this.Clear();
			if(iXML == null||iXML=="")
				return 0;
			var pageXml:XML = iXML;			
			page.parseXml(pageXml);
			page.parseTitle(pageXml.title[0]);
			
			var layoutXml:XML = pageXml.layout[0];
			parseLayout(this,layoutXml);
			
			return 0;
		}
		
		public function parseLayout(layout:Layout,iXML:XML):void{	
			if(iXML==null||iXML=="")return;
			layout.ID = IDGenerator.delPrefix(PREFIX,iXML.@id);
			//layout.Name = iXML.@name;
			layout._sizes = iXML.@sizes;
			layout.skin = iXML.@name;
			
			var elements:XMLList = iXML.children();//iXML.portlet+iXML.layout;
			var layoutElements:XMLList = iXML.layout;
			var portletElements:XMLList = iXML.portlet;
			var maxColumn:int = 0;
			for each(var temp:XML in layoutElements){
				var column:int = int(temp.@column)+1;
				if(maxColumn<column) 
					maxColumn = column;
			}
			var flag:Boolean = false;
			for each (var elementXml:XML in elements) {
				if(flag==false&&elementXml.name()=='layout'){
					//var num:int = layoutElements.length();
					var layouts:Array = new Array();
					if(maxColumn>1){
						layouts = addLayout(maxColumn,layout);				
						for(var i:int=0;i<maxColumn;i++){
							var layout_i:LayoutNode = layouts[i];					
							layout_i.ParseFromXml(layoutElements[i]);
							//递归解析layout
							layout_i.parseLayout(layout_i,layoutElements[i]);
						}
						flag = true;
					}
					else {
						layouts = addLayout(maxColumn,layout);
						var layout_0:LayoutNode = layouts[0];					
						layout_0.ParseFromXml(elementXml);
						//递归解析layout
						layout_0.parseLayout(layout_0,elementXml);	
					}
				}					
				else if(elementXml.name()=='portlet'){
					var pt:Node = new Portlet(layout); 
					pt.ParseFromXml(elementXml);
					pt.Draw();
				}					
			}
			//			var layoutElements:XMLList = iXML.layout;
			//			var portletElements:XMLList = iXML.portlet;
			//			if(layoutElements!=null&&layoutElements.length()>0){				
			//				var num:int = layoutElements.length();
			//				var layouts:Array = addLayout(num,layout);				
			//				for(var i:int=0;i<num;i++){
			//					var layout_i:LayoutNode = layouts[i];					
			//					layout_i.ParseFromXml(layoutElements[i]);
			//					//递归解析layout
			//					layout_i.parseLayout(layout_i,layoutElements[i]);
			//				}					
			//			}
			//			
			//			for each (var elementXml:XML in portletElements) {
			//				var pt:Node = new Portlet(layout); 
			//				pt.ParseFromXml(elementXml);
			//				pt.Draw();
			//			}
		}		
		
		//产生界面元素id
		public function GetNewElementID():String{
			var maxId:int = 0;
			var childs:Array = this.getChildren();
			var childs2:Array = new Array();
			//如果是layoutpanel，则把其中的lalyout拿出来
			for(var j:int=0;j<childs.length;j++) {
				if(!(childs[j] is Element)&&!(childs[j] is LayoutPanel))continue;				
				if(childs[j] is LayoutPanel){
					var layoutPanel:LayoutPanel = childs[j] as LayoutPanel;
					var layouts:Array = layoutPanel.getChildren();
					for(var k:int=0;k<layouts.length;k++){
						if(layouts[k] is Layout){
							childs2.push(layouts[k]);
						}
					}
					childs.splice(j,1);
					j--;
				}
			}
			for each(var temp:Layout in childs2){
				childs.push(temp);
			}
			for(var i:int=0;i<childs.length;i++) {
				if(!(childs[i] is Element))continue;
				var myelement:Element = childs[i] as Element;	
				var m:int = -1;
				if(myelement.ID.lastIndexOf("_")==-1)
					m = int(myelement.ID);
				else m = int(myelement.ID.substring(myelement.ID.lastIndexOf("_")+1,myelement.ID.length));				
				if (m>maxId) {
					maxId = int(m);
				}
			}
			++maxId;
			return maxId+"";
		}
		
		//通过元素节点id获得节点
		public function GetElementFromID(iID:String):Element{
			var childs:Array = this.getChildren();
			for (var i:int=0;i<childs.length;i++) {
				if(!(childs[i] is Element))continue
				var myelement:Element = childs[i];
				if (myelement.ID==iID) {
					return myelement;
				}
			}
			return null;
		}
		
		//得到所有子节点
		public function getAllNodes():Array{
			var children:Array = this.getChildren();
			var array:Array = new Array();
			for(var i:int=0;i<children.length;i++){
				if(children[i] is Node)
					array.push(children[i]);
			}
			return array;	
		}	
		
		//取消所有元素的选择
		public function UnSelectedAllElement():void{
			var childs:Array = this.getChildren();
			for(var i:int=0;i<childs.length;i++) {
				if(!(childs[i] is Element)&&!(childs[i] is LayoutPanel))continue;
				var myelement:Element;
				if(childs[i] is LayoutPanel)(childs[i] as LayoutPanel).UnSelectedAllElement();
				else if(childs[i] is Element){
					myelement = childs[i];
					if (myelement.Selected) {
						myelement.Selected=false;
					}
					if(myelement is Layout){
						var childLayout:Layout = myelement as Layout;
						childLayout.UnSelectedAllElement();
					}
				}
			}
		}
		
		/**
		 * 得到最顶级Layout
		 **/
		public function getTopLayout():Layout{
			var topLayout:Layout = this;
			while(topLayout.parent is Layout||topLayout.parent is LayoutPanel){
				if(topLayout.parent is Layout)
					topLayout = topLayout.parent as Layout;
				else if(topLayout.parent is LayoutPanel)
					topLayout = (topLayout.parent as LayoutPanel).TruePanel;
			}
			return topLayout;
		}
		
		//保存为图片
		public function saveAsImage(url:String):void{
			var bitmapData:BitmapData=new BitmapData(this.width,this.height);
			bitmapData.draw(this);
			//var bitmap:Bitmap = new Bitmap(bitmapData);
			
			var request:URLRequest = new URLRequest(url);
			request.method=URLRequestMethod.POST;
			request.contentType = "application/octet-stream";
			request.data = new JPEGEncoder(100).encode(bitmapData);
			var loader:URLLoader = new URLLoader();
			loader.load(request);
			loader.addEventListener(Event.COMPLETE,saveImageComplete);					
		}
		
		private function saveImageComplete(event:Event):void{
			Alert.show("图片保存成功");
		}
		
		//拖入的节点进入容器时的处理
		public  function dragEnterHandler(event:DragEvent):void {   
			if (event.dragSource.hasFormat("baseElement"))   
			{   
				DragManager.acceptDragDrop(event.currentTarget as UIComponent);   
			}   
			else if(event.dragSource.hasFormat("movePortlet")){
				DragManager.acceptDragDrop(event.currentTarget as UIComponent);   
			}
			else if (event.dragSource.hasFormat("treeItems"))   
			{   
				DragManager.acceptDragDrop(event.currentTarget as UIComponent);   
			}   
		}  		
		
		//拖入的节点元素被放下时的处理
		public  function dragDropHandler(event:DragEvent):void {   
			var dragObject:UIComponent=UIComponent(event.dragInitiator);   
			var node:Portlet;
			if(dragObject is PortletElement){
				if(this.getTopLayout().containsPortlet((dragObject as PortletElement).Name))return;
				node = new Portlet(this); 
				node.Name = (dragObject as PortletElement).Name;
				node.Title = (dragObject as PortletElement).Title;
				node.Draw();
			}
			else if(dragObject is LayoutElement){
				openNumDialog();			
			}
			else if(dragObject is Portlet){
				var pt:Portlet = dragObject as Portlet;
				if(pt.parent==this||!this.containsPortlet(pt.Name))movePortlet(event);
			}
			AddUndo();
		}
		//判断当前topLalyout中是否包含了portalet
		public function containsPortlet(id:String):Boolean{
			var flag:Boolean = false;
			var childs:Array = this.getChildren();
			for (var i:int=0;i<childs.length;i++){
				if(childs[i] is Layout){
					flag = (childs[i] as Layout).containsPortlet(id);
					if(flag==true)return flag;
				}
				else if(childs[i] is LayoutPanel){
					var layouts:Array = (childs[i] as LayoutPanel).getChildren();
					for (var j:int=0;j<layouts.length;j++){
						flag = (layouts[j] as Layout).containsPortlet(id);
						if(flag==true)return flag;
					}
				}
				else if(childs[i] is Portlet){
					var pt:Portlet = childs[i] as Portlet;
					if(pt.Name==id)return true;
				}
			}
			return flag;
			
			//			var childs:Array = this.getChildren();
			//			for (var i:int=0;i<childs.length;i++){
			//				if(!(childs[i] is Portlet))continue;
			//				var pt:Portlet = childs[i] as Portlet;
			//				if(pt.Name==id)return true;
			//			}
			//			return false;
		}
		public function movePortlet(event:DragEvent):void{
			var dragPortlet:Portlet=UIComponent(event.dragInitiator) as Portlet;   
			var oldParent:Layout = dragPortlet.parent as Layout;
			var locx:Number = event.localX;
			var locy:Number = event.localY;
			var i:int = getIndexByPoint(new Point(locx,locy));
			if(i!=-1)this.addChildAt(dragPortlet,i);
			else this.addChild(dragPortlet);
			adjustPortletSize(dragPortlet);
			dragPortlet.Draw();
		}
		//根据父容器调整portlet的大小
		public function adjustPortletSize(dragPortlet:Portlet):void{
			//只调整宽度，高度用默认值
			dragPortlet.Mywidth = dragPortlet.parent.width-LocalConst.LEFT*2;
			dragPortlet.Draw();
		}
		
		//调整layout(及其子元素)的大小
		public function adjustLayouttSize(layoutpanel:LayoutPanel):void{
			var childs:Array = layoutpanel.getChildren();
			var layouts:Array = new Array();
			for(var k:int=0;k<childs.length;k++){
				if(childs[i] is Layout){
					layouts.push(childs[k]);
				}
			}
			//平均宽度
			var avewidth:Number = (layoutpanel.width)/layouts.length-LocalConst.LEFT;
			for(var i:int=0;i<layouts.length;i++){
				var layout:Layout = layouts[i];
				layout.Mywidth = avewidth;
				layout.Myheight = layout.parent.height-LocalConst.TOP;
				layout.Draw();
				var layoutchilds:Array = layout.getChildren();
				for(var j:int=0;j<layoutchilds.length;j++){
					if (layoutchilds[j] is Portlet)adjustPortletSize(layoutchilds[j]);
					else if(layoutchilds[j] is LayoutPanel){
						//把虚面板的宽度也重设置一下
						(layoutchilds[j] as LayoutPanel).width = avewidth;
						adjustLayouttSize(layoutchilds[j]);
					}
				}
			}		
		}
		
		private function getIndexByPoint(point:Point):int{
			var childs:Array = this.getChildren();
			//因为第0个是lable
			var index:int = -1;
			for(var i:int=0;i<childs.length;i++){
				if(!(childs[i] is Element))continue;
				if(childs[i].x<=point.x&&childs[i].y<=point.y&&(childs[i].x+childs[i].width)>=point.x&&(childs[i].y+childs[i].height)>=point.y){
					index = i;
				}
			}
			return index;
		}
		
		private function openNumDialog():void{
			var numdialog:NumberConfirm = NumberConfirm(PopUpManager.createPopUp(this,NumberConfirm,true)); 
			numdialog.x = Math.ceil((screen.width-numdialog.width)/2); 
			numdialog.y = Math.ceil((screen.height-numdialog.height)/2); 			
			numdialog.callbackFunction = numCallback;
			numdialog.mainApp = this;		
			PopUpManager.centerPopUp(numdialog);
		}
		
		private function numCallback(num:int):void{
			addLayout(num,this);
		}	
		//批量增加layout
		private function addLayout(num:int,parent:Layout):Array{
			var panel:LayoutPanel = new LayoutPanel(parent);
			setLayoutLocV(panel);
			//add by limingf  默认宽度比例
			if(num==1&&(parent.Sizes==null||parent.Sizes=="")){
				parent.Sizes = "100%";
			}
			var layouts:Array = new Array();
			for(var i:int=0;i<num;i++){
				var layout:LayoutNode = new LayoutNode(panel);
				//平均宽度
				var avewidth:Number = (layout.parent.width)/num-LocalConst.LEFT;
				layout.Mywidth = avewidth;
				layout.Myheight = layout.parent.height-LocalConst.TOP-LocalConst.BOTTOM;
				layout.Draw();		
				layouts.push(layout);
			}	
			return layouts;
		}
		//增加一个layout，num:一个layout里layout总数量
		private function addOneLayout(num:int,parent:Layout):Array{
			var panel:LayoutPanel = new LayoutPanel(parent);
			setLayoutLocV(panel);
			var layouts:Array = new Array();
			var layout:LayoutNode = new LayoutNode(panel);
			//平均宽度
			var avewidth:Number = (layout.parent.width)/num-LocalConst.LEFT;
			layout.Mywidth = avewidth;
			layout.Myheight = layout.parent.height-LocalConst.TOP-LocalConst.BOTTOM;
			layout.Draw();		
			layouts.push(layout);				
			return layouts;
		}
		
		public function setLayoutLocV(node:Container):void{			
			node.width = this.width;
			node.height = this.height;			
		}
		
		public function set Sizes(value:String):void{
			this._sizes = value;
		}
		public function get Sizes():String{
			return this._sizes;
		}
	}
}