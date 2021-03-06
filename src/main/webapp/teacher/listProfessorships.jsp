<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@page import="com.google.common.collect.Lists"%>
<%@page import="org.fenixedu.academic.domain.Degree"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.stream.Collectors"%>
<%@page import="org.fenixedu.ulisboa.specifications.domain.services.ExecutionCourseServices"%>
<%@page import="java.util.List"%>
<%@page import="org.fenixedu.academic.domain.ExecutionCourse"%>
<%@page import="java.util.Collections"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2><bean:message key="label.professorships"/></h2>


<table class="tstyle5 thmiddle mtop1 mbottom1">
	<tr>
		<td nowrap="nowrap">
			<bean:message key="property.executionPeriod"/>:
    	</td>
		<td nowrap="nowrap">
			<fr:form action="/showProfessorships.do">
				<select name="executionPeriodID" onchange="this.form.submit();">
					<option value=""><bean:message key="option.all.execution.periods"></bean:message></option>
					<c:forEach items="${semesters}" var="semester">
						<option value="${semester.externalId}" ${semester == executionPeriod ? 'selected' : ''}><c:out value="${semester.qualifiedName}" /></option>
					</c:forEach>
				</select>
			</fr:form>
    	</td>
    </tr>
</table>

<logic:empty name="executionCourses">
	<p><em><bean:message key="label.noProfessorships"/></em></p>
</logic:empty>


<logic:notEmpty name="executionCourses">
	<p class="mbottom05"><bean:message key="label.choose.course.to.administrate"/>:</p>
	<table class="tstyle4 mtop05">
		<tr>
			<th><bean:message key="label.semestre"/></th>
			<th><bean:message key="label.executionCourseManagement.menu.view.courseAndPage"/></th>
			<th><bean:message key="label.teachersInformation.associatedLecturingCourses.degrees"/></th>
		</tr>
        
    <% // qubExtension
    final List<ExecutionCourse> executionCourses = (List<ExecutionCourse>) request.getAttribute("executionCourses");
    Collections.sort(executionCourses, ExecutionCourse.EXECUTION_COURSE_COMPARATOR_BY_EXECUTION_PERIOD_AND_NAME.reversed());
    %>
	<logic:iterate id="executionCourse" name="executionCourses" type="org.fenixedu.academic.domain.ExecutionCourse">
			<tr>
				<td style="width: 150px;" class="acenter">
		            <span class="smalltxt">
						<bean:write name="executionCourse" property="executionPeriod.qualifiedName"/>
					</span>
				</td>
				<td style="width: 450px;">
					<strong>
						<html:link page="/manageExecutionCourse.do?method=instructions&executionCourseID=${executionCourse.externalId}">
                            <%-- qubExtension --%>
                            <bean:define id="executionCourseCode">
                                <%=ExecutionCourseServices.getCode(executionCourse)%>
                            </bean:define>
							<c:out value="${executionCourseCode} - ${executionCourse.nome}" />
						</html:link>
					</strong>
					
		            <p class="mtop05 mbottom0">
			            <span class="smalltxt breakword color888" style="word-wrap: break-word !important;">
			            	<a href="${executionCourse.siteUrl}">${executionCourse.siteUrl}</a>
		                </span>
	                </p>
				</td>
				<td>
		            <span class="smalltxt">
                        <%
                        final List<Degree> degrees = Lists.newArrayList(executionCourse.getDegreesSortedByDegreeName());
                        Collections.sort(degrees, Degree.COMPARATOR_BY_DEGREE_TYPE_DEGREE_NAME_AND_ID.reversed());
                        
                        for (final Iterator<Degree> iterator = degrees.iterator(); iterator.hasNext();) {

                            out.append(iterator.next().getCode());

                            if (iterator.hasNext()) {
                                out.append(" ; ");
                            }
                        }
                        %>
					</span>				
				</td>
			</tr>

	</logic:iterate>
	</table>
</logic:notEmpty>



