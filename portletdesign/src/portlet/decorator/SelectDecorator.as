package workflow.decorator
{
	import flash.display.Graphics;
	import flash.events.MouseEvent;
	
	import mx.managers.CursorManager;
	
	import portlet.Node;
	
	import workflow.DrawBoard;
	import workflow.Node;

	/**
	 * 流程节点装饰类:负责流程节点选中标识，节点大小操作的控制。
	 * */
	public class SelectDecorator
	{
		private var node :portlet.Node = null;
		private var w:int = 10;
		private var h:int = 10;	
		
		public function SelectDecorator(node : Node)
		{
			this.node = node;
		}
		
		//装饰
		public function decorate() : void {
			var gc:Graphics = this.node.graphics;
			gc.lineStyle(1,0x80888c);
			var width:int = this.node.width;
			var height:int = this.node.height;
			
			gc.drawRect(-w/2,-h/2,w,h);
			gc.drawRect((width-w)/2,-h/2,w,h);
			gc.drawRect(width-w/2,-h/2,w,h);
			gc.drawRect(-w/2,(height-h)/2,w,h);
			gc.drawRect(width-w/2,(height-h)/2,w,h);
			gc.drawRect(-w/2,height-h/2,w,h);
			gc.drawRect((width-w)/2,height-h/2,w,h);
			gc.drawRect(width-w/2,height-h/2,w,h);									
		}
		
	}
}