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
										<td class="xslt_fieldHeader" colspan="2">Stock Transfer Memo Details</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="trademust">
							<table class="trademust">
								<tr>
									<td class="trademust" style="width:35%;vertical-align:top;">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading">Date :</td>
												<td class="xslt_fieldData" id="m_strDate">
													<xsl:value-of select="StockTransferMemoData/m_strDate" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading" style="vertical-align:top;">Transferred From :</td>
												<td class="xslt_fieldData" id="m_strCompanyName">
													<xsl:value-of select="StockTransferMemoData/TransferredFrom/LocationData/m_strName" /><br/>
													<xsl:value-of select="StockTransferMemoData/TransferredFrom/LocationData/m_strAddress" /><br/>
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading" style="vertical-align:top;">Transferred To :</td>
												<td class="xslt_fieldData" id="m_strModeName">
													<xsl:value-of select="StockTransferMemoData/TransferredTo/LocationData/m_strName" /><br/>
													<xsl:value-of select="StockTransferMemoData/TransferredTo/LocationData/m_strAddress" /><br/>
												</td>
											</tr>
										</table>
									</td>
									<td class="trademust" style="vertical-align: top; width:65%;">
										<div id="div_dataGrid">
											<table id="stockTransferMemoListDetails_table_memoListDetailsDG" class="easyui-datagrid"
												style="height:200px;" title="List of Articles"
												data-options="striped:true, pagesize:10, rownumbers:true, fitColumns:true, showFooter:true,singleSelect:true">
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