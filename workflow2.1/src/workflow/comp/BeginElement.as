package workflow.comp
{
	import flash.display.Graphics;	
	import mx.controls.Image;
	import mx.core.UIComponent;
	
	/**
	 * 开始图元类 
	 * */
	public class BeginElement extends BaseElement
	{
		public function BeginElement()
		{
			super();
			image.source = "images/start.png";
			this.toolTip = "开始";		
		}		
	}
}