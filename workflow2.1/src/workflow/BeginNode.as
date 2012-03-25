package workflow
{
	import flash.display.Graphics;
	
	import workflow.decorator.SelectDecorator;
	
	/**
	 * 开始节点
	 * */
	public class BeginNode extends Node
	{		
		public var serviceClass:String;
		//是否允许打印，默认允许，0，允许，1，不允许
		public var allowPrint:String = "0";
		//是否包含制单节点，0，包含，1，不包含
		public var allowIncludeBill:String = "0";
		
		private static const DEFAULT_WIDTH:Number = 40;
		private static const DEFAULT_HEIGHT:Number = 40;
		
		public function BeginNode(iDrawBoard:DrawBoard,iXML:XML=null)
		{
			super(iDrawBoard,iXML);
			this.width = DEFAULT_WIDTH;
			this.height = DEFAULT_WIDTH;
			this.Name="开始";	      	
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
			var gc:Graphics = this.graphics;
			gc.clear();
			this.SetLineColor();
			//gc.beginFill(0x0000ff,1); 
			//gc.beginFill(0xffffff,1); 
			gc.moveTo(x,y);
			gc.drawCircle(this.width/2,this.height/2,this.width/2);			
			gc.endFill();
			
			//重画与该节点有关系的关联线
			this.myDrawBoard.ReDrawRelationElement(this);	
		}
		
		override public function BuildXml(): XML{
			var xml:XML = new XML("<StartEvent/>");
			if(serviceClass!=null&&serviceClass!="")xml.@serviceClass=serviceClass;
			xml.@id = this.ID;
			xml.@name = this.Name;
			//是否允许打印
			if(allowPrint=="0")xml.@allowPrint="true";
			else if(allowPrint=="1")xml.@allowPrint="false";
			//是否包含制单节点
			if(allowIncludeBill=="0")xml.@allowIncludeBill="true";
			else if(allowIncludeBill=="1")xml.@allowIncludeBill="false";
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
			this.serviceClass = iXML.@serviceClass;
			//是否允许打印
			var allowPrintStr:String = iXML.@allowPrint;
			if(allowPrintStr=="true")this.allowPrint = "0";
			else if(allowPrintStr=="false")this.allowPrint = "1";
			//是否 包含制单节点
			var allowIncludeBillStr:String = iXML.@allowIncludeBill;
			if(allowIncludeBillStr=="true")this.allowIncludeBill = "0";
			else if(allowIncludeBillStr=="false")this.allowIncludeBill = "1";
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