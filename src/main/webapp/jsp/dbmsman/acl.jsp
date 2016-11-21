<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

    <div class="page-content">
        <div class="flex-grid no-responsive-future" style="height: 100%;">
            <div class="row" style="height: 100%">
                <div class="cell size-x200" id="cell-sidebar" style="background-color: #71b1d1; height: 100%">
 
                </div>
                <div class="cell auto-size padding20 bg-white" id="cell-content">
                    <h1 class="text-light">PostgreSQL Access Control<span class="mif-pin place-right"></span></h1>
                    <h5 class="sub-alt-header">* 사용자, 데이터베이스, 호스트별 접근 설정을 관리합니다.</h5>
                    <hr class="thin bg-grayLighter">
                    <hr class="thin bg-grayLighter">
                    <!-- <table id="serverListtab" class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="false" data-page-length='5' class="hover" cellspacing="0" width="100%"> -->
                    <table id="serverListtab" class="row-border hover order-column" cellspacing="0" width="100%"  data-page-length='5' data-bInfo="false">
                    
                        <thead>
                        <tr>
                            <th class="sortable-column sort-asc" style="width: 180px">Server Name</th>
                            <th class="sortable-column" style="width: 180px">IP Address</th>
                            <th class="sortable-column" style="width: 100px">Port</th>
                            <th class="sortable-column" style="width: 50%">Database</th>
                            <th></th>
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
										<td>${item.db_nm}</td>
										<td>
											<button style="margin:0;height:20px;width:50px;" class="button" onclick="javascript:aclModal('V', '${item.sys_nm}');"><span class="icon mif-search"></span></button>
										</td>
									</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
                        </tbody>
                    </table>
                    
 				<div class="cell auto-size padding10 bg-white" id="cell-content">
				                    <hr class="thin bg-grayLighter">
				                    <hr class="thin bg-grayLighter">
				</div>
                
 	            <div class="separator top form-inline small">	
					<div class="filter-bar">
						<div style="margin: 0px 5px 10px 0px; float: left; padding:">
							<label style="padding-top:3px;">PostgreSQL 서버명  :  </label>
							<div class="input-control text">
							    <input id="selectServerName" name="selectServerName" value="" style="width: 150px;height: 30px;margin: 0px 0px 00px 20px;" type="text" disabled >
							</div>
						</div>
						<div class="pagination pagination-small pull-right" style="margin: 0px 5px 10px 0px; float: right; padding:">
							<button id="saveBtn" class="button primary" onclick="javascript:aclModal('S',selectServerName.value);"><span class="mif-floppy-disk"></span> 저장</button>
						</div>
						
						<div class="pagination pagination-small pull-right" style="margin: 0px 5px 10px 0px; float: right; padding:">
							<button id="removeBtn" class="button primary" onclick="javascript:aclModal('D',selectServerName.value);"><span class="mif-cross"></span> 삭제</button>
						</div>
		
						<div class="pagination pagination-small pull-right" style="margin: 0px 5px 10px 0px; float: right; padding:">
 							<button id="addBtn" class="button primary" onclick="javascript:aclModal('U',selectServerName.value);"><span class="mif-pencil"></span> 편집</button>
						</div>

						<div class="pagination pagination-small pull-right" style="margin: 0px 5px 10px 0px; float: right; padding:">
 							<button id="addBtn" class="button primary" onclick="javascript:aclModal('I',selectServerName.value);"><span class="mif-plus"></span> 등록</button>
						</div>
		
						<div class="clearfix"></div>
					</div>				
 				 </div>
 				 <table id="acllisttab" class="row-border hover no-footer"  cellspacing="0" width="100%">
                   <thead>
                   <tr>
						<th style="width:0;visibility:hidden;display:none;">Seq</th>
						<th style="width:1%;">Set</th>
						<th style="width:5%">Type</th>
						<th style="width:15%">Database</th>
						<th style="width:10%">User</th>
						<th style="width:17%">IP-Address</th>
						<th style="width:12%">Method</th>
						<th style="width:12%">Option</th>
 						<th style="visibility:hidden;display:none;">Changed</th>
                   </tr>
                   </thead>

               </table> 				 
                </div>
            </div>
        </div>
