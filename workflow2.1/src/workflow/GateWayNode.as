package workflow
{
	import flash.display.Graphics;
	
	import mx.controls.Image;
	
	import workflow.decorator.SelectDecorator;
	
	public class GateWayNode extends Node
	{
		//网关类型，0：内嵌网关，1：唯一网关
		public var type:String = "0";
		//聚合逻辑,0,逻辑与，1，逻辑或，2，按数量与
		public var logic:String = "0";
		public var count:int;
		
		public function GateWayNode(iDrawBoard:DrawBoard=null, iXML:XML=null)
		{
			super(iDrawBoard, iXML);
			this.width=40;
			this.height=40;
			this.Name="决策";
			decorator = new SelectDecorator(this);
		}
		override public function set Name(value: String): void{
			this._name = value;
			Lable.text=value;
		}
			
		override public function Draw(): void{	
			Lable.width=width;
			Lable.height=20;
			Lable.x=0;
			Lable.y=(this.height-Lable.height)/2;
			
			drowDiamond();
			
			this.myDrawBoard.ReDrawRelationElement(this);
		}
		//绘制菱形 
		private function drowDiamond():void{  
			var gc:Graphics = this.graphics;
			gc.clear();
			this.SetLineColor();	
			var width:int=this.width;
			var height:int=this.height;   
			gc.beginFill(0xffffff,1); 
			gc.moveTo(0,height/2);//移动到某个点 （左边的中点）   
			gc.lineTo(width/2,0);//移动到上边中点   
			gc.lineTo(width,height/2);//移动到右边中点   
			gc.lineTo(width/2,height);//下边的重点   
			gc.lineTo(0,height/2);//移动到起始点   
			gc.endFill();   
		}   
		
		override public function BuildXml(): XML{
			var xml:XML;
			if(type=="0")xml = new XML("<InclusiveGateway/>");
			if(type=="1")xml = new XML("<ExclusiveGateway/>");
			xml.@id = this.ID;
			xml.@name = this.Name;
			if(logic=="0")xml.@logic="And";
			else if(logic=="1")xml.@logic="Or";
			else if(logic=="2")xml.@logic="BycountAnd:"+count;
			return xml;
		}
		override public function BuildDIXMl():XML{
			var xml:XML = new XML("<EventShape/>");
			xml.@id = "Shape_"+this.ID;
			xml.@name = "Shape_"+this.Name;
			addBaseDIAttribute(xml);
			xml.@eventRef = this.ID;
			return xml;
		}
		override public function ParseFromXml(iXML:XML): int{
			this.ID=iXML.@id;
			this.Name=iXML.@name;
			if(iXML.name()=='InclusiveGateway')this.type = "0";
			else if(iXML.name()=='ExclusiveGateway')this.type = "1";
			return 0;
		}
	}
}