package workflow.service
{
	import workflow.*;
	
	
	/**
	 * 运行时xml生成器(废弃了)
	 * */
	public class RuntimeXmlBuilder
	{
		private var drawBoard:DrawBoard;
		
		public function RuntimeXmlBuilder(drawBoard:DrawBoard)
		{
			this.drawBoard = drawBoard;
		}
		
		public function BuildXml(): XML{			
			var xml:XML=new XML("<Definitions/>");
			BuildProcessXml(xml);
			BuildDIXml(xml,drawBoard);
			
			return xml;
		}
		
		//元素节点信息转化为xml
		public function BuildProcessXml(root:XML): XML{
			var xml:XML=new XML("<Process/>");
			xml.@id = drawBoard.ID;
			xml.@name = drawBoard.Name;
			if(drawBoard.description!=null&&drawBoard.description!="")xml.@description = drawBoard.description;
			
			var childs:Array = drawBoard.getChildren();
			//trace("childs:"+childs.length);
			
			var lansetXml:XML = new XML("<LaneSet/>");
			var lane:XML = new XML("<Lane/>");
			lane.@id = "";
			lansetXml.appendChild(lane);
			//xml.appendChild(lansetXml);
			
			var tempXML:XML;
			for (var i:int = 0;i<childs.length;i++) {
				if(!(childs[i] is Element))continue;
				var myelement:Element = childs[i];
				if(myelement is DrawBoard){
					xml.appendChild(BuildDrawBoardXML(myelement as DrawBoard));
				}
				else xml.appendChild(myelement.BuildXml());
				if(!(myelement is Route)){					
					tempXML = <FlowElementRef>{myelement.ID}</FlowElementRef>;
					lansetXml.appendChild(tempXML);									
				}
			}
			//xml.appendChild(BuildLansetXml(lanset));
			//trace("xml:\n"+xml.toString());
			root.appendChild(xml);
			return xml;
		}
		//如果是流程或者子流程，则递归调用转化xml
		private function BuildDrawBoardXML(subProcess:DrawBoard):XML{
			var boardXml:XML = subProcess.BuildXml();
			
			var childs:Array = subProcess.getChildren();
			for (var i:int=0;i<childs.length;i++) {
				if(!(childs[i] is Element))continue;
				var myelement:Element = childs[i];
				if(myelement is DrawBoard){
					boardXml.appendChild(BuildDrawBoardXML(myelement as DrawBoard));
				}
				else boardXml.appendChild(myelement.BuildXml());
			}
			return boardXml;
		}
		
		//节点的图形信息转化为xml
		public function BuildDIXml(root:XML,drawBoard:DrawBoard): XML{
			var xml:XML=new XML("<ProcessDiagram/>");
			xml.@id="ProcessDiagram_"+drawBoard.ID;
			xml.@name=drawBoard.Name;
			xml.@x=drawBoard.x;
			xml.@y=drawBoard.y;
			xml.@width=drawBoard.width;
			xml.@height=drawBoard.height;
			xml.@processRef=drawBoard.ID;
			
			var childs:Array = drawBoard.getChildren();
			//trace("childs:"+childs.length);
			var lansetXML:XML = new XML("<LaneCompartment/>");
			xml.appendChild(lansetXML);
			for (var i:int=0;i<childs.length;i++) {
				if(!(childs[i] is Element))continue;
				var myelement:Element=childs[i];
				if(myelement is Route){
					xml.appendChild(myelement.BuildDIXMl());
				}
				else if(myelement is DrawBoard){	
					var subprocessxml:XML=myelement.BuildDIXMl();
					subprocessxml.appendChild("<LaneCompRef/>");
					
					lansetXML.appendChild(subprocessxml);					
					BuildDIXml(root,myelement as DrawBoard);
				}
				else lansetXML.appendChild(myelement.BuildDIXMl());
			}
			//trace("xml:\n"+xml.toString());
			root.appendChild(xml);
			return xml;
		}
	}
}