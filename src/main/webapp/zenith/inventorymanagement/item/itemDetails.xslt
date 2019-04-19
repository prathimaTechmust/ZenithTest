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
										<td class="xslt_fieldHeader" colspan="2">Item Details</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="trademust">
							<table class="trademust">
								<tr>
									<td class="trademust" style="width:30%">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading">Article Number :</td>
												<td class="xslt_fieldData" id="m_strArticleNumber">
													<xsl:value-of select="ItemData/m_strArticleNumber" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">ItemName :</td>
												<td class="xslt_fieldData" id="m_strItemName">
													<xsl:value-of select="ItemData/m_strItemName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Brand :</td>
												<td class="xslt_fieldData" id="m_strBrand">
													<xsl:value-of select="ItemData/m_strBrand" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Detail :</td>
												<td class="xslt_fieldData" id="m_strDetail">
													<xsl:value-of select="ItemData/m_strDetail" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Unit :</td>
												<td class="xslt_fieldData" id="m_strUnit">
													<xsl:value-of select="ItemData/m_strUnit" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Location Code :</td>
												<td class="xslt_fieldData" id="m_strLocationCode">
													<xsl:value-of select="ItemData/m_strLocationCode" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Created By :</td>
												<td class="xslt_fieldData" id="m_strUserName">
													<xsl:value-of select="ItemData/m_strUserName" />
												</td>
											</tr>
											<tr>
												<td colspan="2">
													<table class="xsltDetailBorder trademust">
														<tr>
															<td class="xslt_fieldHeading xsltDetailBorder">Selling Price</td>
															<td class="xslt_fieldData xsltDetailBorder" id="m_nSellingPrice">
																<span class="rupeeSign">R </span>
																<xsl:value-of select="format-number(ItemData/m_nSellingPrice,'##,##,##,##0.00')" />
															</td>
														</tr>
														<tr>
															<td class="xslt_fieldHeading xsltDetailBorder">Current Stock</td>
															<td class="xslt_fieldData xsltDetailBorder" id="m_nOpeningStock">
																<xsl:value-of
																	select="format-number(ItemData/m_nOpeningStock + ItemData/m_nReceived - ItemData/m_nIssued,'##,##,##,##0.00')" />
															</td>
														</tr>
														<tr>
															<td class="xslt_fieldHeading xsltDetailBorder">Reorder Level</td>
															<td class="xslt_fieldData xsltDetailBorder" id="m_nReorderLevel">
																<xsl:value-of select="format-number(ItemData/m_nReorderLevel,'##,##,##,##0.00')" />
															</td>
														</tr>
														<tr>
															<td class="xslt_fieldHeading xsltDetailBorder">ApplicableTax </td>
															<td class="xslt_fieldData xsltDetailBorder" id="m_strApplicableTaxName">
																<xsl:value-of select="ItemData/m_oApplicableTax/Applicabletax/m_strApplicableTaxName" />
															</td>
														</tr>
														<tr>
															<td class="xslt_fieldHeading xsltDetailBorder">Tax With C Form </td>
															<td class="xslt_fieldData xsltDetailBorder" id="m_strTaxWithCForm">
																<xsl:value-of select="ItemData/m_oTaxWithCForm/Applicabletax/m_strApplicableTaxName" />
															</td>
														</tr>
													</table>
												</td>
											</tr>
										</table>
									</td>
									<td class="trademust" align="left" style="vertical-align:top;width:15%">
										<table class="trademust">
											<tr>
												<td class="trademust" align="center">
													<a href="javascript:itemList_setPreview ({ItemData/m_nItemId})">
														<img id="ItemListDetails_img_itemImage" class="itemDetailImage"/>
													</a>
													<br />
												</td>
											</tr>
										</table>
									</td>
									<td class="trademust" style="vertical-align: top; width:55%;">
										<div id="div_dataGrid">
											<table id="ItemListDetails_table_itemDetailsDG" class="easyui-datagrid"
												style="height:200px;" title="Last 5 Sales"
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