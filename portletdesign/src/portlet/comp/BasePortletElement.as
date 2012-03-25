package portlet.comp
{
	import flash.display.Graphics;
	import flash.events.MouseEvent;
	
	import mx.containers.Canvas;
	import mx.controls.Image;
	import mx.controls.Label;
	import mx.controls.Text;
	import mx.core.DragSource;
	import mx.core.UIComponent;
	import mx.managers.DragManager;
	
	/**
	 *  基本图元基类
	 * */
	public class BasePortletElement extends UIComponent
	{
		public var image:Image = new Image();
		public var Lable:Text=new Text();
		protected var _name:String="";
		
		public function BasePortletElement()
		{
			super();
			this.width = 200;
			this.height = 40;
			//image.height = 20;
			//image.width = 80;		
			//this.addChild(image);
			this.addChild(Lable);
			this.addEventListener(MouseEvent.CLICK,onclick);
			this.addEventListener(MouseEvent.MOUSE_DOWN,onmousedown);
			this.addEventListener(MouseEvent.MOUSE_UP,onmouseup);
			
			this.focusEnabled=true;
			this.buttonMode=true;
			this.mouseChildren=false;
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
			
			gc.beginFill(0x8bdef9,1); 
			gc.moveTo(x,y);
			gc.drawRoundRect(0,0,width,height,10,10);			
			gc.endFill();
	
		}
		
		public function get Name(): String{
			return _name;
		}
		
		public function set Name(value: String): void{
			this._name = value;
			Lable.text=value;
		}
	}
}