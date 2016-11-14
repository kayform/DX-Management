<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="cell auto-size padding20 bg-white" id="cell-content">
    <p>* DB 리스트
    <table class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="t">
        <thead>
        <tr>
            <td class="sortable-column sort-asc" style="width: 120px">Database 명</td>
            <td class="sortable-column">연계 상태</td>
            <td class="sortable-column">Connection 명</td>
            <td style="width: 100px">연계설정</td>
            <td style="width: 100px">연계동작</td>
        </tr>
        </thead>
        <tbody>
			<c:choose>
				<c:when test="${databaseList.size() < 1}">
					<tr>
						<td colspan="6" style="text-align: center;">No Data.</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach items="${databaseList}" var="item">
						<tr>
							<td>${item.database_name}</td>
							<td>${item.pg_get_status_ingest}</td>
							<td>${item.connect_name}</td>
							<td>
								<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:tableList('{item.sys_nm}');"><span class="icon mif-search"></span></button>
								<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:tableList('{item.sys_nm}');"><span class="icon mif-pencil"></span></button>
							</td>
							<td>
								<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:tableList('{item.sys_nm}');"><span class="icon mif-play"></span></button>
								<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:tableList('{item.sys_nm}');"><span class="icon mif-stop"></span></button>
							</td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
        </tbody>
    </table>
</div> 


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
    		successTxt = '사용자가 수정되었습니다.';

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
    		successTxt = '사용자가 등록되었습니다.';

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
    		successTxt = '사용자가 삭제되었습니다.';

    		dialog_profile = $("#dialog_profile").dialog({
           		title: titleTxt,
            	height: 200,
              	width: 400,
    	    	buttons: {
    	    		"삭제": function() {
   	    				//var url = '/userProcess?mode='+mode;
   	    				var url = '/userProcess?mode='+mode+"&userId=" + userId;
    	    			zephyros.callAjax({
    	    				url : url,
    	    				type : 'post',
    	    				data : null,
    	    				success : function(data, status, xhr) {
    	    					zephyros.loading.hide();
    	  						dialog_profile.dialog("close");
    	  						//zephyros.checkAjaxDialogResult(dialog_info,  data)
    	  						zephyros.showDialog(dialog_info,  successTxt)
    	    				}
    	    			});
    	    	  	},
    	    	  	"취소": function() {
    	    	  		dialog_profile.dialog("close");
    	    	  	    $("#dialog_profile").empty();
    	    	  	}
    	    	},
    			close: function() {
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