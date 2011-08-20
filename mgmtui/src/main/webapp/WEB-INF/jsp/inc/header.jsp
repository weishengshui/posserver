<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>	
<%@ taglib prefix="s" uri="/struts-tags"  %>
<%
// disable the cache for web browser
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1    
response.setHeader("Pragma","no-cache"); //HTTP 1.0    
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server    
%>
<!-- 
<script type="text/javascript" src="<s:url value='/js/moveEvent.js'/>"></script>
<script type="text/javascript" src="<s:url value='/js/showDialog.js'/>"></script>
<script type="text/javascript" src="<s:url value='/js/china-rewards-common.js' />"></script>
-->

<!-- 帮助页面 --><a name="top"></a>

<div id="Warp">
	<!--Start header-->
	<div class="header">
		<div class="headTop">
			<!-- attention this hidden value : use by login system from microblog auth -->
			<input type="hidden" id="isDialogLogin" value="true"/>
			<span> 
				<s:if test="userSession.userLoggedIn">
				<ul>
					<%-- 屏蔽 "邀请" 功能
					<li id="inviteFriend_head_" style="display: none;">
					<s:if test="userSession.userLoggedIn">
						<s:if 
						test='userSession.userContext.loginStore.status.toString() == "USER_INFO_COLLECTED" ||
						userSession.userContext.loginStore.status.toString() == "FRIENDS_ADDED" || 
						userSession.userContext.loginStore.status.toString() == "PHOTO_UPLOADED" '>
							<a id="invite_link_from_head_" href="<s:url value='/invite/show_inviteFriend' />">邀请</a>
						</s:if>
					</s:if>
					</li>
					--%>
					<li><a href="<s:url value='/livingChannel_details/find_globalShop?searchContent=' />">查找</a></li>
					<li><a href="<s:url action='inbox_search' namespace='/customer/msgbox' />">消息</a></li>
					<li><a href="<s:url value='/profile/accountManagement'/>">设置</a></li>
					<li class="no"><a href="<s:url value='/customer/logout' />">退出</a></li>
				</ul>
				</s:if>
				<s:else>
				<ul>
					<li class="frist"><a href="<s:url value='/customer/loginAction'/>">登录</a></li>
					<li><a href="<s:url value='/customer/toRegister'/>">注册</a></li>
					<li class="frist"><a href="javascript:void(0);" onclick="openSinaLogin();"><img src="<s:url value='/images/weibo_btn.gif'/>" style="  margin-bottom:-3px;"/></a></li>
				</ul>
				</s:else>
			</span>
			<ul>
				<s:if test="userSession.userLoggedIn">
					<li class="yesLogin">
						<a href="<s:url value='/profile/myHome' />">
						<img src="<s:url value='/avatar/thumbnail/22/22/' /><s:property value='userSession.userContext.loginStore.baseUserId' />" align="absmiddle" />
						</a>
						<a href="<s:url value='/profile/myHome' />">
						<s:property value="userSession.userContext.loginStore.nickName" />
						</a>
					</li>
				</s:if>
				<%-- 这下面的样式容易出问题，修改要慎重! --%>
				<li style="z-index:10001">
					<label id="_current_city_from_head_page"></label><a id="c_fship_c" href="#" target="_self" class="city" onmouseover="show_menu('f_city_id')" onmouseout="hide_menu('f_city_id')">切换城市</a>
					<div class="f_city" id="f_city_id" style="display: none;" onmouseover="show_menu('f_city_id')" onmouseout="hide_menu('f_city_id')">
						<div class="f_city_img"><img src="<s:url value='/images/city.gif' />" class="hand"/></div>
						<div class="f_city_a">
							<div id="_all_city_area_head_page" class="f_city_b">
								<%--
								<ul>
									<li class="link"><a href="javascript:changeCurrentCity();">深圳</a></li>
									<li class=""><a href="javascript:changeCurrentCity();">广州</a></li>
									<li class=""><a href="javascript:changeCurrentCity();">珠海</a></li>
									<li class=""><a href="javascript:changeCurrentCity();">青岛</a></li>
								</ul>
								 --%>
							</div>
						</div>
					</div>
				</li>
				<%-- --%>
				<li class="noLogin"><a href="<s:url value='/saveIcon/' />">设为桌面图标</a></li>
				
				<li class="no"><a href="javascript:addBookmark('缤纷网','http://www.binfen.cc')">加为收藏</a></li>
			</ul>
		</div>
		<div id="menuNav">
			<div class="navLogo"><a href="<s:url value='/' />"><img src="<s:url value='/images/logo.gif'/>" /></a></div>
			<div class="nav" style='z-index:10000'>
				<ul>
					<li id="headBut_1">
						<s:if test="userSession.userLoggedIn">
							<a href="<s:url value='/profile/myHome' />"><span>我的首页</span></a>
						</s:if>
						<s:else>
							<a href="<s:url value='/' />"><span>首页</span></a>
						</s:else>
					</li>
					<li id="headBut_7" style="display: none; position:relative; "><p style="position:absolute; top:-20px; left:18px; width:25px; height:25px; background:url('<s:url value="/images/new.gif"/>') no-repeat;"></p><a href='<s:property value="#application.tuanUrl"/>'><span>团购</span></a></li>
					<li id="headBut_6" style="display: none;"><a href='<s:url value="/super_privilege/home" namespace="/"/>'><span>优惠尊享</span></a></li>
					<li id="headBut_2" class="life">
						<%-- 解决事件冒泡,href属性请勿改 --%>
						<a id="livingOuterA" 
							<%-- 屏蔽快速分类入口	
							href="javascript:void(0);" onclick='window.location.href = "<s:url value='/livingChannel_details/show_livingChannelHome'/>";return false;' 
							--%>
							href='<s:url value="/livingChannel_details/show_livingChannelHome" namespace="/"/>'
							>
							<span style="padding-bottom:7px; padding-top:6px">
								生活频道
								<%-- 屏蔽快速分类入口	
								<img src="<s:url value='/images/top_bg08_white.gif'/>"  onmousemove="this.src='<s:url value="/images/top_bg08.gif"/>'" onmouseout="this.src='<s:url value="/images/top_bg08_white.gif"/>'" onclick="showPop(event);"/>
								--%>
							</span>
						</a>
						<%-- 屏蔽快速分类入口	
						<div class="popmenu" id="popmenu" onmouseout="this.style.display='none'" onmousemove="this.style.display='block'">
							<div style=" height:29px; width:91px; position:absolute; top:-23px; left:0px"><img src='<s:url value="/images/top_bg14.gif"/>'/></div>
							<div class="popmenu_a">
								<div class="popmenu_b" id="biz_type_area"></div>
							</div>
						</div>
					 --%>
					</li>
					<li id="headBut_3" style="display: none;"><a href='<s:url value="/card/membercard" namespace="/"/>'><span>会员卡</span></a></li>
					<li id="headBut_4"><a href='<s:url value="/binFenShop_details/show_funShoppingHome" namespace="/"/>'><span>缤分商城</span></a></li>
					<s:if test="userSession.userLoggedIn">
					<li id="headBut_5"  class="message" style="display:none">
						<div class="popmenu" id="popmenu_xinxi"  style="display: block;">
							<div style="height:32px; width:61px; position:absolute; top:-30px; left:0px"><img src='<s:url value="/images/top_bg0.gif"/>'/></div>
							<div class="popmenu_a">
								<div class="popmenu_b popmenu_b_a">
									<ul id="notification_ul">
										<%-- the data will loaded by javascript --%>
									</ul>
								</div>
							</div>
						</div>
						<script language="javascript" type="text/javascript">
							var url = '<s:url action="notification" namespace="/customer/msgcenter" />';					
							var element = $("#popmenu_xinxi ul:first-child");
							
							$.getJSON(url,function(json){
								var hasNew = json.hasNew;
								if(hasNew){
									$("#headBut_5").show();
									$.each(json.notifications, function(key, value) { 
										var buf = new cr.StringBuffer();
										buf.append('<li id="msg_li_').append(key).append('" onmouseover="msgOnmouseOver(this);" onmouseout="msgOnmouseOut(this);" > <span><img src="').append("<s:url value='/images/account_x.gif'/>");
										buf.append('" class="head" onclick="closeMsg(');
										buf.append("'msg_li_");
										buf.append(key);
										buf.append("'");
										buf.append(')" /> </span>');
										buf.append('<a href="javascript:read_news(').append("'").append(key).append("'").append(');">').append(value).append('</a>');
										$(element).append(buf.toString());			
									});
								}
							});	
					</script>
					</li>
					</s:if>
				</ul>
			</div>
			<div class="navSearch">
				<input id="searchContent" style="color:#ddd;" type="text" value="找商户..." onblur="if(this.value==''){this.value='找商户...';this.style.color='#ddd'} else{this.value=this.value}" onfocus="if(this.value=='找商户...'){this.value='';this.style.color='#444'}else{this.value=this.value;this.style.color='#444'}"/>
				<input type="button" class="anniu" onclick="forwardFindPage();"/>
			</div>
			<div class="c"></div>
			<script type="text/javascript">
				function forwardFindPage() {
					var txtObj = document.getElementById('searchContent');
					txtObj.focus();
					var searchContent = txtObj.value;
					if(searchContent == "找商户..."){
						searchContent = "";
					}
					var pram = '';
					//if(searchContent != ''){
						if(txtObj.style.color != ''){
							forwardFindPageInBarByKey(searchContent);
							//searchContent = encodeURIComponent(encodeURIComponent(searchContent));
							//pram = '?searchContent=' + searchContent;
						}
					//}
					//window.location.href = '<s:url value="/livingChannel_details/find_globalShop"/>' + pram;
				}
			</script>
		</div>
	</div>
	<!--End header-->
</div>

<!-- 门店搜索 关键字 -->
<script type="text/javascript">
	function forwardFindPageInBarByKey(content){
		if(content.length > 0){
			content = content.replace(new RegExp("['./-]","gm"),"")
		}
		var tempContent = Kernel.String.trim(content);
		tempContent = encodeURIComponent(encodeURIComponent(tempContent));
		window.location.href = '<s:url value="/search/shop/0/p1-ore1"/>-k'+tempContent;
		
		//var searchContent = encodeURIComponent(content);
		//window.location.href = '<s:url value="/livingChannel_details/find_globalShop"/>?searchContent=' + searchContent;
	}
</script>

<script language="javascript" type="text/javascript">
	selectHeadBut('<s:property value="userSession.selectHeadBut"/>');
	
	<%-- 屏蔽 "邀请" 功能,屏蔽分类快速入口
	injectDataToLiving();
	initInviteCode();
	--%>
	initAllCityHeadPage('<s:property value="currentNavigationCity" escape="false"/>', true);
</script>