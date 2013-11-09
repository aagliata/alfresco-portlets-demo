<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<portlet:defineObjects/>

<div class="portlet-section-header">CMIS - Upload content</div>
<br/>
<div class="portlet-section-body">

<form action="<portlet:actionURL name="editFormAction"/>" method="POST">
	<table>
		<tr>
			<td><input class="portlet-form-button" type="submit" name="submit" value="Configure CMIS server"></td>
		</tr>
	</table>
</form>

<br />

<form action="<portlet:actionURL name="uploadAction"/>" method="POST"
	enctype="multipart/form-data">
	<table>
		<tr>
			<td><span class="portlet-form-label">Content file:</span></td>
			<td><input class="portlet-form-input-field"  type="file" name="binary"></td>
		</tr>
		<tr><td><br /><br /></td></tr>
		<tr>
			<td><input class="portlet-form-button"  type="submit" name="submit" value="Upload"></td>
			<td><input class="portlet-form-button"  type="reset" name="reset" title="Reset"></td>
		</tr>
		
	</table>
</form>
</div>