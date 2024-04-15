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
	<acme:input-textbox code="any.claim.form.label.code" path="code"/>
	<acme:input-textbox code="any.claim.form.label.instantiationMoment" path="instantiationMoment" readonly="true"/>
	<acme:input-textarea code="any.claim.form.label.heading" path="heading"/>
	<acme:input-textarea code="any.claim.form.label.description" path="description"/>
	<acme:input-textbox code="any.claim.form.label.department" path="department"/>
	<acme:input-email code="any.claim.form.label.email" path="email"/>
	<acme:input-url code="any.claim.form.label.link" path="link"/>
	
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command,'show|publish') && draftMode == true}">
			<acme:input-checkbox code="any.claim.form.confirmation" path="confirmation"/>
			<acme:submit code="any.claim.form.button.publish" action="/any/claim/publish"/>
		</jstl:when>
		
		<jstl:when test="${_command == 'create'}">
			<acme:input-checkbox code="any.claim.form.confirmation" path="confirmation"/>
			<acme:submit code="any.claim.form.button.create" action="/any/claim/create"/>
		</jstl:when>
	</jstl:choose>

</acme:form>	