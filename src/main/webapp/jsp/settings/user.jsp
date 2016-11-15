<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

    <div class="page-content">
        <div class="flex-grid no-responsive-future" style="height: 100%;">
            <div class="row" style="height: 100%">
                <div class="cell size-x200" id="cell-sidebar" style="background-color: #71b1d1; height: 100%">
                </div>            
                <div class="cell auto-size padding20 bg-white" id="cell-content">
                    <h1 class="text-light">사용자 관리</h1>  디버그 정보 : userId=${sessionScope.userId}  userAuth=${sessionScope.userAuth} 
                    <h5 class="sub-alt-header">* 사용자 정보 및 메뉴별 접근 권한을 관리합니다.</h5>
                    <hr class="thin bg-grayLighter">
                    사용자명 <input type="text"  id="searchUserNameText" name="searchUserNameText"> 
                    <button class="button primary" type="submit" form="userListForm" id="searchUserBtn" name="searchUserBtn" onclick="javascript:searchUserList('I', '');" ><span class="mif-search"></span>조회</button>
                    <button class="button primary" onclick="javascript:profile('I', '');"><span class="mif-plus"></span>등록</button>
                    <span id="message"></span>
                    <hr class="thin bg-grayLighter">
                    <table class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="false">
                        <thead>
                        <tr>
                            <td class="sortable-column sort-asc" style="width: 120px">사용자 아이디</td>
                            <td class="sortable-column">권한구분</td>
                            <td class="sortable-column">소속</td>
                            <td class="sortable-column">사용자명</td>
                            <td class="sortable-column">휴대폰번호</td>
                            <td style="width: 200px">관리</td>
                        </tr>
                        </thead>
                        <tbody>
						<c:choose>
							<c:when test="${userList.size() < 1}">
								<tr>
									<td colspan="6" style="text-align: center;">No Data.</td>
								</tr>
							</c:when>
							<c:otherwise>
								<c:forEach items="${userList}" var="item">
									<tr>
										<td>${item.user_id}</td>
										<td>${item.auth_dv}</td>
										<td>${item.blg}</td>
										<td>${item.user_nm}</td>
										<td>${item.hpnm_no}</td>
										<td>
											<!-- 슈퍼유저(3), 관리자(2)일 경우 조회, 수정 및 삭제 권한부여-->
											<!-- 사용자(1) 일 경우 본인 정보 조회, 수정권한 부여 및 타 사용자 조회 기능 부여. 현재는 메뉴권한에서 사용자관리 권한 없음.-->
											<c:choose>
												<c:when test="${sessionScope.userAuth == '3' || sessionScope.userAuth == '2' }">
													<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:profile('V', '${item.user_id}');"><span class="icon mif-search"></span></button>
													<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:profile('U', '${item.user_id}');"><span class="icon mif-pencil"></span></button>
													<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:profile('D', '${item.user_id}');"><span class="icon mif-cancel"></span></button>
													<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:manageUserAuth('${item.user_id}');"><span class="icon mif-tools"></span></button>
												</c:when>
												<c:when test="${sessionScope.userAuth == '1'}">
													<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:profile('V', '${item.user_id}');"><span class="icon mif-search"></span></button>
													<c:if test="${sessionScope.userId == item.user_id}">
													<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:profile('U', '${item.user_id}');"><span class="icon mif-pencil"></span></button>
													</c:if>
												</c:when>
												<c:otherwise>
													관리권한 없음.
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
                        </tbody>
                    </table>
                </div> 
            </div>
        </div>
    </div>

    
<form id="userListForm" name="userListForm" method="post" >
	<input type="hidden" id="searchUserName" name="searchUserName"> 
</form>




