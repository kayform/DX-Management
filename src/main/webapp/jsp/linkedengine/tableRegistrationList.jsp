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
								return "<label class=\"checkbox\"><input type=\"checkbox\" class=\"checkbox\" id="+full.table_schema+" name="+full.table_name+"  value=\"original\"  onchange=\"setModified(this)\"></label>";
							}
							else if(data == 1) {
								return "<label class=\"checkbox\"><input type=\"checkbox\" class=\"checkbox\" id="+full.table_schema+" name="+full.table_name+"  value=\"original\" onchange=\"setModified(this)\" checked=\"checked\" ></label>";
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
	

	//페이징 부분에서 이벤트가 발생하면 
	//변경내역이 있는지 체크하여 변경사항이 있으면 메소드를 호출하여 이동하기 전에 저장할지 결정하게 한다.	 
//	$('#tableRegistrationList').on('page.dt', function () {
	$('#tableRegistrationList_paginate').on('click', function() {
	    //var info = tableRegistrationList.page.info();
	    //alert(info.page);
	    
		var rowCount = $('#tableRegistrationList tbody tr').length;
	    var root = $('#tableRegistrationList tbody tr td input');
		var value = "";
		var reqData = "";
		var modifiedCount = 0;

		for (var i = 0; i < root.length; i++) {
			if(root[i].value == "modified"){
				console.log(root[i]);
				modifiedCount++;
				if(i!=0) reqData += "&";
				reqData += "mode=" + (root[i].checked ? "I" : "D");
				reqData += "&systemName=" + "${systemName}";
				reqData += "&databaseName=" + "${databaseName}";
				reqData += "&schemaName=" + root[i].id;
				reqData += "&tableName=" + root[i].name;
			}
		}
		//console.log("reqData="+reqData);
	    if(modifiedCount > 0){
			//console.log(modifiedCount+'개가 변경되었습니다.');
			registerTableList(reqData);
	    }
	    else{
			//console.log(modifiedCount+'개가 변경되었습니다.');
	    	//페이지 호출. 따로 호출 안해도 자동으로 호출됨
	    	//tableRegistrationList.page( info.page ).draw( 'page' );
	    }

	} );
	
});   

	 
//인포창 띄워서 저장할지 안할지 물어 보고 확인을 클릭하면 저장한다. !!
function registerTableList(reqData){
	zephyros.loading.show();
	
	dialog_tableRegisterInfo = $("#dialog_tableRegisterInfo").dialog({
		title: '연계 테이블 변경 저장',
		height: 200,
		width: 400,
    	buttons: {
    		"변경내용 저장": function() {
		    	zephyros.loading.show();
		    	
				zephyros.callAjax({
    				url : '/addTableProcess',
    				type : 'post',
    				data : reqData,
    				success : function(data, status, xhr) {
    					zephyros.loading.hide();
  						zephyros.checkAjaxDialogResult(dialog_tableRegisterInfo,  data);
    				}
    			});
    	  	},
    	  	"취소": function() {
				zephyros.loading.hide();
				dialog_tableRegisterInfo.dialog("close");
    	  	    $("#dialog_tableRegisterInfo").empty();
    	  	}
    	},
		close: function() {
			zephyros.loading.hide();
			$("#dialog_tableRegisterInfo").empty();
    	}
	});   
   	
	zephyros.showDialog(dialog_tableRegisterInfo, "변경된 내용을 저장하시겠습니까?");
}


//체크박스에 변경이 생기면.. 해당 로우가 변경되었는지 값을 셋팅한다.
function setModified(obj){
	if(obj.value == 'original') obj.value = 'modified';
	else if (obj.value == 'modified') obj.value = 'original';
	else alert('error at setModified function()');
}



</script>