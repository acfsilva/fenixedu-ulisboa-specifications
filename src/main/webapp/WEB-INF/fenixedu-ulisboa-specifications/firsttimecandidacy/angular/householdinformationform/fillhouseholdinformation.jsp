<%@page import="org.fenixedu.academic.domain.Country"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt"%>
<%@ taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

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

<%-- TITLE --%>
<div class="page-header">
	<h1><spring:message code="label.firstTimeCandidacy.fillHouseHoldInformation" />
		<small></small>
	</h1>
</div>

<%-- NAVIGATION --%>
<div class="well well-sm" style="display:inline-block">
    <span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>&nbsp;<a class="" href="${pageContext.request.contextPath}${controllerURL}/back"><spring:message code="label.back"/></a> 
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

<script>
angular.module('angularApp', ['ngSanitize', 'ui.select', 'bennuToolkit']).controller('angularController', ['$scope', function($scope) {

    $scope.object= ${householdInformationFormJson};
    $scope.postBack = createAngularPostbackFunction($scope);
    $scope.isUISelectLoading = {};
    $scope.getUISelectLoading = function() {
	    if($scope.isUISelectLoading == undefined) {
    		$scope.isUISelectLoading = {};		
	    }
	    return $scope.isUISelectLoading;
    };
    
    $scope.booleanvalues = [
                            { name : '<spring:message code="label.no"/>', value : false },
                            { name : '<spring:message code="label.yes"/>', value : true } 
                    ];
    $scope.onGrantOwnerProviderRefresh = function(grantOwnerProvider, namePart, model) {
	    if(namePart.length <= 3 || namePart === $scope.object.grantOwnerProviderNamePart) {
		    return;
	    }
	    
	    if($scope.getUISelectLoading()['grantOwnerProvider'] == undefined) {
	        angular.extend($scope.getUISelectLoading(),{'grantOwnerProvider' : true});
	    }
	    $scope.isUISelectLoading.grantOwnerProvider = true;
	    
	    $scope.object.grantOwnerProviderNamePart = namePart;
	    $scope.object.otherGrantOwnerProvider = namePart;
        $scope.$apply();  
        $scope.transformData();
        $scope.postBack(model);
    };
    $scope.oldGrantOwnerTypeValue = $scope.object.grantOwnerType;
    $scope.onGrantOwnerTypeChange = function(grantOwnerType, model) {
	    if($scope.oldGrantOwnerTypeValue !== grantOwnerType.id) {
 	        $scope.oldGrantOwnerTypeValue = grantOwnerType.id;
 	        $scope.grantOwnerProviderValues = undefined;
 	        $scope.object.grantOwnerProvider = undefined;
	    }
    };
    $scope.transformData = function () {
	    var index = -1;
	    angular.forEach($scope.object.grantOwnerProviderValues, function(value, key) {
		    if(value.id === value.text && $scope.object.grantOwnerProvider == value.id) {
			    $scope.object.otherGrantOwnerProvider = value.id;
			    $scope.object.grantOwnerProvider = undefined;
			    index = key;
		    }
	    }, index);
	    if( index != -1) {
		    $scope.object.grantOwnerProviderValues.splice(index, 1);
	    }
	    $scope.$apply();
    };
    $scope.typpingMessage = "<spring:message code='label.startTyping'/>";
    
    $scope.submitForm = function() {
	    $scope.transformData();
        $('form').submit();
    };

    $scope.$watch('object.grantOwnerProviderValues', function() {
	    if($scope.object.grantOwnerProviderValues.length <= 1 && ($scope.object.grantOwnerType === 'OTHER_INSTITUTION_GRANT_OWNER' || $scope.object.grantOwnerType === 'ORIGIN_COUNTRY_GRANT_OWNER')) {
		    $scope.object.grantOwnerProviderValues.push(
			    {
				  'id': $scope.object.grantOwnerProviderNamePart, 
				  'text':$scope.object.grantOwnerProviderNamePart
				});
	    }
    });
}]);
</script>