<script>    

	function searchUserList() {
		var searchUserName ='';
		searchUserName = document.getElementById('searchUserNameText').value;
    	
    	if(searchUserName == '') {
    		document.getElementById('message').innerHTML ='검색할 사용자 명을 입력하세요.'
    		return false;
    	}
    	document.getElementById("searchUserName").value = searchUserName;
    	document.forms["userListForm"].action.value = "user";
    	return true;
    }    
    
    function profile(mode, userId) {
    	zephyros.loading.show();
    	var url = '';
    	var success = null;
    	var error = null;

    	var titleTxt = '';
    	var successTxt = '';
    	var heightVal = 570;
      	var widthVal = 1250;

		url = '/userForm?mode='+mode+'&userId=' + userId;

    	//CRUD에 따른 dialog_profile 오프젝트 설정
    	if (mode == 'U') {
    		titleTxt = '사용자 수정';

    		dialog_profile = $("#dialog_profile").dialog({
           		title: titleTxt,
            	height: heightVal,
              	width: widthVal,
    	    	buttons: {
    	    		"저장": function() {
    	    			if (zephyros.isFormValidate('form02')){
	   	    				var url = '/userProcess';
	    	    			var formData = $("#form02").serialize();
	    	    			zephyros.callAjax({
	    	    				url : url,
	    	    				type : 'post',
	    	    				data : formData,
	    	    				success : function(data, status, xhr) {
	    	    					zephyros.loading.hide();
	    	    					dialog_profile.dialog("close");
	    	    					zephyros.checkAjaxDialogResult(dialog_profile, data);
	    	    				}
	    	    			});
    	    			}
    	    	  	},
    	    	  	"취소": function() {
    	    	  		zephyros.loading.hide();
    	    	  		dialog_profile.dialog("close");
    	    	  	    $("#dialog_profile").empty();
    	    	  	}
    	    	},
    			close: function() {
    				zephyros.loading.hide();
    				$("#dialog_profile").empty();
    	    	}
            });   
    	}
    	else if (mode == 'I') {
    		titleTxt = '사용자 등록';

    		dialog_profile = $("#dialog_profile").dialog({
           		title: titleTxt,
            	height: heightVal,
              	width: widthVal,
    	    	buttons: {
    	    		"등록": function() {
    	    			if (zephyros.isFormValidate('form02')){
	   	    				var url = '/userProcess';
	    	    			var formData = $("#form02").serialize();
	    	    			zephyros.callAjax({
	    	    				url : url,
	    	    				type : 'post',
	    	    				data : formData,
	    	    				success : function(data, status, xhr) {
	    	    					zephyros.loading.hide();
	    	    					dialog_profile.dialog("close");
	    	    					zephyros.checkAjaxDialogResult(dialog_profile, data);
	    	    				}
	    	    			});
    	    			}
    	    	  	},
    	    	  	"취소": function() {
    	    	  		zephyros.loading.hide();
    	    	  		dialog_profile.dialog("close");
    	    	  	    $("#dialog_profile").empty();
    	    	  	}
    	    	},
    			close: function() {
    				zephyros.loading.hide();
    				$("#dialog_profile").empty();
    	    	}
            });   
    	} 
    	else if (mode == 'V') {
    		titleTxt = '사용자 조회';
    		dialog_profile = $("#dialog_profile").dialog({
           		title: titleTxt,
            	height: heightVal,
              	width: widthVal,
    	    	buttons: {
    	    	  	"닫기": function() {
  						zephyros.loading.hide();
    	    	  		dialog_profile.dialog("close");
    	    	  	    $("#dialog_profile").empty();
    	    	  	}
    	    	},
    			close: function() {
					zephyros.loading.hide();
    				$("#dialog_profile").empty();
    	    	}
            });   
    	}
    	else if (mode == 'D') {
    		titleTxt = '사용자 삭제';

    		dialog_profile = $("#dialog_profile").dialog({
           		title: titleTxt,
            	height: 200,
              	width: 400,
    	    	buttons: {
    	    		"삭제": function() {
   	    				var url = '/userProcess?mode='+mode+"&userId=" + userId;
   	    				zephyros.callAjax({
    	    				url : url,
    	    				type : 'post',
    	    				data : null,
    	    				success : function(data, status, xhr) {
    	    					zephyros.loading.hide();
    	  						dialog_profile.dialog("close"); 
    	  						zephyros.checkAjaxDialogResult(dialog_info,  data)
    	    				}
    	    			});
    	    	  	},
    	    	  	"취소": function() {
    					zephyros.loading.hide();
    	    	  		dialog_profile.dialog("close");
    	    	  	    $("#dialog_profile").empty();
    	    	  	}
    	    	},
    			close: function() {
					zephyros.loading.hide();
    				$("#dialog_profile").empty();
    	    	}
            });   
    	}
    	
    	
    	//user profile modal 창 만들기
    	//삭제일경우 회원정보 조회없이 아이디와
		if (mode == 'D') {
			zephyros.loading.show();
			zephyros.showDialog(dialog_profile, "아이디 "+userId+" 사용자를 삭제하시겠습니까?");
		}
    	else if (mode == 'U' || mode == 'V' || mode == 'I') {
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
    	else{
    		alert("잘못된 값이 입력되었습니다. 모드는 D, I, U, V 중에 하나여야합니다.");
    	}
    }
    
    
    //화면에서 사용자 권한관리 버튼을 클릭하면 사용자 권한관리를 위한 화면을 modal로 출력한다.
    function manageUserAuth(userId) {
    	
    	zephyros.loading.show();
    	var url = '';
    	var success = null;
    	var error = null;

    	var titleTxt = '';
    	var successTxt = '';
    	var heightVal = 570;
      	var widthVal = 1250;

		url = '/userAuthForm?userId=' + userId;
		
		dialog_userAuth = $("#dialog_userAuth").dialog({
	 		autoOpen: false,
	 		height: 650,
	 		width: 1250,
	 		modal: true,
	 		title: "메뉴 권한",
	 		resizable: false,
	    	buttons: {
	    		"저장": function() {
					saveAuth();
    	  		},
	    	  	"닫기": function() {
						zephyros.loading.hide();
						dialog_userAuth.dialog("close");
	    	  	    $("#dialog_userAuth").empty();
	    	  	}
	    	},
			close: function() {
				zephyros.loading.hide();
				$("#dialog_userAuth").empty();
	    	}
        });       	
		
		zephyros.callAjax({
    		url : url,
    		type : 'post',
    		data : null,
    		success : function(data, status, xhr) {
    			zephyros.loading.show();
    			zephyros.showDialog(dialog_userAuth, data);
    		}
    	});
     	
    }
    

</script>