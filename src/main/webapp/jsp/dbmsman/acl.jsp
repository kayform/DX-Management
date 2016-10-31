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
                    <hr class="thin bg-grayLighter">
                    <hr class="thin bg-grayLighter">
                    <table class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="false">
                        <thead>
                        <tr>
                            <th class="sortable-column sort-asc" style="width: 180px">Server Name</th>
                            <th class="sortable-column" style="width: 180px">IP Address</th>
                            <th class="sortable-column" style="width: 100px">Port</th>
                            <th class="sortable-column" style="width: 50%">Comment</th>
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
										<td>${item.user_id}</td>
										<td>${item.user_id}</td>
										<td>${item.user_id}</td>
										<td>${item.user_id}</td>
										<td>
											<button style="margin:0;height:20px;width:50px;" class="button success" onclick="javascript:aclModal('V', '${item.user_id}');"><span class="icon mif-search"></span></button>
										</td>
									</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
                        </tbody>
                    </table>
                    
				<div class="cell auto-size padding20 bg-white" id="cell-content">
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
 							<button id="addBtn" class="button success" onclick="javascript:aclModal('I',selectServerName.value);"><span class="mif-plus"></span> Add</button>
						</div>
		
						<div class="clearfix"></div>
					</div>				
 				 </div>
 				 <table id="acllisttab" class="cell-border dataTable no-footer" cellspacing="0" width="100%">
                   <thead>
                   <tr>
						<th style="width:0">Seq</th>
						<th style="width:0">Set</th>
						<th style="width:40">Type</th>
						<th style="width:17%">Database</th>
						<th style="width:17%">User</th>
						<th style="width:17%">IP-Address</th>
						<th style="width:12%">Method</th>
						<th style="width:12%">Option</th>
 						<th style="visibility:hidden;display:none;">Changed</th>
						<th style="width:10pt"></th>
                   </tr>
                   </thead>

               </table> 				 
                </div>
            </div>
        </div>
        <div data-role="dialog" id="modify-aclinfo" class="padding20" data-close-button="false" data-type="success" data-width="500" data-height="400" data-overlay="true">	
    </div>

<script type="text/javascript">

    
function fnClickAddRow() {
	var table = $('#acllisttab').DataTable();
//	table.draw();

   table.fnAddData({
		    "Enable"   :  "1",
		    "Enable"   :  "2",
		    "Type"     :  "3",
		    "Database" :  "1",
		    "User"     :  "2",
		    "Ip"       :  "3",
		    "Method"   :  "1",
		    "Option"   :  "2",
		    "Changed"  :  "3"} 
 ).draw();



/* 	table.row.add({
    "Enable"   :  "1",
    "Enable"   :  "2",
    "Type"     :  "3",
    "Database" :  "1",
    "User"     :  "2",
    "Ip"       :  "3",
    "Method"   :  "1",
    "Option"   :  "2",
    "Changed"  :  "3"
	}); */
}
    
/* function fnClickAddRow() {
    $('#acllisttab').ataTable().fnAddData(
    	"1",   
    	".2",  
    	".3",  
    	".1",  
    	".2",  
    	".3",  
    	".1",  
    	".2",  
    	".3",  
    	".4" );
} */

