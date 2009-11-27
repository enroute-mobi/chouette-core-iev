<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%-- Titre et barre de navigation --%>	
<title><s:text name="text.itl.list.title" /></title>
<s:url id="urlITLs" action="list" namespace="/itl" includeParams="none"/>
<s:property value="filAriane.addElementFilAriane(getText('text.itl.list.title'), '', #urlITLs)"/>
<div class="panelData">
  <s:property value="filAriane.texteFilAriane" escape="false"/>
</div>
<br>
<div class="actions">
  <s:url action="add" namespace="/itl" id="addURL"/>
  <s:a href="%{addURL}"><b><s:text name="text.itl.create.button"/></b></s:a>
</div>

<div class="panel" id="displaytag"> 
  <display:table name="itls" pagesize="20" requestURI="" id="itl" export="false">
    <display:column title="Action" sortable="false">
      <s:url id="removeUrl" action="delete" namespace="/itl">
        <s:param name="idItl">${itl.id}</s:param>
      </s:url>
      <s:url id="editUrl" action="edit" namespace="/itl">
        <s:param name="idItl">${itl.id}</s:param>
      </s:url>
      <s:a href="%{editUrl}">
        <img border="0" alt="Edit" src="/images/editer.png" title="<s:text name="tooltip.edit"/>">
      </s:a>&nbsp;&nbsp;
      <s:a href="%{removeUrl}" onclick="return confirm('%{getText('itl.delete.confirmation')}');">
        <img border="0" alt="Delete" src="/images/supprimer.png" title="<s:text name="tooltip.delete"/>">
      </s:a>
    </display:column>
    <display:column title="Nom" property="nom" sortable="false"/>
  </display:table>
</div>
