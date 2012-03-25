package workflow
{
	import mx.containers.Canvas;
	import mx.controls.Text;
	import mx.core.UIComponent;
	import mx.messaging.AbstractConsumer;
	import mx.utils.UIDUtil;
	
	/**
	 * 流程界面元素基类
	 * */
	public class Element extends Canvas
	{
		public var myDrawBoard:DrawBoard;
		public static  const LINE_SELECT_COLOR:int=0x00ff00; 
		public static  const LINE_UNSELECT_COLOR:int=0x000000; 
		public static  const LINE_FINISHED_COLOR:int=0x0000ff;
		public static  const LINE_CURRENT_EXCUTE_COLOR:int=0xff0000;
		public static  const LINE_ADDSIGNATURE_COLOR:int=0x00ff00;
			
		public static const MIN_WIDTH:int = 30;
		public static const MIN_HEIGHT:int = 30;
		
		protected var _selected:Boolean;
		private var _id:String="";
		protected var _name:String="";
		protected var Lable:Text=new Text();
		//节点执行状态,0,没有执行，1,已经完成，2，正在执行,3,加签节点，其他的默认
		public var _state:int;
		
		public function Element(iDrawBoard:DrawBoard,iXML:XML=null)
		{
			super();
			if(iDrawBoard==null){
				//ID="main_process";
				ID = UIDUtil.createUID();    //主流程唯一ID
				return;
			}
			this.myDrawBoard=iDrawBoard;
			iDrawBoard.addChild(this);
			//iDrawBoard.setChildIndex(this,iDrawBoard.getChildren().length-1);
			if (iXML==null) {
					 ID=iDrawBoard.GetNewElementID();
				     //ID = UIDUtil.createUID();
			}
			else {
				//ParseFromXml(iXML);
			}
			
//			this.focusEnabled=true;
//			this.useHandCursor=true;
//			this.buttonMode=true;
//			this.mouseChildren=true;
			//this.cursorManager.setCursor
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
		
		public function get State():int{
			return _state;
		}
		public function set State(value:int):void{
			this._state = value;
		}
		
		public function get Lable2():Text{
			return this.Lable;
		}
				
		public function get Selected(): Boolean{
			return _selected;
		}
		
		public function set Selected(value: Boolean): void{
			if (_selected!=value) {
				this._selected = value;
				if (value==false) {
					myDrawBoard.selectedElement=null;
				} else {
					this.setFocus();
					myDrawBoard.selectedElement=this;
				}
				
				Draw();
				var evt:ElementEvent=new ElementEvent(ElementEvent.ELEMENT_SELECT_CHANGED);
				evt.srcElement=this;
				myDrawBoard.dispatchEvent(evt);
			}
		}
		
		//设置绘图时的颜色
		public function SetLineColor():void{
			this.graphics.lineStyle(1,LINE_UNSELECT_COLOR);
			if(State==1){
				this.graphics.lineStyle(2,LINE_FINISHED_COLOR);
			}
			else if(State==2){
				this.graphics.lineStyle(2,LINE_CURRENT_EXCUTE_COLOR);
			}
			else if(State==3){
				this.graphics.lineStyle(2,LINE_ADDSIGNATURE_COLOR);
			}
//			if (Selected==true) {
//				this.graphics.lineStyle(2,LINE_SELECT_COLOR);
//			} else {
//				this.graphics.lineStyle(1,LINE_UNSELECT_COLOR);
//			}
		}
		//画出元素
		public function Draw(): void{
			this.graphics.clear();
			if(Selected)
				this.DrawSelected();
			else
				this.DrawUnselected();
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
		
		public function addListener():void{};
		public function removeAllListener():void{};
	}
}