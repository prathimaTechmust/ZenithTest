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
										<td class="xslt_fieldHeader" colspan="2">Quotation
											Details</td>
										<td align="right" style="padding-right:1%">
											<table align="right">
												<tr>
													<td style="border:none">
														<img title="Edit" src="images/edit_database_24.png"
															width="20" id="quotationDetails_img_edit" style="visibility: hidden"
															align="center"
															onClick="quotationList_edit ({QuotationData/m_nQuotationId})" />
													</td>
													<td style="border:none">
														<img title="Add New Comment" src="images/WriteMessage.png"
															style="visibility: hidden" id="quotationDetails_img_createLog"
															width="20" align="center"
															onClick="quotationList_createLog ({QuotationData/m_nQuotationId})" />
													</td>
													<td style="border:none">
														<img title="Print" src="images/print.jpg" width="20"
															align="center" onClick="quotationList_print ()" />
													</td>
													<td style="border:none">
														<img title="Archive" src="images/Archive.png" width="20"
															align="center"
															onClick="quotationList_makeArchive ({QuotationData/m_nQuotationId})" />
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
						<td class="trademust">
							<table class="trademust">
								<tr>
									<td class="trademust" style="width:100%;vertical-align:top;text-align:left;">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading" style="width:25%;">To :</td>
												<td class="xslt_fieldData" id="m_strCompanyName" style="width:75%">
													<xsl:value-of select="QuotationData/ClientData/m_strCompanyName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading" style="width:25%;">Site :</td>
												<td class="xslt_fieldData" id="m_strSiteName" style="width:75%">
													<xsl:value-of select="QuotationData/SiteData/m_strSiteName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading" style="width:25%;">Date :</td>
												<td class="xslt_fieldData" id="m_strDate" style="width:75%">
													<xsl:value-of select="QuotationData/m_strDate" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading" style="width:25%;">Quotation No :</td>
												<td class="xslt_fieldData" id="m_strQuotationNumber" style="width:75%">
													<xsl:value-of select="QuotationData/m_strQuotationNumber" />
												</td>
											</tr>
										</table>
									</td>
									<td class="trademust" style="width:100%;vertical-align:top;text-align:left;">
										<table class="trademust">
											<tr>
												<td class="xslt_fieldHeading" style="width:30%;">ContactName :</td>
												<td class="xslt_fieldData" style="width:70%">
													<xsl:value-of select="QuotationData/ContactData/m_strContactName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading" style="width:30%;">Designation :</td>
												<td class="xslt_fieldData" style="width:70%">
													<xsl:value-of select="QuotationData/ContactData/m_strDesignation" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading" style="width:30%;">PhoneNumber :</td>
												<td class="xslt_fieldData" style="width:70%">
													<xsl:value-of select="QuotationData/ContactData/m_strPhoneNumber" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading" style="width:30%;">Email :</td>
												<td class="xslt_fieldData" style="width:70%">
													<xsl:value-of select="QuotationData/ContactData/m_strEmail" />
												</td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td class="trademust" colspan="2">
										<table class="trademust">
											<tr>
												<td class="trademust" align="left"
													style="vertical-align:top;width:60%">
													<div id="div_dataGrid">
														<table id="QuotationDetails_table_quotationDetailsDG"
															class="easyui-datagrid" style="height:200px;" title="List Of Articles"
															data-options="striped:true, rownumbers:true, fitColumns:true, showFooter:true, singleSelect:true">
														</table>
													</div>
												</td>
												<td class="trademust" align="right"
													style="vertical-align:top;width:40%;background:#fff;border: 1px solid gray;">
													<div align="left" class="formHeading">
														 Comment List 
														<hr />
													</div>
													<div id="quotationDetails_div_logDetails" style="height:140px;overflow:auto">
													</div>
													<a id="quotationDetails_div_loadMoreDetails" class="anchorTag"
														style="visibility: hidden" href="javascript:quotationDetails_loadMoreContent()">
														show more
									                  </a>
												</td>
											</tr>
											 <tr>
										          <td class="trademust" colspan="2">
										            <table 
										                id="QuotationDetails_table_itemsDG"
										                class="easyui-datagrid"
										                title="List Of Purchase Order"
										                data-options="striped:true, fitColumns:true, rownumbers:true, checkOnSelect:false"
										                style="height:200px;">
										            </table>
										          </td>
										        </tr>
										</table>
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