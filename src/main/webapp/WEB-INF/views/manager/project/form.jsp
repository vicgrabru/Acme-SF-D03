<%--
- form.jsp
-
- Copyright (C) 2012-2024 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="manager.project.form.label.code" path="code" readonly="true" />
	<acme:input-textbox code="manager.project.form.label.title" path="title" readonly="true" />
	<acme:input-textarea code="manager.project.form.label.abstract-field" path="abstractField" readonly="true" />
	<acme:input-textarea code="manager.project.form.label.has-fatal-errors" path="hasFatalErrors" readonly="true" />
	<acme:input-textarea code="manager.project.form.label.cost" path="cost" readonly="true" />
	<acme:input-textarea code="manager.project.form.label.optional-link" path="optionalLink" readonly="true" />
	<acme:input-textarea code="manager.project.form.label.draft-mode" path="draftMode" readonly="true" />
	
	
	<acme:submit
		test="${acme:anyOf(_command, 'show|update') && draftMode}"
		code="manager.project.form.button.update"
		action="/manager/project/update" />
</acme:form>


