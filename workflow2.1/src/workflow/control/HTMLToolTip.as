package workflow.control
{
	import flash.text.StyleSheet;
	import flash.text.TextFormat;
	
	import mx.controls.Text;
	import mx.controls.ToolTip;
	
	public class HTMLToolTip extends ToolTip{  
		public function HTMLToolTip(){    
			super();
		}  
		
		override protected function commitProperties():void{ 
			super.commitProperties();  
			//设置字体格式
			//todo
//			var format:TextFormat = new TextFormat();
//			textField.setTextFormat(format);
			//设置字体样式,在text中引用.textCss样式
			//todo
//			var style:StyleSheet = new StyleSheet();
//			var styleObj:Object = new Object();
//			style.setStyle(".textCss",styleObj);
//			textField.styleSheet = style;
			
			textField.htmlText = text;  
		}  
	}  
} 

