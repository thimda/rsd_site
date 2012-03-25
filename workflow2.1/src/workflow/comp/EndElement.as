package workflow.comp
{
	import flash.display.Graphics;	
	import mx.core.UIComponent;
	import mx.controls.Image;
	
	/**
	 * 结束图元类
	 * */
	public class EndElement extends BaseElement
	{
		public function EndElement()
		{
			super();			
			image.source = "images/end.png";
			this.toolTip = "结束";
		}
	}
}