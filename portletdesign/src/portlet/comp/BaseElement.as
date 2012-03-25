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
	public class BaseElement extends UIComponent
	{
		public var image:Image = new Image();
		public var lable:Label = new Label();
		
		public function BaseElement()
		{
			super();
			//this.percentHeight = 80;
			this.height = 20;
			image.height = 20;
			image.width = 20;		
			this.addChild(image);
			this.addChild(lable);
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
	}
}