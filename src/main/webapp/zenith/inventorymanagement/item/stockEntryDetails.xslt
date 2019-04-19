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
										<td class="xslt_fieldHeader">Item Details</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="trademust">
							<div id="stockEntryDetails_div_itemData" class="detailsScroll">
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
													<td class="xslt_fieldHeading">Created By :</td>
													<td class="xslt_fieldData" id="m_strUserName">
														<xsl:value-of select="ItemData/m_strUserName" />
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
													<td class="xslt_fieldHeading">Selling Price :</td>
													<td class="xslt_fieldData" id="m_nSellingPrice">
														<xsl:value-of select="ItemData/m_nSellingPrice" />
													</td>
												</tr>
												<tr>
													<td class="xslt_fieldHeading">Opening Stock :</td>
													<td class="xslt_fieldData" id="m_nOpeningStock">
														<xsl:value-of select="ItemData/m_nOpeningStock" />
													</td>
												</tr>
												<tr>
													<td class="xslt_fieldHeading">Reorder Level :</td>
													<td class="xslt_fieldData" id="m_nReorderLevel">
														<xsl:value-of select="ItemData/m_nReorderLevel" />
													</td>
												</tr>
												<tr>
													<td class="xslt_fieldHeading">ApplicableTax :</td>
													<td class="xslt_fieldData" id="m_strApplicableTaxName">
														<xsl:value-of select="ItemData/m_strApplicableTaxName" />
													</td>
												</tr>
											</table>
										</td>
										<td class="trademust" align="left" style="vertical-align:top;width:50%">
											<div id="div_dataGrid">
												<table id="stockEntryDetails_table_stockEntryDetailsDG" class="easyui-datagrid"
													style="height:200px;" title="Last 10 Transactions"
													data-options="striped:true, pagesize:10, rownumbers:true, fitColumns:true, singleSelect:true">
												</table>
											</div>
										</td>
										<td class="trademust" style="vertical-align: top; width:20%;">
											<table class="trademust">
												<tr>
													<td class="xslt_fieldHeading" style="text-align:left">Item Image :
													</td>
												</tr>
												<tr>
													<td class="trademust">
														<img id="stockEntryDetails_img_itemImage" height="100"
															width="100px" />
														<br />
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>