<!--         <div class="modal fade color-10 in" id="modify-aclinfo" role="dialog" data-backdrop="static" style="display: none;"></div> -->
<!--         <div data-role="dialog" id="modify-aclinfo" data-close-button="true" data-overlay="true"></div> -->
<div id="modify-aclinfo">
  <form>
  </form>
</div> 
    </div>

<script type="text/javascript">
$(document).ready(function() {
  	var stable = $('#serverListtab').DataTable({"bLengthChange": false});
    $('#serverListtab tbody')
    .on( 'mouseenter', 'td', function () {
        var colIdx = stable.cell(this).index().column;
        $( stable.cells().nodes() ).removeClass( 'highlight' );
    }); 
    
	table = $('#acllisttab').DataTable({
        fixedHeader: true,
		retrieve: true,
		scrollY: 240,
		bscrollCollapse: false,
		bInfo: false,
		paging: false,
		autoWidth : true,
		processing: true,
		ordering: false,
		searching: false,
		destroy: true,
		ajax: {
			url : '/aclSearch',
			type : 'post',
	  			data : function(d) {
				d.serverId = $('#selectServerName').val();
			},  
			dataSrc : function(json){
				return json;
			}
		},
        "columns" : 
			[
 			{"data" : 'Seq', "width": "1%"},
 			{"data" : 'Set', "width": "1%",
 	            render: function ( data, type, row ) {
 	                if ( type === 'display' ) {
 	                	if(data == "1")
 	                    	return '<input type="checkbox" disabled checked class="checkbox_check">';
 	                    else
 	                    	return '<input type="checkbox" disabled class="checkbox_check">';
 	                }
 	                return data;
 	            },
 			}, 
	        {"data" : 'Type', "width": "5%"},
	        {"data" : 'Database', "width": "15%"}, 
	        {"data" : 'User', "width": "12%"}, 
	        {"data" : 'Ip', "width": "12%"},
	        {"data" : 'Method', "width": "12%"}, 
	        {"data" : 'Option', "width": "12%"},
	        {"data" : 'Changed', "width": "0%"}
        ],
		columnDefs: [
            {
                "targets": [ 0 ],
                "visible": false
            },
            {
                "targets": [ 8 ],
                "visible": false
            }
        ],
		select: {
		     //style: 'os',
		     style: 'single',
		     selector: 'td:not(:last-child)' // no row selection on last column
		},
		buttons: [
	    	{
	            text: 'Reload table',
	            action: function () {
	                table.ajax.reload();
	            }
	        }
	    ]
	});
	
 	$('#removeBtn').click( function () {
         table.row('.selected').remove().draw( false );
    });
}); 

