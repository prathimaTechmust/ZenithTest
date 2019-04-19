var template_includeDataObjects = 
[
	'widgets/crmanagement/template/TemplateData.js',
	'widgets/crmanagement/template/TemplateCategoryData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js'
];

includeDataObjects (template_includeDataObjects, "template_loaded()");

function template_memberData ()
{
	this.m_nTemplateId = -1;
	this.m_oBufferTemplateData = null;
	this.m_strTemplateFile = "" ;
}

var m_oTemplateMemberData = new template_memberData ();

function template_new ()
{
	template_init ();
}

function template_init ()
{
	createPopup("dialog", "#template_button_create", "#template_button_cancel", true);
	initFormValidateBoxes ("template_form_id");
	template_populateCategories ();
}

function template_populateCategories ()
{
	var oTemplateCategoryData = new TemplateCategoryData ();
	TemplateCategoryDataProcessor.list (oTemplateCategoryData, "m_strCategoryName", "asc", 1, 10, template_gotTemplate);
}

function template_gotTemplate (oResponse)
{
	template_prepareCategoryNameDD ("template_select_category", oResponse);
}

function template_prepareCategoryNameDD (strCategoryNameDD,oResponse)
{
	var arrOptions = new Array ();
	for (var nIndex = 0; nIndex < oResponse.m_arrTemplateCategories.length; nIndex++)
		arrOptions.push (CreateOption (oResponse.m_arrTemplateCategories [nIndex].m_nCategoryId,
				oResponse.m_arrTemplateCategories [nIndex].m_strCategoryName));
	PopulateDD (strCategoryNameDD, arrOptions);
}

function template_edit ()
{
	template_init();
	document.getElementById("template_button_create").setAttribute('update', true);
	document.getElementById("template_button_create").innerHTML = "Update";
	var oTemplateData = new TemplateData ();
	oTemplateData.m_nTemplateId = m_oTemplateMemberData.m_nTemplateId;
	TemplateDataProcessor.get(oTemplateData,template_gotData);
}

function template_gotData (oResponse)
{	
	var oTemplate = oResponse.m_arrTemplates[0];
	$("#template_input_templateName").val(oTemplate.m_strTemplateName);
	m_oTemplateMemberData.m_strTemplateName =  oTemplate.m_strTemplateName;
	document.getElementById("template_td_templateName").innerHTML =oTemplate.m_strTemplateFileName;
	$("#template_select_category").val(oTemplate.m_oCategoryData.m_strCategoryName);
	initFormValidateBoxes ("template_form_id");
}

function template_submit ()
{
	if (template_validate())
	{
		if(!template_validateTemplateName ())
		{
			oTemplateData = template_getFormData ();
			var oFile = $("#template_input_templateFile").val();
			if(document.getElementById("template_button_create").getAttribute('update') == "false")
				TemplateDataProcessor.createTemplate (oTemplateData, oFile, template_created);
			else
			{
				oTemplateData.m_nTemplateId = m_oTemplateMemberData.m_nTemplateId;
				TemplateDataProcessor.updateTemplate(oTemplateData, oFile,template_updated);
			}
		}
	}
}

function template_validate ()
{
	return validateForm("template_form_id")&& template_validateTemplateFile () ;
}

function template_getFormData()
{
	var oTemplateData = new TemplateData();
	oTemplateData.m_oCategoryData.m_nCategoryId = $("#template_select_category").val();
	oTemplateData.m_strTemplateName = $("#template_input_templateName").val();
	oTemplateData.m_strTemplateFileName = $("#template_input_templateFile").val();
	oTemplateData.m_strTemplateFileName = getImageName (oTemplateData.m_strTemplateFileName.value);
	oTemplateData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	return oTemplateData;
}

function template_updated (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Template updated successfully.", "kSuccess");
		HideDialog ("dialog");
		document.getElementById("templateList_div_listDetail").innerHTML = "";
		navigate("", "widgets/crmanagement/template/templateList.js")
	}
	
}

function template_created (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser("Template created successfully.", "kSuccess");
		HideDialog ("dialog");
		navigate("TemplateList", "widgets/crmanagement/template/templateList.js");
	}
	else
		informUser("Template creation failed.", "kError");
}

function template_cancel ()
{
	HideDialog ("dialog");
}

function template_getTemplateFileName ()
{
	var oTemplateFileName = $("#template_input_templateFile").val();
	document.getElementById("template_td_templateName").innerHTML = getImageName (oTemplateFileName.value);
}
		
function template_validateTemplateName ()
{
	var bIsTemplateNameExist = false;
	oTemplateData = new TemplateData ()
	oTemplateData.m_oUserCredentialsData = new UserInformationData ();
	oTemplateData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	TemplateDataProcessor.list(oTemplateData, "", "",
		function (oResponse)
		{		
			var strTemplateName = $("#template_input_templateName").val();
			if(m_oTemplateMemberData.m_strTemplateName != strTemplateName)
			{
				for(var nIndex=0; nIndex <oResponse.m_arrTemplates.length ; nIndex++)
				{
					if(strTemplateName == oResponse.m_arrTemplates[nIndex].m_strTemplateName)
					{
						informUser("Template name already exists!", "kWarning");
						$("#template_input_templateName").val("");
						$("#template_input_templateName").focus ();
						bIsTemplateNameExist = true;
						break;
					}
				}
			}
		}
	);
	return bIsTemplateNameExist;
}


function template_validateTemplateFile ()
{
	var bIsTemplateFileValid = true;
	if($("#template_td_templateName").val()== "")
	{
		informUser("Please Select Template File.", "kWarning");
		bIsTemplateFileValid = false;
	}
	return  bIsTemplateFileValid;
}


