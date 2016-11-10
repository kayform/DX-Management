<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
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
<!-- 내용 -->
<div class="widget" style="margin-bottom: 0px;" >
	<form id="form02" name="form02"  method="post" data-role="validator" data-on-before-submit="return false" data-on-submit="return false" data-hint-mode="hint" data-hint-easing="easeOutBounce">
		<table summary="접근권한등록/수정" style="width: 80%;" class="table table-bordered table-condensed">
			<colgroup>
				<col width="15%">
				<col width="35%">
				<col width="15%">
				<col width="35%">
			</colgroup>
			<tbody>	
				<tr>
					<input type=text id="aclIndex" name="aclIndex"  value=""  style="width:95%;display:none;" >					
					<th scope="row">Set </th>					
					<td>
						<c:if test="${mode =='I' }">
							<label class="checkbox"><input type="checkbox" class="checkbox" value="10" id="enableAcl" name="enableAcl" ></label>
						</c:if>
						<c:if test="${mode =='U' }">
							<c:if test="${aclInfo.Set == 0}">
								<label class="checkbox"><input type="checkbox" class="checkbox" value="10" id="enableAcl" name="enableAcl"></label>
							</c:if>
							<c:if test="${aclInfo.Set == 1}">
								<label class="checkbox"><input type="checkbox" class="checkbox" value="10" checked="checked" id="enableAcl" name="enableAcl"></label>
							</c:if>
						</c:if>
						
					</td>
				</tr>	
				<tr>
					<th scope="row">Type *</th>
					<td >
						<select id="selectConnType" name="selectConnType" style="width: 140px;" selected="selected">						
							<option value="0">local</option>
							<option value="1">host</option>
							<option value="2">hostssl</option>
							<option value="3">hostnossl</option>
						</select>
						<input type="hidden" id="mode" value="${mode}"/>
					</td>
				</tr>	
				<tr>					
					<th scope="row">Database *</th>					
					<td>
 						<input type="text" name="str_database" id="str_database" value="${aclInfo.Database}" style="width:45%;" data-validate-func="required, custom" data-validate-arg=",charCheck" data-validate-hint="이 필드는 필수이며, 특수문자를 입력할수 없습니다." data-validate-hint-position="top">
						<select margin-right:10px" name="selectDatabase" id="selectDatabase"  style="width:48%;">					
							<option value="0">직접 입력</option>
							<option value="1">all</option>
							<option value="2">sameuser</option>
							<option value="3">@&lt;filename&gt;</option>
							<option value="4">samegroup</option>
							<option value="5">samerole</option>
							<option value="6">replication</option>
						</select>
					</td>					
				</tr>
				
				<tr>						
					<th scope="row">User  *</th>
					<td>
 						<input type="text" name="str_user" id="str_user" value="${aclInfo.User}" style="width:45%;" data-validate-func="required, custom" data-validate-arg=",charCheck" data-validate-hint="이 필드는 필수이며, 특수문자를 입력할수 없습니다." data-validate-hint-position="top">
						<select margin-right:10px" name="selectUser" id="selectUser"  style="width:48%;">					
							<option value="1">직접 입력</option>
							<option value="2">all</option>
						</select>
					</td>
				</tr>
				
				<tr>
					<th scope="row">IP address *</th>
					<td>			
						<input type=text id="ip" name="ip"  value="${aclInfo.Ip}" style="width:95%;">
					</td>
				</tr>	
				
				<tr>
					<th scope="row">Method *</th>
					<td>
						<select margin-right:10px" name="selectMethod" id="selectMethod"  style="width:99%;">					
							<option value="0">trust</option>
							<option value="1">reject</option>
							<option value="2">md5</option>
							<option value="3">password</option>
							<option value="4">krb4</option>
							<option value="5">krb5</option>
							<option value="6">ident</option>
							<option value="7">pam</option>
							<option value="8">ldap</option>
							<option value="9">gss</option>
							<option value="10">sspi</option>
							<option value="11">cert</option>
							<option value="12">crypt</option>
							<option value="13">radius</option>
							<option value="14">peer</option>
						</select>
					</td>
				</tr>
					
				<tr>						
					<th scope="row">Option</th>
					<td>
						<input type="text" id="authOption" name="authOption"  value="${aclInfo.Option}" style="width:95%;">
					</td>
				</tr>	
			</tbody>
		</table>
		<label style="text-align: right; font-weight: bold;">* 필수입력항목</label>
	</form>	
