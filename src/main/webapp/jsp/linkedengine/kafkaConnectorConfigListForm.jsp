<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="cell auto-size padding20 bg-white" id="cell-content">
    <p>* KAFKA CONNECT 리스트
    <table class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="t">
        <thead>
        <tr>
            <td class="sortable-column sort-asc" style="width: 150px">Connect 명</td>
            <td class="sortable-column">HDFS URL</td>
            <td class="sortable-column">상태</td>
            <td style="width: 80px">관리</td>
        </tr>
        </thead>
        <tbody>
			<c:choose>
				<c:when test="${configList.size() < 1}">
					<tr>
						<td colspan="4" style="text-align: center;">No Data.</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach items="${configList}" var="item">
						<tr>
							<td>${item.get("name")}</td>
							<td>${item.get("hdfs.url")}</td>
							<td>${item.get("status")}</td>
							<td>
								<button style="margin:0;height:20px;width:50px;" class="button" onclick="javascript:showConnectConfigForm('${item.get('connector.class')}', '${item.get('flush.size')}','${item.get('hadoop.conf.dir')}','${item.get('topics')}','${item.get('tasks.max')}','${item.get('hdfs.url')}','${item.get('name')}','${item.get('rotate.interval.ms')}');"><span class="icon mif-search"></span></button>
							</td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
        </tbody>
    </table>
</div> 

<div id="dialog_connectConfig">
  <form>
  </form>
</div> 

<script>    
$(document).ready(function() {

}); 

	function showConnectConfigForm(connectorClass, flushSize, hadoopConfDir, topics, tasksMax, hdfsURL, name, rotateIntervalMS) {
    	zephyros.loading.show();

		url = '/kafkaConnectorConfigForm?connectorClass='+connectorClass+'&flushSize='+flushSize+'&hadoopConfDir='+hadoopConfDir+'&topics='+topics+'&tasksMax='+tasksMax+'&hdfsURL='+hdfsURL+'&name='+name+'&rotateIntervalMS='+rotateIntervalMS;
		//alert(url);
		var width = 1000;
		var height = 800;
		var title = "KAFKA CONNECT CONFIG 정보";
		var button = null;
		
		button =  {
			    "확인": function() {
			    	dialog_connectConfig.dialog("close");
			      $("#dialog_connectConfig").empty();
			    }};
		    
		dialog_connectConfig = $("#dialog_connectConfig").dialog({
			  autoOpen: false,
			  height: height,
			  width: width,
			  modal: true,
			  title: title,
			  resizable: false,
			  buttons: button,
			  close: function() {
				$("#dialog_connectConfig").empty();
			  }
			});

		
		var data = null;
		
		
	 	zephyros.callAjax({
			url : url,
			type : 'post',
			data : data,
			success : function(data, status, xhr) {
				zephyros.loading.hide();
				zephyros.showDialog(dialog_connectConfig, data);
			}
		});	
	}


</script>