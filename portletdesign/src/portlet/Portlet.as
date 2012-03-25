package portlet
{
	import flash.display.Graphics;
	import flash.events.MouseEvent;
	import flash.geom.Matrix;
	import flash.display.GradientType;
	import flash.filters.GradientGlowFilter;
	import flash.filters.BitmapFilterQuality;
	import flash.filters.BitmapFilterType;
	import flash.filters.GlowFilter;
	import mx.core.DragSource;
	import mx.managers.DragManager;
	
	import utils.IDGenerator;
	
	/**
	 * portlet
	 * */
	public class Portlet extends Node
	{		
		public var _title:String = "";
		public var theam:String = "";
		
		public static const PREFIX:String = "P_";

		private static const DEFAULT_HEIGHT:Number = 100;
		
		public function Portlet(iDrawBoard:Layout,iXML:XML=null)
		{
			super(iDrawBoard,iXML);
			this.height = DEFAULT_HEIGHT;
			this.Name="Portlet";	  
			this.addChildAt(Lable,0);
			this.addEventListener(MouseEvent.MOUSE_DOWN,onmousedown);
			this.addEventListener(MouseEvent.MOUSE_UP,onmouseup);
		}
		
		override public function set Name(value: String): void{
			this._name = value;
			//Lable.text = value;
		}
		
		public function set Title(value:String):void{
			this._title = value;
			Lable.text = value;
		}
		public function get Title():String{
			return _title;
		}
				
		override public function Draw(): void{				
			this.width = myDrawBoard.width-LocalConst.LEFT*2;
			if(Mywidth.toString(10)!="NaN")this.width = Mywidth;
			if(Myheight.toString(10)!="NaN")this.height = Myheight;
			Lable.width = this.width;
			Lable.height = this.height;
			Lable.x=(this.width-Lable.width)/2;
			Lable.y=(this.height-Lable.height)/2;
			var gc:Graphics = this.graphics;
			gc.clear();
			this.SetLineColor();
			gc.moveTo(x,y);
			gc.drawRoundRect(0,0,width,height,10,10);	
			gc.endFill();	
		}
		
		//设置绘图时的颜色
		override public function SetLineColor():void{
			var gc:Graphics = this.graphics;
			var gradientBoxMatrix:Matrix = new Matrix();
			gradientBoxMatrix.createGradientBox(this.width, this.height, Math.PI/4, 0, 0); 
			var glow :GlowFilter = new GlowFilter();
			glow.blurX = 10;
			glow.blurY = 10;
			if (Selected==true) {
				gc.lineStyle(1,LINE_SELECT_COLOR);
				glow.color = 0xff0000;
				//gc.beginGradientFill(GradientType.LINEAR, [0xffffff,0x00ff00], [1,1], [0,255], gradientBoxMatrix);
			} else {
				gc.lineStyle(1,LINE_UNSELECT_COLOR);
				glow.color = 0x8bdef9;
				//gc.beginGradientFill(GradientType.LINEAR, [0xffffff,0x8bdef9], [1,1], [0,255], gradientBoxMatrix);
			}		
			this.filters = new Array(glow);
		}
		
		public function onmousedown(event: MouseEvent):void{
			var truedrawboard:Layout = this.TruePanel;
			truedrawboard.UnSelectedAllElement();
			unSelectOthers(this);
				if(this.Selected==false){
					this.Selected = true;
				}else if(this.Selected==true){
					this.Selected = false;
				}
				var dragSource:DragSource = new DragSource();
				dragSource.addData(this, "movePortlet");
				DragManager.doDrag(this,dragSource,event);					
			event.stopPropagation();		
		}
		
		override public function set Selected(value: Boolean): void{
			if (_selected!=value) {
				this._selected = value;
				if (value==false) {
					(myDrawBoard as Layout).selectedElement=null;
				} else {
					this.setFocus();
					(myDrawBoard as Layout).selectedElement=this;
				}				
				Draw();
				var evt:ElementEvent=new ElementEvent(ElementEvent.ELEMENT_SELECT_CHANGED);
				evt.srcElement=this;
				this.TruePanel.getTopLayout().dispatchEvent(evt);
			}
		}
		
		public function onmouseup(event: MouseEvent):void{
			this.stopDrag();
		}
		
		//取消父容器中其他选中节点
		override public function unSelectOthers(node:Element):void{
//			var temp:Layout = node.parent as Layout;
//			while(temp is Layout){
//				temp.UnSelectedAllElement();	
//				temp = temp.parent as Layout;
//			}
			var temp:Layout = node.TruePanel;
			while(temp.TruePanel!=null&&temp.TruePanel is Layout){
				var parent:Layout = temp.TruePanel as Layout;
				parent.UnSelectedAllElement();	
				temp = parent;
			}
		}

		override public function BuildXml(): XML{
			var xml:XML=new XML("<portlet/>");
			xml.@id = IDGenerator.addPrefix(PREFIX,this.ID);
			xml.@name = Name;	
			xml.@title = Title;
			xml.@theme = theam;
			return xml;
		}
		override public function ParseFromXml(iXML:XML): int{
			var idstr:String = iXML.@id;
			this.ID=IDGenerator.delPrefix(PREFIX,idstr);
			this.Name=iXML.@name;
			this.Title = iXML.@title;
			this.theam = iXML.@theme;
			return 0;
		}
	}
}