<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:variable name="m_nVendorId" select="VendorDetails/VendorData/m_nVendorId" />
  <xsl:template match="/">
    <html>
      <body>
		<table class="trademust">
			<tr>
				<td class="trademust">
					<div>
						<table class="trademust">
							<tr>
								<td class="xslt_fieldHeader" colspan="2">Vendor Details</td>
							</tr>
						</table>
					</div>
				</td>
			</tr>
			<tr> 
				<td  class="trademust">
					<table  class="trademust">
				 		<tr>
							<td width="35%" valign="top">
								<table class="trademust">
									<tr>
										<td class="xslt_fieldHeading" >
										Vendor Name :
										</td>
										<td class="xslt_fieldData" id="m_strCompanyName"><xsl:value-of select="VendorDetails/VendorData/m_strVendorCompanyName"/></td>
									</tr>
									<tr>
										<td class="xslt_fieldHeading">
										Address :
										</td>
										<td class="xslt_fieldData" id="m_strAddress"><xsl:value-of select="VendorDetails/VendorData/m_strVendorAddress"/></td>
									</tr>
									<tr>
										<td class="xslt_fieldHeading">
										City :
										</td>
										<td class="xslt_fieldData" id="m_strCity"><xsl:value-of select="VendorDetails/VendorData/m_strVendorCity"/></td>
									</tr>
									<tr>
										<td class="xslt_fieldHeading">
										Pin Code :
										</td>
										<td class="xslt_fieldData" id="m_strPinCode"><xsl:value-of select="VendorDetails/VendorData/m_strVendorPinCode"/></td>
									</tr>
									<tr>
										<td class="xslt_fieldHeading">
										Landline No :
										</td>
										<td class="xslt_fieldData" id="m_strTelephone"><xsl:value-of select="VendorDetails/VendorData/m_strVendorTelephone"/></td>
									</tr>
									<tr>
										<td class="xslt_fieldHeading">
										Mobile No :
										</td>
										<td class="xslt_fieldData" id="m_strMobileNumber"><xsl:value-of select="VendorDetails/VendorData/m_strVendorMobileNumber"/></td>
									</tr>
								</table>
							</td>
							<td width="35%" valign="top" text-align="left">
								<table class="trademust">
									<tr>
										<td class="xslt_fieldHeading">
										E-mail :
										</td>
										<td class="xslt_fieldData" id="m_strEmail"><xsl:value-of select="VendorDetails/VendorData/m_strVendorEmail"/></td>
									</tr>
									<tr>
										<td class="xslt_fieldHeading">
										Web URL :
										</td>
										<td class="xslt_fieldData" id="m_strWebAddress"><xsl:value-of select="VendorDetails/VendorData/m_strVendorWebAddress"/></td>
									</tr>
									<tr>
										<td class="xslt_fieldHeading">
										Business Type :
										</td>
										<td class="xslt_fieldData" id="m_oDemography"><xsl:value-of select="VendorDetails/VendorData/m_oDemography/DemographyData/m_oBusinessType/BusinessTypeData/m_strBusinessName"/></td>
									</tr>
									<tr>
										<td class="xslt_fieldHeading">
										TIN Number :
										</td>
										<td class="xslt_fieldData" id="m_strTinNumber"><xsl:value-of select="VendorDetails/VendorData/m_strVendorTinNumber"/></td>
									</tr>
									<tr>
										<td class="xslt_fieldHeading">
										VAT Number :
										</td>
										<td class="xslt_fieldData" id="m_strVatNumber"><xsl:value-of select="VendorDetails/VendorData/m_strVendorVatNumber"/></td>
									</tr>
									<tr>
										<td class="xslt_fieldHeading">
										CST Number :
										</td>
										<td class="xslt_fieldData" id="m_strCSTNumber"><xsl:value-of select="VendorDetails/VendorData/m_strVendorCSTNumber"/></td>
									</tr>
								</table>
							</td>
							<td width="30%" valign="top">
								<table width="100%" cellspacing="5px">
									<tr>
										<td class="xslt_fieldHeading" width="120px">
											Opening Balance :
										</td>
										<td class="xslt_fieldData" id="nTotalBusiness">
											<span class="rupeeSign">R  </span><xsl:value-of select="format-number(VendorDetails/VendorData/m_nVendorOpeningBalance,'##,##,##,##0.00')" />
										</td>
									</tr>
									<tr>
										<td class="xslt_fieldHeading" width="120px">
											Total Business :
										</td>
										<td class="xslt_fieldData" id="nTotalBusiness">
											<span class="rupeeSign">R  </span><xsl:value-of select="format-number(VendorDetails/BusinessDetails/nTotalBusiness,'##,##,##,##0.00')" />
										</td>
									</tr>
									<tr>
										<td class="xslt_fieldHeading">
											Total Paid :
										</td>
										<td class="xslt_fieldData" id="nTotalPaid">
											<span class="rupeeSign">R  </span><xsl:value-of select="format-number(VendorDetails/BusinessDetails/nTotalPaid,'##,##,##,##0.00')" />
										</td>
									</tr>
									<tr>
										<td class="xslt_fieldHeading">
											Total Outstanding :
										</td>
										<td class="xslt_fieldData" id="nOutstandingAmount">
											<xsl:variable name="nOutstandingAmount" select="VendorDetails/VendorData/m_nVendorOpeningBalance + VendorDetails/BusinessDetails/nTotalBusiness - VendorDetails/BusinessDetails/nTotalPaid" />
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
				<td class="trademust">
					<table id="vendorDetails_table_vendorDetailsDG" class="easyui-datagrid"
						style="height:200px;" title="Contact Details"
						data-options="striped:true, pagesize:10, rownumbers:true, fitColumns:true, showFooter:true, singleSelect:true">
					</table>
				</td>
			</tr>
		</table>
	 </body>
	</html>
</xsl:template>

</xsl:stylesheet>
