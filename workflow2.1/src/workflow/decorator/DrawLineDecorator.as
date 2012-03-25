package workflow.decorator
{
	import design.StatusConst;
	
	import flash.display.Graphics;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import mx.events.IndexChangedEvent;
	import mx.managers.CursorManager;
	
	import workflow.DrawBoard;
	import workflow.Node;
	import workflow.Route;

	/**
	 * 流程节点画线辅助类。 
	 * */
	public class DrawLineDecorator
	{
		private var node : Node = null;
		private var w:int = 10;
		private var h:int = 10;
		private var senseW:int = 20;
		private var senseH:int = 20;
		private var lineble:Boolean;
		private var mouseDown:Boolean;
		[Embed(source="images/cursor/fork.png")]			
		private var senseCursor:Class;
		
		public function DrawLineDecorator(node : Node)
		{
			this.node = node;			
		}
		
		//装饰
		public function decorate() : void {
				drawAnglePoint();
				node.addEventListener(MouseEvent.MOUSE_MOVE,onMouseMove);		
				node.addEventListener(MouseEvent.MOUSE_DOWN,onMouseDown);
				node.addEventListener(MouseEvent.MOUSE_UP,onMouseUp);
				node.addEventListener(MouseEvent.MOUSE_OUT,onMouseOut);	
		}
		public function undecorate():void{
			node.removeEventListener(MouseEvent.MOUSE_MOVE,onMouseMove);		
			node.removeEventListener(MouseEvent.MOUSE_DOWN,onMouseDown);
			node.removeEventListener(MouseEvent.MOUSE_UP,onMouseUp);
			node.removeEventListener(MouseEvent.MOUSE_OUT,onMouseOut);
			//node.Draw();
		}
		
		public function drawAnglePoint():void{
			var points:Array = new Array();
			//points.push(new Point(0,0));	
			points.push(new Point(node.width/2,0));	
			//points.push(new Point(this.width,0));	
			points.push(new Point(0,node.height/2));	
			points.push(new Point(node.width,node.height/2));	
			//points.push(new Point(0,this.height));	
			points.push(new Point(node.width/2,node.height));	
			//points.push(new Point(this.width,this.height));	
			
			for(var i:int=0;i<points.length;i++){
				drawFork(points[i]);
			}			
		}
		//画感应十字架
		public function drawFork(point:Point):void{
			var lineX:int = 3;
			var lineY:int = 3;			
			var gc:Graphics = node.graphics;
			//gc.clear();
			gc.lineStyle(1,0x0000ff);
			
			gc.moveTo(point.x-lineX,point.y-lineY);
			gc.lineTo(point.x+lineX,point.y+lineY);
			gc.moveTo(point.x-lineX,point.y+lineY);
			gc.lineTo(point.x+lineX,point.y-lineY);
		}
		//画感应区域标识，表示画线节点选中
		public function drawSenseRectUI(x0:int,y0:int,w:int,h:int):void{
			var gc:Graphics = node.graphics;
			gc.lineStyle(2,0xff0000);
			gc.drawRect(x0,y0,w,h);
		}
		//移除感应区域标识
		public function removeSenseRectUI():void{
			var gc:Graphics = node.graphics;
			gc.clear();
			node.Draw();
		}
		//标识是否可以拖动
		private function onMouseDown(event:MouseEvent):void{
			var drawBoard:DrawBoard = node.parent as DrawBoard;
			var width:int = this.node.width;
			var height:int = this.node.height;
			var x:int = event.localX;
			var y:int = event.localY;	
			if(lineble==true){
				mouseDown = true;
				var route:Route = drawBoard.tmpElement as Route;
				if(new Rectangle((width-senseW)/2,-senseH/2,senseW,senseH).contains(x,y)){
					if(route.toElement==null)
						route.routeFromElementPoint = 0;
					else route.routeToElementPoint = 0;
				}
				else if(new Rectangle(-senseW/2,(height-senseH)/2,senseW,senseH).contains(x,y)){
					if(route.toElement==null)
						route.routeFromElementPoint = 1;
					else route.routeToElementPoint = 1;
				}
				else if(new Rectangle(width-senseW/2,(height-senseH)/2,senseW,senseH).contains(x,y)){
					if(route.toElement==null)
						route.routeFromElementPoint = 2;
					else route.routeToElementPoint = 2;
				}
				else if(new Rectangle((width-senseW)/2,height-senseH/2,senseW,senseH).contains(x,y)){
					if(route.toElement==null)
						route.routeFromElementPoint = 3;
					else route.routeToElementPoint = 3;
				}				
			}		
			event.stopPropagation();
		}
		
		//当鼠标离开节点图标时，恢复鼠标原有样式
		private function onMouseOut(event:MouseEvent):void{
			mouseDown = false;
			lineble = false;
			CursorManager.removeAllCursors();
			removeSenseRectUI();
		}
		
		//根据鼠标的拖拽方式，对节点图标的大小进行改变
		private function onMouseUp(event:MouseEvent):void{
			var drawBoard:DrawBoard = node.parent as DrawBoard;			
			node.stopDrag();          //由于mouseup事件的调用顺序关系（该onMouseUp比Node中的onMouseUp后调用，故加上这句）
			
			
			lineble = false;
			mouseDown = false;
			CursorManager.removeAllCursors();
			removeSenseRectUI();
		}
		
		//当鼠标移动时，根据鼠标的位置，设置感应位置
		public function onMouseMove(event:MouseEvent):void{
			var drawBoard:DrawBoard = node.parent as DrawBoard;			
			var width:int = this.node.width;
			var height:int = this.node.height;
			var x:int = event.localX;
			var y:int = event.localY;	
			var mousePoint:Point = new Point(event.localX,event.localY);
			mousePoint = node.globalToContent(mousePoint);
			
			if(mouseDown==true){
				node.stopDrag();
				return;
			}
			
			if(pointInRect(x,y,node.width/2-senseW/2,-h/2,senseW,h)){
				CursorManager.setCursor(senseCursor);
				lineble = true;
				drawSenseRectUI(node.width/2-w/2,-h/2,w,h);
			}
			else if(pointInRect(x,y,-senseW/2,(node.height-senseH)/2,senseW,senseH)){
				CursorManager.setCursor(senseCursor);
				lineble = true;
				drawSenseRectUI(-w/2,node.height/2-h/2,w,h);
			}
			else if(pointInRect(x,y,node.width-senseW/2,(node.height-senseH)/2,senseW,senseH)){
				CursorManager.setCursor(senseCursor);
				lineble = true;
				drawSenseRectUI(node.width-w/2,(node.height-h)/2,w,h);
			}
			else if(pointInRect(x,y,node.width/2-senseW/2,node.height-h/2,senseW,h)){
				CursorManager.setCursor(senseCursor);
				lineble = true;
				drawSenseRectUI(node.width/2-w/2,node.height-h/2,w,h);
			}
			else {
				CursorManager.removeAllCursors();
				lineble = false;
				mouseDown = false;
			}	
			//为了支持鼠标放下与不放下时都能画线
			var route:Route = drawBoard.tmpElement as Route;
			if(lineble==true&&event.buttonDown==true&&route!=null&&route.fromElement!=null&&route.toElement==null){
				if(new Rectangle((width-senseW)/2,-senseH/2,senseW,senseH).contains(x,y)){
					 route.routeToElementPoint = 0;
				}
				else if(new Rectangle(-senseW/2,(height-senseH)/2,senseW,senseH).contains(x,y)){
					 route.routeToElementPoint = 1;
				}
				else if(new Rectangle(width-senseW/2,(height-senseH)/2,senseW,senseH).contains(x,y)){
					 route.routeToElementPoint = 2;
				}
				else if(new Rectangle((width-senseW)/2,height-senseH/2,senseW,senseH).contains(x,y)){
					 route.routeToElementPoint = 3;
				}		
			}
		}
		
		//判断鼠标落点是否在矩形区域内
		private function pointInRect(x:int,y:int,x0:int,y0:int,x1:int,y1:int):Boolean{
			x1+=x0;
			y1+=y0;
			if(x>=x0&&y>=y0&&x<=x1&&y<=y1){
				return true;
			}
			return false;
		}
	}
}