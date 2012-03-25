package workflow
{
	import workflow.decorator.SelectDecorator;
	
	/**
	 * 结束节点
	 * */
	public class EndNode extends Node
	{
		private static const DEFAULT_WIDTH:Number = 40;
		private static const DEFAULT_HEIGHT:Number = 40;
		
		public function EndNode(iDrawBoard:DrawBoard,iXML:XML=null)
		{
			super(iDrawBoard,iXML);
			this.width = DEFAULT_WIDTH;
			this.height = DEFAULT_WIDTH;
			this.Name="结束 ";
			decorator = new SelectDecorator(this);
		}
		override public function set Name(value: String): void{
			this._name = value;
			Lable.text=value;
		}
		override public function Draw(): void{	
			Lable.width=this.width;
			Lable.height=20;
			Lable.x=(this.width-Lable.width)/2;
			Lable.y=(this.height-Lable.height)/2;
	      	this.graphics.clear();
			this.SetLineColor();
			//this.graphics.beginFill(0xFF0000,1); 
			this.graphics.beginFill(0xdadada,1); 
			this.graphics.moveTo(x,y);
			//this.graphics.drawRect(0,0,width,height);
			this.graphics.drawCircle(this.width/2,this.height/2,this.width/2);
	      	this.graphics.endFill();

	      	this.myDrawBoard.ReDrawRelationElement(this);
		}
		
		override public function SetLineColor():void{
			this.graphics.lineStyle(2,LINE_UNSELECT_COLOR);
			if(State==1){
				this.graphics.lineStyle(2,LINE_FINISHED_COLOR);
			}
			else if(State==2){
				this.graphics.lineStyle(2,LINE_CURRENT_EXCUTE_COLOR);
			}
			else if(State==3){
				this.graphics.lineStyle(2,LINE_ADDSIGNATURE_COLOR);
			}		
		}

		override public function BuildXml(): XML{
			var xml:XML = new XML("<EndEvent/>");
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
		//兼容1.x (2.0后可以删除该方法)
		override public function ParseDIFromXML(iXML:XML):int{
			this.x=iXML.@x;
			this.y=iXML.@y;	
			//为了兼容1.x
			var tmprx:String = iXML.@rx;
			var tmpry:String = iXML.@ry;
			if(tmprx=="")this.rx = DEFAULT_LOC;
			else this.rx = iXML.@rx;
			if(tmpry=="")this.ry = DEFAULT_LOC;
			else this.ry = iXML.@ry;
			//2.x
			//			this.rx = iXML.@rx;
			//			this.ry = iXML.@ry;		
			if(myDrawBoard.simpleMode==true){
				if(rx==DEFAULT_LOC)rx = this.x;          
				if(ry==DEFAULT_LOC)ry = this.y;
				this.x = rx;
				this.y = ry;
			}
			this.width=iXML.@Width;
			this.height=iXML.@Height;
			if(this.width==0){
				this.width = DEFAULT_WIDTH;
			}
			if(this.height==0){
				this.height = DEFAULT_HEIGHT;
			}
			return 0;
		}
		
	}
}