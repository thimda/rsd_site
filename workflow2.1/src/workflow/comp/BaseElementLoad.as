package workflow.comp
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
			var route:RouteElement = new RouteElement();
			var begin:BeginElement = new BeginElement();
			var task:WorkElement = new WorkElement();
			var end:EndElement = new EndElement();
			
			var usertask:UserActivitiesElement = new UserActivitiesElement();
			var scripttask:ScriptActivitiesElement = new ScriptActivitiesElement();
			var sendtask:MessageElement = new MessageElement();
			var subprocess:SubProcessElement = new SubProcessElement();
			var ncprocess:NCProcessElement = new NCProcessElement();
			var esbprocess:EsbProcessElement = new EsbProcessElement();
			
			var gateway:GateWayElement = new GateWayElement();
			
			container.addChild(route);
			container.addChild(begin);
			//container.addChild(task);
						
			container.addChild(usertask);
			container.addChild(scripttask);
			container.addChild(sendtask);
			container.addChild(subprocess);
			container.addChild(ncprocess);
			container.addChild(esbprocess);
			
			container.addChild(gateway);
			container.addChild(end);
									
//			var select:Image = new Image();
//			select.source = "images/select.png";
//			var sequence:Image = new Image();
//			sequence.source = "images/sequence.png";
//			var start:Image = new Image();
//			start.source = "images/start.png";
//			var event:Image = new Image();
//			event.source = "images/event.png";
//			var end:Image = new Image();
//			end.source = "images/end.png";
//			var task:Image = new Image();
//			task.source = "images/task.png";
//			var activities:Image = new Image();
//			activities.source = "images/activities.png";
//			var fold:Image = new Image();
//			fold.source = "images/fold.png";
//			var unfold:Image = new Image();
//			unfold.source = "images/unfold.png";
//			var gateway:Image = new Image();
//			gateway.source = "images/gateway.png";
//			var text:Image = new Image();
//			text.source = "images/text.png";
//			var dataobject:Image = new Image();
//			dataobject.source = "images/dataobject.png";
//			var datasave:Image = new Image();
//			datasave.source = "images/datasave.png";
//			var message:Image = new Image();
//			message.source = "images/message.png";
//			var group:Image = new Image();
//			group.source = "images/group.png";
//			var lane:Image = new Image();
//			lane.source = "images/lane.png";			
//						
//			container.addChild(select);
//			container.addChild(sequence);
//			container.addChild(start);
//			container.addChild(event);
//			container.addChild(end);
//			container.addChild(task);
//			container.addChild(activities);
//			container.addChild(fold);
//			container.addChild(unfold);
//			container.addChild(gateway);
//			container.addChild(text);
//			container.addChild(dataobject);
//			container.addChild(datasave);
//			container.addChild(message);
//			container.addChild(group);
//			container.addChild(lane);
		}
				
		public function onclick(event: MouseEvent):void{	
			//this.Selected=true;
		}
//		public function onmousedown(event: MouseEvent):void{
//			//this.startDrag();
//			var dragSource:DragSource = new DragSource();
//			dragSource.addData(this, "baseElement");
//			DragManager.doDrag(this,dragSource,event);
//			
//		}
//		public function onmouseup(event: MouseEvent):void{
//			this.stopDrag();
//		}
		
		public function set HBox(container:HBox):void{
			this.container = container;
		}
	}
}