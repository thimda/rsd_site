package workflow
{
	import design.StatusConst;
	
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
	import mx.controls.Alert;
	import mx.controls.Image;
	import mx.core.Container;
	import mx.core.UIComponent;
	import mx.events.DragEvent;
	import mx.formatters.DateFormatter;
	import mx.graphics.codec.JPEGEncoder;
	import mx.graphics.codec.PNGEncoder;
	import mx.managers.CursorManager;
	import mx.managers.DragManager;
	
	import wevent.WStatusEvent;
	
	import workflow.comp.*;
	import workflow.decorator.DrawLineDecorator;
	import workflow.decorator.SelectCanvas;
	import workflow.service.DrawBoardParser;
	import workflow.service.RuntimeXmlBuilder;
	import workflow.utils.HashMap;
	
	/**
	 * 流程设计界面器容器类,可以在该容器中画出各种元素
	 * */
	//[Event(name="myStatusChanged", type="flash.events.Event")]
	public class DrawBoard extends Node
	{
		protected var _status:String="";
		public var description:String;
		public var tmpElement:Element;
		//public static  const STATUSCHANGED:String="myStatusChanged";
		//public static  const SELECT_CHANGED:String="SelectChanged";
		[Bindable]
		public var selectedElement:Element;
		public var selectedElements:Array = new Array();
		public var selectCanvas:SelectCanvas;
		public var fromElement:Node;
		public var toElement:Node;
		private var clickPoint:Point;                                   //鼠标点击位置
		//[Embed(source="images/logo/user.swf")]			
		//private var moveCursor:Class;	
		
		//可发起子流程(pk,name)
		public var startProdefs:Object = {pk_prodef:null,prodefName:null};
		//是否是浏览模型，如果是浏览模式才解析中文显示
		public var browseMode:Boolean = false;
		//是否是精简模式(不带网关...)
		public var simpleMode:Boolean = false;
		
		public var undoXML:XML=new XML("<UndoList><WorkFlows/></UndoList>");
		public var redoXML:XML=new XML("<RedoList/>");
		
		public function DrawBoard(iDrawBoard:DrawBoard=null,iXML:XML=null)
		{
			super(iDrawBoard,iXML);
			this.Name = "主流程";
			this.addEventListener(MouseEvent.CLICK,onClick);
			this.addEventListener(MouseEvent.MOUSE_DOWN,onMouseDown);
			this.addEventListener(MouseEvent.MOUSE_UP,onMouseUp);
			this.addEventListener(MouseEvent.MOUSE_MOVE,onMouseMove);
			this.addEventListener(KeyboardEvent.KEY_DOWN,onKeyDown);
			this.addEventListener(Event.RESIZE,setBackgroud);
									
			this.addEventListener(DragEvent.DRAG_ENTER,dragEnterHandler);
			//this.addEventListener(DragEvent.DRAG_OVER,dragOverHandler);
			this.addEventListener(DragEvent.DRAG_DROP,dragDropHandler);
			this.addEventListener(WStatusEvent.STATUS_CHANGED,onStatusChanged);
		}
		override public function removeAllListener():void{
			this.removeEventListener(MouseEvent.CLICK,onClick);
			this.removeEventListener(MouseEvent.MOUSE_DOWN,onMouseDown);
			this.removeEventListener(MouseEvent.MOUSE_UP,onMouseUp);
			this.removeEventListener(MouseEvent.MOUSE_MOVE,onMouseMove);
			this.removeEventListener(KeyboardEvent.KEY_DOWN,onKeyDown);
			this.removeEventListener(WStatusEvent.STATUS_CHANGED,onStatusChanged);
		}
		public function setListenerEnable(enable:Boolean):void{
			if(enable==false){
				this.removeAllListener();
				var childs:Array = this.getChildren();
				for(var i:int=0;i<childs.length;i++){
					if(!(childs[i] is Element))continue;
					var ele:Element = childs[i] as Element;
					ele.removeAllListener();
					if(childs[i] is DrawBoard){
						var drawboard:DrawBoard = childs[i] as DrawBoard;
						drawboard.setListenerEnable(false);
					}
				}
			}
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
			for(var i:int = 0; i <=vnum; i ++) {
				gc.moveTo(0, i  * spa);
				gc.lineTo(width, i  * spa);
			}
			for(var j:int = 0; j <=hnum; j ++) {
				gc.moveTo(j * spa,0);
				gc.lineTo(j * spa,height);
			}
			//设置背景图片
			//this.setStyle("backgroundImage","images/pic/lv.gif");
			
			event.stopPropagation();
		}
	
		public function onStatusChanged(event:WStatusEvent):void{
			if(event.status==StatusConst.ROUTE_DRAW||event.status==StatusConst.ROUTE_BEGIN){
				var nodes:Array = this.getAllNodes();
				for each(var node:Node in nodes){
					var drawLineDec:DrawLineDecorator = node.drawLineDec;
					if(drawLineDec!=null)drawLineDec.decorate();
				}
			}
			else{
				var nodes2:Array = this.getAllNodes();
				for each(var node2:Node in nodes2){
					var drawLineDec2:DrawLineDecorator = node2.drawLineDec;
					if(drawLineDec2!=null)drawLineDec2.undecorate();
				}
			}
		}
		
		public function onClick(event: MouseEvent):void {
			if(myDrawBoard==null&&this.Status=="click"){
				this.UnSelectedAllElement();
				var evt:ElementEvent=new ElementEvent(ElementEvent.ELEMENT_SELECT_CHANGED);
				evt.srcElement=this;
				this._selected = true;
				this.dispatchEvent(evt);
				selectedElements.splice(0,selectedElements.length);
				if(selectCanvas!=null){
					this.removeChild(selectCanvas);
					selectCanvas = null;
				}
				CursorManager.removeAllCursors();
				this.Status = "";
			}

			//this.Status = "";
			//this.setFocus();
		}
		public function onMouseDown(event:MouseEvent):void{
			//只支持对顶级容器的拖拉多选
			if(myDrawBoard==null&&this.Status==""){
				this.Status = "click";
				clickPoint = new Point(mouseX,mouseY);
				event.stopPropagation();
			}
		}
		
		//如果是画线状态，当鼠标释放时，画出节点间的连线
		public function onMouseUp(event: MouseEvent):void{
//			if (this.Status=="routebegin"&&this.toElement!=null){
//				var nodeCheck:Boolean = true;
////				nodeCheck=((this.fromElement is WorkNode) && (this.toElement is WorkNode));//业务环节-业务环节
////				nodeCheck=nodeCheck || ((this.fromElement is BeginNode) && (this.toElement is WorkNode));//开始环节-业务环节
////				nodeCheck=nodeCheck || ((this.fromElement is BeginNode) && (this.toElement is SubprocessNode));//开始环节-子流程环节
////				nodeCheck=nodeCheck || ((this.fromElement is WorkNode) && (this.toElement is SubprocessNode));//业务环节-子流程环节
////				nodeCheck=nodeCheck || ((this.fromElement is SubprocessNode) && (this.toElement is EndNode));//子流程环节-结束环节
////				nodeCheck=nodeCheck || ((this.fromElement is SubprocessNode) && (this.toElement is WorkNode));//子流程环节-结束环节
////				nodeCheck=nodeCheck || ((this.fromElement is WorkNode) && (this.toElement is EndNode));//业务环节-结束环节
//				
//				nodeCheck = !(this.fromElement is EndNode);                 //fromElement不能是结束节点
//				nodeCheck = nodeCheck /*&& !(this.toElement is BeginNode)*/;    //toElement不能是开始节点
//				
//				nodeCheck = nodeCheck && tmpElement is Route;
//				if(this.fromElement == this.toElement)return;				
//				nodeCheck = nodeCheck && checkExistRoute(this.fromElement,this.toElement);
//				if (nodeCheck==true) {
//					var myroute:Route=tmpElement as Route;
//					myroute.fromElement=this.fromElement;
//					myroute.toElement=this.toElement;
//					myroute.Name="名称";
//					myroute.Draw();
//					AddUndo();
//			  	}
//			  	else {
//			  		if (tmpElement!=null) { 
//			  			this.removeChild(tmpElement);
//			  		}
//			  	}
//				tmpElement=null;
//				this.fromElement=null;
//				this.toElement=null;
//				this.Status="";
//				if(this.parent is DrawBoard){
//					(this.parent as DrawBoard).Status = "";
//				}
//			}
			//如果是拖动节点大小，则鼠标放开时，设置为非拖动大小状态
			if(this.Status=="dragable"||this.Status=="routeDragable"||this.Status=="routeMoveble"){
				this.Status="";
				this.AddUndo();
			}
			//拖动鼠标选择节点
			if(this.Status=="selectNodes"){
				var area:Rectangle = new Rectangle(Math.min(mouseX,clickPoint.x),Math.min(mouseY,clickPoint.y),Math.abs(clickPoint.x-mouseX),Math.abs(clickPoint.y-mouseY));
				if(selectCanvas!=null&&this.contains(selectCanvas)){
					
				}
				var children:Array = this.getChildren();
				selectedElements.splice(0,selectedElements.length);
				this.UnSelectedAllElement();
				for(var i:int=0;i<children.length;i++){
					if(!(children[i] is Element))continue;
					var ele:Element = children[i];
										
					if(ele is Node){
						var nodeRect:Rectangle = new Rectangle(ele.x,ele.y,ele.width,ele.height);
						if(area.containsRect(nodeRect)){
							ele.Selected = true;
							selectedElements.push(ele);
						}
					}
					else if(ele is Route){
						var r:Route = ele as Route;
						if(area.contains(r.fromX,r.fromY)&&area.contains(r.toX,r.toY)){
							ele.Selected = true;
							selectedElements.push(ele);	
						}
					}
				}
				if(selectedElements==null||selectedElements.length<1){
					this.removeChild(selectCanvas);
					selectCanvas = null;
				}
				this.Status = "";
			}
		}
		
		public function onMouseMove(event: MouseEvent):void{
			//如果是画线状态，当鼠标移动时，箭头跟着鼠标移动
			if(this.Status=="routebegin"){
				var myroute:Route = this.tmpElement as Route;
				if (myroute==null) {
					return;
				  	}
				else{ 
				   myroute.toX=mouseX;
			       myroute.toY=mouseY;
			       myroute.Draw();
					}
			  	}
			//改变选中节点的大小
			else if(this.Status=="dragable"){
				var node:Node = this.selectedElement as Node;
				var rects:Array = node.decorator.rects;
				var witch:int = -1;
				for(var i:int=0;i<rects.length;i++){
					if(rects[i]==true)witch=i;
				}
				var rect:Rectangle = node.getBounds(node.myDrawBoard);
				//trace("x:"+node.x+" y:"+node.y+" \nwidth:"+node.width+" height:"+node.height);
				//trace("localx:"+event.localX+" localy:"+event.localY);
				var loc:Point = stage.localToGlobal(new Point(node.x,node.y));
				
				//根据拖动不同的位置，进行不同的改变（条件判断还有问题,0,1,3）
				if(witch==0){	 //判断条件问题
//					if(event.stageX>loc.x&&event.stageY>loc.y){ 
//						node.x = node.x + event.localX;
//						node.y = node.y + event.localY;
//						node.width = node.width - event.localX;
//						node.height = node.height - event.localY;
//					}
//					else if(event.stageX<loc.x&&event.stageY<loc.y){
//						node.width = node.width+(node.x-event.localX);
//						node.height = node.height+(node.y-event.localY);
//						node.x = event.localX;
//						node.y = event.localY;
//					}
				}
				else if(witch==1){	 //判断条件问题
//					if(!rect.contains(event.localX,event.localY)){
//						node.height = node.height+(node.y-event.localY);
//						node.y = event.localY;
//					}
//					else{
//						node.height = node.height - event.localY;
//						node.y = node.y + event.localY;	
//					} 
				}
				else if(witch==3){	   //判断条件问题
//					if(event.localX<node.width){
//						node.x = node.x + event.localX;
//						node.width = node.width - event.localX;
//					}
//					else{
//						node.width = node.width + (node.x - event.localX);
//						node.x = event.localX;	
//					}
				}
				else if(witch==2){	
					if(event.localX<node.width){
						node.y = node.y + event.localY;
						node.width = event.localX;
						node.height = node.height - event.localY;
					}
					else {
						node.width = event.localX - node.x;
						node.height = node.height+(node.y-event.localY);
						node.y = event.localY;							
					}			
				}
				else if(witch==4){	 
					if(event.localX<=node.width)node.width = event.localX;
					else if(event.localX>node.width) node.width = event.localX - node.x;
				}
				else if(witch==5){  
					if(event.localY<node.height){
						node.x = node.x + event.localX;
						node.width = node.width - event.localX;
						node.height = event.localY;
					}
					else {
						node.width = node.width + (node.x - event.localX);
						node.height = event.localY-node.y;
						node.x = event.localX;							
					}			
				}
				else if(witch==6){	 //判断条件问题
					if(event.localY<=node.height)node.height = event.localY;
					else if(event.localY>node.width)node.height = event.localY - node.y;	
				}				
				else if(witch==7){
					if(event.localX<=node.width)node.width = event.localX;
					else if(event.localX>node.width) node.width = event.localX - node.x;
				
					if(event.localY<=node.height)node.height = event.localY;
					else if(event.localY>node.width)node.height = event.localY - node.y;					
				}
				if(node.width<MIN_WIDTH)node.width = MIN_WIDTH;
				if(node.height<MIN_HEIGHT)node.height = MIN_HEIGHT;
				node.Draw();
			}
			else if(this.Status=="routeDragable"){
				var route:Route = this.selectedElement as Route;
				var point1:Point = route.turnPoints[route.currentTurnPointId];
				var point2:Point = route.turnPoints[route.currentTurnPointId+1];
				if(route.turnType==Route.LEFT_RIGHT){
					point1.x = event.localX;
					point2.x = event.localX;
				}
				if(route.turnType==Route.UP_DOWN){
					point1.y = event.localY;
					point2.y = event.localY;
				}
				route.Draw();
			}
			else if(this.Status=="routeMoveble"){
				var route2:Route = this.selectedElement as Route;
				route2.toElement = null;
				this.Status = "routebegin";
				this.tmpElement = route2;
				route2.Selected = false;
			}
			else if(this.Status=="nodeStartDrag"){
				var element:Element = this.selectedElement;
				ReDrawRelationElement(element);
//				var routes:Array = this.getRoutesByNode(element as Node);
//				for(var r:int=0;r<routes.length;r++){
//					var tmpRoute:Route = routes[r];
//					var changePoint:Point;
//					if(tmpRoute.fromElement==element){
//						changePoint = tmpRoute.turnPoints[0];
//						changePoint.y = tmpRoute.fromY;
//					}
//					else if(tmpRoute.toElement==element){
//						changePoint = tmpRoute.turnPoints[tmpRoute.turnPoints.length-1];
//						changePoint.y = tmpRoute.toY;
//					}
//				}
			}
			else if(this.Status=="click"||this.Status=="selectNodes"){
				if(selectCanvas==null)selectCanvas = new SelectCanvas(this);
				selectCanvas.width = Math.abs(mouseX-clickPoint.x);
				selectCanvas.width = Math.abs(mouseY-clickPoint.y);
//				selectCanvas.x = Math.min(mouseX,clickPoint.x);
//				selectCanvas.y = Math.min(mouseY,clickPoint.y);
				this.addChild(selectCanvas);
				drawSelectRect(selectCanvas,mouseX,mouseY,clickPoint.x,clickPoint.y);
				this.Status = "selectNodes";
			}
		}
		//画鼠标拖动选择框
		private function drawSelectRect(target:UIComponent,x1:int,y1:int,x2:int,y2:int):void{
			var gc:Graphics = target.graphics;
			gc.clear();
			gc.lineStyle(0,0xffffff);
			var pattern:Array = new Array(5,3);
			gc.beginFill(0x00ff00,0.1);				
			gc.drawRect(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x2-x1),Math.abs(y2-y1));
			gc.endFill();
			gc.lineStyle(1,0xff0000);
			drawDashLine(target,x1,y1,x1,y2,pattern);
			drawDashLine(target,x1,y2,x2,y2,pattern);
			drawDashLine(target,x2,y2,x2,y1,pattern);
			drawDashLine(target,x2,y1,x1,y1,pattern);
		}
		//画虚线
		private function drawDashLine(target:UIComponent,x1:int,y1:int,x2:int,y2:int,pattern:Array):void{
			var gc:Graphics = target.graphics;			
			var x:Number = x2 - x1;			
			var y:Number = y2 - y1;			
			var hyp:Number = Math.sqrt((x)*(x) + (y)*(y));				
			var units:Number = hyp/(pattern[0]+pattern[1]);			
			var dashSpaceRatio:Number = pattern[0]/(pattern[0]+pattern[1]);
			
			var dashX:Number = (x/units)*dashSpaceRatio;			
			var spaceX:Number = (x/units)-dashX;			
			var dashY:Number = (y/units)*dashSpaceRatio;			
			var spaceY:Number = (y/units)-dashY;	
			gc.moveTo(x1, y1);				
			while (hyp > 0)				
			{				
				x1 += dashX;				
				y1 += dashY;				
				hyp -= pattern[0];				
				if (hyp < 0)					
				{					
					x1 = x2;					
					y1 = y2;					
				}
				gc.lineTo(x1, y1);				
				x1 += spaceX;				
				y1 += spaceY;				
				gc.moveTo(x1, y1);				
				hyp -= pattern[1];				
			}
			gc.moveTo(x2, y2);
		}
		//检测两个节点间是否已经有了连线
		public function checkExistRoute(fromNode:Node,toNode:Node):Boolean{
			var routes:Array = getAllRoutes();
			for each(var route:Route in routes){
				if(route.fromElement == fromNode&&route.toElement == toNode)
					return false;
			}
			return true;
		}
		//取消所有元素的选择
		public function UnSelectedAllElement():void{
			var childs:Array = this.getChildren();
			for(var i:int=0;i<childs.length;i++) {
				if(!(childs[i] is Element))continue;
				var myelement:Element = childs[i];
				if (myelement.Selected) {
					myelement.Selected=false;
				}
				if(myelement is DrawBoard){
					var childDrawBoard:DrawBoard = myelement as DrawBoard;
					childDrawBoard.UnSelectedAllElement();
				}
			}
		}
		
		public function get Status(): String{
			return _status;
		}
		//设置当前界面的状态
		public function set Status(value: String): void{
			trace("_status:"+value);
			this._status = value;
			var e:WStatusEvent =new WStatusEvent(WStatusEvent.STATUS_CHANGED);
			e.status = value;
			dispatchEvent(e);
		}
		
		public function AddUndo() :void {
			this.getTopDrawBoard().undoXML.appendChild(new XML(this.getTopDrawBoard().BuildXml().toXMLString()));
			//trace("AddUndo:\n"+this.getTopDrawBoard().undoXML.length()+"\n"+this.getTopDrawBoard().undoXML.toString());
		}
		
		//撤销
		public function Undo() :void {
			var workflows:XMLList= this.getTopDrawBoard().undoXML..Definitions;
			if (workflows.length()>1) {
				var index:int=workflows.length()-2;
				var xml:XML=new XML(workflows[index+1].toString());
				//trace(xml);
				this.getTopDrawBoard().redoXML.appendChild(xml);
				delete workflows[index+1];
				//trace(workflows[index].toString());	
				this.getTopDrawBoard().ParseFromXml(workflows[index]);
			}
		}
		
		//重做
		public function Redo() :void {
			var workflows:XMLList= this.getTopDrawBoard().redoXML..Definitions;
			//trace("redoXML:\n"+redoXML);
			if (workflows.length()>0) {
				var index:int=workflows.length()-1;
				var xml:XML=new XML(workflows[index].toString());
				//trace(xml.toString());
				this.getTopDrawBoard().undoXML.appendChild(xml);
				this.ParseFromXml(workflows[index]);
				delete workflows[index];
			}
		}
	    //删除元素节点
		public function DeleteElement(iElement:Element):void{
			if(!this.contains(iElement))return;
			this.removeChild(iElement);
			//如果是节点，删除上面的关联线 
			if(iElement is Node){
				for each(var r:Route in getRoutesByNode(iElement as Node)){
					this.removeChild(r);
					//删除线上的条件
					if(this.contains(r.Lable2))this.removeChild(r.Lable2);
				}
			}			
		}
		
		//重画元素节点之间的关联线
		public function ReDrawRelationElement(iElement:Element):void{
			if (iElement is Node) {
				var childs:Array = this.getChildren();
				for (var i:int=0;i<childs.length;i++) {
					if(!(childs[i] is Element))continue;
					var myelement:Element = childs[i];
					if (myelement is Route) {
						var myroute:Route=myelement as Route;
						if ((myroute.fromElement==iElement)||(myroute.toElement==iElement)) {
							myroute.Draw();
						}
					}
				}
			}
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
				//同时删除外面的选择框
				if(selectCanvas!=null&&this.contains(selectCanvas)){
					this.removeChild(selectCanvas);
					selectCanvas = null;
				}
				AddUndo();
			}
			else if ((keyCode==90) && event.ctrlKey){//Ctrl+Z Undo
				this.Undo();
			}
			else if ((keyCode==89) && event.ctrlKey){//Ctrl+Y Redo
				this.Redo();
			}
			//左，右，上，下
			else if(keyCode==37||keyCode==38||keyCode==39||keyCode==40){
				var nodes:Array = this.getChildren();
				var step:int = 1;
				for (var j:int=0;j<nodes.length;j++) {
					if (!(nodes[j] is Node))continue;
					var node:Element = nodes[j] as Node;
					if(node.Selected==true){
						if(keyCode==37)node.x -=step;
						else if(keyCode==38)node.y -=step;
						else if(keyCode==39)node.x +=step;
						else if(keyCode==40)node.y +=step;
						ReDrawRelationElement(node);
					}					
				}
				if(this.selectCanvas!=null){
					if(keyCode==37)selectCanvas.x -=step;
					else if(keyCode==38)selectCanvas.y -=step;
					else if(keyCode==39)selectCanvas.x +=step;
					else if(keyCode==40)selectCanvas.y +=step;
				}
				AddUndo();
			}
			
			this.setFocus();            //只有得到焦点的组件才能监听键盘事件
		}
		/**
		 * 得到最顶级画板容器
		 **/
		public function getTopDrawBoard():DrawBoard{
			var topDrawBoard:DrawBoard = this;
			while(topDrawBoard.parent is DrawBoard){
				topDrawBoard = topDrawBoard.parent as DrawBoard;
			}
			return topDrawBoard;
		}
		//在生成xml前检查全局id是否有重复
		public function checkIdDupli(drawBoard:DrawBoard,ids:Array):String{
			var childs:Array = drawBoard.getChildren();
			var dupIds:String = "";
			for(var i:int=0;i<childs.length;i++){
				if(!(childs[i] is Element))continue;
				var ele:Element = childs[i];
				if(ids.indexOf(ele.ID)!=-1){
					return ele.ID;
				}				
				ids.push(ele.ID);
				if(childs[i] is DrawBoard)dupIds+=checkIdDupli(childs[i],ids);
			}
			return dupIds;
		}
		
		//转化为xml
		override public function BuildXml(): XML{
//			//由顶级画板生成xml
//			var topDrawBoard:DrawBoard = this;
//			while(topDrawBoard.parent is DrawBoard){
//				topDrawBoard = topDrawBoard.parent as DrawBoard;
//			}
//			topDrawBoard.BuildXml();
						
			var xml:XML=new XML("<Definitions/>");
			BuildProcessXml(xml);
			BuildDIXml(xml,this);
		//trace("definitions: \n"+xml.toString());
			
			//this.designXml = xml;
			
			return xml;
		}
		
		//元素节点信息转化为xml
		public function BuildProcessXml(root:XML): XML{
			var xml:XML=new XML("<Process/>");
			xml.@id = this.ID;
			xml.@name = this.Name;
			if(description!=null&&description!="")xml.@description = description;
			//可发起的子流程
			if(startProdefs.pk_prodef!=null&&startProdefs.pk_prodef!=""){
				xml.@startprodefs = "{"+startProdefs.pk_prodef+","+startProdefs.prodefName+"}";
			}
			var childs:Array = this.getChildren();
			//trace("childs:"+childs.length);
			
			var lansetXml:XML = new XML("<LaneSet/>");
			var lane:XML = new XML("<Lane/>");
			lane.@id = "";
			lansetXml.appendChild(lane);
			//xml.appendChild(lansetXml);
				
			var tempXML:XML;
			for (var i:int = 0;i<childs.length;i++) {
				if(!(childs[i] is Element))continue;
				var myelement:Element = childs[i];
				if(myelement is DrawBoard){
					xml.appendChild(BuildDrawBoardXML(myelement as DrawBoard));
				}
				else xml.appendChild(myelement.BuildXml());
				if(!(myelement is Route)){					
					tempXML = <FlowElementRef>{myelement.ID}</FlowElementRef>;
					lansetXml.appendChild(tempXML);									
				}
			}
			//xml.appendChild(BuildLansetXml(lanset));
	//trace("xml:\n"+xml.toString());
			root.appendChild(xml);
			return xml;
		}
		//如果是流程或者子流程，则递归调用转化xml
		private function BuildDrawBoardXML(subProcess:DrawBoard):XML{
			var boardXml:XML = subProcess.BuildXml();
			
			var childs:Array = subProcess.getChildren();
			for (var i:int=0;i<childs.length;i++) {
				if(!(childs[i] is Element))continue;
				var myelement:Element = childs[i];
				if(myelement is DrawBoard){
					boardXml.appendChild(BuildDrawBoardXML(myelement as DrawBoard));
				}
				else boardXml.appendChild(myelement.BuildXml());
			}
			return boardXml;
		}
