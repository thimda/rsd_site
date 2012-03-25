package workflow
{
	import workflow.decorator.SelectDecorator;
	import flash.display.Graphics;	
	
	/**
	 * 子流程
	 * */
	public class SubprocessNode extends ProcessNode 
	{				
		public function SubprocessNode(iDrawBoard:DrawBoard, iXML:XML=null)
		{
			super(iDrawBoard, iXML); 
			this.width=300;
			this.height=200;
			this.Name="子流程";
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
			Lable.y=0;
			
			var gc:Graphics = this.graphics;
			gc.clear();
			this.SetLineColor();			
			
			gc.beginFill(0xffffff,1); 
			gc.moveTo(x,y);
			gc.drawRoundRect(0,0,width,height,10,10);			
			gc.endFill();
			
			this.myDrawBoard.ReDrawRelationElement(this);
		}
		
		override public function BuildXml(): XML{
			var xml:XML=new XML("<SubProcess/>");
			xml.@id=this.ID;
			xml.@name=this.Name;			
			return xml;
		}	
		override public function BuildDIXMl(): XML{
			var xml:XML=new XML("<EmbeddedSubprocessShape/>");
			xml.@id="Sub-Process_"+this.ID;
			xml.@name="Shape_"+this.Name;
			addBaseDIAttribute(xml);
			xml.@activityRef=this.ID;
			return xml;
		}
		override public function ParseFromXml(iXML:XML): int{
			this.ID=iXML.@id;
			this.Name=iXML.@name;
			return 0;
		}	
	}
}