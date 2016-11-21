<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="cell auto-size padding20 bg-white" id="cell-content">
    <table class="dataTable border bordered" data-role="datatable" data-searching="false" data-auto-width="t">
        <thead>
        <tr>
            <td class="sortable-column sort-asc" style="width: 150px">서비스명</td>
            <td class="sortable-column">서비스URL</td>
            <td class="sortable-column">서비스 상태</td>
            <td class="sortable-column">헬스요약</td>
        </tr>
        </thead>
        <tbody>
			<c:choose>
				<c:when test="${servicesList.size() < 1}">
					<tr>
						<td colspan="4" style="text-align: center;">No Data.</td>
					</tr>
				</c:when>
				<c:otherwise>
					<c:forEach items="${servicesList}" var="item">
						<tr>
							<td>${item.get("type")}</td>
							<td>${item.get("serviceUrl")}</td>
							<td>${item.get("serviceState")}</td>
							<td>${item.get("healthSummary")}</td>
						</tr>
					</c:forEach>
				</c:otherwise>
			</c:choose>
        </tbody>
    </table>
</div> 


<script>    
$(document).ready(function() {

}); 


</script>