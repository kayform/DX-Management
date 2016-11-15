<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<style>
.widget .table thead th {
	background-color: rgba(0, 0, 0, 0.5);
	text-align: center;
	vertical-align: middle;
}
.btn-connectTest {
  border: 1px solid #a6bf85;
  color: #ffffff;
  text-shadow: none;
}
</style>
    <div class="page-content">
        <div class="flex-grid no-responsive-future" style="height: 100%;">
            <div class="row" style="height: 100%">
                <div class="cell size-x200" id="cell-sidebar" style="background-color: #71b1d1; height: 100%">
                </div>            
                <div class="cell auto-size padding20 bg-white" id="cell-content">
                    <h1 class="text-light">서버 관리<span class="mif-drive-eta place-right"></span></h1>
                    <h5 class="sub-alt-header">* 관리대상 서버를 등록/수정합니다.</h5>
                    <hr class="thin bg-grayLighter">								
                    <!--  검색 조건 -->
					<div class="filter-bar">
							<button id="selBtn" name="selBtn" class="button primary"><span class="mif-search"></span>조회</button>
                    		<button id="regSvrBtn" name="regSvrBtn" class="button primary"><span class="mif-plus"></span>등록</button>  		
					</div>					
                    <hr class="thin bg-grayLighter">                    							
					<div style="padding-left: 10px;">
					<table style="width: 60%;" class="table">
						<colgroup>
							<col width="33%">
							<col width="33%">
							<col width="33%">
						</colgroup>
						<tr>
							<td>
								<label>서버명  </label>
								<input type="text" id="searchSysNm" name="searchSysNm" value="${sys_nm}" class="input-mini" style="width: 200px;"/>
							</td>
							<td>
								<label>유형  </label>
								<select id="searchType" name="searchType" style="width: 250px;">
									<c:forEach var="item" items="${serverTypeList}">
										<option value="${item.sys_mnt_cd}">${item.sys_mnt_cd_nm}</option>
									</c:forEach>
								</select>
							</td>
							<td>
								<label>아이피  </label>
								<input type="text" id="searchIp" name="searchIp" value="${ip}" class="input-mini" style="width: 200px;"/>
							</td>
						</tr>
					</table>
					</div>							
                    <table id='serverTbl' class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="false">
                        <thead>
                        <tr>
                            <td class="sortable-column sort-asc" style="width: 200px">서버명</td>
                            <td class="column">아이피</td>
                            <td class="column">포트</td>
                            <td class="sortable-column">유형</td>
                            <td style="width: 150px">관리</td>
                        </tr>
                        </thead>
                        <tbody>
						<c:choose>
							<c:when test="${serverList.size() < 1}">
								<tr>
									<td colspan="6" style="text-align: center;">No Data.</td>
								</tr>
							</c:when>
							<c:otherwise>
								<c:forEach items="${serverList}" var="item">
									<tr>
										<td>${item.sys_nm}</td>
										<td>${item.ip}</td>
										<td>${item.port}</td>
										<td>${item.type}</td>
										<td>
											<!-- 슈퍼유저(3), 관리자(2)일 경우 조회, 수정 및 삭제 권한부여-->
											<!-- 사용자(1) 일 경우 본인 정보 조회, 수정권한 부여 및 타 사용자 조회 기능 부여-->
											<c:choose>
												<c:when test="${sessionScope.userAuth == '3' or sessionScope.userAuth == '2'}">
													<button id="viewBtn" style="margin:0;height:20px;width:50px;" class="button" onclick="javascript:showServerForm('V', '${item.sys_nm}');"><span class="mif-search"></span></button>
													<button id="modifyBtn" style="margin:0;height:20px;width:50px;" class="button" onclick="javascript:showServerForm('U', '${item.sys_nm}');"><span class="mif-pencil"></span></button>
													<button id="deleteBtn" style="margin:0;height:20px;width:50px;" class="button" onclick="javascript:deleteServer('${item.sys_nm}');"><span class="mif-cancel"></span></button>
												</c:when>
												<c:when test="${sessionScope.userAuth == '1'}">
													<button id="viewBtn" style="margin:0;height:20px;width:50px;" class="button" onclick="javascript:showServerForm('V', '${item.sys_nm}');"><span class="icon mif-search"></span></button>
													<button id="modifyBtn" style="margin:0;height:20px;width:50px;" class="button" onclick="javascript:showServerForm('U', '${item.sys_nm}');"><span class="icon mif-pencil"></span></button>
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
<div id="dialog_serverForm">
  <form>
  </form>
</div> 

<script>
	
function showServerForm(mode, sys_nm) {
	zephyros.loading.show();
	var url = '/serverForm?mode='+mode;
	var width = 840;
	var height = 420;
	var title = "서버등록/수정";
	var button = null;
	
	if (mode == "V") {
		button =  {
			    "확인": function() {
			    	dialog_serverForm.dialog("close");
			      $("#dialog_serverForm").empty();
			    }};
	}else{
		button =  {
			    "저장" : function() {
			    	if (zephyros.isFormValidate('serverForm')){
			    		zephyros.loading.show();

						var url = '/serverProcess';

						var formData = $("#serverForm").serialize();				

						zephyros.callAjax({
							url : url,
							type : 'post',
							data : formData,
							success : function(data, status, xhr) {
								zephyros.loading.hide();
								zephyros.checkAjaxDialogResult(dialog_serverForm, data);
							}
						});		
			    	}

			    },
			    "취소": function() {
			    	dialog_serverForm.dialog("close");
			      $("#dialog_serverForm").empty();
			    }};
	}
	    
	dialog_serverForm = $("#dialog_serverForm").dialog({
		  autoOpen: false,
		  height: height,
		  width: width,
		  modal: true,
		  title: title,
		  resizable: false,
		  buttons: button,
		  close: function() {
			$("#dialog_serverForm").empty();
		  }
		});

	
	var data = null;
	
	if (mode != 'I'){
		data = {
			sys_nm : sys_nm
		}
	}
 	zephyros.callAjax({
		url : url,
		type : 'post',
		data : data,
		success : function(data, status, xhr) {
			zephyros.loading.hide();
			zephyros.showDialog(dialog_serverForm, data);
		}
	}); 
}

$("#regSvrBtn").on("click", function() {
	showServerForm('I');
});

$("#selBtn").on("click", function() {
	zephyros.loading.show();
	var url = '/server';
	
 	zephyros.callAjax({
		url : url,
		type : 'post',
		data : {
			sys_nm : $('#searchSysNm').val(),
			type : $('#searchType').val(),
			ip : $('#searchIp').val()
		},
		success : function(data, status, xhr) {
			zephyros.loading.hide();
		}
	}); 
});

function deleteServer(sys_nm) {
	zephyros.loading.show();
	var url = '/serverProcess';
	
 	zephyros.callAjax({
		url : url,
		type : 'post',
		data : {
			mode : "D",
			sys_nm : sys_nm
		},
		success : function(data, status, xhr) {
			zephyros.loading.hide();
			zephyros.checkAjaxDialogResult(null, data);
		}
	}); 
}
</script>