</div>

<script type="text/javascript">
	$(document).ready(function() {

		var mode = $("#mode").val();
		
		if (mode == 'U') {
			selectedText('selectConnType', '${aclInfo.Type}');
			selectedText('selectDatabase', '${aclInfo.Database}');
			selectedText('selectUser', '${aclInfo.User}');
			selectedText('selectMethod', '${aclInfo.Method}');
			
		}

		charCheck = function(value) {
			var userId = value;
			var excludeCharacter = "{}[]()<>?_|~`!@#$%^&*-+\"'\\/ "; //입력을 막을 특수문자 기재.					
			for (var i = 0; i < userId.length; i++) {
				if (-1 != excludeCharacter.indexOf(userId[i])) {
					return false;
				}
			}
			return true;
		}; 
/*  		jQuery.validator.addMethod("charCheck", function(value, element) {
			var serverId = value;
			var excludeCharacter = "{}[]()<>?_|~`!@#$%^&*-+\"'\\/ "; //입력을 막을 특수문자 기재.					

			for (var i = 0; i < serverId.length; i++) {
				if (-1 != excludeCharacter.indexOf(serverId[i])) {
					return false;
				}
			}
			return true;
		}, "특수문자를 입력할수 없습니다."); 
*/
/* 		
		if (mode == 'I') {
		// validate signup form on keyup and submit
			$("#form02").validate({
				rules: {
					str_database: {required : true, charCheck: true},
					str_user: {required : true, charCheck: true},
					ip: {required : true},
				}, messages: {
					str_database: {required : "Required"},
					str_user: {required : "Required"},
					ip: {required : "Required"},
				}
			});
		} else if (mode == 'U') {
			$("#form02").validate({
				rules: {
					str_database: {required : true, charCheck: true},
					str_user: {required : true, charCheck: true},
					ip: {required : true},
				}, messages: {
					str_database: {required : "Required"},
					str_user: {required : "Required"},
					ip: {required : "Required"},
				}
			});
		}
*/
	});  
	
	$('#selectDatabase').change(function(){
		   $("#selectDatabase option:selected").each(function () {
		        
		        if($(this).val()== '1'){ //직접입력일 경우
		             $("#str_database").val('');                        //값 초기화
		             $("#str_database").attr("disabled",false); //활성화
		        }else{ //직접입력이 아닐경우
		             $("#str_database").val($(this).text());      //선택값 입력
		             $("#str_database").attr("disabled",true); //비활성화
		        }
		   });
	});
	
	$('#selectUser').change(function(){
		   $("#selectUser option:selected").each(function () {
		        
		        if($(this).val()== '1'){ //직접입력일 경우
		             $("#str_user").val('');                //값 초기화
		             $("#str_user").attr("disabled",false); //활성화
		        }else{ //직접입력이 아닐경우
		             $("#str_user").val($(this).text());    //선택값 입력
		             $("#str_user").attr("disabled",true);  //비활성화
		        }
		   });
	});
	
	function selected(target, value) {
		for (i = 0; i < document.getElementById(target).options.length; i++) {
		    if (document.getElementById(target).options[i].value == value) {
		        document.getElementById(target).options[i].selected = "selected";
		    }
		}
	}	
	
	function selectedText(target, value) {
		for (i = 0; i < document.getElementById(target).options.length; i++) {
		    if (document.getElementById(target).options[i].text == value) {
		        document.getElementById(target).options[i].selected = "selected";
		    }
		}
	}
			
</script>
