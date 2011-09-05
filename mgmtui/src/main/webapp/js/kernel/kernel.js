/**
 * Kernel库  1.0  
 * 内容包含Common、String、Ajax、EventUtil、KeyCode、PagingToolbar等几个类
 * @author Seek
 * @date 2011-06-10
 */
var Kernel = new function(){
	
	/**************************************************************       Common类         *********************************************************/
	/**
	 * Common类提供常用工具库
	 * @author Seek
	 * @date 2011-06-10
	   
	   对外函数：
	   getFunction();  根据函数名称返回函数对象
	   windowEval();   与window.eval()功能一致
	 */
	this.Common = (function(){
		var innerObj = function() {
		};
		
		/**
		 * 根据函数名称返回函数对象
		 * @param functionName 函数名 [字符串]
		 * @return function对象
		 */
		innerObj.getFunction = function(functionName){			//getFunction
			var func = new Function('return '+functionName)();
			return func;
		}
		
		/**
		 * 根据ID获取Dom
		 * @param id 元素ID
		 * @return 元素对象
		 */
		innerObj.getDomById = function(id){
			var dom = document.getElementById(id);
			return dom;
		}
		
		/**
		 * 根据Json字符串转js对象
		 * @param jsonStr json字符串
		 * @return js对象
		 */
		innerObj.jsonToObj = function(jsonStr){
			var obj = new Function('return '+jsonStr)();
			return obj;
		}
		
		/**
		 * 根据参数判断该参数是否是一个函数
		 * @return true是  false不是
		 */
		innerObj.isFunction = function(func){
			return !!func && !func.nodeName && func.constructor != String && func.constructor != RegExp 
				&& func.constructor != Array && /function/i.test(func + '');
		}
		
		/**
		 * 将一段字符串当成js执行，且functions变成全局的，与window.eval()功能一致。 利用 createElement 取代 eval()安全问题
		 * @param strScript 要执行的代码片段
		 * @param scriptId 新的元素ID
		 */
		innerObj.windowEval = function(strScript, scriptId){
			strScript = Kernel.String.trim(strScript);  //去除空白
            var firstHead = document.getElementsByTagName('head')[0]; // 第一个head
            
            if(Kernel.Common.getDomById(scriptId) == null){
            	var temp = document.createElement('script');	//会自动执行
                temp.type= 'text/javascript';
                temp.text= strScript;
                temp.id = scriptId;
                firstHead.appendChild(temp);
            }else {
            	new Function(strScript)();	//createElement只执行创建那一次
            }
		}
		
		return innerObj;
	})();
	
	/**************************************************************       String类         *********************************************************/
	/**
	 * String类提供常用字符串工具库
	 * @author Seek
	 * @date 2011-06-10
	   
	   对外函数：
	   trim();  去除字符串两边空白
	 */
	this.String = (function(){
		var innerObj = function() {
		};
		
		/**
		 * 去除字符串两边空白
		 * @param src 源字符串
		 * @return 处理后的字符串
		 */
		innerObj.trim = function(src) { 
			return src.replace(/(^\s*)|(\s*$)/g, ''); 
		} 
		
		return innerObj;
	})();
	
	/**************************************************************       KeyCode类         *********************************************************/
	/**
	 * KeyCode类用来保存所有键盘keycode常量
	 * @author Seek
	 * @date 2011-06-09
	  
	   对外函数：
	   getKeyCode();  根据字符串获取匹配的keyCode
	 */
	this.KeyCode = (function() {
		//不可被外部访问的闭包私有静态变量
		var constants = {
			//功能键
			Key_BackSpace : 8,
			Key_Tab : 9,
			Key_Enter : 13,
			Key_Shift_L : 16,
			Key_Control_L : 17,
			Key_Alt_L : 18,
			Key_Pause : 19,
			Key_CapsLock : 20,
			Key_Esc : 27,
			Key_Space : 32,

			//数字
			Key_0 : 48,
			Key_1 : 49,
			Key_2 : 50,
			Key_3 : 51,
			Key_4 : 52,
			Key_5 : 53,
			Key_6 : 54,
			Key_7 : 55,
			Key_8 : 56,
			Key_9 : 57,

			//字母
			Key_A : 65,
			Key_B : 66,
			Key_C : 67,
			Key_D : 68,
			Key_E : 69,
			Key_F : 70,
			Key_G : 71,
			Key_H : 72,
			Key_I : 73,
			Key_J : 74,
			Key_K : 75,
			Key_L : 76,
			Key_M : 77,
			Key_N : 78,
			Key_O : 79,
			Key_P : 80,
			Key_Q : 81,
			Key_R : 82,
			Key_S : 83,
			Key_K : 84,
			Key_U : 85,
			Key_V : 86,
			Key_W : 87,
			Key_X : 88,
			Key_Y : 89,
			Key_Z : 90,

			//小键盘
			Key_KP_0 : 96,
			Key_KP_1 : 97,
			Key_KP_2 : 98,
			Key_KP_3 : 99,
			Key_KP_4 : 100,
			Key_KP_5 : 101,
			Key_KP_6 : 102,
			Key_KP_7 : 103,
			Key_KP_8 : 104,
			Key_KP_9 : 105,

			Key_KP_Add : 107,
			Key_KP_Subtract : 109,
			Key_KP_Multiply : 106,
			Key_KP_Divide : 111,
			Key_KP_Separator : 108, //斜杠/
			Key_KP_Decimal : 110,	//小数点

			//F键
			Key_F1 : 112,
			Key_F2 : 113,
			Key_F3 : 114,
			Key_F4 : 115,
			Key_F5 : 116,
			Key_F6 : 117,
			Key_F7 : 118,
			Key_F8 : 119,
			Key_F9 : 120,
			Key_F10 : 121,
			Key_F11 : 122,
			Key_F12 : 123
		};
		//构造函数
		var constructor = function() {
		};
		
		/**
		 * 可被外部访问，也可访问闭包内私有静态变量的方法，根据key获取keycode
		 * @returns 返回的keycode
		 */
		constructor.getKeyCode = function(key) {
			return constants[key];
		};
		
		return constructor;
	})();
	
	
	/**************************************************************       EventUtil类         *********************************************************/
	/**
	 * EventUtil类是对常用Dom事件进行简单的封装以达到方便公用的目的
	 * @author Seek
	 * @date 2011-06-09
	   
	   对外函数：
	   listenKeyUpByTarget();  对某个元素进行某个keycode的监听，监听成功则传入的调用回调函数
	   activeOnclickEvent();   激活某元素的onclick事件，传入该元素id即可
	 */
	this.EventUtil = (function(){
		var innerObj = function() {
		};
		
		/**
		 * 对某元素键盘加监听
		 * @param targetObj 被监听的元素对象
		 * @param Key_NUM 键盘keycode
		 * @param responseFunName 响应该keycode的回调函数名  [字符串]
		 */
		innerObj.listenKeyUpByTarget = function(targetObj, Key_NUM, responseFunName){
			//attachEvent(IE)与addEventListener(FF)
			try{
				targetObj.addEventListener('keyup', keyUpEvent(responseFunName, Key_NUM), true);
			} catch(ex) {
				targetObj.attachEvent('onkeyup', keyUpEvent(responseFunName, Key_NUM));
			}
		}
		
		/**
		 * 键盘响应
		 * @param Key_NUM 键盘的keycode
		 * @param responseFunName 匹配keycode后调用的响应函数
		 */
		var keyUpEvent = function(responseFunName, Key_NUM){
		    return function(e){
		    	e = e || window.event;
		    	var key = e ? (e.charCode || e.keyCode) : 0;
		    	if(key == Key_NUM) { //事件
		    		var func = Kernel.Common.getFunction(responseFunName)
		    		func();
		    	}
		    }
		}
		
		/**
		 * 触发某元素的onclick事件
		 * @param element_ID  该元素的ID
		 */
		innerObj.activeOnclickEventById = function(element_ID){
			var evt = window.event?window.event:innerObj.activeOnclickEventById.caller.arguments[0];//获取event对象
		    var obj = Kernel.Common.getDomById(element_ID);
		    if(obj.fireEvent){//IE
		    	obj.fireEvent('onclick',evt);//IE下可以直接使用fireEvent方法触发事件
		    }else{//other browsers
		        //创建一个对象
		        var event = document.createEvent('MouseEvents');
		                   
		        //设置event对象属性
		        event.initMouseEvent('click', true, true, document.defaultView, 0, 0, 0, 0, 0, 
		                             false, false, false, false, 0, null);
		        //触发事件
		        obj.dispatchEvent(event); 
		    }
		}
		
		return innerObj;
	})();
	
	/**************************************************************       Ajax类         *********************************************************/
	/**
	 * Ajax类主要做异步的一些处理
	 * @author Seek
	 * @date 2011-06-10
			   
	   对外函数：
	   asynloadPageToArea();  异步加载页面到某区域
	   runInnerJsById();   执行区域中的js内容	一般在做异步load页面时调用
	 */
	this.Ajax = (function(){
		var innerObj = function() {
		};
		
		var POST = 'POST';
		var GET = 'GET';
		
		/**
		 * 异步加载页面到某区域
		 * @param URL 完整的URL，包括WebContext 以及 actionPath 以及 参数		?key=value&key2=value2 形式
		 * @param areaID	需要注入的区域  ID
		 */
		innerObj.asynloadPageToArea = function(URL, areaID){
			var xmlHttp = getXmlHttpObject();
			if (xmlHttp==null){
				alert('您的浏览器不支持AJAX!');
				return;
			}
			xmlHttp.onreadystatechange=
				function (){
					if (xmlHttp.readyState==4){
						var htmlContent = xmlHttp.responseText;
						Kernel.Common.getDomById(areaID).innerHTML = htmlContent;
						innerObj.runInnerJsById(areaID);
					}
				};
			xmlHttp.open(POST, URL, true);
			xmlHttp.send(null);
		}
		
		/**
		 * 异步请求
		 * @param URL 完整的URL，包括WebContext 以及 actionPath 以及 参数		?key=value&key2=value2 形式
		 * @param requestType 请求方式，POST 或者 GET
		 * @param functionName 回调函数名称 或者 回调函数
		 */
		innerObj.ajaxRequest = function(URL, requestType, functionName){
			var xmlHttp = getXmlHttpObject();
			if (xmlHttp==null){
				alert('您的浏览器不支持AJAX!');
				return;
			}
			
			xmlHttp.onreadystatechange=
				function (){
					if (xmlHttp.readyState==4){
						var dataObj = Kernel.Common.jsonToObj(xmlHttp.responseText);
						if(Kernel.Common.isFunction(functionName)){	//如果是函数
							functionName(dataObj);
						}else {	//如果是函数名[字符串]
							var func = Kernel.Common.getFunction(functionName);
							func(dataObj);
						}
					}
				};
			
			if(requestType == null || (POST != requestType.toUpperCase() && GET != requestType.toUpperCase()) ){	//转大写处理
				requestType = POST;
			}
			xmlHttp.open(requestType, URL, true);
			xmlHttp.send(null);
		}
		
		/**
		 * 获取ajax操作对象
		 */
		function getXmlHttpObject(){
			var xmlHttp=null;
			try {
			  // Firefox, Opera 8.0+, Safari
			  xmlHttp=new XMLHttpRequest();
			}catch (e) {
			  // Internet Explorer
			  try{
			    xmlHttp=new ActiveXObject('Msxml2.XMLHTTP');
			  }catch (e){
			    xmlHttp=new ActiveXObject('Microsoft.XMLHTTP');
			  }
			}
			return xmlHttp;
		}
		
		/**
		 * 执行区域中的js内容	一般在做异步load页面时调用
		 * @param divID 某DIV ID
		 */
		innerObj.runInnerJsById = function(divID) {
		    //xxx(this.document.body.innerHTML);
			
		    // 第一步：匹配加载的页面中是否含有js
		    var regDetectJs = /<script(.|\n)*?>(.|\n|\r\n)*?<\/script>/ig;
		    var jsContained = Kernel.Common.getDomById(divID).innerHTML.toString().match(regDetectJs);

		    // 第二步：如果包含js，则一段一段的取出js再加载执行
		    if (jsContained) {
		        // 分段取出js正则
		        var regGetJS = /<script(.|\n)*?>((.|\n|\r\n)*)?<\/script>/im;

		        // 按顺序分段执行js
		        var jsNums = jsContained.length;
		        for (var i = 0; i < jsNums; i++) {
		            var jsSection = jsContained[i].match(regGetJS);

		            if (jsSection[2]) {
		                if (window.execScript) {
		                    // 给IE的特殊待遇
		                    window.execScript(jsSection[2]);
		                } else {
		                    // 给其他大部分浏览器用的
		                    //window.eval(jsSection[2]);  已经取代
		                    Kernel.Common.windowEval(jsSection[2], 'Kernel.PagingToolBar.innerJs_ID');
		                }
		            }
		        }
		    }
		}
		return innerObj;
	})();
	
	/**************************************************************       PagingToolBar类         *********************************************************/
	/**
	 * 本类提供分页条组件，支持回调函数、模版标记等两种方式使用。
	   
	   对外接口：
	   loadPagingToolBar_DIY();   提供回调函数的形式加载分页条
	   loadPagingToolBar();   提供模版+标记的形式加载分页条 [同步与异步两种方式]
	 */
	this.PagingToolBar = (function(){
		var innerObj = function() {
		};
		
		/**
		 * Element 状态
		 */
		var checked = 'checked';  //选中状态
		var disabled = 'disabled';  //弃用状态
		var normal = 'normal';  //一般状态
		var text = 'text';  //文本状态
		
		/**
		 * 生成URL模版	只针对于常规的&key=value形式
		 * @param path 请包含URI+参数
		 * @param delimiter 分隔符
		 * @param key 请包含=号     例如：page=50    那么key等于 key=
		 * @param urlMark URL标记
		 */
		innerObj.buildURLTemplate = function(path, delimiter, key, urlMark) {
			var beginIndex;
			var endIndex;
			if((beginIndex = path.lastIndexOf(key)) == -1){
				return null;   //error path format!
			}
			
			var currentGroupParm = null;
			if((endIndex = path.indexOf(delimiter, beginIndex)) == -1){		//delimiter一般是&
				endIndex = path.length;
			}
			currentGroupParm = path.substring(beginIndex, endIndex);
			
			var URLTemplate = path.replace(currentGroupParm, key+urlMark);	
			return URLTemplate;
		}
		
		/**
		 * 加载分页条，调用者自己实现setPage()	如果用户想做异步的不刷新加载的列表，可以在自己的回调函数里，调用asynloadPageToArea();实现
		 * @param pagingToolBarID 分页条DIV id号
		 * @param page 当前页号
		 * @param size 每页显示条数
		 * @param countTotal 总数据量
		 * @param butNum 分页条中按钮数量	[默认为5个，范围在3-20之间，且如果是偶数自动加1，为了两边对称]		[不是必填,请赋值null]
		 * @param setPageFunName 调用者传入的回调函数名  字符串	标准：function(toPageValue, pageCount);	第一个是要去的页号，第二个是总页数
		 */
		innerObj.loadPagingToolBar_DIY = function(pagingToolBarID, page, size, countTotal, butNum, setPageFunName){
			//总页数
			var pageCount = Math.floor(countTotal / size + (countTotal % size == 0 ? 0 : 1));
			//生成元素集合
			var elementList = buildPagingAllElement(page, size, countTotal, butNum, setPageFunName, pageCount);
			//构造分页条
			buildPagingToolBar(pagingToolBarID, elementList, setPageFunName, pageCount);
		}
		
		/**
		 * 加载分页条	[此方法带有强制性，需要提供URL模版 以及 Mark标记]
		 * @param pagingToolBarID 分页条DIV id号
		 * @param page 当前页号
		 * @param size 每页显示条数
		 * @param countTotal 总数据量
		 * @param butNum 分页条中按钮数量	[默认为5个，范围在3-20之间，且如果是偶数自动加1，为了两边对称]		[不是必填,请赋值null]
		 * @param urlTemplate URL模版   /WebContext/.../xx.html
		 * @param urlMark 标记
		 * @param loadType 加载方式  如果填入字符串Ajax，那么就做异步加载， 否则就同步  	[不是必填]
		 * @param outerDivID 最外层的DIV ID  [如果是Ajax加载的话，必填]   	[不是必填]
		 */
		innerObj.loadPagingToolBar = function(pagingToolBarID, page, size, countTotal, butNum, urlTemplate, urlMark, loadType, outerDivID){
			var innerLoadType = null;
			if(loadType != null && loadType != '' && typeof(loadType) != 'undefine'){
				innerLoadType = loadType;
			}
			var innerOuterDivID = null;
			if(outerDivID != null && outerDivID != '' && typeof(outerDivID) != 'undefine'){
				innerOuterDivID = outerDivID;
			}
			
			//总页数
			var pageCount = Math.floor(countTotal / size + (countTotal % size == 0 ? 0 : 1));
			//生成元素集合
			var elementList = buildPagingAllElement(page, size, countTotal, butNum, urlTemplate, urlMark, pageCount);
			//构造分页条
			buildPagingToolBar(pagingToolBarID, elementList, urlTemplate, urlMark, pageCount, innerLoadType, innerOuterDivID);
		}
		
		/**
		 * 生成要显示的元素
		 */
		function buildPagingAllElement(){
			//参数初始化
			var page = new Number(arguments[0]);
			var size = new Number(arguments[1]);
			var countTotal = new Number(arguments[2]);
			var butNum = new Number(arguments[3]==null?5:arguments[3]);
			var urlTemplate = null;
			var urlMark = null;
			var setPageFunName = null;
			var pageCount = null;	//总页数
			//越界处理
			if(butNum < 3 || butNum > 20){
				butNum == 5;
			}
			//处理偶数	如果是偶数自动减1，为了两边对称
			if(butNum%2 == 0){
				butNum = butNum-1;
			}
			
			//模版+标记
			if(arguments.length == 7){
				urlTemplate = arguments[4];
				urlMark = arguments[5];
				pageCount = arguments[6];
			}
			//DIY
			else if(arguments.length == 6){
				setPageFunName = arguments[4];
				pageCount = arguments[5];
			}
			
			//返回的数组
			var list = new Array();
			
			
			//页面越界处理
			if(page < 1 || page > pageCount){
				return null;
			}
			//只有一页不分页
			if (pageCount <= 1) {
				return null;
			}
			
			var start = page - Math.floor(butNum / 2);
			var end = page + Math.floor(butNum / 2);
			
			if (start <= 1) {
				end = end + 1 - start + 1;		//补了一个
				//start = 1;
			}
			if (end >= pageCount) {
				start = start - (end - pageCount) - 1;		//补了一个
			}
			
			//分页栏目越界处理
			if (start < 1){
				start = 1;
			}
			if (end > pageCount) {
				end = pageCount;
			}
			
			var tempPage;	//临时变量
			
			//创建一个Element对象
			var aPagingElement = null;
			
			//上一页
			aPagingElement = new PagingElement();
			aPagingElement.value = '上一页';
			if(page == 1){
				aPagingElement.status = disabled;
			}else {
				aPagingElement.status = normal;
				tempPage = page - 1;
				aPagingElement.URL = getElementURL(setPageFunName, urlTemplate, urlMark, tempPage, pageCount);	//获取URL
			}
			list[getListIndex(list)] = aPagingElement;	//加入list
			
			//首页
			aPagingElement = new PagingElement();
			aPagingElement.value = '1';
			if(page == 1){
				aPagingElement.status = checked;
			}else {
				aPagingElement.status = normal;
			}
			tempPage = 1;
			aPagingElement.URL = getElementURL(setPageFunName, urlTemplate, urlMark, tempPage, pageCount);	//获取URL
			list[getListIndex(list)] = aPagingElement;	//加入list
			
			for (i=start; i<=end; i++) {
				if(i == start && start-1 >= 2){
					aPagingElement = new PagingElement();
					aPagingElement.value = '...';	//省略
					aPagingElement.status = text;
					list[getListIndex(list)] = aPagingElement;
				}
				
				if(i!=1 && i!=pageCount){	//两个都不相等否则重复
					aPagingElement = new PagingElement();
					aPagingElement.value = '' + i;
					if (i == page) {
						aPagingElement.status = checked;	//表示选中
					} else {
						aPagingElement.status = normal;
						tempPage = i;
						aPagingElement.URL = getElementURL(setPageFunName, urlTemplate, urlMark, tempPage, pageCount);	//获取URL
					}
					list[getListIndex(list)] = aPagingElement;	//加入list
				}
				
				if(i == end && pageCount-end >= 2){
					aPagingElement = new PagingElement();
					aPagingElement.value = '...';	//省略
					aPagingElement.status = text;
					list[getListIndex(list)] = aPagingElement;
				}
			}
			
			//末页
			aPagingElement = new PagingElement();
			aPagingElement.value = ''+pageCount;
			if(page == pageCount){
				aPagingElement.status = checked;
			}else {
				aPagingElement.status = normal;
			}
			tempPage = pageCount;
			aPagingElement.URL = getElementURL(setPageFunName, urlTemplate, urlMark, tempPage, pageCount);	//获取URL
			list[getListIndex(list)] = aPagingElement;	//加入list
			
			//下一页
			aPagingElement = new PagingElement();
			aPagingElement.value = '下一页';
			if(page == pageCount){
				aPagingElement.status = disabled;
			}else {
				aPagingElement.status = normal;
				tempPage = page + 1;
				aPagingElement.URL = getElementURL(setPageFunName, urlTemplate, urlMark, tempPage, pageCount);	//获取URL
			}
			list[getListIndex(list)] = aPagingElement;	//加入list
			
			return list;
		}
		
		/**
		 * 构造分页条
		 * @param pagingToolBarID 分页条DIV ID
		 * @param elementList 分页条元素列表
		 */
		function buildPagingToolBar(pagingToolBarID, elementList){
			//没有内容就跳出
			if(elementList == null || elementList.length == 0){
				return;
			}
			
			var urlTemplate = arguments[2];
			var urlMark = arguments[3];
			var pageCount = arguments[4];	//总页数
			var loadType = arguments[5];
			var outerDivID = arguments[6];
			var setPageFunName = arguments[2];
			
			//模版+标记
			if(arguments.length == 7){
				urlTemplate = arguments[2];
				urlMark = arguments[3];
				pageCount = arguments[4];
				loadType = arguments[5];
				outerDivID = arguments[6];
			}else if(arguments.length == 4){//DIY 自己玩
				setPageFunName = arguments[2];
				pageCount = arguments[3];
			}
			
			var content = '';
			for(i=0;i<elementList.length;i++){
				var aPagingElement = elementList[i];
				if(aPagingElement.status == disabled){
					content += '<a href="javascript:void(0);" class="disabled">'+aPagingElement.value+'</a>';
				}else if(aPagingElement.status == checked){
					content += '<a href="javascript:void(0);" class="checked">'+aPagingElement.value+'</a>';
				}else if(aPagingElement.status == normal){
					
					if(loadType != null && 'AJAX' == loadType.toUpperCase()){
						content += '<a href="'+aPagingElement.URL+'" onclick="return Kernel.PagingToolBar.asynLoadListPageToDIV(\''+aPagingElement.URL+'\', \''+outerDivID+'\');">'+aPagingElement.value+'</a>';
					}else {
						content += '<a href="'+aPagingElement.URL+'">'+aPagingElement.value+'</a>';
					}
					
				}else if(aPagingElement.status == text){
					content += aPagingElement.value;
				}
				content += ' ';
			}
			
			content += '&nbsp;&nbsp;&nbsp;';
			content += '到 <input type="text" class="text" size="2" maxlength="4" id="toPageTxt_ID" onfocus="Kernel.EventUtil.listenKeyUpByTarget(this, Kernel.KeyCode.getKeyCode(\'Key_Enter\'), \'Kernel.PagingToolBar.proxyActiveEnterKeyOnclickEvent\');"/> 页&nbsp;';
			
			//模版+标记
			if(arguments.length == 7){
				content += '<a id="pagingConfirmBut_ID" href="javascript:void(0);" onclick="return Kernel.PagingToolBar.pagingToolBarToPage(\''+urlTemplate+'\',\''+urlMark+'\', '+pageCount+', \''+loadType+'\', \''+outerDivID+'\');">确定</a>';
			}else if(arguments.length == 4){//DIY	自己玩
				content += '<a id="pagingConfirmBut_ID" href="javascript:void(0);" onclick="return Kernel.PagingToolBar.proxySetPage(\''+setPageFunName+'\', '+pageCount+');">确定</a>';
			}
			Kernel.Common.getDomById(pagingToolBarID).innerHTML = content;
		}
		
		/**
		 * 代理触发确定按钮的onclick事件
		 */
		innerObj.proxyActiveEnterKeyOnclickEvent = function(){
			Kernel.EventUtil.activeOnclickEventById('pagingConfirmBut_ID');
		}
		
		/**
		 * 异步加载列表到DIV
		 * @param URL 带请求参数的URL
		 * @param outerDivID 外部DIV ID
		 */
		innerObj.asynLoadListPageToDIV = function(URL, outerDivID){
			Kernel.Ajax.asynloadPageToArea(URL, outerDivID);
			return false;	//解决<a>
		}
		
		/**
		 * 获取文本框的值
		 */
		function getToPageTxtValue(pagingToolBarID){
			return Kernel.Common.getDomById('toPageTxt_ID').value;
		}
		
		/**
		 * 去指定数目页面
		 * @param toPageTxtValue 文本框输入的值
		 * @param urlTemplate URL模版
		 * @param urlMark URL标记
		 * @param pageCount 总页数
		 * @param loadType 加载方式  如果填入字符串Ajax，那么就做异步加载， 否则就同步
		 */
		innerObj.pagingToolBarToPage = function(urlTemplate, urlMark, pageCount, loadType, outerDivID){
			var toPageTxtValue = getToPageTxtValue();
			//非数字
			if(isNaN(toPageTxtValue)){
				return;
			}
			//越界
			if(toPageTxtValue<1 || toPageTxtValue>pageCount){
				return;
			}
			//转向指定URL
			var URL = urlTemplate.replace(urlMark, toPageTxtValue);
			if(loadType != null && 'AJAX' == loadType.toUpperCase()){
				innerObj.asynLoadListPageToDIV(URL, outerDivID);	//异步加载到外部DIV中
			}else {
				window.location.href = URL;
			}
			return false;
		}
		
		/**
		 * 调用传入的函数去指定页面
		 * @param setPageFunName 调用者传入的回调函数名  字符串
		 * @param pageCount 总页数
		 */
		innerObj.proxySetPage = function(setPageFunName, pageCount){
			var toPageTxtValue = getToPageTxtValue();
			var func = Kernel.Common.getFunction(setPageFunName);
			func(toPageTxtValue, pageCount);
			return false;
		}
		
		/**
		 * 获取数组的当前可用下标
		 * @param list
		 * @returns
		 */
		function getListIndex(list){
			if(list == null || typeof(list) == 'undefine'){
				return 0;
			}else {
				return list.length;
			}
		}
		
		/**
		 * 获取元素的URL
		 * @param setPageFunName 调用者传入的回调函数名  字符串
		 * @param urlTemplate URL模版   /WebContext/.../xx.html
		 * @param mark 标记
		 * @param tempPage 页号
		 */
		function getElementURL(setPageFunName, urlTemplate, urlMark, tempPage, pageCount){
			var url = null;
			if(setPageFunName != null){
				url = 'javascript:'+ setPageFunName + '(\''+tempPage+'\', '+pageCount+');';
			}else {
				url = urlTemplate.replace(urlMark, tempPage);
			}
			return url;
		}
		
		/**
		 * description: PagingElement类
		 */
		function PagingElement(){
			this.status = null;
			this.value = null;
			this.URL = null;
		}
		
		return innerObj;
	})();
	
}
