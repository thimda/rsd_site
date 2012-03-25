package workflow
{
	import flash.display.Graphics;
	import workflow.decorator.SelectDecorator;
	import mx.controls.Image;
	
	/**
	 * 脚本活动
	 * */
	public class ScriptactivitiesNode extends ActivitiesNode
	{
		public function ScriptactivitiesNode(iDrawBoard:DrawBoard, iXML:XML=null)
		{
			super(iDrawBoard, iXML);
			this.width=100;
			this.height=50;
			this.Name="脚本活动";
			decorator = new SelectDecorator(this);
		}
		
		private function setImage():void{
			var image:Image = new Image();
			image.source = "images/logo/script.swf";
			image.width = 6;
			image.height = 6;
			image.x = image.width+5;
			image.y = image.height+5;
			this.addChild(image);
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
			setImage();
			
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
			var xml:XML=new XML("<ScriptTask/>");
			xml.@id=this.ID;
			xml.@name=this.Name;			
			return xml;
		}	
		
		override public function BuildDIXMl(): XML{
			var xml:XML=new XML("<TaskShape/>");
			xml.@id="Shape_"+this.ID;
			xml.@name = "Shape_"+this.Name;
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