<form name='form' method="post" class="form-horizontal" ng-app="angularApp" ng-controller="angularController"
     action="${pageContext.request.contextPath}${controllerURL}/${postAction}">

    <input type="hidden" name="postback"
        value='${pageContext.request.contextPath}${controllerURL}/fillPostback' />
        
    <input name="bean" type="hidden" value="{{ object }}" />
		
    <div class="panel panel-default">
        <div class="panel-body">
            <div class="form-group row">
                <label for="householdInformationForm_professionalCondition" class="col-sm-2 control-label required-field">
                    <spring:message code="label.HouseholdInformationForm.professionalCondition" />
                </label>

                <div class="col-sm-6">
                    <ui-select  id="householdInformationForm_professionalCondition" name="professionalCondition" ng-model="$parent.object.professionalCondition" theme="bootstrap">
                        <ui-select-match >{{$select.selected.text}}</ui-select-match> 
                        <ui-select-choices  repeat="professionalCondition.id as professionalCondition in object.professionalConditionValues | filter: {normalizedText : $select.search}">
                            <span ng-bind-html="professionalCondition.text"></span>
                        </ui-select-choices> 
                    </ui-select>
                </div>
            </div>
            <div class="form-group row">
                <label for="householdInformationForm_professionType" class="col-sm-2 control-label required-field">
                    <spring:message code="label.HouseholdInformationForm.professionType" />
                </label>

                <div class="col-sm-6">
                    <ui-select  id="householdInformationForm_professionType" name="professionType" ng-model="$parent.object.professionType" theme="bootstrap">
                        <ui-select-match >{{$select.selected.text}}</ui-select-match> 
                        <ui-select-choices  repeat="professionType.id as professionType in object.professionTypeValues | filter: {normalizedText : $select.search}">
                            <span ng-bind-html="professionType.text"></span>
                        </ui-select-choices> 
                    </ui-select>                
                </div>
            </div>          
            <div class="form-group row">
                <label for="householdInformationForm_profession" class="col-sm-2 control-label">
                    <spring:message code="label.HouseholdInformationForm.profession" />
                </label>

                <div class="col-sm-6">
                    <input id="householdInformationForm_profession" class="form-control"
                        type="text" ng-model="object.profession" name="profession"
                        value='<c:out value='${not empty param.profession ? param.profession : householdInformationForm.profession }'/>' />
                </div>
            </div>
            <div class="form-group row">
                <label for="householdInformationForm_professionTimeType" class="col-sm-2 control-label">
                    <spring:message code="label.HouseholdInformationForm.professionTimeType" />
                </label>

                <div class="col-sm-6">
                    <ui-select  id="householdInformationForm_professionTimeType" name="professionTimeType" ng-model="$parent.object.professionTimeType" theme="bootstrap">
                        <ui-select-match allow-clear="true" >{{$select.selected.text}}</ui-select-match> 
                        <ui-select-choices  repeat="professionTimeType.id as professionTimeType in object.professionTimeTypeValues | filter: {normalizedText : $select.search}">
                            <span ng-bind-html="professionTimeType.text"></span>
                        </ui-select-choices> 
                    </ui-select>                
                </div>
            </div>
        </div>
    </div>
    
    <div class="panel panel-default">
        <div class="panel-body">
            <div class="form-group row">
                <label for="householdInformationForm_grantOwnerType" class="col-sm-2 control-label required-field">
                    <spring:message code="label.HouseholdInformationForm.grantOwnerType" />
                </label>

                <div class="col-sm-6">
                    <ui-select  id="householdInformationForm_grantOwnerType" name="grantOwnerType" ng-model="$parent.object.grantOwnerType" on-select="onGrantOwnerTypeChange($item,$model)" theme="bootstrap">
                        <ui-select-match >{{$select.selected.text}}</ui-select-match> 
                        <ui-select-choices  repeat="grantOwnerType.id as grantOwnerType in object.grantOwnerTypeValues | filter: {normalizedText : $select.search}">
                            <span ng-bind-html="grantOwnerType.text"></span>
                        </ui-select-choices> 
                    </ui-select>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 control-label" ng-class="{'required-field' : object.grantOwnerType !== 'STUDENT_WITHOUT_SCHOLARSHIP' && object.grantOwnerType !== 'HIGHER_EDUCATION_SAS_GRANT_OWNER_CANDIDATE'}">
                    <spring:message
                        code="label.HouseholdInformationForm.grantOwnerProvider" />
                </label>

                <div class="col-sm-6">
                    <ui-select reset-search-input="false" id="householdInformationForm_grantOwnerProvider" name="grantOwnerProvider" ng-model="$parent.object.grantOwnerProvider" ng-disabled="object.grantOwnerType === 'STUDENT_WITHOUT_SCHOLARSHIP'" theme="bootstrap">
                        <ui-select-match placeholder="{{typpingMessage}}">{{$select.selected.text}}</ui-select-match> 
                        <ui-select-choices  repeat="grantOwnerProvider.id as grantOwnerProvider in object.grantOwnerProviderValues"
                                            refresh="onGrantOwnerProviderRefresh($item, $select.search, $model)"
                                            refresh-delay="0">
                            <span ng-bind-html="grantOwnerProvider.text"></span>
                        </ui-select-choices> 
                    </ui-select>
                </div>
                <div class="col-sm-1">
                    <i class="fa fa-spinner fa-spin" aria-hidden="true" ng-show="isUISelectLoading.grantOwnerProvider"></i>
                </div>                    
            </div>
        </div>
    </div>
    
    <div class="panel panel-default">
        <div class="panel-body">
            
            <div class="form-group row">
                <div class="col-sm-2 control-label required-field">
                    <spring:message
                        code="label.HouseholdInformationForm.motherSchoolLevel" />
                </div>

                <div class="col-sm-6">
                    <ui-select  id="householdInformationForm_motherSchoolLevel" name="motherSchoolLevel" ng-model="$parent.object.motherSchoolLevel" theme="bootstrap">
                        <ui-select-match >{{$select.selected.text}}</ui-select-match> 
                        <ui-select-choices  repeat="motherSchoolLevel.id as motherSchoolLevel in object.schoolLevelValues | filter: {normalizedText : $select.search}">
                            <span ng-bind-html="motherSchoolLevel.text"></span>
                        </ui-select-choices> 
                    </ui-select> 
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label required-field">
                    <spring:message
                        code="label.HouseholdInformationForm.motherProfessionType" />
                </div>

                <div class="col-sm-6">
                    <ui-select  id="householdInformationForm_motherProfessionType" name="motherProfessionType" ng-model="$parent.object.motherProfessionType" theme="bootstrap">
                        <ui-select-match >{{$select.selected.text}}</ui-select-match> 
                        <ui-select-choices  repeat="motherProfessionType.id as motherProfessionType in object.professionTypeValues | filter: {normalizedText : $select.search}">
                            <span ng-bind-html="motherProfessionType.text"></span>
                        </ui-select-choices> 
                    </ui-select> 
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label required-field">
                    <spring:message
                        code="label.HouseholdInformationForm.motherProfessionalCondition" />
                </div>

                <div class="col-sm-6">
                    <ui-select  id="householdInformationForm_motherProfessionalCondition" name="motherProfessionalCondition" ng-model="$parent.object.motherProfessionalCondition" theme="bootstrap">
                        <ui-select-match >{{$select.selected.text}}</ui-select-match> 
                        <ui-select-choices  repeat="motherProfessionalCondition.id as motherProfessionalCondition in object.professionalConditionValues | filter: {normalizedText : $select.search}">
                            <span ng-bind-html="motherProfessionalCondition.text"></span>
                        </ui-select-choices> 
                    </ui-select> 
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label required-field">
                    <spring:message
                        code="label.HouseholdInformationForm.fatherSchoolLevel" />
                </div>

                <div class="col-sm-6">
                    <ui-select  id="householdInformationForm_fatherSchoolLevel" name="fatherSchoolLevel" ng-model="$parent.object.fatherSchoolLevel" theme="bootstrap">
                        <ui-select-match >{{$select.selected.text}}</ui-select-match> 
                        <ui-select-choices  repeat="fatherSchoolLevel.id as fatherSchoolLevel in object.schoolLevelValues | filter: {normalizedText : $select.search}">
                            <span ng-bind-html="fatherSchoolLevel.text"></span>
                        </ui-select-choices> 
                    </ui-select> 
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label required-field">
                    <spring:message
                        code="label.HouseholdInformationForm.fatherProfessionType" />
                </div>

                <div class="col-sm-6">
                    <ui-select  id="householdInformationForm_fatherProfessionType" name="fatherProfessionType" ng-model="$parent.object.fatherProfessionType" theme="bootstrap">
                        <ui-select-match >{{$select.selected.text}}</ui-select-match> 
                        <ui-select-choices  repeat="fatherProfessionType.id as fatherProfessionType in object.professionTypeValues | filter: {normalizedText : $select.search}">
                            <span ng-bind-html="fatherProfessionType.text"></span>
                        </ui-select-choices> 
                    </ui-select> 
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label required-field">
                    <spring:message
                        code="label.HouseholdInformationForm.fatherProfessionalCondition" />
                </div>

                <div class="col-sm-6">
                    <ui-select  id="householdInformationForm_fatherProfessionalCondition" name="fatherProfessionalCondition" ng-model="$parent.object.fatherProfessionalCondition" theme="bootstrap">
                        <ui-select-match >{{$select.selected.text}}</ui-select-match> 
                        <ui-select-choices  repeat="fatherProfessionalCondition.id as fatherProfessionalCondition in object.professionalConditionValues | filter: {normalizedText : $select.search}">
                            <span ng-bind-html="fatherProfessionalCondition.text"></span>
                        </ui-select-choices> 
                    </ui-select>                 
                </div>
            </div>
            <div class="form-group row">
                <div class="col-sm-2 control-label required-field">
                    <spring:message code="label.HouseholdInformationForm.householdSalarySpan" />
                </div>

                <div class="col-sm-6">
                    <ui-select  id="householdInformationForm_householdSalarySpan" name="householdSalarySpan" ng-model="$parent.object.householdSalarySpan" theme="bootstrap">
                        <ui-select-match >{{$select.selected.text}}</ui-select-match> 
                        <ui-select-choices  repeat="householdSalarySpan.id as householdSalarySpan in object.salarySpanValues | filter: {normalizedText : $select.search}">
                            <span ng-bind-html="householdSalarySpan.text"></span>
                        </ui-select-choices> 
                    </ui-select> 
                </div>
            </div>
        </div>
    </div>

    <div class="panel panel-default">
		<div class="panel-footer">
            <button type="button" class="btn btn-primary" role="button" ng-click="submitForm()"><spring:message code="label.submit" /></button>
		</div>
    </div>
</form>

<style>
	.required-field:after {
		content: '*';
		color: #e06565;
		font-weight: 900;
		margin-left: 2px;
		font-size: 14px;
		display: inline;
	}
</style>

<script>
    $(document).ready(function() {
        
    });
</script>