//		private function BuildLansetXml(lanset:Array):XML{
//			var xml:XML=new XML("<laneSet/>");
//			xml.@id = "";			
//			for each(var element:Element in lanset){
//				var tempXml :XML = new XML("<flowElementRef/>");
//				
//			}
//			return xml;
//		}
		
		//节点的图形信息转化为xml
		public function BuildDIXml(root:XML,drawBoard:DrawBoard): XML{
			var xml:XML=new XML("<ProcessDiagram/>");
			xml.@id="ProcessDiagram_"+drawBoard.ID;
			xml.@name=drawBoard.Name;
			xml.@x=drawBoard.x;
			xml.@y=drawBoard.y;
			xml.@width=drawBoard.width;
			xml.@height=drawBoard.height;
			xml.@processRef=drawBoard.ID;
			
			var childs:Array = drawBoard.getChildren();
			//trace("childs:"+childs.length);
			var lansetXML:XML = new XML("<LaneCompartment/>");
			xml.appendChild(lansetXML);
			for (var i:int=0;i<childs.length;i++) {
				if(!(childs[i] is Element))continue;
				var myelement:Element=childs[i];
				if(myelement is Route){
					xml.appendChild(myelement.BuildDIXMl());
				}
				else if(myelement is DrawBoard){	
					var subprocessxml:XML=myelement.BuildDIXMl();
					subprocessxml.appendChild("<LaneCompRef/>");
					
					lansetXML.appendChild(subprocessxml);					
					BuildDIXml(root,myelement as DrawBoard);
				}
				else lansetXML.appendChild(myelement.BuildDIXMl());
			}
	//trace("xml:\n"+xml.toString());
			root.appendChild(xml);
			return xml;
		}

		//从xml文件中解析节点到界面显示
		override public function ParseFromXml(iXML:XML): int{
			this.Clear();
			if(iXML == null||iXML=="")
				return 0;
			var mainProcess:XML = iXML.Process[0];
			var shapeXml:XMLList = iXML.ProcessDiagram;	
	
			this.ID = mainProcess.@id;
			this.Name = mainProcess.@name;
			this.description = mainProcess.@description;			
			//可发起子流程
			var startprodefsStr:String = mainProcess.@startprodefs;
			startprodefsStr = startprodefsStr.substring(1,startprodefsStr.length-1);
			if(startprodefsStr!=null&&startprodefsStr!=""){
				this.startProdefs.pk_prodef = startprodefsStr.split(",")[0];
				this.startProdefs.prodefName = startprodefsStr.split(",")[1];
			}
						
			parseProcess(this,mainProcess,shapeXml);			
			return 0;
		}
				
		public function parseProcess(drawBoard:DrawBoard,nodeXML:XML,shapeXML:XMLList):void{
			if(nodeXML == null || shapeXML == null)
				return ;
			var newElementClass : Class
			var myelement:Element;
			var domain : ApplicationDomain = ApplicationDomain.currentDomain;
			var elementName:String="";

			var elements:XMLList = nodeXML.StartEvent+nodeXML.EndEvent+nodeXML.Task+nodeXML.UserTask+
				nodeXML.ScriptTask+nodeXML.SendTask+nodeXML.SubProcess+nodeXML.NcSubProcess+
				nodeXML.EsbSubProcess+nodeXML.JoinEvent+nodeXML.InclusiveGateway+nodeXML.ExclusiveGateway
				+nodeXML.SequenceFlow;

			var classes : HashMap = new HashMap();	    //如何为联合数组动态赋值问题		
			var processes:HashMap = new HashMap();
						
			for each (var elementXml:XML in elements) {
				if(elementXml.name()=='StartEvent')
					elementName="workflow.BeginNode";  //动态创建类的名称需要加包名
				else if(elementXml.name()=='EndEvent')
					elementName="workflow.EndNode";
				else if(elementXml.name()=='Task')
					elementName="workflow.WorkNode";
				else if(elementXml.name()=='UserTask')
					elementName="workflow.UseractivitiesNode";
				else if(elementXml.name()=='ScriptTask')
					elementName="workflow.ScriptactivitiesNode";
				else if(elementXml.name()=='SendTask')
					elementName="workflow.MessageNode";
				else if(elementXml.name()=='JoinEvent')
					elementName="workflow.JoinNode";
				else if(elementXml.name()=='SubProcess')
					elementName="workflow.SubprocessNode";						
				else if(elementXml.name()=='NcSubProcess')
					elementName="workflow.NCProcessNode";
				else if(elementXml.name()=='EsbSubProcess')
					elementName="workflow.ESBProcessNode";
				else if(elementXml.name()=='InclusiveGateway'||elementXml.name()=='ExclusiveGateway')
					elementName="workflow.GateWayNode";
				else if(elementXml.name()=='SequenceFlow'){
					elementName="workflow.Route";
				}
				
				if(domain.hasDefinition(elementName))
				{
					//动态创建类
					newElementClass = domain.getDefinition(elementName) as Class;
					myelement = new newElementClass(drawBoard,elementXml);

					myelement.ParseFromXml(elementXml);
										
					drawBoard.setChildIndex(myelement,0);
					classes.put(myelement.ID,myelement);
					//保存子流程，后面递归解析
					if(elementXml.name()=='SubProcess'||elementXml.name()=='EsbSubProcess'||elementXml.name()=='NcSubProcess'){
						processes.put(myelement.ID, elementXml);
					}
				}
			}
			
			var refName:String = "";
			var classname:Element;			
			var processDiagramXml:XML;
			
			for each(var tempXml:XML in shapeXML){
				var processRefName:String = tempXml.@processRef;
				if(processRefName==nodeXML.@id){
					processDiagramXml = tempXml;
					break;
				}
			}
			var dis:XMLList = processDiagramXml.LaneCompartment.EventShape+processDiagramXml.LaneCompartment.TaskShape+processDiagramXml.LaneCompartment.EmbeddedSubprocessShape
				+processDiagramXml.SequenceFlowConnector;
			
			for each(var diXml:XML in dis){
				if(diXml.name()=="EventShape")
					refName = diXml.@eventRef;
				else if(diXml.name()=="TaskShape")
					refName = diXml.@activityRef;
				else if(diXml.name()=="EmbeddedSubprocessShape"){
					refName = diXml.@activityRef;
				}
				else if(diXml.name()=="SequenceFlowConnector"){
					refName = diXml.@sequenceFlowRef;
				}
				if(refName!="")
					classname = classes.get(refName) as Element;
				
				classname.ParseDIFromXML(diXml);				
			}
			for each(var class_key:String in classes.entry()){
				(classes.get(class_key) as Element).Draw();
			}
			
			//如果是精简模式
			if(this.getTopDrawBoard().simpleMode==true)
				refactor(this);
			
			//解析界面中文显示
			new DrawBoardParser(drawBoard).parseCN();			
			//递归子流程		
			for each(var key:String in processes.entry()){   	
				parseProcess(classes.get(key) as DrawBoard,processes.get(key) as XML,shapeXML);
			}
			//如果是浏览态，移除所有监听器
			if(this.browseMode==true)setListenerEnable(false);
			return ;
		}
		
		
		
		//暂时不用
		public function draw(drawBoard:DrawBoard):void{
			var childs:Array = drawBoard.getChildren();
			for(var i:int=0;i<childs.length;i++){
				if(!(childs[i] is Element))continue;
				var element:Element = childs[i] as Element;
				if(element is DrawBoard)draw(element as DrawBoard);
				else element.Draw();
			}
		}
		//重构,1,去除网关节点
		public function refactor(drawBoard:DrawBoard):void{
			var childs:Array = this.getChildren();
			var gateways:Array = new Array();
			for(var i:int=0;i<childs.length;i++){
				if(!(childs[i] is GateWayNode))continue;
				var gateway:GateWayNode = childs[i];
				gateways.push(gateway);
				var fromroutes:Array = this.getFromRoutesByNode(gateway);
				var toElements:Array = new Array();
				toElements = getToElement(drawBoard,gateway,toElements);
				for(var j:int=0;j<fromroutes.length;j++){
					var tmproute:Route = fromroutes[j];
					if(!(tmproute.fromElement is GateWayNode)){
						for(var k:int=0;k<toElements.length;k++){
							var route:Route = new Route(drawBoard);
							route.fromElement = tmproute.fromElement;
							route.toElement = toElements[k];
							route.Draw();
						}						
					}
				}
				if(childs[i] is DrawBoard){
					refactor(childs[i] as DrawBoard);
				}
			}
			//删除网关
			for(var m:int=0;m<gateways.length;m++){
				drawBoard.DeleteElement(gateways[m]);
			}
		}
		//得到drawBoard中由gateway出发的所有非GateWayNode节点。
		private function getToElement(drawBoard:DrawBoard,gateway:GateWayNode,toElements:Array):Array{
			var outRoutes:Array = drawBoard.getOutRoutesByNode(gateway);
			for(var i:int=0;i<outRoutes.length;i++){
				var route:Route = outRoutes[i];
				if(!(route.toElement is GateWayNode)&&toElements.indexOf(route.toElement)==-1)
					toElements.push(route.toElement);
				else if(route.toElement is GateWayNode){
					getToElement(drawBoard,route.toElement as GateWayNode,toElements);
				}
			}
			return toElements;
		}
		
		//产生界面元素id
		public function GetNewElementID():String{
			var maxId:int = 0;
			var childs:Array = this.getChildren();
			for(var i:int=0;i<childs.length;i++) {
				if(!(childs[i] is Element))continue;
				var myelement:Element = childs[i] as Element;
				//如果id是手动修改的，且不能转换为整数，则忽略
				if (int(myelement.ID)>maxId) {
					maxId = int(myelement.ID);
				}
			}
			++maxId;
			return maxId+"";
		}
		
		//清空当前界面
		public function Clear():void{
			this.removeAllChildren();
			if(selectCanvas!=null){
				selectCanvas = null;
			}
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
		
		//拖入的节点进入容器时的处理
		public  function dragEnterHandler(event:DragEvent):void {   
			if (event.dragSource.hasFormat("baseElement"))   
			{   
				DragManager.acceptDragDrop(event.currentTarget as UIComponent);   
			}   
			else if(event.dragSource.hasFormat("route")){
				dragRoute();
			}
		}  		
		
		//拖入的节点元素被放下时的处理
		public  function dragDropHandler(event:DragEvent):void {   
			var dragObject:UIComponent=UIComponent(event.dragInitiator);   
			var node:Element;
			if(dragObject is BeginElement){
				node = new BeginNode(this); 
			}
			else if(dragObject is EndElement){
				node = new EndNode(this);
			}
			else if(dragObject is WorkElement){
				node = new WorkNode(this);
			}
			else if(dragObject is UserActivitiesElement){
				node = new UseractivitiesNode(this);
			}
			else if(dragObject is ScriptActivitiesElement){
				node = new ScriptactivitiesNode(this);
			}
			else if(dragObject is MessageElement){
				node = new MessageNode(this);
			}
			else if(dragObject is SubProcessElement){
				node = new SubprocessNode(this);
			}
			else if(dragObject is NCProcessElement){
				node = new NCProcessNode(this);
			}
			else if(dragObject is EsbProcessElement){
				node = new ESBProcessNode(this);
			}
			else if(dragObject is GateWayElement){
				node = new GateWayNode(this);
			}
			
			node.x = Container(event.currentTarget).mouseX; 
			node.y = Container(event.currentTarget).mouseY; 
			node.Draw();
			
			AddUndo();
		}
		
		//如果拖入的关联线，单独处理
		public function dragRoute():void{
//			var childs:Array = this.getChildren();
//			var selects:Array = new Array();
//			for(var i:int=0;i<childs.length;i++){
//				if(!(childs[i] is Node))continue;
//				var element:Element = childs[i];
//				if(element.Selected==true)
//					selects.push(element);
//				if(element is DrawBoard){
//					
//				}
//			}
			if(this.Status=="")this.Status = StatusConst.ROUTE_DRAW;
			
//			if(selects.length==1&&this.Status=="drawroute")
//			{
//				fromElement = selects.pop();
//				this.Status = "routebegin";
//			}
		}
		//得到当前iDrawBoard画板所有子画板的状态(递归子)
		public function getAllProcessStatus(iDrawBoard:DrawBoard,array:Array):Array{
			if(array==null)array = new Array();
			var childNodes:Array = iDrawBoard.getAllNodes();
			for(var j:int=0;j<childNodes.length;j++){
				if(!(childNodes[j] is DrawBoard))continue;
				var drawboard:DrawBoard = childNodes[j] as DrawBoard;
				array.push(drawboard.Status);
				getAllProcessStatus(drawboard,array);
			}
			return array;
		}
		//得到所有的关联线
		public function getAllRoutes():Array{
			var children:Array = this.getChildren();
			var array:Array = new Array();
			for(var i:int=0;i<children.length;i++){
				if(children[i] is Route)
					array.push(children[i]);
			}
			return array;	
		}
		//得到某个节点上的所有关联线(包括进，出)
		public function getRoutesByNode(node:Node):Array{
			var children:Array = this.getChildren();
			var array:Array = new Array();
			for(var i:int=0;i<children.length;i++){
				if(children[i] is Route){
				    var tmp:Route = children[i];
					if(tmp.fromElement == node || tmp.toElement == node)
						array.push(tmp);
				}					
			}
			return array;	
		}
		//得到某个节点上的所有进入关联线
		public function getFromRoutesByNode(node:Node):Array{
			var children:Array = this.getChildren();
			var array:Array = new Array();
			for(var i:int=0;i<children.length;i++){
				if(children[i] is Route){
					var tmp:Route = children[i];
					if(tmp.toElement == node)
						array.push(tmp);
				}					
			}
			return array;	
		}
		//得到某个节点上的所有出去关联线
		public function getOutRoutesByNode(node:Node):Array{
			var children:Array = this.getChildren();
			var array:Array = new Array();
			for(var i:int=0;i<children.length;i++){
				if(children[i] is Route){
					var tmp:Route = children[i];
					if(tmp.fromElement == node)
						array.push(tmp);
				}					
			}
			return array;	
		}
		//得到两个节点上的关联线
		public function getRouteBetweenNodes(pre:Node,current:Node):Route{
			var routes:Array = this.getAllRoutes();
			if(routes==null)return null;
			for(var i:int=0;i<routes.length;i++){
				var route:Route = routes[i];
				if(route.fromElement==pre&&route.toElement==current)
					return route;
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
		//得到所有用户活动节点
		public function getAllUserActivities(includeSelf:Boolean):Array{
			var children:Array = this.getChildren();
			var array:Array = new Array();
			for(var i:int=0;i<children.length;i++){
				if(children[i] is UseractivitiesNode)
					array.push(children[i]);
			}
			if(includeSelf==false)array.splice(array.indexOf(this.selectedElement),1);
			return array;			
		}
		//得到当前节点的所有前置用户活动节点,
		public function getPreviousUserActivities(current:Node):Array{
			var children:Array = this.getChildren();
			var array:Array = new Array();
			array = getPreviousUserActivities2(current,array,0);	
			for(var j:int=0;j<array.length;j++){
				if(array[j]==current){
					array.splice(j,1);
					j--;
				}
			}
			return array;			
		}
		
		//dep记录搜索深度，控制dep来避免循环  （这个算法需改进，如果节点太多很影响效率）
		private function getPreviousUserActivities2(current:Node,array:Array,dep:int):Array{
			var children:Array = this.getChildren();
			var nodes:Array = this.getAllNodes();
			if(dep>=nodes.length)return array;
			if(array==null)array = new Array();
			for(var i:int=0;i<children.length;i++){
				if(!(children[i] is Element))continue;
				var ele:Element = children[i];
				if(ele is Route){
					var route:Route = ele as Route;
					var fromElement:Node = route.fromElement as Node;
					if(route.toElement == current){
						if(fromElement is UseractivitiesNode&&array.indexOf(fromElement)==-1){
							array.push(fromElement);
						}	
						getPreviousUserActivities2(fromElement,array,++dep);
					}
				}				
			}
			
			return array;	
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
		
	}
}