package portlet.comp
{
	import flash.display.GradientType;
	import flash.display.Graphics;
	import flash.events.MouseEvent;
	import flash.filters.GlowFilter;
	import flash.geom.Matrix;
	
	import mx.controls.Image;
	import mx.controls.Text;
	import mx.core.DragSource;
	import mx.core.UIComponent;
	import mx.managers.DragManager;
	
	/**
	 * portlet图元类
	 * */
	public class PortletElement extends UIComponent
	{
		public var image:Image = new Image();
		public var Lable:Text=new Text();
		public var _title:String = "";
		protected var _name:String="";
		
		public function PortletElement()
		{
			super();
			this.width = 86;
			this.height = 40;
			//image.height = 20;
			//image.width = 80;		
			//this.addChild(image);
			this.addChild(Lable);
			Lable.setStyle("textAlign","center");		
			this.addEventListener(MouseEvent.CLICK,onclick);
			this.addEventListener(MouseEvent.MOUSE_DOWN,onmousedown);
			this.addEventListener(MouseEvent.MOUSE_UP,onmouseup);
			
			initShow();
			this.addEventListener(MouseEvent.MOUSE_OVER,onmouseover);
			this.addEventListener(MouseEvent.MOUSE_OUT,onmouseout);
			
			this.focusEnabled=true;
			this.buttonMode=true;
			this.mouseChildren=false;
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
		
		public function onclick(event: MouseEvent):void{	
			//this.Selected=true;
		}
		public function onmousedown(event: MouseEvent):void{
			var dragSource:DragSource = new DragSource();
			dragSource.addData(this, "baseElement");
			DragManager.doDrag(this,dragSource,event);			
		}
		public function onmouseup(event: MouseEvent):void{
			this.stopDrag();
		}
		
		public function Draw(): void{				
			Lable.width=this.width;
			Lable.height=20;
			Lable.x=(this.width-Lable.width)/2;
			Lable.y=(this.height-Lable.height)/2;
			var gc:Graphics = this.graphics;
			gc.clear();
			
			SetLineColor();
			gc.moveTo(x,y);
			gc.drawRoundRect(0,0,width,height,0,0);			
			gc.endFill();
			
		}
		public function SetLineColor():void{
			var gc:Graphics = this.graphics;
			gc.clear();
			var gradientBoxMatrix:Matrix = new Matrix();
			gradientBoxMatrix.createGradientBox(this.width, this.height, Math.PI/4, 0, 0); 			
			
			gc.lineStyle(1,0x036999);
			gc.beginGradientFill(GradientType.LINEAR, [0xffffff,0x8bdef9], [1,1], [0,255], gradientBoxMatrix);	
		}
		
		public function get Name(): String{
			return _name;
		}
		
		public function set Name(value: String): void{
			this._name = value;
			//Lable.text=value;
		}
		
		public function get Title(): String{
			return _title;
		}
		
		public function set Title(value: String): void{
			this._title = value;
			Lable.text=value;
		}
	}
}