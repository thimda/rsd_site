package workflow
{
	import flash.display.Graphics;
	import flash.events.MouseEvent;
	
	import workflow.decorator.SelectDecorator;
	import mx.controls.Image;
	
	/**
	 * 一般活动
	 * */
	public class WorkNode extends ActivitiesNode
	{
		private var _nodetype:String="";
		//private var decorator : SelectDecorator = null;
		
		public function WorkNode(iDrawBoard:DrawBoard,iXML:XML=null)
		{
			super(iDrawBoard,iXML);
			this.width=100;
			this.height=50;
            this.Name="任务";
			decorator = new SelectDecorator(this);
		}

		public function get NodeType(): String{
			return _nodetype;
		}
		
		public function set NodeType(value: String): void{
			this._nodetype = value;
		}
		
		override public function set Name(value: String): void{
			this._name = value;
			Lable.text=value;
		}
		
		private function setImage():void{
			var image:Image = new Image();
			image.source = "images/logo/task.swf";
			image.width = 6;
			image.height = 6;
			image.x = image.width+5;
			image.y = image.height+5;
			this.addChild(image);
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
			
			//gc.beginFill(0xdff660,1); 
			gc.beginFill(0xffffff,1); 
			gc.moveTo(x,y);
			gc.drawRoundRect(0,0,width,height,10,10);			
			gc.endFill();
						
			this.myDrawBoard.ReDrawRelationElement(this);
		}
		
//		override public function DrawSelected():void {
//			this.graphics.lineStyle(2, LINE_SELECT_COLOR);
//			
//			Lable.width=width;
//			Lable.height=height;
//			
//			this.graphics.beginFill(0xdff660,1); 
//			this.graphics.moveTo(x,y);
//			this.graphics.drawRoundRect(0,0,width,height,10,10);
//			
//			this.graphics.endFill();
//			this.myDrawBoard.ReDrawRelationElement(this);
//			//decorator.decorate();
//		}
//		
//		override public function DrawUnselected():void {
//			this.graphics.lineStyle(1,LINE_UNSELECT_COLOR);
//			Lable.width=width;
//			Lable.height=height;
//			
//			this.graphics.beginFill(0xdff660,1); 
//			this.graphics.moveTo(x,y);
//			this.graphics.drawRoundRect(0,0,width,height,10,10);
//			
//			this.graphics.endFill();
//			this.myDrawBoard.ReDrawRelationElement(this);	
//		}
		
		
		override public function BuildXml(): XML{
			var xml:XML=new XML("<Task/>");
			xml.@id=this.ID;
			xml.@name=this.Name;			
			return xml;
		}	
		
		override public function BuildDIXMl(): XML{
			var xml:XML=new XML("<TaskShape/>");
			xml.@id="Shape_"+this.ID;
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