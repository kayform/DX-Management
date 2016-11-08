<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
    <div class="page-content">
        <div class="flex-grid no-responsive-future" style="height: 100%;">
            <div class="row" style="height: 100%">
                <div class="cell size-x200" id="cell-sidebar" style="background-color: #71b1d1; height: 100%">
                </div>            
                <div class="cell auto-size padding20 bg-white" id="cell-content">
                    <h1 class="text-light">사용자 관리</h1>
                    <h5 class="sub-alt-header">* 사용자 정보 및 메뉴별 접근 권한을 관리합니다.</h5>
                    <hr class="thin bg-grayLighter">
                    사용자명 <input type="text"  id="userName" name="userName"> userId=${sessionScope.userId}  userAuth=${sessionScope.userAuth} 
                    <button class="button primary" onclick="zephyros.showDialog(dialog_info, 'TODO 조회구현하기 !')"><span class="mif-search"></span>조회</button>
                    <button class="button primary" onclick="zephyros.showDialog(dialog_info, 'TODO 등록구현하기 !')"><span class="mif-plus"></span>등록</button>
                    <hr class="thin bg-grayLighter">
                    <table class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="false">
                        <thead>
                        <tr>
                            <td class="sortable-column sort-asc" style="width: 100px">사용자 아이디</td>
                            <td class="sortable-column">권한구분</td>
                            <td class="sortable-column">소속</td>
                            <td class="sortable-column">사용자명</td>
                            <td class="sortable-column">휴대폰번호</td>
                            <td style="width: 150px">관리</td>
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
											<!-- 사용자(1) 일 경우 본인 정보 조회, 수정권한 부여 및 타 사용자 조회 기능 부여-->
											<c:choose>
												<c:when test="${sessionScope.userAuth == '3'}">
													<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:aclModal('V', '${item.user_id}');"><span class="icon mif-search"></span></button>
													<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:aclModal('V', '${item.user_id}');"><span class="icon mif-pencil"></span></button>
													<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:aclModal('V', '${item.user_id}');"><span class="icon mif-cancel"></span></button>
												</c:when>
												<c:when test="${sessionScope.userAuth == '2'}">
													<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:aclModal('V', '${item.user_id}');"><span class="icon mif-search"></span></button>
													<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:aclModal('V', '${item.user_id}');"><span class="icon mif-pencil"></span></button>
													<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:aclModal('V', '${item.user_id}');"><span class="icon mif-cancel"></span></button>
												</c:when>
												<c:when test="${sessionScope.userAuth == '1'}">
													<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:aclModal('V', '${item.user_id}');"><span class="icon mif-search"></span></button>
													<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:aclModal('V', '${item.user_id}');"><span class="icon mif-pencil"></span></button>
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
