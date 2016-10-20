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
<div data-role="dialog" id="modify-userinfo" class="padding20" data-close-button="false" data-type="info">
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

function profile(mode, userId) {
	var url = '';
	var titleTxt = '';
	var successTxt = '';
	var width = 0;
	
	if (mode == 'U') {
		url = '/userForm?mode='+mode+'&userId=' + userId;
		titleTxt = '사용자 수정';
		successTxt = '사용자가 수정되었습니다.';
		width = 900;
	} 
	
	$.ajax({
		url : url,
		type : 'post',
		data : null,
		success : function(data, status, xhr) {
			showDialog('modify-userinfo', data);
		}, error: function (e) { 
			ajaxErrorHandler(e);
		},
		complete : function(xhr, status) {
			// double click 방지 해제
			// $(':button', form).attr('disabled', false).removeClass('disabled');
		}
	});
	
}

function userModal(mode, userId) {
	zephyros.loading.show();
	var url = '';
	var titleTxt = '';
	var successTxt = '';
	var width = 0;
	
	if (mode == 'U') {
		url = '/userForm?mode=U&userId=' + userId;
		titleTxt = '사용자 수정';
		successTxt = '사용자가 수정되었습니다.';
		width = 900;
	} 

	var html = '';
	$.ajax({
		url : url,
		type : 'post',
		data : null,
		success : function(data, status, xhr) {
			zephyros.loading.hide();
			html = data;
			zephyros.dialog({
				id : "userDialog",
				title : titleTxt,
				contents : html,
				width : width,
				buttons : [ {
					text : "저장",
					click : function() {
						var formData = $("#form02").serialize();
						//if (validationCheack()) {
							$.ajax({
								url : '/userProcess?mode='+ mode,
								type : 'post',
								data : formData,
								success : function(data, status, xhr) {
									$("#userDialog").modal("hide");
									zephyros.alert({
										contents : successTxt,
										close : function() {
											window.location.href ='/user';
										}
									});
								}, error: function (e) { 
									ajaxErrorHandler(e);
								}
							});
						//}							
					}
				}, {
					text : "닫기",
					click : function() {
						$("#userDialog").modal("hide");
					}
				} ]
			}).modal("show");
		}, error: function (e) { 
			ajaxErrorHandler(e);
		},
		complete : function(xhr, status) {
			// double click 방지 해제
			// $(':button', form).attr('disabled', false).removeClass('disabled');
		}
	});
	
}

function passwordModal(mode, userId) {
	zephyros.loading.show();
	var url = '';
	var titleTxt = '';
	var successTxt = '';
	var width = 0;
	url = '/userPasswordForm?mode=U&userId=' + userId;
	titleTxt = '비밀번호 수정';
	successTxt = '비밀번호가 수정되었습니다.';
	width = 500;

	var html = '';

	$.ajax({
		url : url,
		type : 'post',
		data : null,
		success : function(data, status, xhr) {
			zephyros.loading.hide();
			html = data;
			zephyros.dialog({
				id : "passwordDialog",
				title : titleTxt,
				contents : html,
				width : width,
				buttons : [ {
					text : "저장",
					click : function() {
						if ($("#passwordForm").valid()) {
							var passwordFormData = $("#passwordForm").serialize();
							var formData = $("#form02").serialize();
							var data = passwordFormData + '&' + formData;
							$.ajax({
								url : '/userPasswordProcess?mode=U',
								type : 'post',
								data : data,
								success : function(data, status, xhr) {
									$("#passwordDialog").modal("hide");
									zephyros.alert({
										contents : successTxt,
										close : function() {
											$("#userDialog").modal("show");
										}
									});
								}, error: function (e) { 
									ajaxErrorHandler(e);
								}
							});
						}
					}
				}, {
					text : "닫기",
					click : function() {
						$("#passwordDialog").modal("hide");
						$("#userDialog").modal("show");
					}
				} ]
			}).modal("show");
		}, error: function (e) { 
			ajaxErrorHandler(e);
		},
		complete : function(xhr, status) {
			// double click 방지 해제
			// $(':button', form).attr('disabled', false).removeClass('disabled');
		}
	});
}

function passwordCahnge(userId) {
	$("#userDialog").modal("hide");
	passwordModal("U", userId);
}
</script>