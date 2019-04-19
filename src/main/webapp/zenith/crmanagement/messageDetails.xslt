<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="trademust">
							<div>
								<table class="trademust">
									<tr>
										<td class="xslt_fieldHeader" colspan="2">
										Message Details
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="trademust" style="vertical-align:top; text-align: left" colspan="2">
							<table class="trademust">
								<tr>
									<td style="vertical-align:top; text-align: right; width:120px">Date :</td>
									<td class="xslt_fieldData" id="m_nId">
										<xsl:value-of select="EmailMessageData/m_strDate" />
									</td>
								</tr>
								<tr>
									<td style="vertical-align:top; text-align: right ;width:120px">Subject :</td>
									<td class="xslt_fieldData" id="m_strSubject">
										<xsl:value-of select="EmailMessageData/m_strSubject" />
									</td>
								</tr>
								<tr>
									<td style="vertical-align:top; text-align: right; width:120px">Content :</td>
									<td class="xslt_fieldData" id="m_strContent">
										<xsl:value-of select="EmailMessageData/m_strContent" />
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
					<td class="trademust">
					<table class="trademust"><tr>
						<td class="trademust" align="left" style="vertical-align:top;width:30%">
							<table id="messageDetails_table_messageDetailsDG" class="easyui-datagrid"
								style="height:200px" title="List Of Attachments"
								data-options="striped:true, rownumbers:true, fitColumns:true, singleSelect:true">
							</table>
						</td>
						<td class="trademust" align="left" style="vertical-align:top;width:70%">
							<table id="messageDetails_table_messageDetailsContactDG"
								class="easyui-datagrid" style="height:200px;" title="List Of Contacts"
								data-options="striped:true, rownumbers:true, fitColumns:true, singleSelect:true">
							</table>
						</td>
					</tr></table></td></tr>
					
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>