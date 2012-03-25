package portlet.comp
{
	import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.controls.Image;
	import mx.core.DragSource;
	import mx.managers.DragManager;
	
	/**
	 * 基本元素加载类：负责基本图元的加载
	 * 
	 * */
	public class BaseElementLoad
	{
		private var container:HBox;
		public function BaseElementLoad()
		{
			
		}
		
		public function load():void{	
			var layout:LayoutElement = new LayoutElement();
			var portlet:PortletElement = new PortletElement();
			container.addChild(layout);
			container.addChild(portlet);
		}
		
		public function set HBox(container:HBox):void{
			this.container = container;
		}
	}
}