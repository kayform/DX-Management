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
                    <h5 class="sub-alt-header">* PostgreSQL 서버를  모니터링/암호화관리 합니다.</h5>
                    <hr class="thin bg-grayLighter">	
                    <div>
                            <button id="pgmonBtn" name="pgmonBtn" class="button primary" onclick="javascript:runProgram('M')"><span class="mif-display"></span>모니터링</button>
                            <button id="encBtn" name="encBtn" class="button primary" onclick="javascript:runProgram('E')"><span class="mif-display"></span>암호화관리</button>
                    </div>
                    <hr class="thin bg-grayLighter">								
                    <!--  검색 조건 -->			                							
					<div style="padding-left: 10px;">
					<table style="width: 100%;" class="table">
						<colgroup>
							<col width="20%">
							<col width="20%">
							<col width="30%">
							<col width="30%">
						</colgroup>
						<tr>
							<td>
								<label>서버명  </label>
								<input type="text" id="searchSysNm" name="searchSysNm" value="${sys_nm}" class="input-mini" style="width: 200px;"/>
							</td>
							<td>
								<label>아이피  </label>
								<input type="text" id="searchIp" name="searchIp" value="${ip}" class="input-mini" style="width: 200px;"/>
							</td>
							<td>
								<label>RereshTime  </label>
								<select id="refreshTime" name="refreshTime" style="width: 250px;" onchange="javascript:refresh()">
									<!-- 
									<c:forEach var="item" items="${serverTypeList}">
										<option value="${item.sys_mnt_cd}">${item.sys_mnt_cd_nm}</option>
									</c:forEach>
									 -->
									 <option value="0">없음</option>
									 <option value="10000">10</option>
									 <option value="30000">30</option>
									 <option value="60000">60</option>
								</select>
							</td>
							<td>
								<div class="filter-bar">
									<button id="selBtn" name="selBtn" class="button primary place-right" onclick="javascript:reloadServerTable()"><span class="mif-search"></span>조회</button>
								</div>		
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
                            <td class="sortable-column">상태</td>
                            <td style="width: 150px">관리</td>
                        </tr>
                        </thead>
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
$(document).ready(function() {
	var auth = '${sessionScope.userAuth}';
	var columns = null;
	
	columns =  [
	              { data: 'sys_nm' },
	              { data: 'ip'},
	              { data: 'port' },
	              { data: 'status' },
	              { data: 'mng', defaultContent : "<button id=\"viewBtn\" style=\"margin:0;height:20px;width:50px;\" class=\"button\" \"><span class=\"mif-search\"></span></button>"}
	          ];
	
	table = $("#serverTbl").DataTable({		   
	    bDestroy: true,
	    paging : true,
	    /*"bJQueryUI": true,*/
		ajax: {
			url : '/pgmonList',
			type : 'post',
			data : function(d) {
				d.searchSysNm = $('#searchSysNm').val(),
				d.searchIp = $('#searchIp').val()
			},
			dataSrc : ""
		},
	    columns: columns
	});
	
	$('#serverTbl tbody').on('click', 'button', function() {
		var data = table.row( $(this).parents('tr') ).data();
		if ($(this)[0].id == "viewBtn"){
			showServerForm('V', data.sys_nm);
		}else if ($(this)[0].id == "modifyBtn") {
			showServerForm('U', data.sys_nm);
		}else if ($(this)[0].id == "deleteBtn") {
			deleteServer(data.sys_nm);
		}				
	});
});

function showServerForm(mode, sys_nm) {
	zephyros.loading.show();
	var url = '/serverForm?mode='+mode;
	var width = 840;
	var height = 420;
	var title = "POSTGRESQL 서버정보";
	var button = null;
	
	button =  {
		    "확인": function() {
		    	dialog_serverForm.dialog("close");
		      $("#dialog_serverForm").empty();
		    }};
	    
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

function reloadServerTable() {
	zephyros.loading.show();
	table.ajax.reload();
	zephyros.loading.hide();
};

function runProgram(type) {
	zephyros.loading.show();
	var url = "/userDetail"
 	zephyros.callAjax({
		url : url,
		type : 'post',
		data : {
			userId : '${sessionScope.userId}'
		},
		success : function(data, status, xhr) {
			zephyros.loading.hide();
			var value = null;
			var fileName = null;
			if (type == "M") {
				value = data.pg_mon_client_path;
				fileName = "DX.MonPostgres.exe";
			}else {
				value = data.enc_mng_path;
				fileName = "experDB-admin-console.exe";
			}
			
			if (zephyros.existsFile(value, fileName)){
				zephyros.runProgram(value);
			}else {
				if (value == null || value == "") {
					zephyros.showDialog(dialog_info, "실행파일 경로가 미설정되어 있습니다.\nprofile에서 경로를 추가하세요.");
				}else {
					zephyros.showDialog(dialog_info, "실행파일 경로가 유효하지 않습니다.");	
				}
			}
		}
	}); 
};

var timerId = null;
function refresh() {
	var refreshTime = $("#refreshTime").val();
	if (refreshTime == 0 && timerId != null) {
		clearInterval(timerId);
	}else if(refreshTime > 0) {
		timerId = setInterval("reloadServerTable()", refreshTime);		
	}
}
</script>