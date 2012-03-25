package workflow.service
{
	import flash.display.Graphics;
	
	import mx.controls.Alert;
	
	import workflow.DrawBoard;
	import workflow.Element;
	import workflow.GateWayNode;
	import workflow.Node;
	import workflow.Route;
	import workflow.utils.HashMap;
	
	public class ExcuteSequenceParser
	{
		//已经走过的线
		public var finishedRoutes:Array = new Array();
		//已办节点
		public var finishedElements:Array = new Array();		
		//在办节点
		public var currentElements:Array = new Array();
		//待办节点
		public var pendingElements:Array = new Array();
		//停办节点
		public var stopElements:Array = new Array();
		//退回节点
		public var backElements:Array = new Array();
		
		private var drawBoard:DrawBoard;
		
		public function ExcuteSequenceParser(drawBoard:DrawBoard)
		{
			this.drawBoard = drawBoard;
		}
		//根据允许状态xml返回执行过的节点，连线
		public function parseFromXml(insxml:XML):Array{
			//			if(drawBoard.simpleMode==true){
			//				insxml = refactor(insxml);
			//			}
			//Alert.show("insxml:"+insxml);
			var elements:XMLList = insxml.Node;
			for each(var exml:XML in elements ){
				var id:String = exml.@id;
				var pid:String = exml.@pid;
				var rid:String = exml.@rid;       //驳回到id
				var isExe:String = exml.@isExe;
				var isPas:String = exml.@isPas;
				var isRej:String = exml.@isRej;
				var isPending:String = exml.@isPending;
				var isStop:String = exml.@isStop;
				var isBack:String = exml.@isBack;
				var tooltip:String = exml.@tooltip;
				var node:Node = null;
				var preNode:Node = null;
				//0,未处理（默认值），1,待办，2，在办,3,已办，4，停办，5，退回，6，加签，7，改派
				if(isPending=="true"){
					node = drawBoard.GetElementFromID(id) as Node;
					if(pendingElements.indexOf(node.ID)==-1)
						pendingElements.push(node.ID);
				}
				if(isExe=="true"){
					node = drawBoard.GetElementFromID(id) as Node;
					if(currentElements.indexOf(node.ID)==-1)
						currentElements.push(node.ID);
				}	
				if(isPas=="true"){
					node = drawBoard.GetElementFromID(id) as Node;
					if(finishedElements.indexOf(node.ID)==-1)
						finishedElements.push(node.ID);
				}							
				if(isStop=="true"){
					node = drawBoard.GetElementFromID(id) as Node;
					if(stopElements.indexOf(node.ID)==-1)
						stopElements.push(node.ID);
				}
				if(isBack=="true"){
					node = drawBoard.GetElementFromID(id) as Node;
					if(backElements.indexOf(node.ID)==-1)
						backElements.push(node.ID);
				}
				if(pid!=null&&pid!=""&&(isPas=="true"||isExe=="true"||isPending=="true"||isStop=="true"||isBack=="true")){
					preNode = drawBoard.GetElementFromID(pid) as Node;
					var route:Route = drawBoard.getRouteBetweenNodes(preNode,node);
					if(route!=null&&finishedRoutes.indexOf(route.ID)==-1)
						finishedRoutes.push(route.ID);
				}
				if(isRej=="true"){
					//todo
					node = drawBoard.GetElementFromID(id) as Node;
					preNode = drawBoard.GetElementFromID(rid) as Node;
					var rejroute:Route = new Route(drawBoard);
					rejroute.routeStyle = "1";
					rejroute.fromElement = node;
					rejroute.toElement = preNode;
					rejroute.line_color = 0xff0000;
					rejroute.Draw();
				}	
				if(node!=null)node.Tooltip_text = tooltip;
			}			
			for(var j:int=0;j<finishedRoutes.length;j++){
				finishedElements.push(finishedRoutes[j]);
			}
			return finishedElements;
		}
		//重构xml(去除网关后，重新调整父子关系，前提：给的执行序列是有序的)--deprecated
		private function refactor(insxml:XML):XML{			
			var map:HashMap = new HashMap();
			var elements:XMLList = insxml.Node;
			for (var k:int=0;k<elements.length();k++){
				var exml:XML = new XML(k);
				var id:String = exml.@id;
				var pid:String = exml.@pid;
				var order:String = exml.@order;
				map.put(order,exml);
			}
			for each(var exml2:XML in elements ){
				var id2:String = exml2.@id;
				var pid2:String = exml2.@pid;
				var order2:String = exml2.@order;
				exml2.@pid = getTrueParentId(map,order2);
			}
			//删除网关节点的xml
			for (var i:int=0;i<elements.length();i++){
				var tempxml:XML = elements[i];
				var id3:String = tempxml.@id;
				var node3:Node = drawBoard.GetElementFromID(id3) as Node;
				if(node3==null){
					delete elements[i];
					i--;
				}
			}
			return insxml;
		}
		//获取真正的父节点id
		private function getTrueParentId(map:HashMap,order:String):String{
			var xml:XML = map.get(order) as XML;
			var pid:String = xml.@pid;
			var node:Node = drawBoard.GetElementFromID(pid) as Node;
			//node==null表示已经删除的网关
			if(pid!=null&&pid!=""&&node==null){
				pid = getTrueParentId(map,String(int(order)-1));
			}
			return pid;
			//			for(var i:int=0;i<ids.length;i++){
			//				
			//			}
			//			return id;
		}
		//根据字符序列返回执行过的节点，连线
		public function parseFromString(sequence:String):Array{			
			var nodeIds:Array = sequence.split(",");
			var preNode:Node = null;
			for(var i:int=0;i<nodeIds.length;i++){
				var node:Node = drawBoard.GetElementFromID(nodeIds[i]) as Node;
				if(finishedElements.indexOf(node.ID)==-1)
					finishedElements.push(node.ID);
				if(preNode!=null){
					var route:Route = drawBoard.getRouteBetweenNodes(preNode,node);
					if(route!=null&&finishedRoutes.indexOf(route.ID)==-1)
						finishedRoutes.push(route.ID);
				}
				preNode = node;	
			}
			for(var j:int=0;j<finishedRoutes.length;j++){
				finishedElements.push(finishedRoutes[j]);
			}
			return finishedElements;
		}
	}
}