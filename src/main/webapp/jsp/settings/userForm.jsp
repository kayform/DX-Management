<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- <div data-role="dialog" id="modify-userinfo" class="padding20" data-close-button="false" data-type="info"> -->
        <form>
            <h3 class="text-light">${userInfo.user_nm}</h4>
            <hr class="thin bg-grayLighter">
		<table summary="사용자정보등록/수정" style="width: 100%;" class="table table-bordered table-condensed">
			<colgroup>
				<col width="15%">
				<col width="35%">
				<col width="15%">
				<col width="35%">
			</colgroup>
			<tbody>				
				<tr>
					<th scope="row">사용자 아이디 *</th>
					<td >
						<div class="input-control text">					
						<c:if test="${mode =='I' }">
							<input type="text" id="userId" name="userId" value="${userInfo.user_id}" onchange="javascript:userIdChange();" style="width:80%;">							
						</c:if>	
						<c:if test="${mode =='U'}">
							<input type="text" id="userId" name="userId" readonly="readonly" value="${userInfo.user_id}" style="width:80%;">
							<div class="pagination pagination-small pull-right" style="margin: 1px 4px 0px 0px;">
								<div data-toggle="tooltip" data-placement="bottom" style="word-break:break-all; word-wrap:break-word; float: left;" title="비밀번호 수정">
									<a id="passwordChangeBtn" class="btn-action glyphicons keys btn-inverse"  href="javascript:passwordCahnge('${userInfo.user_id}');"><i></i></a>
								</div>
							</div>							
						</c:if>	
						<c:if test="${mode =='V'}">
							${userInfo.user_id}
						</c:if>		
						<input type="hidden" id="mode" value="${mode}"/>
						</div>
					</td>					
					<th scope="row">사용자명 *</th>					
					<td>
						<c:if test="${mode !='V' }">
							<input class="input-control text" type="text" id="userName" name="userName"  value="${userInfo.user_nm}" style="width:80%;">
						</c:if>
						<c:if test="${mode =='V' }">
							${userInfo.user_nm}
						</c:if>
					</td>
				</tr>
				<c:if test="${mode =='I' }">
					<tr>
						<th scope="row">패스워드 *</th>
						<td>			
							<input class="input-control text" type="password" id="password1" name="password1"  value="" style="width:80%;">
						</td>
						<th scope="row">패스워드확인 *</th>
						<td>
							<input class="input-control text" type="password" id="password2" name="password2"  value="" style="width:80%;">							
						</td>					
					</tr>
				</c:if>		
				<tr>						
					<th scope="row">소속 *</th>
					<td>
						<c:if test="${mode !='V' }">
							<input class="input-control text" type="text" id="blg" name="blg"  value="${userInfo.blg}" style="width:80%;">
						</c:if>
						<c:if test="${mode =='V' }">
							${userInfo.blg}
						</c:if>
					</td>
					<th scope="row">부서 *</th>
					<td>
						<c:if test="${mode !='V' }">
							<input class="input-control text" type="text" id="department" name="department"  value="${userInfo.dept}" style="width:80%;">
						</c:if>
						<c:if test="${mode =='V' }">
							${userInfo.dept}
						</c:if>
					</td>
				</tr>			
				<tr>
					<th scope="row">직급 *</th>
					<td>
						<c:if test="${mode !='V' }">
							<input class="input-control text" type="text" id="jgd" name="jgd"  value="${userInfo.jgd}" style="width:80%;">
						</c:if>
						<c:if test="${mode =='V' }">
							${userInfo.jgd}
						</c:if>
					</td>
					<th scope="row">담당업무</th>
					<td>
						<c:if test="${mode !='V' }">
							<input class="input-control text" type="text" id="cg_biz_def" name="cg_biz_def"  value="${userInfo.cg_biz_def}" style="width:80%;">
						</c:if>
						<c:if test="${mode =='V' }">
							${userInfo.cg_biz_def}
						</c:if>
					</td>
							
				</tr>					
				<tr>
					<th scope="row">휴대폰번호</th>
					<td>
						<c:if test="${mode !='V' }">
							<input class="input-control text" type="text" id="phoneNo" name="phoneNo"  value="${userInfo.hpnm_no}" style="width:80%;">
						</c:if>
						<c:if test="${mode =='V' }">
							${userInfo.hpnm_no}
						</c:if>
					</td>
					<th scope="row">권한구분</th>
					<td>					
						<c:choose>
							<c:when test="${mode =='I' }">
								<select id="authDivision" name="authDivision" style="width: 140px;"  selected="selected">						
									<option value="1">사용자</option>
									<option value="3">관리자</option>
								</select>
							</c:when>
							<c:when test="${mode =='U' }">
								<select id="authDivision" name="authDivision" style="width: 140px;" selected="selected" disabled="disabled">						
									<option value="1">사용자</option>
									<option value="3">관리자</option>
								</select>
							</c:when>
							<c:when test="${mode =='V' }">
								<c:choose>										
									<c:when test="${userInfo.auth_dv == '슈퍼유저'}">
										관리자
									</c:when>
									<c:otherwise>
										${userInfo.auth_dv}
									</c:otherwise>
								</c:choose>
							</c:when>
						</c:choose>						
					</td>	
				</tr>
				<tr>
					<th scope="row">사용자만료일</th>
					<td>
						<c:if test="${mode !='V' }">
							<div class="input-append">	
								<input class="input-control text" type="text"  id="userExpired"   name="userExpired" value="${userInfo.user_expd}" style="width:80%;"/>
								<span class="add-on glyphicons calendar" ><i></i></span>
							</div>
						</c:if>
						<c:if test="${mode =='V' }">
							${userInfo.user_expd}
						</c:if>
					</td>	
					<th scope="row">사용여부</th>
					<td>
						<c:if test="${mode !='V' }">
							<select id="useYn" name="useYn" style="width:140px;" >							
								<option value="Y">사용</option>				         
				        		<option value="N">미사용</option>	
							</select>	
						</c:if>
						<c:if test="${mode =='V' }">
							${userInfo.use_yn}
						</c:if>
					</td>
				</tr>		
				<tr>
					<th scope="row">PG 모니터링 경로</th>
					<td>
						<c:if test="${mode !='V' }">
							<input class="input-control text" type="text" id="pg_mon_client_path" name="pg_mon_client_path" value="${userInfo.pg_mon_client_path}" style="width:80%;">	
						</c:if>
						<c:if test="${mode =='V' }">
							${userInfo.pg_mon_client_path}
						</c:if>
					</td>	
					<th scope="row">암호화 관리 경로</th>
					<td>
						<c:if test="${mode !='V' }">
							<input class="input-control text" type="text" id="enc_mng_path" name="enc_mng_path" value="${userInfo.enc_mng_path}" style="width:80%;">
						</c:if>
						<c:if test="${mode =='V' }">
							${userInfo.enc_mng_path}
						</c:if>
					</td>
				</tr>			
			</tbody>
		</table>
		<label style="text-align: right; font-weight: bold;">* 필수입력항목</label>            
            <div class="input-control text">
                <span class="mif-user prepend-icon"></span>
            	<input type="text">
            </div>
            <div class="input-control text">
                <span class="mif-lock prepend-icon"></span>
            	<input type="password">
            </div>
            <label class="input-control checkbox small-check">
                <input type="checkbox">
                <span class="check"></span>
            	<span class="caption">Remember me</span>
            </label>
            <div class="form-actions">
                <button class="button">Login</button>
        		<button class="button link">Cancel</button>
        	</div>
        </form>   
<!-- </div>  -->