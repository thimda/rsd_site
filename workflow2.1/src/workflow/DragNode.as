package wf
{
    import wf.drag.*;
    import flash.events.*;
    import flash.geom.*;
    import mx.core.*;
    import mx.events.*;
    //import net.joinwork.studio.view.paper.*;

    public class DragNode extends Node
    {
		
        private var _wv1575:int;
        public var container:Container;
        public var _aw905:uint = 0;
        private var _ei1635:Number;
        public var _direction:String = null;
        private var _xo585:Point;
        private var _oh18:Point;
        private var _target:UIComponent;
        public var minX:uint = 0;
        public var minY:uint = 0;
        private var bl:_eu1126;
        private var _fo169:uint = 0;
        private var br:_eu1126;
        private var _jq427:_iq682;
        public var _pb1275:uint = 0;
        private var _kh1168:Number;
        private var b:_eu1126;
        public var _eg2111:Boolean = true;
        public var _rj834:Boolean = false;
        private var _cm2096:Point;
        private var l:_eu1126;
        private var r:_eu1126;
        private var t:_eu1126;
        private var _up144:int;
        private var tl:_eu1126;
        private var tr:_eu1126;
        public static var _ir614:Number = 8;
        private static var _cg1285:int = 6;
        private static var _fe1854:int = 4;
        private static var _yo1714:int = 7;
        private static var _nv1652:int = 1;
        public static var _mj315:String = "2";//irrcrpt("xgtvkecn", 2);
        private static var _uh1350:int = 2;
        private static var _uy1906:int = 5;
        private static var _vy734:int = 3;
        private static var _vq123:int = 0;
        public static var _lg139:String = "3";//irrcrpt("krulcrqwdo", 3);
        public static var _vv425:Number = 8;

        public function DragNode(iDrawBoard:DrawBoard,iXML:XML=null)
        {
			super(iDrawBoard, iXML);
            _cm2096 = new Point();
            return;
        }// end function

        private function _ff926() : void
        {
            tl = _xf1944();
            tr = _xf1944();
            bl = _xf1944();
            br = _xf1944();
            t = _xf1944();
            b = _xf1944();
            l = _xf1944();
            r = _xf1944();
            return;
        }// end function

        override protected function updateDisplayList(param1:Number, param2:Number) : void
        {
            super.updateDisplayList(param1, param2);
            if (isNaN(param1) || isNaN(param2))
            {
                return;
            }
            var _loc_3:* = _ei1635 ? (_ei1635) : (DragNode._ir614);
            var _loc_4:* = _kh1168 ? (_kh1168) : (DragNode._vv425);
            if (tl != null)
            {
                tl.setActualSize(_loc_3, _loc_4);
            }
            if (tr != null)
            {
                tr.setActualSize(_loc_3, _loc_4);
            }
            if (bl != null)
            {
                bl.setActualSize(_loc_3, _loc_4);
            }
            if (br != null)
            {
                br.setActualSize(_loc_3, _loc_4);
            }
            if (t != null)
            {
                t.setActualSize(_loc_3, _loc_4);
            }
            if (b != null)
            {
                b.setActualSize(_loc_3, _loc_4);
            }
            if (l != null)
            {
                l.setActualSize(_loc_3, _loc_4);
            }
            if (r != null)
            {
                r.setActualSize(_loc_3, _loc_4);
            }
            _yt2141(new Rectangle(0, 0, param1, param2));
            return;
        }// end function

        public function set direction(param1:String) : void
        {
            if (param1 == _direction)
            {
                return;
            }
            _direction = param1;
            _id36();
            return;
        }// end function

        public function get target() : UIComponent
        {
            return _target;
        }// end function

        override protected function createChildren() : void
        {
            _jq427 = new _iq682();
            _jq427.visible = false;
            _jq427._is1379();
            _id36();
            addChild(_jq427);
            return;
        }// end function

        private function _id36() : void
        {
            if (_direction == null)
            {
                this._ff926();
                return;
            }
			//irrcrpt("dqvj", 2)
            if (_direction == "2")
            {
                r = _xf1944();
                b = _xf1944();
                br = _xf1944();
                return;
            }
            if (_direction == _lg139)
            {
                b = _xf1944();
                bl = _xf1944();
                br = _xf1944();
            }
            else
            {
                r = _xf1944();
                tr = _xf1944();
                br = _xf1944();
            }
            return;
        }// end function

        public function set target(param1:UIComponent) : void
        {
            _target = param1;
            return;
        }// end function

        public function get _im91() : Number
        {
            return _ei1635 ? (_ei1635) : (_ir614);
        }// end function

        private function _kt1821(event:MouseEvent) : void
        {
            var _loc_5:Number = NaN;
            var _loc_6:Number = NaN;
            var _loc_7:Number = NaN;
            var _loc_8:Number = NaN;
            var _loc_2:* = event.stageX;
            var _loc_3:* = event.stageY;
            if (_direction != null)
            {
                if (_direction == _lg139)
                {
                    _loc_2 = _cm2096.x;
                    if (container != null)
                    {
                        if (_loc_3 < minY - container.verticalScrollPosition)
                        {
                            _loc_3 = minY - container.verticalScrollPosition;
                        }
                    }
                    else if (_loc_3 < minY)
                    {
                        _loc_3 = minY;
                    }
                }
                else if (_direction == _mj315)
                {
                    _loc_3 = _cm2096.y;
                    if (container != null)
                    {
                        if (_loc_2 < minX - container.horizontalScrollPosition)
                        {
                            _loc_2 = minX - container.horizontalScrollPosition;
                        }
                    }
                    else if (_loc_2 < minX)
                    {
                        _loc_2 = minX;
                    }
                }
            }
            var _loc_4:* = this.globalToLocal(new Point(_loc_2, _loc_3));
            _loc_2 = this.globalToLocal(new Point(_loc_2, _loc_3)).x;
            _loc_3 = _loc_4.y;
            switch(_up144)
            {
                case _vq123:
                {
                    _loc_7 = _xo585.x - _loc_2;
                    _loc_8 = _xo585.y - _loc_3;
                    _loc_5 = _loc_2;
                    _loc_6 = _loc_3;
                    break;
                }
                case _nv1652:
                {
                    _loc_7 = _loc_2 - _oh18.x;
                    _loc_8 = _xo585.y - _loc_3;
                    _loc_5 = _oh18.x;
                    _loc_6 = _loc_3;
                    break;
                }
                case _uh1350:
                {
                    _loc_7 = _xo585.x - _loc_2;
                    _loc_8 = _loc_3 - _oh18.y;
                    _loc_5 = _loc_2;
                    _loc_6 = _oh18.y;
                    break;
                }
                case _vy734:
                {
                    _loc_7 = _loc_2 - _oh18.x;
                    _loc_8 = _loc_3 - _oh18.y;
                    _loc_5 = _oh18.x;
                    _loc_6 = _oh18.y;
                    break;
                }
                case _cg1285:
                {
                    _loc_8 = _xo585.y - _loc_3;
                    _loc_7 = this.width;
                    _loc_5 = 0;
                    _loc_6 = _loc_3;
                    break;
                }
                case _yo1714:
                {
                    _loc_7 = this.width;
                    _loc_8 = _loc_3 - _oh18.y;
                    _loc_5 = 0;
                    _loc_6 = _oh18.y;
                    break;
                }
                case _fe1854:
                {
                    _loc_7 = _xo585.x - _loc_2;
                    _loc_8 = this.height;
                    _loc_5 = _loc_2;
                    _loc_6 = 0;
                    break;
                }
                case _uy1906:
                {
                    _loc_7 = _loc_2 - _oh18.x;
                    _loc_8 = this.height;
                    _loc_5 = _oh18.x;
                    _loc_6 = 0;
                    break;
                }
                default:
                {
                    break;
                }
            }
            if (_loc_7 < 0)
            {
                _loc_5 = _loc_5 + _loc_7;
                _loc_7 = _loc_7 * -1;
            }
            if (_loc_8 < 0)
            {
                _loc_6 = _loc_6 + _loc_8;
                _loc_8 = _loc_8 * -1;
            }
            if (_loc_8 < _pb1275)
            {
                _loc_8 = _pb1275;
            }
            if (_loc_7 < _aw905)
            {
                _loc_7 = _aw905;
            }
            _fo169 = 0;
            _yt2141(new Rectangle(_loc_5, _loc_6, _loc_7, _loc_8));
            return;
        }// end function

        private function _rw1343(event:MouseEvent) : void
        {
            event.stopImmediatePropagation();
            systemManager.addEventListener(MouseEvent.MOUSE_UP, handleRelease, true);
            _jq427.visible = true;
            _jq427.showDataTip();
            var _loc_2:* = localToGlobal(new Point(0, 0));
            var _loc_3:* = localToGlobal(new Point(width, height));
            _oh18 = this.globalToLocal(_loc_2);
            _xo585 = this.globalToLocal(_loc_3);
            _cm2096.x = event.stageX;
            _cm2096.y = event.stageY;
            switch(event.target)
            {
                case tl:
                {
                    _up144 = _vq123;
                    break;
                }
                case tr:
                {
                    _up144 = _nv1652;
                    break;
                }
                case bl:
                {
                    _up144 = _uh1350;
                    break;
                }
                case br:
                {
                    _up144 = _vy734;
                    break;
                }
                case t:
                {
                    _up144 = _cg1285;
                    break;
                }
                case b:
                {
                    _up144 = _yo1714;
                    break;
                }
                case l:
                {
                    _up144 = _fe1854;
                    break;
                }
                case r:
                {
                    _up144 = _uy1906;
                    break;
                }
                default:
                {
                    break;
                }
            }
            _rj834 = true;
            systemManager.addEventListener(MouseEvent.MOUSE_MOVE, _kt1821, true);
            return;
        }// end function

        private function _xf1944() : _eu1126
        {
            var _loc_1:* = new _eu1126();
            _loc_1.addEventListener(MouseEvent.MOUSE_DOWN, _rw1343);
            var _loc_2:Boolean = true;
            _loc_1.useHandCursor = true;
            _loc_1.buttonMode = _loc_2;
            addChild(_loc_1);
            return _loc_1;
        }// end function

        public function set _im91(param1:Number) : void
        {
            _kh1168 = param1;
            invalidateDisplayList();
            return;
        }// end function

        private function _qq2062(event:MouseEvent) : void
        {
           // _qr2021._qq2062();
            event.stopPropagation();
            return;
        }// end function

        private function _yt2141(param1:Rectangle) : void
        {
            if (_fo169 != 0)
            {
                param1.height = _fo169;
            }
            if (tl != null)
            {
                tl.move(param1.x - tl.width / 2, param1.y - tl.height / 2);
            }
            if (tr != null)
            {
                tr.move(param1.x + param1.width - tr.width / 2, param1.y - tr.height / 2);
            }
            if (bl != null)
            {
                bl.move(param1.x - bl.width / 2, param1.y + param1.height - bl.height / 2);
            }
            if (br != null)
            {
                br.move(param1.x + param1.width - br.width / 2, param1.y + param1.height - br.height / 2);
            }
            if (t != null)
            {
                t.move(param1.x + param1.width / 2 - t.width / 2, param1.y - t.height / 2);
            }
            if (b != null)
            {
                b.move(param1.x + param1.width / 2 - b.width / 2, param1.y + param1.height - b.height / 2);
            }
            if (l != null)
            {
                l.move(param1.x - l.width / 2, param1.y + param1.height / 2 - l.height / 2);
            }
            if (r != null)
            {
                r.move(param1.x + param1.width - r.width / 2, param1.y + param1.height / 2 - r.height / 2);
            }
            _jq427.move(param1.x, param1.y);
            _jq427.setActualSize(param1.width, param1.height);
            return;
        }// end function

        private function handleRelease(event:MouseEvent) : void
        {
            systemManager.removeEventListener(MouseEvent.MOUSE_UP, handleRelease, true);
            systemManager.removeEventListener(MouseEvent.MOUSE_MOVE, _kt1821, true);
            var _loc_2:* = this.localToGlobal(new Point(_jq427.x, _jq427.y));
            _jq427.visible = false;
            _jq427._is1379();
            var _loc_3:* = new Rectangle(_loc_2.x, _loc_2.y, _jq427.width, _jq427.height);
            this.setActualSize(_jq427.width, _jq427.height);
            this.move(this.x + _jq427.x, this.y + _jq427.y);
            parent.dispatchEvent(new _kh69(_kh69._yd1148, false, false, _loc_3));
            if (_target)
            {
                _ni995(_target, _loc_3);
            }
            this._rj834 = false;
            return;
        }// end function

        override public function set height(param1:Number) : void
        {
            super.height = param1;
            _fo169 = param1;
            return;
        }// end function

        public function set _gg211(param1:Number) : void
        {
            _kh1168 = param1;
            invalidateDisplayList();
            return;
        }// end function

        private function _vq342(event:EffectEvent) : void
        {
            this.visible = true;
            return;
        }// end function

        public function get _gg211() : Number
        {
            return _kh1168 ? (_kh1168) : (_vv425);
        }// end function

        private function _ni995(param1, param2:Rectangle) : void
        {
            var _loc_3:* = new Point(param2.x, param2.y);
            _loc_3 = param1.parent.globalToLocal(_loc_3);
            param1.width = param2.width;
            param1.height = param2.height;
            param1.x = _loc_3.x;
            param1.y = _loc_3.y;
            param1.invalidateDisplayList();
            return;
        }// end function

    }
}
