<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<table class="zenith">
					<tr>
						<td class="zenith">
							<div>
								<table class="zenith">
									<tr>
										<td class="xslt_fieldHeader" colspan="2">ActivityLog Details</td>
									</tr>
								</table>
							</div>
						</td>
					</tr>
					<tr>
						<td class="zenith">
							<table class="zenith">
								<tr>
									<td class="zenith" style="width:30% ;vertical-align: top;" >
										<table class="zenith">
											<tr>
												<td class="xslt_fieldHeading">User Name :</td>
												<td class="xslt_fieldData" id="m_strLoginUserName">
													<xsl:value-of select="ActivityLog/m_strLoginUserName"/>
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Function Name :</td>
												<td class="xslt_fieldData" id="m_strTaskPerformed">
													<xsl:value-of select="ActivityLog/m_strTaskPerformed"/>
												</td>
											</tr>
											<tr>
												<td class="xslt_fieldHeading">Date :</td>
												<td class="xslt_fieldData" id="m_dDate">
													<xsl:value-of select="ActivityLog/m_dDate"/>
												</td>
											</tr>
										</table>
									</td>
									<td class="zenith" style="vertical-align: top; width:55%;">
										
									</td>
								</tr>
							</table>
						</td>
						<td class="zenith">
							<table class="zenith">
								<tr>
									<td class="zenith" style="width:30% ;vertical-align: top;">
										<table class="zenith">
											<tr>
												<td class="xslt_fieldHeading">Parameters</td>
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