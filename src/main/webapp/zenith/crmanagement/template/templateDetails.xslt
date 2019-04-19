<?xml version="1.0"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
		<html>
			<body>
				<table class="trademust">
					<tr>
						<td class="xslt_fieldHeader" >Template Details
						</td>
					</tr>
					<tr>
					<td class="trademust">
					<table class="trademust">
					<tr>
						<td class="xslt_fieldHeading">Template Name :</td>
						<td class="xslt_fieldData" id="m_strTemplateName">
							<xsl:value-of select="TemplateData/m_strTemplateName" />
						</td>
					</tr>
					<tr>
						<td class="xslt_fieldHeading">Template Category :</td>
						<td class="xslt_fieldData" id="m_strTemplateName">
							<xsl:value-of select="TemplateData/TemplateCategoryData/m_strCategoryName" />
						</td>
					</tr>
					<tr>
						<td class="xslt_fieldHeading">Template File :</td>
						<td class="xslt_fieldData" id="m_strTemplateFileName">
							<xsl:value-of select="TemplateData/m_strTemplateFileName" />
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