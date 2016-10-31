<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- <div data-role="dialog" id="modify-aclinfo" class="padding20" data-close-button="false" data-type="info"> -->
        <form>
            <h3 class="text-light">${userInfo.user_nm}</h4>
            <hr class="thin bg-grayLighter">
		<table summary="접근제어정보등록/수정" style="width: 100%;" class="table table-bordered table-condensed">
			<colgroup>
				<col width="15%">
				<col width="35%">
				<col width="15%">
				<col width="35%">
			</colgroup>
			<tbody>
				<tr>
					<input type=text id="aclIndex" name="aclIndex"  value=""  style="width:95%;display:none;" >					
					<th scope="row">Enable </th>					
					<td>
						<c:if test="${mode =='I' }">
							<label class="checkbox"><input type="checkbox" class="checkbox" value="10" id="enableAcl" name="enableAcl"></label>
						</c:if>
						<c:if test="${mode !='I' }">
							<label class="checkbox"><input type="checkbox" class="checkbox" value="10" id="${subItem.id }" name="chkSub-${item.id}-${subItem.sub_order -1}" menu-parent-id="${item.id}"></label>
						</c:if>
					</td>
				</tr>			
				<tr>
					<th scope="row">Type *</th>
					<td >
						<c:if test="${mode =='I' }">
							<select id="connType" name="connType" style="width: 140px;" selected="selected">						
								<option value="0">local</option>
								<option value="1">host</option>
								<option value="2">hostssl</option>
								<option value="3">hostnossl</option>
							</select>
						</c:if>	
						<c:if test="${mode !='I'}">
							<input type="text" id="serverId" name="serverId" readonly="readonly" value="${serverInfo.server_id}" style="width:80%;">
							<div class="pagination pagination-small pull-right" style="margin: 1px 4px 0px 0px;">
								<div data-toggle="tooltip" data-placement="bottom" style="word-break:break-all; word-wrap:break-word; float: left;" title="비밀번호 수정">
									<a id="passwordChangeBtn" class="btn-action glyphicons keys btn-inverse"  href="javascript:passwordCahnge('${serverInfo.server_id}');"><i></i></a>
								</div>
							</div>							
						</c:if>			
						<input type="hidden" id="mode" value="${mode}"/>
					</td>
				</tr>					
				<tr>					
					<th scope="row">Database *</th>					
					<td>
						<c:if test="${mode =='I' }">
							<input type="text" name="str_database" id="str_database" value="" style="width:45%;">
 							<select margin-right:10px" name="selectDatabase" id="selectDatabase"  style="width:48%;">					
								<option value="0">직접 입력</option>
								<option value="1">all</option>
								<option value="2">sameuser</option>
								<option value="3">@&lt;filename&gt;</option>
								<option value="4">samegroup</option>
								<option value="5">samerole</option>
								<option value="6">replication</option>
							</select>
						</c:if>
						<c:if test="${mode !='I' }">
							${serverInfo.ipaddr}
						</c:if>
					</td>					
				</tr>
				<tr>						
					<th scope="row">User  *</th>
					<td>
						<c:if test="${mode =='I' }">
							<input type="text" name="str_user" id="str_user" value="" style="width:45%;">
 							<select margin-right:10px" name="selectUser" id="selectUser"  style="width:48%;">					
								<option value="1">직접 입력</option>
								<option value="2">all</option>
							</select>
						</c:if>
						<c:if test="${mode !='I' }">
							${serverInfo.port}
						</c:if>
					</td>
				</tr>	
				<tr>
					<th scope="row">IP address *</th>
					<td>			
						<input type=text id="ip" name="ip"  value="" style="width:95%;">
					</td>
				</tr>
				<tr>
					<th scope="row">Method *</th>
					<td>
						<c:if test="${mode =='I' }">
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
						</c:if>
						<c:if test="${mode !='I' }">
							${serverInfo.port}
						</c:if>
					</td>
				</tr>								
				<tr>						
					<th scope="row">Option</th>
					<td>
						<c:if test="${mode =='I' }">
							<input type="text" id="authOption" name="authOption"  value="" style="width:95%;">
						</c:if>
						<c:if test="${mode !='I' }">
							${serverInfo.port}
						</c:if>
					</td>
				</tr>	

			</tbody>
		</table>
		<label style="text-align: right; font-weight: bold;">* 필수입력항목</label>            
            <div class="form-actions">
                <button class="button">Save</button>
        		<button class="button">Cancel</button>
        	</div>
        </form>   
<!-- </div>  -->