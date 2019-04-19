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
										<td class="xslt_fieldHeader" colspan="2">Property
											Details</td>
										<td class="trademust" align="right" colspan="2">
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="trademust">
							<table class="trademust">
								<tr>
									<td class="trademust" style="width:30%;vertical-align:top;">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading">Client Name :</td>
												<td class="xslt_fieldData" id="m_strCompanyName">
													<xsl:value-of select="PropertyData/m_oClientData/ClientDatam_strCompanyName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Address :</td>
												<td class="xslt_fieldData" id="m_strAddress">
													<xsl:value-of select="PropertyData/m_strAddress" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Locality :</td>
												<td class="xslt_fieldData" id="m_strDate">
													<xsl:value-of select="PropertyData/m_strLocality" />
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td class="trademust" align="left" style="vertical-align:top;width:70%">
										<div id="div_dataGrid">
											<table id="propertyDetails_table_propertyDetailsDG" class="easyui-datagrid"
												style="height:200px;" title="List Of Propertys "
												data-options="striped:true, pagesize:10, rownumbers:true, fitColumns:true, showFooter:true, singleSelect:true">
											</table>
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>