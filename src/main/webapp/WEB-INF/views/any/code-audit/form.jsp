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

	<acme:input-textbox code="any.code-audit.form.label.code" path="code"/>	
	<acme:input-moment code="any.code-audit.form.label.execution-date" path="executionDate"/>
	<acme:input-textbox code="any.code-audit.form.label.type" path="type"/>
	<acme:input-textarea code="any.code-audit.form.label.corrective-actions" path="correctiveActions"/>
	<acme:input-textbox code="any.code-audit.form.label.mark" path="mark"/>
	<acme:input-url code="any.code-audit.form.label.link" path="link"/>
	<acme:input-textbox code="any.code-audit.form.label.project" path="project"/>
	
	<acme:button code="any.any.code-audit.form.button.project" action="/any/project/show?id=${projectId}"/>
	<acme:button code="any.any.code-audit.form.button.audit-record.list" action="/any/audit-record/list?codeAuditId=${codeAuditId}"/>
	
</acme:form>