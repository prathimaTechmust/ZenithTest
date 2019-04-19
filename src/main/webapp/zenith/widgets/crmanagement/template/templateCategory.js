var templateCategory_includeDataObjects = 
[
	'widgets/crmanagement/template/TemplateData.js',
	'widgets/crmanagement/template/TemplateCategoryData.js'
];

includeDataObjects (templateCategory_includeDataObjects, "templateCategory_loaded()");

function templateCategory_memberData ()
{
	this.m_nCategoryId = -1;
	this.m_strCategoryName = "";
}

var m_oTemplateCategoryMemberData = new templateCategory_memberData ();

function templateCategory_new ()
{
	templateCategory_init ();
}

function templateCategory_init ()
{
	createPopup("dialog", "#templateCategory_button_create", "#templateCategory_button_cancel", true);
	initFormValidateBoxes ("templateCategory_form_id");
}

function templateCategory_edit ()
{
	templateCategory_init();
	document.getElementById("templateCategory_button_create").setAttribute('update', true);
	document.getElementById("templateCategory_button_create").innerHTML = "Update";
	var oTemplateCategoryData = new TemplateCategoryData ();
	oTemplateCategoryData.m_nCategoryId = m_oTemplateCategoryMemberData.m_nCategoryId;
	TemplateCategoryDataProcessor.get(oTemplateCategoryData,templateCategory_gotData);
}

function templateCategory_gotData (oResponse)
{	
	var oTemplateCategoryData = oResponse.m_arrTemplateCategories[0];
	m_oTemplateCategoryMemberData.m_strCategoryName =  oTemplateCategoryData.m_strCategoryName;
	$("#templateCategory_input_name").val(oTemplateCategoryData.m_strCategoryName);
	initFormValidateBoxes ("templateCategory_form_id");
}

function templateCategory_submit ()
{
	if (templateCategory_validate())
	{
		oTemplateCategoryData = templateCategory_getFormData ();
		if(document.getElementById("templateCategory_button_create").getAttribute('update') == "false")
			TemplateCategoryDataProcessor.create(oTemplateCategoryData, templateCategory_created);
		else
		{
			oTemplateCategoryData.m_nCategoryId = m_oTemplateCategoryMemberData.m_nCategoryId;
			TemplateCategoryDataProcessor.update(oTemplateCategoryData, templateCategory_updated);
		}
	}
}

function templateCategory_validate ()
{
	return validateForm("templateCategory_form_id");
}

function templateCategory_getFormData()
{
	var oTemplateCategoryData = new TemplateCategoryData ();
	oTemplateCategoryData.m_strCategoryName = $("#templateCategory_input_name").val();
	return oTemplateCategoryData;
}

function templateCategory_updated (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Template Category updated successfully.", "kSuccess");
		HideDialog ("dialog");
		document.getElementById("templateCategoryList_div_listDetail").innerHTML = "";
		navigate("TemplateCategoryList", "widgets/crmanagement/template/templateCategoryList.js")
	}
	
}

function templateCategory_created (oResponse)
{
	if (oResponse.m_bSuccess)
	{
		informUser("Template Category created successfully.", "kSuccess");
		HideDialog ("dialog");
		navigate("TemplateCategoryList", "widgets/crmanagement/template/templateCategoryList.js");
	}
	else
		informUser("Template Category creation failed.", "kError");
}

function templateCategory_cancel ()
{
	HideDialog ("dialog");
}

function templateCategory_checkCategory ()
{
	var oTemplateCategoryData = new TemplateCategoryData ();
	TemplateCategoryDataProcessor.list(oTemplateCategoryData, "", "", 1, 10, templateCategory_gotList);
}
		
function templateCategory_gotList(oResponse)
{
	var strName = $("#templateCategory_input_name").val();
	if(m_oTemplateCategoryMemberData.m_strCategoryName != strName)
		{
			for(var nIndex=0; nIndex <oResponse.m_arrTemplateCategories.length ; nIndex++)
			{
				if(strName == oResponse.m_arrTemplateCategories[nIndex].m_strCategoryName)
					{
						informUser ("Template Category Already Exist.", "kWarning");
						$("#templateCategory_input_name").val("");
						document.getElementById("templateCategory_input_name").focus();
					}
			}
		}
}



