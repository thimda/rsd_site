package workflow.comp
{
	import flash.display.Graphics;	
	import mx.core.UIComponent;
	import flash.events.MouseEvent;
	import mx.core.DragSource;
	import mx.managers.DragManager;
	import mx.controls.Image;
	
	/**
	 * 顺序流元类
	 * */
	public class RouteElement extends BaseElement
	{
		public function RouteElement()
		{
			super();
			image.source = "images/sequence.png";
			this.toolTip = "顺序流";
		}
		
		override public function onclick(event: MouseEvent):void{	
			var dragSource:DragSource = new DragSource();
			dragSource.addData(this, "route");
			DragManager.doDrag(this,dragSource,event);
		}
		override public function onmousedown(event: MouseEvent):void{
			
		}
		override public function onmouseup(event: MouseEvent):void{
			
		}				
	}
}