function aclModal(mode, serverId) {
	var url = '';
	var titleTxt = '';
	var successTxt = '';
	var width = 0;
	if (mode == 'U') {
		var uTable = $('#acllisttab').DataTable();
		var seq = uTable.row('.selected').data().Seq;
		var set = uTable.row('.selected').data().Set;
		var type = uTable.row('.selected').data().Type ? uTable.row('.selected').data().Type : "" ;
		var database = uTable.row('.selected').data().Database ? uTable.row('.selected').data().Database : "";
		var user = uTable.row('.selected').data().User ? uTable.row('.selected').data().User : "";
		var ip = uTable.row('.selected').data().Ip ? uTable.row('.selected').data().Ip : "";
		var method = uTable.row('.selected').data().Method ? uTable.row('.selected').data().Method : "";
		var option = uTable.row('.selected').data().Option ? uTable.row('.selected').data().Option : "";
		url = '/aclForm?mode=U&seq=' + seq + '&set=' + set + '&type=' + type + '&database=' + database + '&user=' + user + '&ip=' + ip + '&method=' + method + '&option=' + option;
		titleTxt = '접근권한 수정';
		successTxt = '접근권한이 수정되었습니다.';
		width = 600;
	} else if (mode == 'V') {
		url = '/aclForm?mode=V&serverId=' + serverId;
		titleTxt = '접근권한 조회';
		successTxt = '접근권한이 수정되었습니다.';
		width = 1000; 
		$('#selectServerName').val(serverId);
	} else if (mode == 'I') {
		url = '/aclForm?mode=I';
		titleTxt = '접근권한 추가';
		successTxt = '접근권한이 추가되었습니다.';
		width = 600;
	} else {
		url = '/aclForm?mode=S';
		titleTxt = '접근권한 등록';
		successTxt = '접근권한이 저장되었습니다.';
		width = 400;
	}

	var html = '';
	if (mode == 'V') {		
		zephyros.loading.show();
		table.ajax.reload();
		zephyros.loading.hide();
	} else if (mode == 'D') {
		successTxt = '접근권한이 삭제되었습니다.';
		var dTable = $('#acllisttab').DataTable();		
		var seq = dTable.row('.selected').data().Seq;
		dTable.row('.selected').remove().draw( false );
   		$.ajax({
				url : '/aclDelete?seq=' + seq,
			    type: 'POST',
				data: null,
				success : function(data, status, xhr) {
				}, error: function (e) { 
					ajaxErrorHandler(e);
				}
			});
 	} else if (mode == 'S') {		
	 	zephyros.loading.show();
		var head = [],
	    i = 0,
	    tableObj = {myrows: []};

		var oTable = $('#acllisttab').DataTable();
		for(i = 0; i < oTable.data().length; i++)
			tableObj.myrows.push(oTable.row(i).data());
		
		var strData = JSON.stringify(tableObj);
		
		zephyros.callAjax({
			url : '/aclProcess?serverId=' + serverId + '&aclArray=' + strData,
			type : 'post',
			data : strData,
			success : function(data, status, xhr) {
				zephyros.checkAjaxDialogResult(dialog_profile, data);
			 	zephyros.loading.hide();
			}
		});			
	} else {
		$.ajax({
			url : url,
			type : 'post',
			data : null,
			success : function(data, status, xhr) {
 				dialog_acl = $("#modify-aclinfo").dialog({
 					  autoOpen: false,
 					  height: 500,
 					  width: width,
 					  modal: true,
 					  title: titleTxt,
 						buttons : [ {
 							text : "저장",
 							click : function() {
 								if (zephyros.isFormValidate('form02')){
 									var str ="";
 					                    //데이터 인풋
	 					            var dialogMode = $("#mode").val();
	 								var vTable = $('#acllisttab').DataTable();

	 								if(dialogMode == 'I') {
	 	 								var seq = vTable.data().length;
									 	vTable.row.add({
 										    "Seq"   :  seq.toString(),									    
										    "Set"   :  ($("#enableAcl").is(':checked') ? "1" : ""),									    
										    "Type"     :  $("#selectConnType option:selected" ).text(),
										    "Database" :  $("#str_database").val(),
										    "User"     :  $("#str_user").val(),
										    "Ip"       :  $("#ip").val(),
										    "Method"   :  $("#selectMethod option:selected" ).text(),
										    "Option"   :  $("#authOption").val(),
										    "Changed"  :  "1"
										}).draw();
	 								} else {
	 									var seq = vTable.row('.selected').data().Seq;
	 								 	vTable.row('.selected').data({
										    "Seq"   :  seq,									    
										    "Set"   :  ($("#enableAcl").is(':checked') ? "1" : ""),									    
										    "Type"     :  $("#selectConnType option:selected" ).text(),
										    "Database" :  $("#str_database").val(),
										    "User"     :  $("#str_user").val(),
										    "Ip"       :  $("#ip").val(),
										    "Method"   :  $("#selectMethod option:selected" ).text(),
										    "Option"   :  $("#authOption").val(),
										    "Changed"  :  "1"
										}).draw();
	 								}
								 	dialog_acl.dialog("close");
								 	$("#modify-aclinfo").empty();
 								}
 							}
 						}, {
 							text : "닫기",
 							click : function() {
						    	dialog_acl.dialog("close");
						        $("#modify-aclinfo").empty();
 							}
 						} ]
 					}); 
 				
 			zephyros.showDialog(dialog_acl, data);
 			
			}, error: function (e) { 
				ajaxErrorHandler(e);
			},
			complete : function(xhr, status) {
				// double click 방지 해제
				// $(':button', form).attr('disabled', false).removeClass('disabled');
			}
		}); 
	}
}

</script>
