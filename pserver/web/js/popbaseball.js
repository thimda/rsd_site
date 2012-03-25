(function($){
  $.baseball=function(p){
	 var  cols=$(p.accepter),//accepter集
	      ground=[],//坐标空间
		  t,//被拖动元素
		  m={},//被拖动元素属性集
		  tip=$("<div style='position:absolute;height:4px;overflow:hidden; background:#F30'></div>"),//占位元素
		  clone,
		  fly,//拖动启动开关
		  base,//被选中的垒
		  mark,//最后一次插入的相对对象
		  above=true,
	      befordrag=function(v){
			  v.stopPropagation();
			  v.preventDefault();
			  m={ex:v.clientX,ey:v.clientY+$(document).scrollTop(),x:t.position().left,y:t.position().top,w:t.width(),h:t.height()};
			  $(document).mousemove(ondrag).mouseup(afterdrag);
		      if(document.body.setCapture){t.get(0).setCapture();t.get(0).onmousewheel=mousewheel}
			  },
		  ondrag=function(v){
			  v.preventDefault();
			  if(!fly){
				  fly=true;
				  makeGround();
				  tip.insertBefore(t);
				  mark=t.get(0);
				  clone=t.clone().css({"position":"absolute","opacity":0.5,"left":m.x,"top":m.y,"width":m.w,"z-index":125058687}).insertAfter(t);
				  };
			  selectBase(v.clientX+$(document).scrollLeft(),v.clientY+$(document).scrollTop());
			  clone.css({"left":v.clientX-m.ex+m.x,"top":v.clientY-m.ey+m.y+$(document).scrollTop()});
			  },
		  afterdrag=function(v){
			  if(fly){
			  var _param=new Object();
			  _param.portletId=$(t).attr("pid");
			   _param.pageName=CUR_PAGE_NAME;
			   _param.pageModule=CUR_PPAGE_MODULE;
			  var  needUpdate=false;
				    if(m.lonely){
						t.appendTo(m.lonely);
						_param.destinationId=$(m.lonely).attr("pid");
						needUpdate=true;
						}else if(mark!==t.get(0)){
						if(above){t.insertBefore(mark)}else{t.insertAfter(mark)}
						_param.destinationId=$(mark).attr("pid");
						_param.isAfter=!above;
							needUpdate=true;
						}
			//如果拖放目标是自身  则不需要发送Ajax请求
			if(needUpdate){
			$.get("/portal/pt/home/layout",_param);
			}
					tip.remove();
					clone.remove();
			        }
			  fly=false;
		      if(document.body.releaseCapture) {t.get(0).releaseCapture();t.get(0).onmousewheel=null};
			  $(document).unbind("mousemove",ondrag).unbind("mouseup",afterdrag);
			  },
		  //make base poz
		  makeGround=function(){
			  ground.length=0;
			  cols.each(function(i,o){
				  var _o=$(o);
				  ground.push([_o.offset().left,_o.width(),homebase(_o),_o]);
				  })
			  },
		  //建立各垒坐标系统
		  homebase=function(q){
			  var area=[];
			  q.find(p.target).each(function(i,o){
					var _o=$(o),_t=_o.offset().top,_h=_o.height();
					area.push([_t+_h,_o.offset().left+_o.width()/2,_o.offset().top+_h/2,o]);
					});
			  return area;
			  },
		  //寻垒
		  selectBase=function(x,y){
			  var ball,_at,pi=Math.PI/4;
			  for(var i=0,el;el=ground[i];i++){
				  if(x>el[0]&&x<el[0]+el[1]) {base=el;break};
				  };
			  for(var i =0,el;el=base[2][i];i++){
				  if(y<el[0]){ball=el;break}
				  };
			   if(base[2].length==0){
				   tip.css({"width":base[3].width(),"height":t.height(),"left":base[3].offset().left,"top":base[3].offset().top});
				   m.lonely=base[3];
				   }else{
				   m.lonely=null;  
				   if(ball==null) {ball=base[2][base[2].length-1]};
				   _at=Math.atan2(ball[2]-y,x-ball[1])+Math.PI;
				   if(_at>pi&&_at<3*pi){niceShock(ball[3],3,false)}
				   else if(_at>3*pi&&_at<5*pi){niceShock(ball[3],2,false)}
				   else if(_at>5*pi&&_at<7*pi){niceShock(ball[3],1,true)}
				   else{niceShock(ball[3],4,true)};
				   };
			  },
			niceShock=function(o,n,u){
				if(n==1){
						tip.css({"width":$(o).width(),"height":$(o).height(),"left":$(o).offset().left,"top":$(o).offset().top-4})
						}
				else if(n==2){
					tip.css({"width":$(o).width(),"height":$(o).height(),"left":$(o).offset().left,"top":$(o).offset().top-4})
						}
				else if(n==3){
						tip.css({"width":$(o).width(),"height":$(o).height(),"left":$(o).offset().left,"top":$(o).offset().top+$(o).height()})
						}
				else{
						tip.css({"width":$(o).width(),"height":$(o).height(),"left":$(o).offset().left,"top":$(o).offset().top})
						};
				if(o==mark&&above===u) return false;
				mark=o;
				above=u;
				},
			mousewheel=function(){
			  window.scrollTo(0,document.documentElement.scrollTop-window.event.wheelDelta/4)
			  };
	    //初始绑定事件
		cols.find(p.target).each(function(i,o){
			  (p.handle?$(o).find(p.handle):$(o)).mousedown(function(v){t=$(o);befordrag(v)})
			  });
		  };
})(jQuery)