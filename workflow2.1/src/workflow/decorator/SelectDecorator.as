package workflow.decorator
{
	import flash.display.Graphics;
	import flash.events.MouseEvent;
	
	import mx.managers.CursorManager;
	
	import workflow.DrawBoard;
	import workflow.Node;

	/**
	 * 流程节点装饰类:负责流程节点选中标识，节点大小操作的控制。
	 * */
	public class SelectDecorator
	{
		private var node : Node = null;
		private var w:int = 8;
		private var h:int = 8;
		private var dragable:Boolean;
		private var mouseDown:Boolean;
		public var rects:Array = new Array(false,false,false,false,false,false,false,false);
		[Embed(source="images/cursor/ud.swf")]			
		private var udCursor:Class;
		[Embed(source="images/cursor/lr.swf")]			
		private var lrCursor:Class;
		[Embed(source="images/cursor/lup.swf")]			
		private var lupCursor:Class;
		[Embed(source="images/cursor/rup.swf")]			
		private var rupCursor:Class;		
		
		public function SelectDecorator(node : Node)
		{
			this.node = node;			
		}
		
		//装饰
		public function decorate() : void {
			if(node.Selected==true){
				node.addEventListener(MouseEvent.MOUSE_MOVE,onMouseMove);		
				node.addEventListener(MouseEvent.MOUSE_DOWN,onMouseDown);
				node.addEventListener(MouseEvent.MOUSE_UP,onMouseUp);
				node.addEventListener(MouseEvent.MOUSE_OUT,onMouseOut);
				
				var gc:Graphics = this.node.graphics;
				gc.lineStyle(1,0x80888c);
				gc.beginFill(0x00ff00,1);
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
			else{
				node.removeEventListener(MouseEvent.MOUSE_MOVE,onMouseMove);		
				node.removeEventListener(MouseEvent.MOUSE_DOWN,onMouseDown);
				node.removeEventListener(MouseEvent.MOUSE_UP,onMouseUp);
				node.removeEventListener(MouseEvent.MOUSE_OUT,onMouseOut);
			}											
		}
		//标识是否可以拖动
		private function onMouseDown(event:MouseEvent):void{
			if(dragable==true){
				mouseDown = true;
				var drawBoard:DrawBoard = node.parent as DrawBoard;
				drawBoard.Status = "dragable";
			}
			var width:int = this.node.width;
			var height:int = this.node.height;
			var x:int = event.localX;
			var y:int = event.localY;
			//重置
			for (var j:int=0;j<rects.length;j++){
				rects[j]=false;
			}
			
			if(pointInRect(x,y,-w/2,-h/2,w,h)){
				rects[0]=true;
			}
			else if(pointInRect(x,y,(width-w)/2,-h/2,w,h)){
				rects[1]=true;
			}
			else if(pointInRect(x,y,width-w/2,-h/2,w,h)){
				rects[2]=true;
			}
			else if(pointInRect(x,y,-w/2,(height-h)/2,w,h)){
				rects[3]=true;
			}
			else if(pointInRect(x,y,width-w/2,(height-h)/2,w,h)){
				rects[4]=true;
			}
			else if(pointInRect(x,y,-w/2,height-h/2,w,h)){
				rects[5]=true;
			}
			else if(pointInRect(x,y,(width-w)/2,height-h/2,w,h)){
				rects[6]=true;
			}
			else if(pointInRect(x,y,width-w/2,height-h/2,w,h)){
				rects[7]=true;
			}
		}
		
		//当鼠标离开节点图标时，恢复鼠标原有样式
		private function onMouseOut(event:MouseEvent):void{
			mouseDown = false;
//			for (var j:int=0;j<rects.length;j++){
//				rects[j]=false;
//			}
			CursorManager.removeAllCursors();
		}
		
		//根据鼠标的拖拽方式，对节点图标的大小进行改变
		private function onMouseUp(event:MouseEvent):void{
			var drawBoard:DrawBoard = node.parent as DrawBoard;
			//drawBoard.AddUndo();
			
			//drawBoard.Status = "";
			node.stopDrag();          //由于mouseup事件的调用顺序关系（该onMouseUp比Node中的onMouseUp后调用，故加上这句）
			if(dragable==true){
				for (var j:int=0;j<rects.length;j++){
					rects[j]=false;
				}
			}
			dragable = false;
			mouseDown = false;
			CursorManager.removeAllCursors();
		}
		
		//当鼠标移动时，根据鼠标的位置，显示不同的鼠标样式
		public function onMouseMove(event:MouseEvent):void{
			var drawBoard:DrawBoard = node.parent as DrawBoard;
			//如果是在画线时，不往下执行
			if(drawBoard.Status=="routebegin"){
				dragable = false;
				return;
			}
			var width:int = this.node.width;
			var height:int = this.node.height;
			var x:int = event.localX;
			var y:int = event.localY;			
			
			if(mouseDown==true){
				node.stopDrag();
				return;
			}
			
			if(pointInRect(x,y,-w/2,-h/2,w,h)){
				CursorManager.setCursor(lupCursor);
				//rects[0]=true;
			}
			else if(pointInRect(x,y,(width-w)/2,-h/2,w,h)){
				CursorManager.setCursor(udCursor);
				//rects[1]=true;
			}
			else if(pointInRect(x,y,width-w/2,-h/2,w,h)){
				CursorManager.setCursor(rupCursor);
				//rects[2]=true;
			}
			else if(pointInRect(x,y,-w/2,(height-h)/2,w,h)){
				CursorManager.setCursor(lrCursor);
				//rects[3]=true;
			}
			else if(pointInRect(x,y,width-w/2,(height-h)/2,w,h)){
				CursorManager.setCursor(lrCursor);
				//rects[4]=true;
			}
			else if(pointInRect(x,y,-w/2,height-h/2,w,h)){
				CursorManager.setCursor(rupCursor);
				//rects[5]=true;
			}
			else if(pointInRect(x,y,(width-w)/2,height-h/2,w,h)){
				CursorManager.setCursor(udCursor);
				//rects[6]=true;
			}
			else if(pointInRect(x,y,width-w/2,height-h/2,w,h)){
				CursorManager.setCursor(lupCursor);
				//rects[7]=true;
			}	
			else {
				CursorManager.removeAllCursors();
				dragable = false;
				mouseDown = false;
//				for (var j:int=0;j<rects.length;j++){
//					rects[j]=false;
//				}
			}	
			//改变选中元素的大小
			if(drawBoard.Status=="dragable"){				
				var witch:int = -1;
				for(var i:int=0;i<rects.length;i++){
					if(rects[i]==true)witch=i;
				}
				//根据拖动不同的位置，进行不同的改变
				if(witch==0){	
				}
				if(witch==1){					
				}
				//......to do
				//目前只支持右下角拖动
//				if(event.localX<=node.width)node.width = event.localX;
//				else node.width = event.localX - node.x;
//				
//				if(event.localY<=node.height)node.height = event.localY;
//				else node.height = event.localY - node.y;
				//node.Draw();
			}
			if(drawBoard.Status=="")
				event.stopPropagation();
		}
		
		//判断鼠标落点是否在矩形区域内
		private function pointInRect(x:int,y:int,x0:int,y0:int,x1:int,y1:int):Boolean{
			x1+=x0;
			y1+=y0;
			if(x>=x0&&y>=y0&&x<=x1&&y<=y1){
				dragable = true;
				return true;
			}
			return false;
		}
	}
}