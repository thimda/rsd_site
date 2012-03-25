package workflow.decorator
{
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.charts.PieChart;
	import mx.containers.Canvas;
	import mx.managers.CursorManager;
	
	import workflow.DrawBoard;
	import workflow.Element;
	import workflow.Node;
	import workflow.utils.HashMap;
	
	//多选画布
	public class SelectCanvas extends Canvas
	{
		private var _status:String="";
		private var startPoint:Point;                                   //画布起始位置
		private var map:HashMap = new HashMap();                  //用来记录选中节点的初始位置，（也许有更好的办法能让选中节点跟着动）
		private var myDrawBoard:DrawBoard;
		
		[Embed(source="images/cursor/move.png")]			
		private var moveCursor:Class;	
		
		public function SelectCanvas(iDrawBoard:DrawBoard)
		{
			super();
			this.myDrawBoard = iDrawBoard;
			//this.addEventListener(MouseEvent.CLICK,onMouseClick);
			this.addEventListener(MouseEvent.MOUSE_DOWN,onMouseDown);
			this.addEventListener(MouseEvent.MOUSE_UP,onMouseUp);
			this.addEventListener(MouseEvent.MOUSE_MOVE,onMouseMove);
			this.addEventListener(MouseEvent.MOUSE_OUT,onMouseOut);
			
		}
		
		private function onMouseDown(event:MouseEvent):void{
			this.startDrag();
			this.Status = "startMove";
			startPoint = this.localToGlobal(new Point(0,0)); 
			
			var selectedElements:Array = myDrawBoard.selectedElements;
			map.clear();
			for(var i:int=0;i<selectedElements.length;i++){
				if(!(selectedElements[i] is Node))continue;
				var node:Node = selectedElements[i];	
				map.put(node.ID,new Point(node.x,node.y));
			}
			
			event.stopPropagation();
		}
		private function onMouseUp(event:MouseEvent):void{
			this.stopDrag();
//			
//			if(this.Status=="startMove"){	
//				var stopPoint:Point = this.localToGlobal(new Point(0,0));
//				var selectedElements:Array = myDrawBoard.selectedElements;
//				for(var i:int=0;i<selectedElements.length;i++){
//					if(!(selectedElements[i] is Node))continue;
//					var node:Node = selectedElements[i];					
//					node.x +=(stopPoint.x-startPoint.x);
//					node.y +=(stopPoint.y-startPoint.y);
//					myDrawBoard.ReDrawRelationElement(node);
//				}
//			}
			
			this.Status = "";
			//event.stopPropagation();
		}
		private function onMouseMove(event:MouseEvent):void{
			CursorManager.setCursor(moveCursor);
			if(this.Status=="startMove"){	
				var stopPoint:Point = this.localToGlobal(new Point(0,0));
				var selectedElements:Array = myDrawBoard.selectedElements;
				for(var i:int=0;i<selectedElements.length;i++){
					if(!(selectedElements[i] is Node))continue;
					var node:Node = selectedElements[i];	
					node.x = (map.get(node.ID) as Point).x+ (stopPoint.x-startPoint.x);
					node.y = (map.get(node.ID) as Point).y + (stopPoint.y-startPoint.y);
					myDrawBoard.ReDrawRelationElement(node);
			 	}
			}
			//event.stopPropagation();
		}
		
		private function onMouseOut(event:MouseEvent):void{
			CursorManager.removeAllCursors();
			event.stopPropagation();
		}	
		
		//设置选择装饰器的状态
		public function set Status(value: String): void{
			this._status = value;			
		}
		public function get Status(): String{
			return _status;
		}
	}
}