function aclModal(mode, serverId) {
	zephyros.loading.show();
	var url = '';
	var titleTxt = '';
	var successTxt = '';
	var width = 0;
	if (mode == 'U') {
		url = '/aclForm?mode=U&serverId=' + serverId;
		titleTxt = '사용자 수정';
		successTxt = '사용자가 수정되었습니다.';
		width = 1000;
	} else if (mode == 'V') {
			url = '/aclForm?mode=V&serverId=' + serverId;
		titleTxt = '사용자 조회';
		successTxt = '사용자가 수정되었습니다.';
		width = 1000; 
		$('#selectServerName').val(serverId);
	} else if (mode == 'I') {
		url = '/aclForm?mode=I';
		titleTxt = '접근권한 추가';
		successTxt = '접근권한이 추가되었습니다.'
		width = 400;
	}else {
		url = '/aclForm?mode=S';
		titleTxt = '접근권한 등록';
		successTxt = '접근권한이 저장되었습니다.'
		width = 400;
	}

	var html = '';
		if (mode == 'V') {		
 			zephyros.loading.show();// 모달이 아닌 경우
			
 	 		var table = $('#acllisttab').DataTable({
 	 			retrieve: false,
 	 			scrollY: 200,
 	 			bscrollCollapse: false,
 	 			paging: false,
 	 			serverSide : true,
 				"autoWidth" : true,
 				"processing": true,
 				"ordering": false,
 				"serverSide": true,
 				"searching": false,
	 	        'ajax': {
	 	            'contentType': 'application/json',
	 	            'dataType': 'json',
	 	            'url': '/aclSearch?serverId=' + serverId,
	 	            'type': 'POST',
  	 				'dataSrc': function(json){
// 	 					console.log(json);
 	 					return json;
  	 				}
 	 		    },
	 	        "columns" : 
		 			[
			 			{"data" : 'Enable', "width": "0pt",
			 				render: function (data, type, row, meta) {
			 			        return meta.row + meta.settings._iDisplayStart + 1;
			 			    }
			 			},
			 			{"data" : 'Enable', "width": "1%",
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
				        {"data" : 'Type', "width": "5%",
			 				render: function (data, type, row, meta) {
		 			    	    return data;
		 			    	}
			 			},
				        {"data" : 'Database', "width": "17%"}, 
				        {"data" : 'User', "width": "17%"}, 
				        {"data" : 'Ip', "width": "17%"},
				        {"data" : 'Method', "width": "12%"}, 
				        {"data" : 'Option', "width": "12%"},
				        {"data" : 'Changed', "width": "0%"},
				        {"data" : 'Changed', "width": "0%"}
			        ],

	 	    });
 			
 	 		table.column( 8 ).visible( false );

		 } else if (mode == 'S') {		
	 		zephyros.loading.show();// 모달이 아닌 경우
			var head = [],
		    i = 0,
		    tableObj = {myrows: []};
			$.each($("#acllisttab thead th"), function() {
			    head[i++] = $(this).text();
			});

			$.each($("#acllisttab tbody tr"), function() {
			    var $row = $(this),
			        rowObj = {};

			    i = 0;
			    $.each($("td", $row), function() {
			        var $col = $(this);
			        rowObj[head[i]] = $col.text();
			        i++;
			    })

			    tableObj.myrows.push(rowObj);
			});

			var strData = JSON.stringify(tableObj);
//			alert(strData);
			
			$.ajax({
					url : '/aclProcess?serverId=' + serverId + '&aclArray=' + strData,
				    type: 'POST',
//					data: JSON.stringify(tableObj),
					data: strData,
					success : function(data, status, xhr) {
						zephyros.alert({
							contents : successTxt,
							close : function() {
								window.location.href ='/acl';
							}
						});
					}, error: function (e) { 
						ajaxErrorHandler(e);
					}
				});

		} else {
			$.ajax({
				url : url,
				type : 'post',
				data : null,
				success : function(data, status, xhr) {
					showDialog('modify-aclinfo', data);
				}, error: function (e) { 
					ajaxErrorHandler(e);
				},
				complete : function(xhr, status) {
					// double click 방지 해제
					// $(':button', form).attr('disabled', false).removeClass('disabled');
				}
			});
			
			
/* 			
		$.ajax({
			url : url,
			type : 'post',
			data : null,
			success : function(data, status, xhr) {
				zephyros.loading.hide();
				html = data;
				zephyros.dialog({
					id : "aclDialog",
					title : titleTxt,
					contents : html,
					width : width,
					buttons : [ {
						text : "저장",
						click : function() {
							if ($("#form02").valid()) {
							var str ="";
				                    //데이터 인풋
			                  str += "<tr>";
			                  str +="<td>"+ (acllist.rows.length) +"</td>" ;  
			                  str +="<td>"+ (enableAcl.checked ? "V" : "") +"</td>" ;  
			                  str +="<td>"+ connType.options[connType.value].text +"</td>" ;  
			                  str +="<td>"+ str_database.value +"</td>" ;  
			                  str +="<td>"+ str_user.value +"</td>" ;  
			                  str +="<td>"+ ip.value +"</td>" ;  
			                  str +="<td>"+ selectMethod.options[selectMethod.value].text +"</td>" ;  
			                  str +="<td>"+ authOption.value +"</td>" ; 
			                  str +="<td style='display:none'>"+ 1 +"</td>" ; 
			                  str += "<td><a id=\"editBtn\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"수정\" class=\"btn-action glyphicons pencil btn-primary\" href=\"javascript:aclModal('U','${item.acl_id}');\"><i></i></a><a id=\"deleteBtn\" data-toggle=\"tooltip\" data-placement=\"bottom\" title=\"삭제\" class=\"btn-action glyphicons remove_2 btn-danger\" href=\"javascript:deleteAcl('${item.acl_id}');\"><i></i></a></td>";
			                  str +="</tr>";

				 			  $("#aclDialog").modal("hide");
							  $('#acllist').append(str);
							}
						}
					}, {
						text : "닫기",
						click : function() {
							$("#aclDialog").modal("hide");
						}
					} ]
				});				
				 $("#aclDialog").modal("show");
			}, error: function (e) { 
				ajaxErrorHandler(e);
			},
			complete : function(xhr, status) {
				// double click 방지 해제
				// $(':button', form).attr('disabled', false).removeClass('disabled');
			}
		}); */
	}
}

/* //사용자 삭제
function deleteServer(server_id) {
	var form = $("#form01");
	zephyros.confirm({
		contents : '사용자를 삭제하시겠습니까?',
		ok : function() {
			zephyros.loading.show();// 모달이 아닌 경우
			$.ajax({
				url : '/userProcess?mode=D&serverId=' + server_id,
				type : 'post',
				success : function(data, status, xhr) {
					zephyros.loading.hide();
					zephyros.alert({
						contents : '사용자가 삭제되었습니다',
						close : function() {
							window.location.href = '/acl';
						}
					});
				}, error: function (e) { 
					ajaxErrorHandler(e);
				}
			})
		},
		cancel : function() {
			zephyros.loading.hide();
		}
	});
} */

</script>
