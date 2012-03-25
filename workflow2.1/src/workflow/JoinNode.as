package workflow
{
	import flash.display.Graphics;
	import workflow.decorator.SelectDecorator;
	import mx.controls.Image;
	
	/**
	 * 汇合节点
	 * */
	public class JoinNode extends Node
	{
		public function JoinNode(iDrawBoard:DrawBoard, iXML:XML=null)
		{
			super(iDrawBoard, iXML);
			this.width=40;
			this.height=40;
			this.Name="汇合";
			decorator = new SelectDecorator(this);
		}
		
		override public function set Name(value: String): void{
			this._name = value;
			Lable.text=value;
		}
		
		private function setImage():void{
			var image:Image = new Image();
			image.source = "images/join.png";
			image.width = this.width;
			image.height = this.height;
			image.x = 0;
			image.y = 0;
			this.addChild(image);
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
			var xml:XML = new XML("<JoinEvent/>");
			xml.@id = this.ID;
			xml.@name = this.Name;
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
			return 0;
		}
		
	}
}