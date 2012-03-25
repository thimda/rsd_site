package workflow
{
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.filters.GlowFilter;
	
	import mx.controls.Alert;
	import mx.controls.Text;
	import mx.controls.ToolTip;
	import mx.core.UIComponent;
	import mx.effects.Glow;
	import mx.managers.ToolTipManager;
	
	import workflow.control.HTMLToolTip;
	import workflow.control.TooltipComp;
	import workflow.decorator.DrawLineDecorator;
	import workflow.decorator.SelectDecorator;

	/**
	 * 节点基类,矩形形状的节点都继承该类
	 * */
	public class Node extends Element
	{
		public var decorator:SelectDecorator = null;
		public var drawLineDec:DrawLineDecorator = null;
		public var _tooltip_text:String = "";
		
		//rx,ry的默认值，主要是区分rx,ry是否被赋过值
		public static const DEFAULT_LOC:Number = -10000;
		private var _rx:Number = DEFAULT_LOC;
		private var _ry:Number = DEFAULT_LOC;
				
		public function Node(iDrawBoard:DrawBoard=null,iXML:XML=null)
		{
			super(iDrawBoard,iXML);
			if(iDrawBoard==null)return;
			Lable.mouseChildren = false;
			this.addChild(Lable);			
			Lable.setStyle("textAlign","center");		
			initShow();
	      	this.addEventListener(MouseEvent.CLICK,onclick);
	     	this.addEventListener(MouseEvent.MOUSE_DOWN,onmousedown);
	     	this.addEventListener(MouseEvent.MOUSE_UP,onmouseup);
			this.addEventListener(MouseEvent.MOUSE_OVER,onmouseover);
			this.addEventListener(MouseEvent.MOUSE_OUT,onmouseout);
		}
		override public function removeAllListener():void{
			this.removeEventListener(MouseEvent.CLICK,onclick);
			this.removeEventListener(MouseEvent.MOUSE_DOWN,onmousedown);
			this.removeEventListener(MouseEvent.MOUSE_UP,onmouseup);
			this.removeEventListener(MouseEvent.MOUSE_OVER,onmouseover);
			this.removeEventListener(MouseEvent.MOUSE_OUT,onmouseout);
		}
		//设置初始时的外观效果
		private function initShow():void{
			var glow :GlowFilter = new GlowFilter();
			glow.color = 0xf2ef9e;
			this.filters = new Array(glow);
		}
		
		//鼠标移上时，发光效果
		private function onmouseover(event:MouseEvent):void{
			var glow :GlowFilter = new GlowFilter();
			glow.color = 0xf9c908;
			this.filters = new Array(glow);
			event.stopPropagation();	
		}
		
		private function onmouseout(event:MouseEvent):void{
			initShow();
		}
		
		protected function onclick(event: MouseEvent):void{
			if(event.ctrlKey==true){
				if(this.Selected==false){
					this.Selected = true;
					myDrawBoard.selectedElements.push(this);
				}else if(this.Selected==true){
					this.Selected = false;
					myDrawBoard.selectedElements.splice(this);
				}
			}
			else if (myDrawBoard.Status==""||myDrawBoard.Status=="drawroute") {
				myDrawBoard.UnSelectedAllElement();
				unSelectOthers(this);
				this.Selected=true;
				myDrawBoard.fromElement = this;
			}
			event.stopPropagation();
		}
		
		override public function set Selected(value: Boolean): void{
			super.Selected = value;
			decorator.decorate();
		}
		//取消父容器中其他选中节点
		public function unSelectOthers(node:Element):void{
			var temp:DrawBoard = node.myDrawBoard;
			while(temp.parent is DrawBoard){
				var parent:DrawBoard = temp.parent as DrawBoard;
				parent.UnSelectedAllElement();	
				temp = parent;
			}
		}
		
		private function onmousedown(event: MouseEvent):void{
			 if (myDrawBoard.Status=="") {
				this.startDrag();
				this.Selected=true;
				myDrawBoard.Status = "nodeStartDrag";
			}
			 if(myDrawBoard.Status=="drawroute"){
				 myDrawBoard.Status="routebegin";
			 }
			 var route:Route = myDrawBoard.tmpElement as Route;
			 if(myDrawBoard.Status=="routebegin"){
				 if(route==null){
					 route = new Route(myDrawBoard);
					 myDrawBoard.tmpElement = route;
					 myDrawBoard.setChildIndex(route,0);
				 }
			 }
			 if (myDrawBoard.Status=="routebegin"&&route.fromElement==null) {
				 //myDrawBoard.fromElement=this;
				 route.fromElement = this;
			 }			 
			 else if (myDrawBoard.Status =="routebegin"&&route.fromElement!=null&&route.toElement==null) {
				if(this!=myDrawBoard){
					//route.toElement = this;
					var nodeCheck:Boolean = true;				
					nodeCheck = !(route.fromElement is EndNode);                 //fromElement不能是结束节点
					nodeCheck = nodeCheck /*&& !(this.toElement is BeginNode)*/;    //toElement不能是开始节点
					
					if(route.fromElement == this)return;				
					nodeCheck = nodeCheck && myDrawBoard.checkExistRoute(route.fromElement as Node,this);
					if (nodeCheck==true) {
						route.toElement=this;
					}
					else {
						if (myDrawBoard.tmpElement!=null) { 
							myDrawBoard.removeChild(route);
						}
					}
				}
			} 
		
			 			 
//_____________控制画线状态，当子，父容器处于画线状态时，是不能在当前容器画线的
			//如果父先处于画线状态，则不能对子进行画线
			if(myDrawBoard.parent is DrawBoard){
				var parent:UIComponent = myDrawBoard.parent as UIComponent;
				while(parent is DrawBoard){
					var p:DrawBoard = parent as DrawBoard;
					if(p.Status=="routebegin")myDrawBoard.Status="";
					parent = p.parent as UIComponent;
				}
			}
			//如果有子先处于画线状态，则不能对父进行画线
			var subProcessStatus:Array = myDrawBoard.getAllProcessStatus(myDrawBoard,null);
			for each(var status:String in subProcessStatus){
				if(status=="routebegin"){
					myDrawBoard.Status = "";
					break;
				}
			}
//______________________________________________			
			
			event.stopPropagation();
		}
		
		private function onmouseup(event: MouseEvent):void{
			if (myDrawBoard.Status=="nodeStartDrag") {
				this.stopDrag();
				myDrawBoard.AddUndo();
				myDrawBoard.Status = "";
				myDrawBoard.ReDrawRelationElement(this);
			}
			if(myDrawBoard.tmpElement is Route){
				var route:Route = myDrawBoard.tmpElement as Route;
				var routetmp:Route = myDrawBoard.tmpElement as Route;
				//为了满足鼠标放下和不放下时都能画线(对应的DrawLineDecorator中的mousedown和mouseup方法)
				if (myDrawBoard.Status =="routebegin"&&route.fromElement!=null&&route.toElement==null) {
					if(this!=myDrawBoard){
						var nodeCheck:Boolean = true;				
						nodeCheck = !(route.fromElement is EndNode);                 //fromElement不能是结束节点
						nodeCheck = nodeCheck /*&& !(this.toElement is BeginNode)*/;    //toElement不能是开始节点
						
						if(route.fromElement == this)return;				
						nodeCheck = nodeCheck && myDrawBoard.checkExistRoute(route.fromElement as Node,this);
						if (nodeCheck==true) {
							trace("route.routetoPoint:"+route.routeToElementPoint);
							route.toElement=this;
						}
						else {
							if (myDrawBoard.tmpElement!=null) { 
								myDrawBoard.removeChild(route);
							}
						}
					}
				} //
				if (myDrawBoard.Status=="routebegin"&&routetmp.fromElement!=null&&routetmp.toElement!=null){					
						routetmp.Draw();
						myDrawBoard.AddUndo();
						
						myDrawBoard.tmpElement=null;
						myDrawBoard.Status="";
						if(myDrawBoard.parent is DrawBoard){
							(myDrawBoard.parent as DrawBoard).Status = "";
						}
					}
			}
		}
		
		public function set rx(value:Number):void{
			this._rx = value;
		}
		public function get rx():Number{
			return _rx;
		}
		public function set ry(value:Number):void{
			this._ry = value;
		}
		public function get ry():Number{
			return _ry;
		}		
		
		public function set Tooltip_text(value:String):void{
			this._tooltip_text = value;
			ToolTipManager.toolTipClass = HTMLToolTip;
			this.toolTip = _tooltip_text;
		}
		public function get Tooltip_text():String{
			return _tooltip_text;
		}
		
		//增加节点基本信息
		public function addBaseDIAttribute(xml:XML):void{
			xml.@x = this.x;
			xml.@y = this.y;
			if(rx==DEFAULT_LOC)rx = this.x;          
			if(ry==DEFAULT_LOC)ry = this.y;
			xml.@rx = this.rx;
			xml.@ry = this.ry;
			xml.@Width=this.width;
			xml.@Height=this.height;
		}
		
		override public function ParseDIFromXML(iXML:XML):int{
			this.x=iXML.@x;
			this.y=iXML.@y;	
			//为了兼容1.x
			var tmprx:String = iXML.@rx;
			var tmpry:String = iXML.@ry;
			if(tmprx=="")this.rx = DEFAULT_LOC;
			else this.rx = iXML.@rx;
			if(tmpry=="")this.ry = DEFAULT_LOC;
			else this.ry = iXML.@ry;
			//2.x
//			this.rx = iXML.@rx;
//			this.ry = iXML.@ry;		
			if(myDrawBoard.simpleMode==true){
				if(rx==DEFAULT_LOC)rx = this.x;          
				if(ry==DEFAULT_LOC)ry = this.y;
				this.x = rx;
				this.y = ry;
			}
			this.width=iXML.@Width;
			this.height=iXML.@Height;
			return 0;
		}
	}
}