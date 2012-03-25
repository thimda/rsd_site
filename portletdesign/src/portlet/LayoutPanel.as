package portlet
{
	import flash.events.Event;
	
	import mx.containers.HBox;

	public class LayoutPanel extends HBox
	{
		private var _truePanel:Layout;
		//private static var backcolor:int = 0xabcdef;
		
		public function LayoutPanel(iDrawBoard:Layout)
		{
			iDrawBoard.addChild(this);
			//backcolor -=100;
			//this.setStyle("backgroundColor", "0xabcdef");
			this.setStyle("verticalAlign","top");
			//this.setStyle("verticalAlign","middle");
			this.setStyle("horizontalAlign","center");
			this.setStyle("horizontalGap",5);
		}
		public function get TruePanel():Layout{
			return this.parent as Layout;
		}
		//取消选择所有layouts
		public function UnSelectedAllElement():void{
			var childs:Array = this.getChildren();
			for(var i:int=0;i<childs.length;i++) {
				if(!childs[i] is Layout)continue;
				var myelement:Layout = childs[i];				
				if (myelement.Selected) {
					myelement.Selected=false;
				}
				if(myelement is Layout){
					var childLayout:Layout = myelement as Layout;
					childLayout.UnSelectedAllElement();
				}
			}
		}
		public function deleteSelectedElement():void{
			var children:Array = this.getChildren();					
			for (var i:int=0;i<children.length;i++) {
				if (!(children[i] is Layout))continue;
				var ele:Layout;
				if(children[i] is Layout){
					ele = children[i] as Layout;
					if(ele.Selected==true){
						if(!this.contains(ele))return;
						this.removeChild(ele);
						this.TruePanel.adjustLayouttSize(this);
					}
				}
			}
			if(this.getChildren().length<1){
				this.TruePanel.removeChild(this);
			}
		}
	}
}