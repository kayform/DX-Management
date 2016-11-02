<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<header id="menu">
	<%-- <jsp:include page="/jsp/settings/userForm.jsp"/>  --%>
    <div class="app-bar fixed-top darcula" data-role="appbar">
        <a class="app-bar-element branding">eXperdb Manager</a>
        <span class="app-bar-divider"></span>
        <ul class="app-bar-menu">
			<c:forEach var="item" items="${sessionScope.userLoginInfo.menuinfo}">
				<c:choose>
					<c:when test="${item.itemOrder eq '1'}">
						<li><a href="${item.url}">${item.name}</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="${item.url}" class="dropdown-toggle">${item.name}</a>
							<ul class="d-menu" data-role="dropdown" id="${item.url}_submenu">
								<c:forEach var="sub" items="${item.subMenuList}">
									<li><a href="${sub.url}">${sub.name}</a></li>
								</c:forEach>
							</ul>
						</li>
					</c:otherwise>
				</c:choose>
			</c:forEach>        
        </ul>
        <div class="app-bar-element place-right">
            <span class="dropdown-toggle"><span class="mif-cog"></span> ${sessionScope.userId}</span>
            <div class="app-bar-drop-container place-right" data-role="dropdown" data-no-close="true" style="width: 220px">
            	<div class="padding20">         	
                    <li><a href="javascript:profile('U', '${sessionScope.userId}')">Profile</a></li>
                    <li><a href="/logoutProcess" class="fg-white3 fg-hover-yellow">Logout</a></li>
                </div>
            </div>
        </div>
    </div>   
</header>
<div id="dialog_info">
  		<form>
  		</form>		
</div> 
<div id="dialog_profile">
  <form>
  </form>
</div> 
<script>
$(document).ready(function() 
{
	var menuName = location.pathname.substring(1, location.pathname.length) + "_menu";
	
	// 메뉴 활성화
	if($("."+menuName).length > 0 && $.nodeName($("."+menuName)[0], "li")) {
		$("."+menuName).addClass("active");
		if($("."+menuName).parent().length > 0 && $.nodeName($("."+menuName).parent()[0], "ul")) {
			$("."+menuName).parent().addClass("in");
			if($("."+menuName).parent().parent().length > 0 && $.nodeName($("."+menuName).parent().parent()[0], "li")) {
				$("."+menuName).parent().parent().addClass("active");
			}
		}
	}
});

dialog_info = $("#dialog_info").dialog({
	  autoOpen: false,
	  modal: true,
	  title: "메시지",
	  close: function() {
	    //form[0].reset();
	    //allFields.removeClass("ui-state-error");
		$("#dialog_info").empty();
	  }
	});
	
dialog_profile = $("#dialog_profile").dialog({
	  autoOpen: false,
	  height: 650,
	  width: 1250,
	  modal: true,
	  title: "사용자수정",
	  buttons: {
	    "저장" : function() {
			var url = '/userProcess?mode=';
			var mode = 'U';
			var titleTxt = "";
			var successTxt = "";
			
			if (mode == 'U') {
				titleTxt = '사용자 수정';
				successTxt = '사용자가 수정되었습니다. ';
			} 
			var formData = $("#form02").serialize();
			
			$.ajax({
				url : '/userProcess?mode='+ mode,
				type : 'post',
				data : formData,
				success : function(data, status, xhr) {
					dialog_profile.dialog("close");
					zephyros.showDialog(dialog_info,  successTxt)
				}, error: function (e) { 
					ajaxErrorHandler(e);
				}
			});
	    },
	    "취소": function() {
	    	dialog_profile.dialog("close");
	      $("#dialog_profile").empty();
	    }
	  },
	  close: function() {
	    //form[0].reset();
	    //allFields.removeClass("ui-state-error");
		$("#dialog_profile").empty();
	  }
	});

function profile(mode, userId) {
	zephyros.loading.show();
	var url = '';
	var titleTxt = '';
	var successTxt = '';
	var width = 0;
	var success = null;
	var error = null;
	
	if (mode == 'U') {
		url = '/userForm?mode='+mode+'&userId=' + userId;
		titleTxt = '사용자 수정';
		successTxt = '사용자가 수정되었습니다.';
		width = 900;
	} 
	
	//callAjax(url, 'post', null);
	
	zephyros.callAjax({
		url : url,
		type : 'post',
		data : null,
		success : function(data, status, xhr) {
			zephyros.loading.show();
			zephyros.showDialog(dialog_profile, data);
		}
	});
	
	/*
	$.ajax({
		url : url,
		type : 'post',
		data : null,
		success : function(data, status, xhr) {
			zephyros.loading.show();
			showDialog(dialog_profile, data);
		}, error: function (e) { 
			ajaxErrorHandler(e);
		},
		complete : function(xhr, status) {
			// double click 방지 해제
			// $(':button', form).attr('disabled', false).removeClass('disabled');
		}
	});
	*/
}
</script>