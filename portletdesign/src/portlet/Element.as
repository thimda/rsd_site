package portlet
{
	import flash.display.Graphics;
	import flash.filters.GlowFilter;
	import flash.geom.Matrix;
	
	import mx.containers.VBox;
	import mx.controls.Text;
	import mx.core.Container;
	/**
	 * 界面元素基类
	 * */
	public class Element extends VBox
	{
		public var myDrawBoard:Container;
		public static  const LINE_SELECT_COLOR:int=0x0000FF; 
		public static  const LINE_UNSELECT_COLOR:int=0x000000; 
		public static  const LINE_FINISHED_COLOR:int=0x0000ff;
		public static  const LINE_CURRENT_EXCUTE_COLOR:int=0xff0000;
			
		public static const MIN_WIDTH:int = 30;
		public static const MIN_HEIGHT:int = 30;
		
		private var _truePanel:Layout;
		
		protected var _selected:Boolean;
		private var _id:String="";
		protected var _name:String="";
		protected var Lable:Text=new Text();
		
		public function Element(iDrawBoard:Container,iXML:XML=null)
		{
			super();
			if(iDrawBoard==null){
				//ID = UIDUtil.createUID();    //主Layout唯一ID
				ID = "main_layout";
				return;
			}
			this.myDrawBoard=iDrawBoard;
			ID=TruePanel.GetNewElementID();
			if(iDrawBoard==null)return;
			this.myDrawBoard=iDrawBoard;
			iDrawBoard.addChild(this);
		}
		public function get ID(): String{
			return _id;
		}
		
		public function set ID(value: String): void{
			this._id = value;
		}

		public function get Name(): String{
			return _name;
		}
		
		public function set Name(value: String): void{
			this._name = value;
		}

		public function get Selected(): Boolean{
			return _selected;
		}
		
		public function get Lable2():Text{
			return this.Lable;
		}
		
		public function set Selected(value: Boolean): void{
			if (_selected!=value) {
				this._selected = value;
				if (value==false) {
					//myDrawBoard.selectedElement=null;
				} else {
					this.setFocus();
					//myDrawBoard.selectedElement=this;
				}				
				Draw();
				var evt:ElementEvent=new ElementEvent(ElementEvent.ELEMENT_SELECT_CHANGED);
				evt.srcElement=this;
				this.TruePanel.getTopLayout().dispatchEvent(evt);
			}
		}
		
		//设置绘图时的颜色
		public function SetLineColor_bake():void{
			this.graphics.lineStyle(1,LINE_UNSELECT_COLOR);
			if (Selected==true) {
				this.graphics.lineStyle(2,LINE_SELECT_COLOR);
			} else {
				this.graphics.lineStyle(1,LINE_UNSELECT_COLOR);
			}
		}
		//设置绘图时的颜色
		public function SetLineColor():void{
			var gc:Graphics = this.graphics;
			var gradientBoxMatrix:Matrix = new Matrix();
			gradientBoxMatrix.createGradientBox(this.width, this.height, Math.PI/4, 0, 0); 
			var glow :GlowFilter = new GlowFilter();
			glow.blurX = 10;
			glow.blurY = 10;
			if (Selected==true) {
				gc.lineStyle(1,LINE_SELECT_COLOR);
				glow.color = 0xff0000;
				//gc.beginGradientFill(GradientType.LINEAR, [0xffffff,0x00ff00], [1,1], [0,255], gradientBoxMatrix);
			} else {
				gc.lineStyle(1,LINE_UNSELECT_COLOR);
				glow.color = 0x8bdef9;
				//gc.beginGradientFill(GradientType.LINEAR, [0xffffff,0x8bdef9], [1,1], [0,255], gradientBoxMatrix);
			}
			
			this.filters = new Array(glow);
		}
		//画出元素
		public function Draw(): void{
			this.graphics.clear();
			if(Selected)
				this.DrawSelected();
			else
				this.DrawUnselected();
		}
		
		public function set TruePanel(value:Layout):void{
			this._truePanel = value;
		}
		public function get TruePanel():Layout{
			if(myDrawBoard==null){
				return null;
			}
			if(this is Portlet)
				_truePanel = (myDrawBoard as Layout);
			else if(this is Layout)
				_truePanel = (myDrawBoard as LayoutPanel).TruePanel;
			return _truePanel;
		}
		//画选中时的图形
		public function DrawSelected(): void{}
		//画非选中时的图形
		public function DrawUnselected(): void{}
		//转换节点信息为xml
		public function BuildXml(): XML{return null;}	
		//转换节点图形信息为xml
		public function BuildDIXMl(): XML{return null;}
		//解析节点xml信息到对象
		public function ParseFromXml(iXML:XML): int{return 0;}	
		//解析节点xml图形到界面
		public function ParseDIFromXML(iXML:XML):int{return 0;}		
	}
}