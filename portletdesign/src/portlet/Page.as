package portlet
{
	public class Page
	{
		public var pagename:String = "";
		public var title:String = "" ;
		public var lable:String = "" ;
		public var template:String ="";
		public var version:String="" ;
		public var isdefault:Boolean=false;
		public var readonly:Boolean=false;
		public var keepstate:Boolean = false;
		public var skin:String="";
		public var level:String;
		public var ordernum:int ;
		public var devices:Array = new Array();
			
		public function Page()
		{
		}
		public function updateVersion():void{
			if(this.version==null||this.version=="")
				this.version = 1+"";
			else this.version = String(int(this.version)+1);
		}
		public function validate():String{
			var errorStr:String = "";
			if(this.pagename==null||this.pagename==""){
				errorStr +="pagename 不能为空\n";
			}
			if(this.title==null||this.title==""){
				errorStr +="title 不能为空\n";
			}
			if(this.template==null||this.template==""){
				errorStr +="template 不能为空\n";
			}
			var pattern:RegExp = /^[a-zA-Z]/;
			if(this.pagename!=null&&this.pagename!=""&&pattern.test(this.pagename)==false){
				errorStr +="页面名称:只能以英文开头\n";
			}
			return errorStr;
		}
		public function BuildXml(): XML{
			var xml:XML = new XML("<page/>");
			xml.@pagename = pagename;
			xml.@template=template;
			xml.@version=version;
			xml.@isdefault=isdefault;
			xml.@readonly=readonly;
			xml.@keepstate=keepstate;
			xml.@skin=skin;
			xml.@level=level;
			xml.@lable=lable;
			xml.@ordernum=ordernum;
			xml.@devices = devices.join(",");
			return xml;
		}
		public function parseXml(iXML:XML):int{
			this.pagename = iXML.@pagename;			
			this.lable = iXML.@lable;
			this.template = iXML.@template;
			this.version = iXML.@version;
			this.isdefault = iXML.@isdefault=="true"?true:false;
			this.readonly = iXML.@readonly=="true"?true:false;
			this.keepstate = iXML.@keepstate=="true"?true:false;
			this.skin = iXML.@skin;
			this.level = iXML.@level;
			this.ordernum = int(iXML.@ordernum);
			var devicesstr:String = iXML.@devices;
			if(devicesstr!=null&&devicesstr!="")
				this.devices = devicesstr.split(",");
			return 0;
		}
		public function parseTitle(iXML:XML):int{
			if(iXML==null)return -1;
			this.title = iXML.text();
			return 0;
		}
	}
}