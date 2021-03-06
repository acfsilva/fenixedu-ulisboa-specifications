<%@page import="org.fenixedu.commons.i18n.I18N"%>
<%@page import="org.fenixedu.ulisboa.specifications.ui.legal.report.raides.ManageLegalMappingController"%>
<%@page import="org.fenixedu.ulisboa.specifications.ui.legal.report.raides.RaidesConfigurationController"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<spring:url var="datatablesUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js" />
<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css" />

<link rel="stylesheet" href="${datatablesCssUrl}" />
<spring:url var="datatablesI18NUrl" value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css" />

<!-- Choose ONLY ONE:  bennuToolkit OR bennuAngularToolkit -->
${portal.angularToolkit()}
<%--${portal.toolkit()}--%>

<link href="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/css/dataTables.responsive.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js"></script>
<script src="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/js/omnis.js"></script>

<script src="${pageContext.request.contextPath}/webjars/angular-sanitize/1.3.11/angular-sanitize.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/webjars/angular-ui-select/0.11.2/select.min.css" />
<script src="${pageContext.request.contextPath}/webjars/angular-ui-select/0.11.2/select.min.js"></script>


<script type="text/javascript">
function openCreateModal() {
    $("#createModal").modal("toggle");
}

function openDeleteModal(externalId) {
    url = "${pageContext.request.contextPath}<%= ManageLegalMappingController.DELETE_ENTRY_URL %>/${ legalMapping.externalId }/" + externalId;
    $("#deleteForm").attr("action", url);
    $('#deleteModal').modal('toggle');
}
</script>

<!-- Due to modal -->
<style type="text/css">
.select2-container--open{
        z-index:9999999         
    }
</style>

<div class="modal fade" id="createModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="createLegalMappingEntryForm" action="${pageContext.request.contextPath}<%= ManageLegalMappingController.CREATE_ENTRY_URL%>/${legalMapping.externalId}" method="POST">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                    aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">
                    <spring:message code="label.LegalMappingEntry.createModal" />
                </h4>
            </div>
            <div class="modal-body">
                <div class="form-group row">
                    <div class="col-sm-2 control-label">
                        <spring:message code="label.LegalMappingEntry.key" />
                    </div>
                    <div class="col-sm-7">
                        <select id="legalMappingEntry_key" name="key" class="js-example-basic-single">
                            <c:forEach var="legalMappingType" items="${ possibleLegalMappingEntryKeys }">
                                <option value="${ legalMappingType.id }"><c:out value="${ legalMappingType.text }" /></option>
                            </c:forEach>
                        </select>
                        
	                    <script>
		                	$(document).ready(function() {
		                	     $("#legalMappingEntry_key").select2();
		                	});
	                	</script>
                	
                    </div>
                </div>
                <div class="form-group row">
                    <div class="col-sm-2 control-label">
                        <spring:message code="label.LegalMappingEntry.value" />
                    </div>
                    <div class="col-sm-7">
                        <input id="legalMappingEntry_value" class="form-control" type="text" name="value" 
                            value='<c:out value='${requestScope["legalMappingEntry_value"]}'/>'
                        />
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    <spring:message code="label.close" />
                </button>
                <button id="createButton" class="btn btn-primary" type="submit">
                    <spring:message code="label.event.create" />
                </button>
            </div>
            </form>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<div class="modal fade" id="deleteModal">
    <div class="modal-dialog">
        <div class="modal-content">
            <form id="deleteForm" action="#" method="POST">
                <div class="modal-header">
                    <button type="button" class="close"
                        data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                    <h4 class="modal-title">
                        <spring:message code="label.confirmation" />
                    </h4>
                </div>
                <div class="modal-body">
                    <p>
                        <spring:message code="label.manageLegalMapping.LegalMappingEntry.confirmDelete" />
                    </p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default"
                        data-dismiss="modal">
                        <spring:message code="label.close" />
                    </button>
                    <button id="deleteButton" class="btn btn-danger" type="submit">
                        <spring:message code="label.delete" />
                    </button>
                </div>
            </form>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->


