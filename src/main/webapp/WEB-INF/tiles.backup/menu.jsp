<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<style>
#menu .profile span { text-align: left; display: block; padding: 10px 15px 15px 0; float: left; width:170px;}
</style>

<span class="profile">
	<img src="/img/menuTitle.png" width="160px;" style="padding-left: 10px; margin-bottom: 35px;">
	<!-- 
	<a class="img" href="form_demo.html?lang=en"><img src="http://www.placehold.it/74x74/232323&amp;text=photo" alt="Mr. Awesome" /></a>
	 -->
	<span>
		<strong><c:out value="${sessionScope.userLoginInfo.username}"/> 님</strong>
		<a href="javascript:userModal('U','${sessionScope.userId}');">사용자 정보변경</a>
		<!-- a id="editBtn"	class="btn-action glyphicons pencil btn-primary" href="javascript:userModal('U','${item.user_id}');"><i></i></a> -->
		<!-- <br/><br/><a href="/logoutProcess" class="btn btn-primary btn-mini color-7" style="float: right;">로그아웃</a> -->
		<a href="/logoutProcess" class="btn btn-primary btn-mini color-7" style="float: right;">로그아웃</a>
	</span>
</span>

<ul>
	<c:forEach var="item" items="${sessionScope.userLoginInfo.menuinfo}">
		<c:choose>
			<c:when test="${item.itemOrder eq '1'}">
				<li class="glyphicons imac active"><a href="${item.url}" class="${item.menuColor}"><i></i><span>${item.name}</span></a></li>
			</c:when>
			<c:otherwise>
				<li class="hasSubmenu">
					<a data-toggle="collapse" class="glyphicons ${item.menuColor}" href="#${item.url}_submenu"><i></i><span>${item.name}</span></a>
					<ul class="collapse" id="${item.url}_submenu">
						<c:forEach var="sub" items="${item.subMenuList}">
							<li class="${sub.url}_menu"><a href="${sub.url}"><span>${sub.name}</span></a></li>
						</c:forEach>
					</ul>
				</li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
</ul>

<div class="modal fade color-6" id="userDialog" role="dialog" data-backdrop="static" style="display: none;"></div>
<div class="modal fade color-6" id="passwordDialog" role="dialog" data-backdrop="static" style="display: none;"></div>
<!-- 
<div id="search">
	<input type="text" placeholder="Quick search ..." />
	<button class="glyphicons search"><i></i></button>
</div>
 -->
 
 <!-- 
<ul>
	<li class="glyphicons home active"><a href="dashboard" class="color-3"><i></i><span>Dashboard</span></a></li>
	<li class="hasSubmenu">
		<a data-toggle="collapse" class="glyphicons vcard color-10" href="#pim_submenu"><i></i><span>개인 정보 점검</span></a>
		<ul class="collapse" id="pim_submenu">
			<li class="pimsetting_menu"><a href="pimsetting"><span>점검 설정</span></a></li>
			<li class="pimhistory_menu"><a href="pimhistory"><span>점검 수행 이력</span></a></li>
			<li class="pimresult_menu"><a href="pimresult"><span>점검 결과</span></a></li>
			<li class="securitysetting_menu"><a href="securitysetting"><span>암호화정책관리</span></a></li>
			<li class="securitypolicy_menu"><a href="securitypolicy"><span>개인정보정책적용</span></a></li>
		</ul>
	</li>
	<li class="hasSubmenu">
		<a data-toggle="collapse" class="glyphicons charts color-5" href="#stat_submenu"><i></i><span>통계 관리</span></a>
		<ul class="collapse" id="stat_submenu">
			<li class="dailystatistic_menu"><a href="dailystatistic"><span>일별 통계</span></a></li>
			<li class="monthlystatistic_menu"><a href="monthlystatistic"><span>월별 통계</span></a></li>
		</ul>
	</li>
	<li class="hasSubmenu">
		<a data-toggle="collapse" class="glyphicons settings color-6" href="#settings_submenu"><i></i><span>시스템 관리</span></a>
		<ul class="collapse" id="settings_submenu">
			<li class="systemstatus_menu"><a href="systemstatus"><span>시스템 상태</span></a></li>
			<li class="systemproperties_menu"><a href="systemproperties"><span>시스템 환경변수</span></a></li>							
			<li class="dbms_menu"><a href="dbms"><span>DBMS 관리</span></a></li>
			<li class="properties_menu"><a href="properties"><span>프로퍼티</span></a></li>
			<li class="user_menu"><a href="user"><span>사용자</span></a></li>
			<li class="log_menu"><a href="log"><span>로그</span></a></li>
		</ul>
	</li>
</ul>
 -->
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