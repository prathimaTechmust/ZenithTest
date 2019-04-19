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
											Applicable Tax Details
										</td>
										<td class="trademust" align="right" colspan="2">
											<table>
												<tr>
													<td>

													</td>
													<td>

													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>

					<tr>
						<td class="trademust" style="vertical-align:top;">
							<table class="trademust">
								<tr>
									<td class="trademust" style="vertical-align:top;">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading">Applicable Tax Name :</td>
												<td class="xslt_fieldData" id="m_strApplicableTaxName">
													<xsl:value-of select="Applicabletax/m_strApplicableTaxName" />
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
						<td class="trademust" align="left" style="vertical-align:top;">
							<div id="div_dataGrid">
								<table id="applicableTaxDetails_table_applicableTaxDetailsDG"
									class="easyui-datagrid" style="height:200px;width:500px;"
									title="List Of Tax"
									data-options="striped:true, pagesize:10, rownumbers:true, showFooter:true, fitColumns:true, singleSelect:true">
								</table>
							</div>
						</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
					