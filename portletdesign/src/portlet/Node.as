package portlet
{
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.filters.GlowFilter;
	
	import mx.controls.Alert;
	import mx.controls.Text;
	import mx.controls.ToolTip;
	import mx.core.Container;
	import mx.core.DragSource;
	import mx.core.UIComponent;
	import mx.effects.Glow;
	import mx.managers.DragManager;

	/**
	 * 节点基类,矩形形状的节点都继承该类
	 * */
	public class Node extends Element
	{
		public var _tooltip_text:String = "";
		
		private var _mywidth:Number;
		private var _myheight:Number;
				
		public function Node(iDrawBoard:Container,iXML:XML=null)
		{
			super(iDrawBoard,iXML);		
			if(iDrawBoard==null)return;
			Lable.mouseChildren = false;
			//this.addChildAt(Lable,0);
			this.toolTip = _tooltip_text;
			Lable.setStyle("textAlign","center");		
			this.addEventListener(MouseEvent.CLICK,onclick);
			initShow();
		}
		//设置初始时的外观效果
		public function initShow():void{
			var glow :GlowFilter = new GlowFilter();
			glow.color = 0xf2ef9e;
			this.filters = new Array(glow);
		}
		
		//鼠标移上时，发光效果
		public function onmouseover(event:MouseEvent):void{
			var glow :GlowFilter = new GlowFilter();
			glow.color = 0xf9c908;
			this.filters = new Array(glow);
			event.stopPropagation();
		}
		
		public function onmouseout(event:MouseEvent):void{
			initShow();
		}
		
		protected function onclick(event: MouseEvent):void{		
				if(this.Selected==false){
					var truedrawboard:Layout = this.TruePanel;
					truedrawboard.UnSelectedAllElement();
					unSelectOthers(this);
					this.Selected = true;
				}else if(this.Selected==true){
					this.Selected = false;
				}
			//event.stopPropagation();
		}
		//取消父容器中其他选中节点
		public function unSelectOthers(node:Element):void{
			var temp:Layout = node.TruePanel;
			while(temp.TruePanel!=null&&temp.TruePanel is Layout){
				var parent:Layout = temp.TruePanel as Layout;
				parent.UnSelectedAllElement();	
				temp = parent;
			}
		}
	
		
		public function set Tooltip_text(value:String):void{
			this._tooltip_text = value;
		}
		public function get Tooltip_text():String{
			return _tooltip_text;
		}
		
		//增加节点基本信息
		public function addBaseDIAttribute(xml:XML):void{
			xml.@x = this.x;
			xml.@y = this.y;
			xml.@Width=this.width;
			xml.@Height=this.height;
		}
		
		override public function ParseDIFromXML(iXML:XML):int{
			
			return 0;
		}
		
		
		public function set Mywidth(value:Number):void{
			this._mywidth = value;
		}
		public function get Mywidth():Number{
			return _mywidth;
		}
		public function set Myheight(value:Number):void{
			this._myheight = value;
		}
		public function get Myheight():Number{
			return _myheight;
		}
	}
}