<%-- TITLE --%>
<div class="page-header">
	<h1>
		<spring:message code="label.manageLegalMapping.read" />
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display: inline-block">

	<span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>
	&nbsp;
	<a class="" href="${pageContext.request.contextPath}<%= ManageLegalMappingController.SEARCH_URL %>">
		<spring:message code="label.event.back" />
	</a>
    &nbsp;|&nbsp;
    <span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
    <a class="" href="#" onClick="openCreateModal()">
        <spring:message code="label.LegalMappingEntry.create" />
    </a>
 
</div>

<c:if test="${not empty infoMessages}">
	<div class="alert alert-info" role="alert">

		<c:forEach items="${infoMessages}" var="message">
			<p>
				<span class="glyphicon glyphicon glyphicon-ok-sign" aria-hidden="true">&nbsp;</span>
				${message}
			</p>
		</c:forEach>

	</div>
</c:if>
<c:if test="${not empty warningMessages}">
	<div class="alert alert-warning" role="alert">

		<c:forEach items="${warningMessages}" var="message">
			<p>
				<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
				${message}
			</p>
		</c:forEach>

	</div>
</c:if>
<c:if test="${not empty errorMessages}">
	<div class="alert alert-danger" role="alert">

		<c:forEach items="${errorMessages}" var="message">
			<p>
				<span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
				${message}
			</p>
		</c:forEach>

	</div>
</c:if>


<script>
angular.isUndefinedOrNull = function(val) {
    return angular.isUndefined(val) || val === null
};
angular.module('angularApp', ['ngSanitize', 'ui.select']).controller('angularController', ['$scope', function($scope) {

//     $scope.object= angular.fromJson('${legalMappingBeanJson}');
    $scope.postBack = createAngularPostbackFunction($scope);
    
    $scope.booleanvalues = [
            { name : '<spring:message code="label.no"/>', value : false },
            { name : '<spring:message code="label.yes"/>', value : true } 
    ];
}]);
</script>

<form name='form' method="post" class="form-horizontal" ng-app="angularApp" ng-controller="angularController" action='#'>

    <input type="hidden" name="postback" value='#' />
    <input name="bean" type="hidden" value="{{ object }}" />

    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title">
                <spring:message code="label.details" />
            </h3>
        </div>
        <div class="panel-body">
            <table class="table">
                <tbody>
                    <tr>
                        <th scope="row" class="col-xs-3">
                            <spring:message code="label.LegalMappingType.name" />
                        </th>
                        <td>
                            <c:out value='${ legalMapping.nameI18N.content }' />
                        </td>
                    </tr>

                </tbody>
            </table>
        </div>
    </div>

    <c:choose>
        <c:when test="${ not empty legalMappingEntries }">
            <table id="legalMappingEntryKeys" class="table responsive table-bordered table-hover" width="100%">
            	<thead>
            		<tr>
            			<th><spring:message code="label.LegalMappingEntry.key" /></th>
                        <th><spring:message code="label.LegalMappingEntry.value" /></th>
            			<th style="width: 25%"></th>
            		</tr>
            	</thead>
            	<tbody>
                    <c:forEach var="legalMappingEntry" items="${ legalMappingEntries }">
                        <tr>
                            <th scope="row" class="col-xs-3">
                                <c:out value="${ legalMappingEntry.localizedNameKeyI18N.content }"></c:out>
                            </th>
                            <td>
                                <c:out value="${ legalMappingEntry.mappingValue }" />
                            </td>
                            <td>
                                <a class="btn btn-danger btn-xs" onClick="openDeleteModal( '${ legalMappingEntry.externalId }' )">
                                    <span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
                                    &nbsp;
                                    <spring:message code='label.delete' />
                                </a>
                            </td>                        
                        </tr>
                    </c:forEach>     
            	</tbody>
            </table>
            <script type="text/javascript">
                createDataTables('legalMappingEntryKeys',true /*filterable*/, false /*show tools*/, true /*paging*/, "${pageContext.request.contextPath}","${datatablesI18NUrl}");
            </script>
        </c:when>
        <c:otherwise>
            <div class="panel panel-body">
                <div class="alert alert-warning" role="alert">
                    <p>
                        <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
                        <spring:message code="label.noResultsFound" />
                    </p>
                </div>
            </div>    
        </c:otherwise>
    </c:choose>  
    
</form>


<script>
	$(document).ready(function() {
	});
</script>
