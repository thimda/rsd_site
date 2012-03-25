package workflow
{
	import flash.display.DisplayObject;
	import flash.display.Graphics;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.text.*;
	
	import mx.controls.Label;
	import mx.managers.CursorManager;
	
	/**
	 * 节点关联线
	 * */
	public class Route extends Element
	{
		public var fromElement:Element;
		public var toElement:Element;
		public var fromX:int;
		public var fromY:int;
		public var toX:int;
		public var toY:int; 
		
		public static const UP_DOWN:String = "UD";
		public static const LEFT_RIGHT:String = "LR";
		public var w:int = 8;
		public var h:int = 8;
		private var endw:int = 12;
		private var endh:int = 12;
		private static const DIS_TO_BOUND:int = 20;
		public var turnPoints:Array = new Array();
		public var currentTurnPointId:int = -1;
		public var turnType:String = UP_DOWN;
		[Embed(source="images/cursor/ud.swf")]			
		private var udCursor:Class;
		[Embed(source="images/cursor/lr.swf")]			
		private var lrCursor:Class;
		[Embed(source="images/cursor/move.png")]			
		private var endCursor:Class;
		
		private var routeDragable:Boolean;
		private var routeMoveble:Boolean;
		private var mouseDown:Boolean;
		//线在节点上的起始\结束位置,该值可以是:0,1,2,3,分别代表节点上的四个位置
		public var routeFromElementPoint:int = -1;
		public var routeToElementPoint:int = -1;
		
		//画线的方式 0,折线，1：直线
		public var routeStyle:String = "0";
		//如果为-1,颜色用默认值，
		public var line_color:int = -1;
		
		public var condition:String = "";
		private var _conditionZH:String = "";
		
		//自定义处理类
		public var selfDefClass:String = "";
		
		
		public function Route(iDrawBoard:DrawBoard,iXML:XML=null)
		{
			super(iDrawBoard,iXML);
			//Lable.width=0;
			//Lable.height=0;
			
			//Lable.validateNow();//立即计算文本的尺寸
			//自动根据文本设置尺寸
			//Lable.width=Lable.textWidth+5;
			//Lable.height=Lable.textHeight+2;
			
			//Lable.width = 60;
			//Lable.height = 20;
			
			//this.addChild(Lable);			
			
			this.Name = "名称";
			this.addEventListener(MouseEvent.CLICK,onclick);
			this.addEventListener(MouseEvent.MOUSE_DOWN,onmousedown);
			this.addEventListener(MouseEvent.MOUSE_UP,onmouseup);
			this.addEventListener(MouseEvent.MOUSE_MOVE,onMouseMove);	
			this.addEventListener(MouseEvent.MOUSE_OUT,onMouseOut);
		}
		
		override public function removeAllListener():void{
			this.removeEventListener(MouseEvent.CLICK,onclick);
			this.removeEventListener(MouseEvent.MOUSE_DOWN,onmousedown);
			this.removeEventListener(MouseEvent.MOUSE_UP,onmouseup);
			this.removeEventListener(MouseEvent.MOUSE_MOVE,onMouseMove);
			this.removeEventListener(MouseEvent.MOUSE_OUT,onMouseOut);
		}
		
		override public function set Name(value: String): void{
			_name = value;
		}		
		
		public function setLineStyle():void{
			var default_line_color:int = 0x000000;
			if(line_color==-1){
				this.graphics.lineStyle(1,default_line_color);
				if(Selected == true){
					this.graphics.lineStyle(2,0xff0000);
				}
				if(State==1){
					this.graphics.lineStyle(2,LINE_FINISHED_COLOR);
				}
			}
			else if(line_color!=-1){
				this.graphics.lineStyle(2,line_color);
			}
		}
		
		private function onmousedown(event: MouseEvent):void{
			var drawBoard:DrawBoard = this.parent as DrawBoard;
			if(routeDragable==true){
				mouseDown = true;
				drawBoard.Status = "routeDragable";
			}
			if(routeMoveble==true){
				mouseDown = true;
				drawBoard.Status = "routeMoveble";
			}
			event.stopPropagation();
		}
		//当鼠标移动时，根据鼠标的位置，显示不同的鼠标样式
		public function onMouseMove(event:MouseEvent):void{	
			//只有被选中的线才有感应区
			if(this.Selected==false)return;
			if(turnPoints.length<2)return;			
			var x:int = event.localX;
			var y:int = event.localY;
			
			//拐点中间(上下，左右感应区域)
			for(var i:int=1;i<turnPoints.length;i++){
				var point1:Point = turnPoints[i-1];
				var point2:Point = turnPoints[i];
				var senseX:Number = point1.x + (point2.x-point1.x)/2.0;
				var senseY:Number = point1.y + (point2.y-point1.y)/2.0;
				
				if(pointInRect(x,y,senseX-w/2,senseY-h/2,w,h)){
					if(point1.x==point2.x){
						CursorManager.setCursor(lrCursor);
						turnType = LEFT_RIGHT;
					}
					else if(point1.y==point2.y){
						CursorManager.setCursor(udCursor);
						turnType = UP_DOWN;
					}
					currentTurnPointId = i-1;
					routeDragable = true;
					break;
				}				
				else {
					CursorManager.removeAllCursors();
					routeDragable = false;
					mouseDown = false;
				}						
			}
			//线的端点(移动端点)
			if(routeDragable==false){
				if(pointInRect(x,y,fromX-endw/2,fromY-endh/2,endw,endh)){
						CursorManager.setCursor(endCursor);
						routeMoveble = true;
				}
				if(pointInRect(x,y,toX-endw/2,toY-endh/2,endw,endh)){
						CursorManager.setCursor(endCursor);
						routeMoveble = true;
				}
				else {
					CursorManager.removeAllCursors();
					routeMoveble = false;
					mouseDown = false;
				}	
			}
		}
		//当鼠标离开感应取悦时，恢复鼠标原有样式
		private function onMouseOut(event:MouseEvent):void{			
			CursorManager.removeAllCursors();
		}		
		private function onmouseup(event: MouseEvent):void{
			routeDragable = false;
			mouseDown = false;
			CursorManager.removeAllCursors();
		}
		
		//判断鼠标落点是否在矩形区域内
		private function pointInRect(x:int,y:int,x0:int,y0:int,x1:int,y1:int):Boolean{
			x1+=x0;
			y1+=y0;
			if(x>=x0&&y>=y0&&x<=x1&&y<=y1)
				return true;
			return false;
		}
		
		override public function Draw():void{
			this.graphics.clear();
			var arrow_line_color:int = 0x000000;	
			if(Selected == true){
				arrow_line_color = 0xff0000;
			}
			setLineStyle();
			
			if(!(fromElement is Node)){
				throw new Error("Route中非法的fromElement")
			}
			var fromMidX:Number = fromElement.x+fromElement.width/2;
			var fromMidY:Number = fromElement.y+fromElement.height/2;
			if(routeFromElementPoint==-1){
				if (fromElement is Node) {
					fromX=fromElement.x+fromElement.width/2;
					fromY=fromElement.y+fromElement.height/2;
				} 
				if(toElement!=null){
					var toMidX:Number = toElement.x+toElement.width/2;
					var toMidY:Number = toElement.y+toElement.height/2;
					
					var rDistance:Number=Math.sqrt(Math.pow((toMidX-fromMidX),2)+Math.pow((toMidY-fromMidY),2));  
					var vDistance:Number = toMidY - fromMidY;
					var hDistance:Number = toMidX -fromMidX;
					var halfFromCrossDistance:Number = Math.sqrt(Math.pow(fromElement.width,2)+Math.pow(fromElement.height,2))/2;
					var f_tv_crossDistance:Number = halfFromCrossDistance*Math.abs(hDistance)/(fromElement.width/2.0);
					
					var location:int = 0;	
					
					if(f_tv_crossDistance<=rDistance)location = 1;
					else location = 2;
					
					//如果起始节点是开始节点
//					if (fromElement is BeginNode || fromElement is GateWayNode) {
//						fromX = fromMidX;
//						fromY = fromMidY;
//					} 
//						//如果起始节点是非开始节点,网关
//					else {
						if(location == 1){
							var y1:Number = fromElement.height/2;
							if(toElement.y<fromMidY)y1=-y1;
							fromX = hDistance*y1/vDistance+fromMidX;
							fromY = fromMidY + y1;
						}
						else if(location == 2){
							var x1:Number = fromElement.width/2;
							if(toElement.x<fromMidX)x1=-x1;
							fromX = fromMidX + x1;
							fromY = vDistance*x1/hDistance+fromMidY;
						}				
					//}				
				}
			}	
			if(routeFromElementPoint==-1){
				var rec0:Rectangle = new Rectangle(fromElement.x,fromElement.y-h,fromElement.width,2*h);
				var rec1:Rectangle = new Rectangle(fromElement.x-w,fromElement.y,2*w,fromElement.height);
				var rec2:Rectangle = new Rectangle(fromElement.x+fromElement.width-w,fromElement.y,2*w,fromElement.height);
				var rec3:Rectangle = new Rectangle(fromElement.x,fromElement.y+fromElement.height-h,fromElement.width,2*h);
				if(rec0.contains(fromX,fromY))routeFromElementPoint = 0;
				else if(rec1.contains(fromX,fromY))routeFromElementPoint = 1;
				else if(rec2.contains(fromX,fromY))routeFromElementPoint = 2;
				else if(rec3.contains(fromX,fromY))routeFromElementPoint = 3;					
			}	
			if(toElement==null&&routeToElementPoint==-1){
				if(routeFromElementPoint==0)routeToElementPoint = 3;
				else if(routeFromElementPoint==1)routeToElementPoint = 2;
				else if(routeFromElementPoint==2)routeToElementPoint = 1;
				else if(routeFromElementPoint==3)routeToElementPoint = 0;
			}
			else if (toElement!=null) {
				//计算终点
				var minDistance:int;	 			
				var distance1:int;
				var distance2:int;
				var distance3:int;
				var distance4:int;
				if(routeToElementPoint==-1){	 			
					distance1=Math.sqrt(Math.pow(fromMidX-toElement.x,2)+Math.pow(fromMidY-(toElement.y+toElement.height/2),2)); //左
					distance2=Math.sqrt(Math.pow(fromMidX-(toElement.x+toElement.width),2)+Math.pow(fromMidY-(toElement.y+toElement.height/2),2)); //右
					distance3=Math.sqrt(Math.pow(fromMidX-(toElement.x+toElement.width/2),2)+Math.pow(fromMidY-toElement.y,2));//上
					distance4=Math.sqrt(Math.pow(fromMidX-(toElement.x+toElement.width/2),2)+Math.pow(fromMidY-(toElement.y+toElement.height),2));//下
					//取最小距离
					minDistance= Math.min(distance1,distance2,distance3,distance4);
					if (minDistance==distance1) {
						routeFromElementPoint==-1?2:routeFromElementPoint;
						routeToElementPoint = 1;
					}
					if (minDistance==distance2) {					
						routeFromElementPoint==-1?1:routeFromElementPoint;
						routeToElementPoint = 2;
					}
					if (minDistance==distance3) {
						routeFromElementPoint==-1?3:routeFromElementPoint;
						routeToElementPoint = 0;
					}
					if (minDistance==distance4) {
						routeFromElementPoint==-1?0:routeFromElementPoint;
						routeToElementPoint = 3;
					}
				}
//				else if(routeFromElementPoint==-1){
//					var rec0:Rectangle = new Rectangle(fromElement.x,fromElement.y-h,fromElement.width,2*h);
//					var rec1:Rectangle = new Rectangle(fromElement.x-w,fromElement.y,2*w,fromElement.height);
//					var rec2:Rectangle = new Rectangle(fromElement.x+fromElement.width-w,fromElement.y,2*w,fromElement.height);
//					var rec3:Rectangle = new Rectangle(fromElement.x,fromElement.y+fromElement.height-h,fromElement.width,2*h);
//					if(rec0.contains(fromX,fromY))routeFromElementPoint = 0;
//					else if(rec1.contains(fromX,fromY))routeFromElementPoint = 1;
//					else if(rec2.contains(fromX,fromY))routeFromElementPoint = 2;
//					else if(rec3.contains(fromX,fromY))routeFromElementPoint = 3;					
//				}	
			}
			
			if(routeFromElementPoint==0){
				fromX = fromElement.x + fromElement.width/2;
				fromY = fromElement.y;
			}
			else if(routeFromElementPoint==1){
				fromX = fromElement.x;
				fromY = fromElement.y + fromElement.height/2;
			}
			else if(routeFromElementPoint==2){
				fromX = fromElement.x + fromElement.width;
				fromY = fromElement.y + fromElement.height/2;
			}
			else if(routeFromElementPoint==3){
				fromX = fromElement.x + fromElement.width/2;
				fromY = fromElement.y + fromElement.height;
			}
			if(toElement!=null){
				if(routeToElementPoint==0){
					toX = toElement.x + toElement.width/2;
					toY = toElement.y;
				}
				else if(routeToElementPoint==1){
					toX = toElement.x;
					toY = toElement.y + toElement.height/2;
				}
				else if(routeToElementPoint==2){
					toX = toElement.x + toElement.width;
					toY = toElement.y + toElement.height/2;
				}
				else if(routeToElementPoint==3){
					toX = toElement.x + toElement.width/2;
					toY = toElement.y + toElement.height;
				}
			}
			
			//toElement为空时的连接点，根据不同的位置画不同的折线
			var toLocation:String = "";
			//if(toElement==null)toLocation = computeToLocation();
						
			//设置线上的label
			setLabel(fromX,fromY,toX,toY);
			//画折线
			if(routeStyle=="0"){
				if(myDrawBoard.Status=="routebegin"||myDrawBoard.Status=="nodeStartDrag")
					computeTurnPoints(toLocation);
				drawCurveLine();
				var point:Point;
				if(toElement!=null)point = turnPoints[turnPoints.length-1];
				else point = turnPoints[turnPoints.length-2];
				//箭头
				drawArrowLine(point.x,point.y,toX,toY,arrow_line_color);
				if(this.Selected==true)drawSensePoint();
			}
				//直线
			else if(routeStyle=="1"){
				this.graphics.moveTo(fromX,fromY);
				this.graphics.lineTo(toX,toY);
				drawArrowLine(fromX,fromY,toX,toY,arrow_line_color);
			}
			this.graphics.endFill();
			//Lable.x=(fromX+toX)/2;
			//Lable.y=(fromY+toY)/2;
			//Lable.width=100;
			//Lable.height=50;
			this.myDrawBoard.ReDrawRelationElement(this);
			
			//避免线重叠时，点击不知道那条线被选中
			if(this.Selected==true||State==1){				
				myDrawBoard.setChildIndex(this,myDrawBoard.numChildren-1);
			}else myDrawBoard.setChildIndex(this,0);
		}
		
		//在线上显示条件
		private function setLabel(fromX:int,fromY:int,toX:int,toY:int):void{
			if(condition==null||condition==""){
				if(myDrawBoard.contains(Lable))myDrawBoard.removeChild(Lable);
				return;
			}
			var midX:int = fromX + (toX-fromX)/2;
			var midY:int = fromY + (toY-fromY)/2;			
			Lable.text = this.conditionZH;	
			
			var width:int = Lable.width==0?200:Lable.width;
			var height:int = Lable.height==0?20:Lable.height;
			
			Lable.x = midX-width/2;
			Lable.y = midY-Lable.height/2;
			
			Lable.toolTip = this.conditionZH;			
			//这本应该加在线上的，但加在线上，线的空间太大
			myDrawBoard.addChildAt(Lable,0);
			Lable.mouseChildren = false;
		}
		//当toElement为空时，计算一个粗略的相对位置
		private function computeToLocation():String{
			var toLocation:String = "";
			var minDistance:int;	 			
			var distance1:int;
			var distance2:int;
			var distance3:int;
			var distance4:int;
			if(routeToElementPoint==-1){	 			
				distance1=Math.sqrt(Math.pow(fromElement.x+fromElement.width/2-toX,2)+Math.pow(fromElement.y-toY,2)); //上
				distance2=Math.sqrt(Math.pow(fromElement.x-toX,2)+Math.pow(fromElement.y+fromElement.height/2-toY,2)); //左
				distance3=Math.sqrt(Math.pow(fromElement.x+fromElement.width-toX,2)+Math.pow(fromElement.y+fromElement.height/2-toY,2));//右
				distance4=Math.sqrt(Math.pow(fromElement.x+fromElement.width/2-toX,2)+Math.pow(fromElement.y+fromElement.height/2-toY,2));//下
				//取最小距离
				minDistance= Math.min(distance1,distance2,distance3,distance4);
				if (minDistance==distance1) {
					toLocation = "top";
				}
				if (minDistance==distance2) {					
					toLocation = "left";
				}
				if (minDistance==distance3) {
					toLocation = "right";
				}
				if (minDistance==distance4) {
					toLocation = "right";
				}
			}
			return toLocation;
		}
		private function computeTurnPoints(toLocation:String):void{
			//删除已经存在的拐点
			turnPoints.splice(0,turnPoints.length);
			
			var point1:Point;
			var pointn:Point;
			if(routeFromElementPoint==0){
				point1 = new Point(fromX,fromY-DIS_TO_BOUND);
			}
			else if(routeFromElementPoint==1){
				point1 = new Point(fromX-DIS_TO_BOUND,fromY);
			}
			else if(routeFromElementPoint==2){
				point1 = new Point(fromX+DIS_TO_BOUND,fromY);
			}
			else if(routeFromElementPoint==3){
				point1 = new Point(fromX,fromY+DIS_TO_BOUND);
			}
			else if(routeFromElementPoint==-1){
				point1 = new Point(fromX,fromY);
			}
			if(routeToElementPoint==0){
				pointn = new Point(toX,toY-DIS_TO_BOUND);
			}
			else if(routeToElementPoint==1){
				pointn = new Point(toX-DIS_TO_BOUND,toY);
			}
			else if(routeToElementPoint==2){
				pointn = new Point(toX+DIS_TO_BOUND,toY);
			}
			else if(routeToElementPoint==3){
				pointn = new Point(toX,toY+DIS_TO_BOUND);
			}
			else if(routeToElementPoint==-1){
				pointn = new Point(toX,toY);
			}
			var temp:int;
			switch(routeFromElementPoint){
				case 0:{
					switch(routeToElementPoint){
						case 0:{
							//0-0
							if(toY>=point1.y){
								turnPoints.push(new Point(pointn.x,point1.y));
							}else{
								turnPoints.push(new Point(point1.x,pointn.y));
							}
							break;
						}
						case 1:{
							//0-1
							if(toX>fromElement.x+fromElement.width+DIS_TO_BOUND){
								if(pointn.y<point1.y)
									turnPoints.push(new Point(point1.x,pointn.y));  //右上
								else turnPoints.push(new Point(pointn.x,point1.y)); //右下
							}
							else{
								temp = toElement!=null?(toElement.y+toElement.height):toY;
								if(temp>point1.y) 
									turnPoints.push(new Point(pointn.x,point1.y));		//左上
								else turnPoints.push(new Point(pointn.x,point1.y));    //左下
							}
							break;
						}
						case 2:{
							//0-2
							if(toX<fromElement.x+fromElement.width+DIS_TO_BOUND){
								if(pointn.y<point1.y)
									turnPoints.push(new Point(point1.x,pointn.y));  
								else turnPoints.push(new Point(pointn.x,point1.y)); 
							}
							else{
								temp = toElement!=null?(toElement.y+toElement.height):toY;
								if(temp<point1.y) 
									turnPoints.push(new Point(pointn.x,point1.y));		
								else turnPoints.push(new Point(pointn.x,point1.y));    
							}
							break;
						}
						case 3:{
							//0-3
							if(toY<point1.y){
								turnPoints.push(new Point(point1.x,pointn.y));
							}
							else{
								temp = fromElement.x+fromElement.width+DIS_TO_BOUND;
								if(temp<(toElement!=null?toElement.x:toX)){
									turnPoints.push(new Point(temp,point1.y));
									turnPoints.push(new Point(temp,pointn.y));
								}				
								else if((toElement!=null?(toElement.x+toElement.width):toX)<temp){
									temp = fromElement.x-DIS_TO_BOUND;
									turnPoints.push(new Point(temp,point1.y));
									turnPoints.push(new Point(temp,pointn.y));
								}
								else {
									temp = Math.max(fromElement.x+fromElement.width,toElement!=null?(toElement.x+toElement.width):toX)+DIS_TO_BOUND;
									turnPoints.push(new Point(temp,point1.y));
									turnPoints.push(new Point(temp,pointn.y));
								}
							}
							break;
						}
					}
					break;
				}
				case 1:{
					switch(routeToElementPoint){
						case 0:{
							//1-0
							if(toX<fromX&&toY>fromY){
								turnPoints.push(new Point(pointn.x,point1.y));  
							}
							else{
								turnPoints.push(new Point(point1.x,pointn.y));		
							}
							break;
						}
						case 1:{
							//1-1
							if(toX>=point1.x){
								turnPoints.push(new Point(point1.x,pointn.y));
							}else{
								turnPoints.push(new Point(pointn.x,point1.y));
							}
							break;
						}
						case 2:{
							//1-2
							if(toX<=point1.x){
								turnPoints.push(new Point(pointn.x,point1.y));
								//turnPoints.push(new Point(pointn.x,pointn.y));
							}else{
								temp = Math.min(fromElement.y,toElement!=null?toElement.y:toY);
								turnPoints.push(new Point(point1.x,temp-DIS_TO_BOUND));
								turnPoints.push(new Point(pointn.x,temp-DIS_TO_BOUND));
							}
							break;
						}
						case 3:{
							//1-3
							if(toX<fromX&&toY<fromY){
								turnPoints.push(new Point(pointn.x,point1.y));  
							}
							else turnPoints.push(new Point(point1.x,pointn.y));		
							break;
						}
					}
					break;
				}
				case 2:{
					switch(routeToElementPoint){
						case 0:{
							//2-0
							if(toX>fromX&&toY>fromY){
								turnPoints.push(new Point(pointn.x,point1.y));  
							}
							else turnPoints.push(new Point(point1.x,pointn.y));		
							break;
						}
						case 1:{
							//2-1
							if(toX>=point1.x){
								turnPoints.push(new Point(pointn.x,point1.y));
								//turnPoints.push(new Point(pointn.x,pointn.y));
							}else{
								temp = Math.min(fromElement.y,toElement!=null?toElement.y:toY);
								turnPoints.push(new Point(point1.x,temp-DIS_TO_BOUND));
								turnPoints.push(new Point(pointn.x,temp-DIS_TO_BOUND));
							}
							break;
						}
						case 2:{
							//2-2	
							if(toX>=point1.x){
								turnPoints.push(new Point(pointn.x,point1.y));
							}else{
								turnPoints.push(new Point(point1.x,pointn.y));
							}
							break;
						}
						case 3:{
							//2-3
							if(toX>fromX&&toY<fromY){
								turnPoints.push(new Point(pointn.x,point1.y));  
							}
							else turnPoints.push(new Point(point1.x,pointn.y));		
							break;
						}
					}
					break;
				}
				case 3:{
					switch(routeToElementPoint){
						case 0:{
							//3-0
							if(toY>point1.y){
								turnPoints.push(new Point(point1.x,pointn.y));
							}
							else{
								temp = fromElement.x+fromElement.width+DIS_TO_BOUND;
								if(temp>(toElement!=null?toElement.x:toX)){
									turnPoints.push(new Point(temp,point1.y));
									turnPoints.push(new Point(temp,pointn.y));
								}				
								else if((toElement!=null?(toElement.x+toElement.width):toX)<temp){
									temp = fromElement.x-DIS_TO_BOUND;
									turnPoints.push(new Point(temp,point1.y));
									turnPoints.push(new Point(temp,pointn.y));
								}
								else {
									temp = Math.max(fromElement.x+fromElement.width,toElement!=null?(toElement.x+toElement.width):toX)+DIS_TO_BOUND;
									turnPoints.push(new Point(temp,point1.y));
									turnPoints.push(new Point(temp,pointn.y));
								}
							}
							break;
						}
						case 1:{
							//3-1
							if(toX>fromX&&toY>fromY){
								turnPoints.push(new Point(point1.x,pointn.y));  
							}
							else turnPoints.push(new Point(pointn.x,point1.y));		
							break;
						}
						case 2:{
							//3-2
							if(toX<fromX&&toY>fromY){
								turnPoints.push(new Point(point1.x,pointn.y));  
							}
							else turnPoints.push(new Point(pointn.x,point1.y));	
							break;
						}
						case 3:{
							//3-3
							if(toY>=point1.y){
								turnPoints.push(new Point(point1.x,pointn.y));
							}else{
								turnPoints.push(new Point(pointn.x,point1.y));
							}
							break;
						}
					}
					break;
				}
				default:break;
			}
			
			if(toElement==null){
				var s:Number = Math.sqrt(Math.pow(toX-fromX,2)+Math.pow(toY-fromY,2));
				var cos:Number = (toX-fromX)/s;
				var sin:Number = (toY-fromY)/s;
				turnPoints.push(new Point(toX-10*cos,toY-10*sin));
			}
			
			{
				turnPoints.unshift(point1);
				turnPoints.push(pointn);
//				turnPoints.unshift(point1);
//				turnPoints.push(pointn);
			}
			
//			var midX:int = point1.x + (pointn.x-point1.x)/2;
//			var midY:int = point1.y + (pointn.y-point1.y)/2;
//			
//			if(toLocation=="left"||toLocation=="right"){
//				turnPoints.push(new Point(midX,point1.y));
//				turnPoints.push(new Point(midX,pointn.y));
//			}
//			else{
//				turnPoints.push(new Point(point1.x,midY));
//				turnPoints.push(new Point(pointn.x,midY));
//			}			
		}
		//画折线
		private function drawCurveLine():void{
			this.graphics.moveTo(fromX,fromY);			
			for(var i:int=0;i<turnPoints.length;i++){
				var tmp:Point = turnPoints[i];
				this.graphics.lineTo(tmp.x,tmp.y);
			}
			this.graphics.lineTo(toX,toY);
		}
		//画线上可以拖动的感应区域
		private function drawSensePoint():void{
			var gc:Graphics = this.graphics;
			gc.lineStyle(0.5,0x00ff00);
			gc.beginFill(0x00ff00,0.5);
			if(turnPoints.length<2)return;
			for(var i:int=1;i<turnPoints.length;i++){
				var point1:Point = turnPoints[i-1];
				var point2:Point = turnPoints[i];
				var senseX:Number = point1.x + (point2.x-point1.x)/2.0;
				var senseY:Number = point1.y + (point2.y-point1.y)/2.0;				
				gc.drawRect(senseX-w/2,senseY-h/2,w,h);
			}	
			gc.lineStyle(1,0x0000ff);
			gc.beginFill(0x0000ff,0.5);
			gc.drawRect(fromX-w/2,fromY-h/2,w,h);
			gc.drawRect(toX-w/2,toY-h/2,w,h);
		}
		
		private function drawArrowLine(fromX:int,fromY:int,toX:int,toY:int,line_color:int):void{
			//trace("from:"+fromX+"-"+fromY+"  to:"+toX+"-"+toY);
			this.graphics.beginFill(line_color,1);
			this.graphics.lineStyle(1,line_color);
			var slopy:Number;
			var cosy:Number;
			var siny:Number;
			var Par:Number=12;
			slopy = Math.atan2((fromY - toY),(fromX - toX));
			cosy = Math.cos(slopy);
			siny = Math.sin(slopy);
			this.graphics.moveTo(toX,toY);
			this.graphics.lineTo(toX + int( Par * cosy - ( Par / 3.0 * siny ) ), toY + int( Par * siny + ( Par / 3.0 * cosy ) ) );
			this.graphics.lineTo(toX + int( Par * cosy + Par / 3.0 * siny ),toY - int( Par / 3.0 * cosy - Par * siny ) );
			this.graphics.lineTo(toX,toY);	
		}
		
		public function get conditionZH():String{
			//${0000Z010000000005FZO.swh}=='jkl'&&${0000Z010000000005FZO.wjmc}=='jlk'"
			//			var usernodes:Array = myDrawBoard.getAllUserActivities(true);			
			//			for each(var node:UseractivitiesNode in usernodes){
			//				
			//			}			
			return _conditionZH;
		}
		
		public function set conditionZH(value:String):void{
			this._conditionZH = value;
		}
		
		private function onclick(event: MouseEvent):void{
			if (myDrawBoard.Status=="") {
				myDrawBoard.UnSelectedAllElement();
				myDrawBoard.unSelectOthers(this);
				this.Selected=true;				
				{
					//使箭头可以拖拽
					//				this.graphics.clear();
					//				myDrawBoard.fromElement = this.fromElement;
					//				myDrawBoard.Status="routebegin";
					//				
				}
			}
			//如果在画线过程中，点击处非目标节点，则去除画线，如：点击空白处，
			if(myDrawBoard.Status=="routebegin"){
				myDrawBoard.removeChild(this);
				myDrawBoard.tmpElement=null;
				myDrawBoard.fromElement=null;
				myDrawBoard.toElement=null;
				myDrawBoard.Status = "";
			}
			event.stopPropagation();
		}
		
		override public function BuildXml(): XML{
			var xml:XML=new XML("<SequenceFlow/>");
			//xml.@id="SequenceFlow_"+this.ID;
			xml.@id=this.ID;
			xml.@name=this.Name;
			xml.@sourceRef=this.fromElement.ID;
			xml.@targetRef=this.toElement.ID;
			xml.@condition=condition;
			xml.@conditionZH = conditionZH;      //这不应该在xml中存在,为了不解析的方便，暂时这样
			xml.@selfDefClass = selfDefClass;			
			return xml;
		}	
		override public function BuildDIXMl():XML{
			var xml:XML=new XML("<SequenceFlowConnector/>");
			//xml.@id="Connector_"+this.ID;
			xml.@id=this.ID;
			xml.@sequenceFlowRef=this.ID;
			xml.@sourceRef=this.fromElement.ID;
			xml.@targetRef=this.toElement.ID;
			xml.@fromPoint = this.routeFromElementPoint;
			xml.@toPoint = this.routeToElementPoint;
			if(turnPoints.length>0){
				var pointStr:String = "";
				for(var i:int=0;i<turnPoints.length;i++){
					var po:Point = turnPoints[i];
					pointStr+="("+po.x+","+po.y+")";
					if(i!=turnPoints.length-1)pointStr+="-";
				}
				xml.@turnPoints = pointStr;
			}
			return xml;
		}
		override public function ParseFromXml(iXML:XML): int{
			this.ID=iXML.@id;
			this.Name=iXML.@name;	
			this.fromElement=this.myDrawBoard.GetElementFromID(iXML.@sourceRef);
			this.toElement=this.myDrawBoard.GetElementFromID(iXML.@targetRef);
			this.condition = iXML.@condition;
			this.conditionZH = iXML.@conditionZH;
			this.selfDefClass = iXML.@selfDefClass;
			return 0;
		}	
		override public function ParseDIFromXML(iXML:XML):int{
			this.ID=iXML.@ID;
			this.Name=iXML.@Name;
			var pointStr:String = iXML.@turnPoints;
			this.routeFromElementPoint = iXML.@fromPoint;
			this.routeToElementPoint = iXML.@toPoint;
			if(pointStr!=null&&pointStr!=""){
				var pointArr:Array = pointStr.split("-");
				for(var i:int=0;i<pointArr.length;i++){
					var tempStr:String = pointArr[i] as String;
					var tempArr:Array = tempStr.substring(1,tempStr.length-1).split(",");;
					var po:Point = new Point(tempArr[0],tempArr[1]);
					this.turnPoints.push(po);
				}
			}
			//			this.fromElement=this.myDrawBoard.GetElementFromID(iXML.@sourceRef);
			//			this.toElement=this.myDrawBoard.GetElementFromID(iXML.@targetRef);
			myDrawBoard.setChildIndex(this,0);
			//this.Draw();
			return 0;
		}		
	}
}