<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style>
	select,textarea,input[type="text"],input[type="password"],input[type="datetime"],input[type="datetime-local"],input[type="date"],input[type="month"],input[type="time"],input[type="week"],input[type="number"],input[type="email"],input[type="url"],input[type="search"],input[type="tel"],input[type="color"],.uneditable-input{margin-bottom:0px;}
	
	#tbody {background-color: rgba(0, 0, 0, 0.5);color:blue;}
	.table-condensed th {background-color: rgba(0, 0, 0, 0.5); text-align: left;}
	.table-condensed td {vertical-align: middle;}
	#pinfoData label {float: left;vertical-align: middle;padding-right:20px;}
	.input-append .add-on{padding:4px;} 
	.input-append {
		margin-bottom: 0px;
	}
</style>
        <form id="serverForm" name="serverForm"  method="post" data-role="validator" data-on-before-submit="return false" data-on-submit="return false" data-hint-mode="hint" data-hint-easing="easeOutBounce">
		<input type="hidden" id="mode" name="mode" value="${mode}" />
		<table summary="서버등록/수정" style="width: 100%;" class="table">
			<colgroup>
				<col width="15%">
				<col width="35%">
				<col width="15%">
				<col width="35%">
			</colgroup>
			<tbody>				
				<tr>
					<th scope="row">서버명 *</th>
					<td >
						<div class="input-mini text">					
						<c:if test="${mode !='V' }">
							<input type="text" id="sys_nm" name="sys_nm" value="${serverInfoList.sys_nm}" style="width:80%;" data-validate-func="required, custom" data-validate-arg=",dupCheck" data-validate-hint="ID는 필수이고, 다른 ID와 중복될 수 없습니다." data-validate-hint-position="top" >							
						</c:if>	
						<c:if test="${mode =='V'}">
							${serverInfoList.sys_nm}
						</c:if>		
						</div>
					</td>					
					<th scope="row">유형 *</th>					
					<td>
						<c:if test="${mode =='I' }">
								<select id="type" name="type" style="width: 250px;" onchange="javascript:refreshElement()">			
									<c:forEach var="item" items="${serverTypeList}">
										<option value="${item.sys_mnt_cd}">${item.sys_mnt_cd_nm}</option>
									</c:forEach>
								</select>							
						</c:if>
						<c:if test="${mode =='U' }">
							<input type="text" id="type" name="type" value="${serverInfoList.type}" style="width:80%;" >
						</c:if>
						<c:if test="${mode =='V' }">
							${serverInfoList.type}
						</c:if>
					</td>
				</tr>
				<tr>
					<th scope="row">아이피 *</th>
					<td>
						<c:if test="${mode !='V' }">
							<input class="input-mini text input-mini" type="text" id="ip" name="ip"  value="${serverInfoList.ip}" style="width:80%;" data-validate-func="required,pattern" data-validate-arg=",^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$" data-validate-hint="이 필드는 필수이며, IP포맷으로 입력되어야 합니다." data-validate-hint-position="top">
						</c:if>
						<c:if test="${mode =='V' }">
							${serverInfoList.ip}
						</c:if>
					</td>
					<th scope="row">포트 *</th>
					<td>
						<c:if test="${mode !='V' }">
							<input class="input-mini text" type="text" id="port" name="port"  value="${serverInfoList.port}" style="width:80%;" data-validate-func="required,min,max" data-validate-arg=",1,65535" data-validate-hint="이 필드는 필수이며, 값은 1~65535이여야 합니다." data-validate-hint-position="top">
						</c:if>
						<c:if test="${mode =='V' }">
							${serverInfoList.port}
						</c:if>
					</td>							
				</tr>	
					<tr id="userInfoTr">								
						<th scope="row">아이디 *</th>
						<td>
							<c:if test="${mode !='V' }">
								<input class="input-mini text" type="text" id="user_id" name="user_id"  value="${serverInfoList.user_id}" style="width:80%;" data-validate-func="required" data-validate-hint="이 필드는 필수입니다." data-validate-hint-position="top">
							</c:if>
							<c:if test="${mode =='V' }">
								${serverInfoList.user_id}
							</c:if>
						</td>									
						<th scope="row">패스워드 *</th>
						<td>
							<c:if test="${mode !='V' }">
								<input class="input-mini text" type="password" id="user_pw" name="user_pw"  value="${serverInfoList.user_pw}" style="width:80%;" data-validate-func="required" data-validate-hint="이 필드는 필수입니다." data-validate-hint-position="top">
							</c:if>
							<c:if test="${mode =='V' }">
								********
							</c:if>
						</td>
					</tr>
				<tr id="dbNmTr">
					<th scope="row">DB명*</th>
					<td>
						<c:if test="${mode !='V' }">
							<input class="input-mini text" type="text" id="db_nm" name="db_nm"  value="${serverInfoList.db_nm}" style="width:80%;" data-validate-func="required" data-validate-hint="이 필드는 필수입니다." data-validate-hint-position="top">
						</c:if>
						<c:if test="${mode =='V' }">
							${serverInfoList.db_nm}
						</c:if>
					</td>
				</tr>		
			</tbody>
		</table>
		<label class="place-right" style="text-align: right; font-weight: bold;">* 필수입력항목</label>    
        </form>
