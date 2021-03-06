<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<spring:url var="datatablesUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.latest.min.js"/>
<spring:url var="datatablesBootstrapJsUrl" value="/javaScript/dataTables/media/js/jquery.dataTables.bootstrap.min.js"></spring:url>
<script type="text/javascript" src="${datatablesUrl}"></script>
<script type="text/javascript" src="${datatablesBootstrapJsUrl}"></script>
<spring:url var="datatablesCssUrl" value="/CSS/dataTables/dataTables.bootstrap.min.css"/>

<link rel="stylesheet" href="${datatablesCssUrl}"/>
<spring:url var="datatablesI18NUrl" value="/javaScript/dataTables/media/i18n/${portal.locale.language}.json"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/dataTables/dataTables.bootstrap.min.css"/>

${portal.toolkit()}

<link href="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/css/dataTables.responsive.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/js/dataTables.responsive.js"></script>
<link href="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/css/dataTables.tableTools.css" rel="stylesheet"/>
<script src="${pageContext.request.contextPath}/webjars/datatables-tools/2.2.4/js/dataTables.tableTools.js"></script>
<link href="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/css/select2.min.css" rel="stylesheet" />
<script src="${pageContext.request.contextPath}/webjars/select2/4.0.0-rc.2/dist/js/select2.min.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/webjars/bootbox/4.4.0/bootbox.js" ></script>
<script src="${pageContext.request.contextPath}/static/fenixedu-ulisboa-specifications/js/omnis.js"></script>

<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.ManageRegistrationResearchArea.edit" />
		<small></small>
	</h1>
</div>

<c:if test="${not empty infoMessages}">
	<div class="alert alert-info" role="alert">
		
		<c:forEach items="${infoMessages}" var="message"> 
			<p> <span class="glyphicon glyphicon glyphicon-ok-sign" aria-hidden="true">&nbsp;</span>
						${message}
					</p>
		</c:forEach>
		
	</div>	
</c:if>
<c:if test="${not empty warningMessages}">
	<div class="alert alert-warning" role="alert">
		
		<c:forEach items="${warningMessages}" var="message"> 
			<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
						${message}
					</p>
		</c:forEach>
		
	</div>	
</c:if>
<c:if test="${not empty errorMessages}">
	<div class="alert alert-danger" role="alert">
		
		<c:forEach items="${errorMessages}" var="message"> 
			<p> <span class="glyphicon glyphicon-exclamation-sign" aria-hidden="true">&nbsp;</span>
						${message}
					</p>
		</c:forEach>
		
	</div>	
</c:if>

<div class="panel panel-primary">
	<div class="panel-heading">
		<h3 class="panel-title">
			<spring:message code="label.details" />
		</h3>
	</div>
	<div class="panel-body">
		<form method="post" class="form-horizontal">
			<table class="table">
				<tbody>
					<tr>
						<th scope="row" class="col-xs-3">
							<spring:message code="label.ManageRegistrationResearchArea.studentNumber" />
						</th>
						<td>
							<c:out value='${registration.student.number}' />
						</td>
					</tr>
					<tr>
						<th scope="row" class="col-xs-3">
							<spring:message code="label.ManageRegistrationResearchArea.studentName" />
						</th>
						<td>
							<c:out value='${registration.student.person.name}' />
						</td>
					</tr>
					<tr>
						<th scope="row" class="col-xs-3">
							<spring:message code="label.ManageRegistrationResearchArea.degree" />
						</th>
						<td>
							<c:out value='${registration.degree.presentationNameI18N.content}' />
						</td>
					</tr>
					<tr>
						<th scope="row" class="col-xs-3">
							<spring:message code="label.ManageRegistrationResearchArea.researchArea" />
						</th>
						<td>
							<c:out value='${registration.researchArea.name.content} [${registration.researchArea.code}]' />
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</div>

<form method="post" class="form-horizontal">
		

			<div class="form-group row">
				<label for="researchArea" class="col-sm-2 control-label required-field">
					<spring:message
						code="label.ManageRegistrationResearchArea.researchArea" />
				</label>

				<div class="col-sm-6">
					<select id="researchArea" class="form-control" name="researchArea">
							<option value=''></option>
						<c:forEach items="${researchAreaSet}" var="field">
							<option value='<c:out value='${field.externalId}'/>'>
								<c:out value='${field.name.content} [${field.code}]' />
							</option>
						</c:forEach>
					</select>
					<script>
					$(document).ready(function() {
						$("#researchArea").select2().select2('val', '<c:out value='${not empty param.researchArea ? param.researchArea.externalId : researchArea.externalId }'/>');
					});
					</script>
				</div>
			</div>
		
			<div class="">
				<input type="submit" class="btn btn-default" role="button"
					value="<spring:message code="label.submit" />" />
			</div>
</form>
