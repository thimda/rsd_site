package portlet
{
	import flash.display.Graphics;
	import flash.events.MouseEvent;
	
	import mx.core.Container;
	
	import utils.IDGenerator;
	
	/**
	 * 子Layout
	 * */
	public class LayoutNode extends Layout
	{
		
		public function LayoutNode(iDrawBoard:Container, iXML:XML=null)
		{
			super(iDrawBoard, iXML);
			Mywidth = iDrawBoard.width - LocalConst.LEFT*2;
			Myheight = iDrawBoard.height - LocalConst.UP*2;
			this.Name="Layout"+ID;
			this.addEventListener(MouseEvent.MOUSE_OVER,onmouseover);
			this.addEventListener(MouseEvent.MOUSE_OUT,onmouseout);
		}
		
		override public function set Name(value: String): void{
			this._name = value;
			Lable.text=value;
		}
		
		override public function Draw(): void{	
			this.width = Mywidth;
			this.height = Myheight;
			Lable.width=width;
			Lable.height=LocalConst.UP;
			Lable.x=0;
			Lable.y=0;			
			var gc:Graphics = this.graphics;
			gc.clear();
			SetLineColor();
			gc.moveTo(x,y);
			gc.drawRoundRect(0,0,width,height,10,10);			
			gc.endFill();		
			
		}
		//设置绘图时的颜色
		override public function SetLineColor():void{
			var gc:Graphics = this.graphics;
			if (Selected==true) {
				gc.lineStyle(2,LINE_SELECT_COLOR);
				gc.beginFill(0xffffff,1);
			} else {
				gc.lineStyle(1,LINE_UNSELECT_COLOR);
				gc.beginFill(0xffffff,1);
			}
//			var gc:Graphics = this.graphics;
//			var gradientBoxMatrix:Matrix = new Matrix();
//			gradientBoxMatrix.createGradientBox(this.width, this.height, Math.PI/4, 0, 0); 
//			var glow :GlowFilter = new GlowFilter();
//			glow.blurX = 10;
//			glow.blurY = 10;
//			if (Selected==true) {
//				gc.lineStyle(1,LINE_SELECT_COLOR);
//				glow.color = 0xff0000;
//				//gc.beginGradientFill(GradientType.LINEAR, [0xffffff,0x00ff00], [1,1], [0,255], gradientBoxMatrix);
//			} else {
//				gc.lineStyle(1,LINE_UNSELECT_COLOR);
//				glow.color = 0x8bdef9;
//				//gc.beginGradientFill(GradientType.LINEAR, [0xffffff,0x8bdef9], [1,1], [0,255], gradientBoxMatrix);
//			}
//			
//			this.filters = new Array(glow);
		}
				
//		override public function SetLineColor():void{
//			if (Selected==true) {
//				this.graphics.lineStyle(2,0x0000ff);
//			} else {
//				this.graphics.lineStyle(1,0x000000);
//			}
//		}
		
		override public function set Selected(value: Boolean): void{
			if (_selected!=value) {
				this._selected = value;
				if (value==false) {
					(myDrawBoard as LayoutPanel).TruePanel.selectedElement=null;
				} else {
					this.setFocus();
					(myDrawBoard as LayoutPanel).TruePanel.selectedElement=this;
				}				
				Draw();
				var evt:ElementEvent=new ElementEvent(ElementEvent.ELEMENT_SELECT_CHANGED);
				evt.srcElement=this;
				this.TruePanel.getTopLayout().dispatchEvent(evt);
			}
		}
		
		override public function GetNewElementID():String{
			var maxID:String = super.GetNewElementID();
			return this.ID+"_"+maxID;
		}
				
		override public function ParseFromXml(iXML:XML): int{
			var idstr:String = iXML.@id;
			this.ID=IDGenerator.delPrefix(PREFIX,idstr);
			//this.Name=iXML.@name;
			var sizes:String = iXML.@sizes;
			if(sizes!=null&&sizes!="")this.Sizes = sizes;
			this.skin = iXML.@name;
			return 0;
		}	
	}
}