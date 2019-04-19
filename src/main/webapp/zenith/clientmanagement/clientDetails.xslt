<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:variable name="m_nClientId" select="ClientDetails/ClientData/m_nClientId" />
	<xsl:template match="/">
		<html>
			<body>
				<table width="100%">
					<tr>
						<td class="trademust">
							<div>
								<table class="trademust">
									<tr>
										<td class="xslt_fieldHeader" colspan="2">Client Details
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
									<td width="35%" valign="top">
										<table width="100%" cellspacing="5px">
											<tr>
												<td class="xslt_fieldHeading" width="120px">
													Client Name :
												</td>
												<td class="xslt_fieldData" id="m_strCompanyName">
													<xsl:value-of select="ClientDetails/ClientData/m_strCompanyName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Address :
												</td>
												<td class="xslt_fieldData" id="m_strAddress">
													<xsl:value-of select="ClientDetails/ClientData/m_strAddress" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													City :
												</td>
												<td class="xslt_fieldData" id="m_strCity">
													<xsl:value-of select="ClientDetails/ClientData/m_strCity" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Pin Code :
												</td>
												<td class="xslt_fieldData" id="m_strPinCode">
													<xsl:value-of select="ClientDetails/ClientData/m_strPinCode" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Landline No :
												</td>
												<td class="xslt_fieldData" id="m_strTelephone">
													<xsl:value-of select="ClientDetails/ClientData/m_strTelephone" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Mobile No :
												</td>
												<td class="xslt_fieldData" id="m_strMobileNumber">
													<xsl:value-of select="ClientDetails/ClientData/m_strMobileNumber" />
												</td>
											</tr>
										</table>
									</td>
									<td width="35%" valign="top">
										<table width="100%" cellspacing="5px">
											<tr>
												<td class="xslt_fieldHeading" width="120px">
													E-mail :
												</td>
												<td class="xslt_fieldData" id="m_strEmail">
													<xsl:value-of select="ClientDetails/ClientData/m_strEmail" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Web URL :
												</td>
												<td class="xslt_fieldData" id="m_strWebAddress">
													<xsl:value-of select="ClientDetails/ClientData/m_strWebAddress" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Business Type :
												</td>
												<td class="xslt_fieldData" id="m_oDemography">
													<xsl:value-of
														select="ClientDetails/ClientData/m_oDemography/DemographyData/m_oBusinessType/BusinessTypeData/m_strBusinessName" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													TIN Number :
												</td>
												<td class="xslt_fieldData" id="m_strTinNumber">
													<xsl:value-of select="ClientDetails/ClientData/m_strTinNumber" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													VAT Number :
												</td>
												<td class="xslt_fieldData" id="m_strVatNumber">
													<xsl:value-of select="ClientDetails/ClientData/m_strVatNumber" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													CST Number :
												</td>
												<td class="xslt_fieldData" id="m_strCSTNumber">
													<xsl:value-of select="ClientDetails/ClientData/m_strCSTNumber" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Client Locked :
												</td>
												<td class="xslt_fieldData" id="m_strCSTNumber">
													<xsl:value-of select="ClientDetails/ClientData/m_bClientLock" />
												</td>
											</tr>
										</table>
									</td>
									<td width="30%" valign="top">
										<table width="100%" cellspacing="5px">
											<tr>
												<td class="xslt_fieldHeading">
													Outstation Client :
												</td>
												<td class="xslt_fieldData" id="OutstationClient">
													<xsl:value-of select="ClientDetails/ClientData/m_bOutstationClient" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading" width="120px">
													Opening Balance :
												</td>
												<td class="xslt_fieldData" id="nTotalBusiness">
													<span class="rupeeSign">R  </span><xsl:value-of select="format-number(ClientDetails/ClientData/m_nOpeningBalance,'##,##,##,##0.00')" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading" width="120px">
													Total Business :
												</td>
												<td class="xslt_fieldData" id="nTotalBusiness">
													<span class="rupeeSign">R  </span><xsl:value-of select="format-number(ClientDetails/BusinessDetails/nTotalBusiness,'##,##,##,##0.00')" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Total Received :
												</td>
												<td class="xslt_fieldData" id="nTotalReceived">
													<span class="rupeeSign">R  </span><xsl:value-of select="format-number(ClientDetails/BusinessDetails/nTotalReceived,'##,##,##,##0.00')" />
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">
													Total Outstanding :
												</td>
												<td class="xslt_fieldData" id="nOutstandingAmount">
													<xsl:variable name="nOutstandingAmount" select="ClientDetails/ClientData/m_nOpeningBalance + ClientDetails/BusinessDetails/nTotalBusiness - ClientDetails/BusinessDetails/nTotalReceived" />
													<xsl:choose>
											          <xsl:when test="$nOutstandingAmount &gt; 0">
											           <span class="rupeeSign">R  </span><span style="color:red"><xsl:value-of select="format-number($nOutstandingAmount,'##,##,##,##0.00')" /></span>
											          </xsl:when>
											          <xsl:otherwise>
											            <span class="rupeeSign">R  </span><span style="color:green"><xsl:value-of select="format-number($nOutstandingAmount,'##,##,##,##0.00')" /></span>
											          </xsl:otherwise>
											        </xsl:choose>
												</td>
											</tr>
										</table>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td class="trademust" colspan="2" style="width:100%">
							<table class="trademust">
								<tr> 
									<td class="trademust"  style="width:60%">
										<table id="clientDetails_table_clientContactDetailsDG"
											class="easyui-datagrid" style="height:200px;" title="Contact Details"
											data-options="striped:true, pagesize:10, rownumbers:true, fitColumns:true, showFooter:true, singleSelect:true">
										</table>
									</td>
									<td class="trademust" style="width:40%">
										<table id="clientDetails_table_clientSiteDetailsDG" class="easyui-datagrid"
											style="height:200px;" title="Site Details"
											data-options="striped:true, pagesize:10, rownumbers:true, fitColumns:true, showFooter:true, singleSelect:true">
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
