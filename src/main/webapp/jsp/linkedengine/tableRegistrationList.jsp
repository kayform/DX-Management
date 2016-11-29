<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="cell auto-size padding20 bg-white" id="cell-content">
    <p>Kafka Connection : <input type="text" id="connectName" name="connectName" readonly="readonly" value="${connectName}" style="width:30%;">
    <p>* 연계 테이블 등록 리스트
    <table id="tableRegistrationList" class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="t">
        <thead>
        <tr>
            <td class="sortable-column sort-asc" style="width: 150px">스키마 명</td>
            <td class="sortable-column">테이블 명</td>
            <td class="sortable-column">선택</td>
        </tr>
        </thead>
    </table>
</div> 

<script>    

$(document).ready(function() {
	var tableColumns = null;
	tableColumns =  [
					{ "data": 'table_schema' },
					{ "data": 'table_name'},
					{ "data": 'is_checked',
					  "render": function ( data, type, full, meta ) {
							if(data == 0) {
								return "<label class=\"checkbox\"><input type=\"checkbox\" class=\"checkbox\" id="+full.table_schema+" tableName="+full.table_name+"  value=\"original\"  onchange=\"setModified(this)\"></label>";
							}
							else if(data == 1) {
								return "<label class=\"checkbox\"><input type=\"checkbox\" class=\"checkbox\" id="+full.table_schema+" tableName="+full.table_name+"  value=\"original\" onchange=\"setModified(this)\" checked=\"checked\" ></label>";
							}
							else {
								console.log("is_checked 값에 오류가 있습니다. is_checked값 = "+data);
							}
						}
					}
				];
	
	tableRegistrationList = $("#tableRegistrationList").DataTable({
	    "paging" : true,
	    "serverSide" : true,
	    //"stateSave" : true,	    
	    'ajax': {
			url : '/tableRegistrationListData',
			type : 'post',
			data : { systemName : "${systemName}",databaseName : "${databaseName}",connectName : "${connectName}"},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log('에러 발생 = '+errorThrown); 
			}
		},
        "columns": tableColumns
        
	});
	

	 
	$('#tableRegistrationList').on( 'page.dt', function () {
	    var info = tableRegistrationList.page.info();
	    //alert(info.page);
	    
	    var root = $('#tableRegistrationList tbody tr td input');
	    console.log(root.length);
		for (var i = 0; i < root.length; i++) {
			if(root[i].value == "modified"){
				console.log(i);
				console.log('변경 ='+root[i].value);
				break;
			}
			else {
				console.log(i);
				console.log('변경안됨 ='+root[i].value);
			}
		}
		
	    
	    
	} );	
	
	
});   

//체크박스에 변경이 생기면.. 해당 로우가 변경되었는지 값을 셋팅한다.
function setModified(obj){
	console.log(obj.value);
	if(obj.value == 'original') obj.value = 'modified';
	else if (obj.value == 'modified') obj.value = 'original';
	else alert('error');
}



</script>