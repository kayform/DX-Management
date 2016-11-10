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
<div id="dialog_password">
  <form>
  </form>
</div> 
<div id="dialog_password2">
  <form>
  </form>
</div> 
<script>
/*
function no_submit(){
    return false;
}*/

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
	  resizable: false,
	  buttons: {
	  "확인" : function() {		  
		  $("#dialog_info").dialog("close");
		  $("#dialog_info").empty();
	    }
	  },
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
	  resizable: false,
	  buttons: {
	    "저장" : function() {
	    	if (zephyros.isFormValidate('form02')){
				//var url = '/userProcess?mode=';
				var url = '/userProcess';
				var mode = $('#mode').val(); 
				var titleTxt = "";
				var successTxt = "사용자가 생성되었습니다";
				
				if (mode == 'U') {
					titleTxt = '사용자 수정';
					successTxt = '사용자가 수정되었습니다. ';
				} 
				var formData = $("#form02").serialize();
				
				zephyros.callAjax({
					url : url,
					type : 'post',
					data : formData,
					success : function(data, status, xhr) {
						dialog_profile.dialog("close");
						zephyros.showDialog(dialog_info,  successTxt)
					}
				});		
	    	}

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

dialog_password = $("#dialog_password").dialog({
	  autoOpen: false,
	  modal: true,
	  title: "암호수정",
	  height: 300,
	  width: 800,
	  resizable: false,
	  buttons: {
	    "저장" : function() {
				if (zephyros.isFormValidate('passwordForm')){
					var passwordFormData = $("#passwordForm").serialize();
					var formData = $("#form02").serialize();
					var data = passwordFormData + '&' + formData;
					var mode = 'U';
					
					if (mode == 'U') {
						successTxt = '암호가 수정되었습니다. ';
					} 
					//zephyros.showDialog(dialog_info,  successTxt);
 					zephyros.callAjax({
						url : '/userPasswordProcess?mode='+ mode,
						type : 'post',
						data : data,
						success : function(data, status, xhr) {
							zephyros.showDialog(dialog_info,  successTxt);
							dialog_password.dialog("close");
						}
					});	 
				}
	    },
	    "취소": function() {
	    	dialog_password.dialog("close");
	      $("#dialog_password").empty();
	    }
	  },
	  close: function() {
		$("#dialog_password").empty();
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

 	zephyros.callAjax({
		url : url,
		type : 'post',
		data : null,
		success : function(data, status, xhr) {
			zephyros.loading.show();
			zephyros.showDialog(dialog_profile, data);
		}
	}); 
}

function passwordModal(mode, userId) {
	//zephyros.loading.show();
	var url = '';
	var titleTxt = '';
	var successTxt = '';
	var width = 0;
	url = '/userPasswordForm?mode=U&userId=' + userId;
	titleTxt = '비밀번호 수정';
	successTxt = '비밀번호가 수정되었습니다.';
	width = 500;

 	zephyros.callAjax({
		url : url,
		type : 'post',
		data : null,
		success : function(data, status, xhr) {
			zephyros.loading.show();
			zephyros.showDialog(dialog_password, data);
		}
	}); 
}
</script>