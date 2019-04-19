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
										Purchase Returned Item Details</td>
										<td class="trademust" align="right" colspan="2">
<!--											<table>
												<tr>
													<td>
														<img src="images/edit_database_24.png" width="20"
															title="Edit" align="center" id="editImageId"
															onClick="salesList_edit({SalesData/m_nId})" />
													</td>
													<td>
														<img src="images/delete.png" width="20" title="Delete"
															align="center" id="deleteImageId" onClick="salesList_listDetail_delete ()" />
													</td>
												</tr>
											</table>
-->											
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
									<td class="trademust" style="width:40%;vertical-align:top;">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading" style="width:20%">From :</td>
												<td class="xslt_fieldData" id="m_strCompanyName" style="width:20%">
													<xsl:value-of select="PurchaseReturnedData/VendorData/m_strVendorCompanyName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading" style="width:20%">Date :</td>
												<td class="xslt_fieldData" id="m_dDate" style="width:20%">
													<xsl:value-of select="PurchaseReturnedData/m_strDate" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading" style="width:20%">Credit Note Number :</td>
												<td class="xslt_fieldData" id="m_strCreditNoteNumber" style="width:20%">
													<xsl:value-of select="PurchaseReturnedData/m_strDebitNoteNumber" />
												</td>
											</tr>
										</table>
									</td>
									<td class="trademust" align="left" style="vertical-align:top;width:60%">
										<div id="div_dataGrid">
											<table id="purchaseReturnedItemDetails_table_itemDetailsDG"
												class="easyui-datagrid" style="height:200px;" title="List Of Articles"
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