<script>
$(document).ready(function() {
	refreshElement();
}); 

charCheck = function(value) {
	var userId = value;
	var excludeCharacter = "{}[]()<>?_|~`!@#$%^&*-+\"'\\/ "; //입력을 막을 특수문자 기재.					

	for (var i = 0; i < userId.length; i++) {
		if (-1 != excludeCharacter.indexOf(userId[i])) {
			return false;
		}
	}
	return true;
}
;

dupCheck = function(value) {
	var dupCheck = false;
	var userId = value;

 	zephyros.callAjax({
		url : '/systemNameCheck',
		type : 'post',
		async: false,
		data: {
			sys_nm: $('#sys_nm').val(),
			ip: $('#ip').val(),
			port: $('#port').val()
		}, 
		success : function(data, status, xhr) {
			if (data.isDuplicate == 'true') {
				dupCheck = false;
			} else {
				dupCheck = true;		
			}
		}
	}); 
	return dupCheck;
};

serverConnCheck = function(value) {
	var dupCheck = false;
	var userId = value;

 	zephyros.callAjax({
		url : '/systemProcess',
		type : 'post',
		async: false,
		data: {
			mode: 'T',
			sys_nm: $('#sys_nm').val(),
			type: $('#type').val(),
			db_nm: $('#db_nm').val(),
			ip: $('#ip').val(),
			port: $('#port').val(),
			user_id: $('#user_id').val(),
			user_pw: $('#user_pw').val()
		}, 
		success : function(data, status, xhr) {
			if (data.isDuplicate == 'true') {
				dupCheck = false;
			} else {
				dupCheck = true;		
			}
		}
	}); 
	return dupCheck;
};

//사용자정보 수정시 Select Box의 Option값이 DB에서 가져온 값과 일치하는 경우 Selected를 설정
function selected(target, value) {
	for (i = 0; i < document.getElementById(target).options.length; i++) {
	    if (document.getElementById(target).options[i].value == value) {
	        document.getElementById(target).options[i].selected = "selected";
	    }
	}
};

function refreshElement() {
	var value = null;
	
	if ( $('#mode').val() == 'I') {
		value = $('#type').val();
	}else {
		value = '${serverInfoList.type}';
	}
		
	//var value = $('#type').val();
	if (value == 'POSTGRESQL' || value == 'CLOUDERA-MANAGER'){
		//$("#userInfoTr").css("display","");
		$("#userInfoTr").prop("disabled",false);  
		$("#user_id").prop("readonly",false);
		$("#user_pw").prop("readonly",false);
		
		if (value == 'POSTGRESQL') {
			$("#dbNmTr").prop("disabled",false);  
			$("#db_nm").prop("readonly",false);
		}else{
			$("#dbNmTr").prop("disabled",true);  
			$("#db_nm").prop("readonly",true);
		}
	}else{
		$("#userInfoTr").prop("disabled",true);
		$("#user_id").prop("readonly",true);
		$("#user_pw").prop("readonly",true);
		$("#dbNmTr").prop("disabled",true);  
		$("#db_nm").prop("readonly",true);
		$("#user_id").val("");
		$("#user_pw").val("");
		$("#db_nm").val("");
		//$("#userInfoTr").css("display","none");
	}
	
	if ( $('#mode').val() == 'U') {
		$("#sys_nm").prop("readonly",true);
		$("#type").prop("readonly",true);
	}
};
</script>