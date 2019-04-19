var itemCategory_includeDataObjects = 
[
	'widgets/usermanagement/userinfo/UserInformationData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js'
];

includeDataObjects (itemCategory_includeDataObjects, "itemCategory_loaded()");

function itemCategory_memberData ()
{
	this.m_nCategoryId = -1;
	this.m_strCategoryName = "";
	this.m_strCurrentCategoryName ="";
}

var m_oItemCategoryMemberData = new itemCategory_memberData ();

function itemCategory_new ()
{
	createPopup("thirdDialog", "#itemCategory_button_submit", "#itemCategory_button_cancel", true);
	initFormValidateBoxes ("itemCategory_form_id");
}

function itemCategory_edit ()
{
	itemCategory_new ();
	document.getElementById("itemCategory_button_submit").setAttribute('update', true);
	document.getElementById("itemCategory_button_submit").innerHTML = "Update";
	var oItemCategoryData = new ItemCategoryData();
	oItemCategoryData.m_nCategoryId = m_oItemCategoryMemberData.m_nCategoryId;
	ItemCategoryDataProcessor.get(oItemCategoryData, itemCategory_gotData);
}

function itemCategory_gotData (oResponse)
{
	m_oItemCategoryMemberData.m_strCategoryName = oResponse.m_arrItemCategory [0].m_strCategoryName;
	$("#itemCategory_input_categoryName").val(oResponse.m_arrItemCategory [0].m_strCategoryName);
	initFormValidateBoxes ("itemCategory_form_id");
}

function itemCategory_submit ()
{
	if (itemCategory_validate () && !itemCategory_checkCategoryName ())
	{
		loadPage ("include/process.html", "ProcessDialog", "itemCategory_progressbarLoaded ()");
	}
}

function itemCategory_progressbarLoaded ()
{
	createPopup('ProcessDialog', '', '', true);
	var oItemCategoryData = itemCategory_getFormData ();
	if(document.getElementById("itemCategory_button_submit").getAttribute('update') == "false")
		ItemCategoryDataProcessor.create(oItemCategoryData, itemCategory_created);
	else
	{
		oItemCategoryData.m_nCategoryId = m_oItemCategoryMemberData.m_nCategoryId;
		ItemCategoryDataProcessor.update(oItemCategoryData, itemCategory_updated);
	}
}

function itemCategory_validate ()
{
	return validateForm ("itemCategory_form_id");
}

function itemCategory_getFormData ()
{
	var oItemCategoryData = new ItemCategoryData ();
	oItemCategoryData.m_strCategoryName = $("#itemCategory_input_categoryName").val();
	oItemCategoryData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	return oItemCategoryData;
}

function itemCategory_created (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		informUser ("Item Category Created Successfully", "kSuccess");
		HideDialog ("thirdDialog");
		navigate("itemCategoryList", "widgets/inventorymanagement/item/itemCategoryList.js");
	}
	else
		informUser ("Item Category Creation faild", "kError");
}

function itemCategory_updated (oResponse)
{
	HideDialog ("ProcessDialog");
	if (oResponse.m_bSuccess)
	{
		informUser ("Item Category Updated Successfully", "kSuccess");
		HideDialog ("thirdDialog");
		navigate("itemCategoryList", "widgets/inventorymanagement/item/itemCategoryList.js");
	}
	else
		informUser ("Item Category Updation faild", "kError");
}

function itemCategory_cancel ()
{
	HideDialog("thirdDialog");
}

function itemCategory_checkCategoryName ()
{
	var bIsCategoryExist = false;
	var strCurrentCategoryName = $("#itemCategory_input_categoryName").val();
	if(strCurrentCategoryName.toLowerCase() != m_oItemCategoryMemberData.m_strCategoryName.toLowerCase())
	{
		var oItemCategoryData = new ItemCategoryData ();
		oItemCategoryData.m_strCategoryName = strCurrentCategoryName;
		ItemCategoryDataProcessor.list (oItemCategoryData, "" ,"", 1, 10, function(oResponse)
			{
				for (var nIndex =0; nIndex< oResponse.m_arrItemCategory.length; nIndex++)
				{
					if(strCurrentCategoryName.toLowerCase() == oResponse.m_arrItemCategory[nIndex].m_strCategoryName.toLowerCase())
					{
						bIsCategoryExist = true;
						informUser("Item Category Name Already Exist","kWarning");
						$("#itemCategory_input_categoryName","").val();
						document.getElementById("itemCategory_input_categoryName").focus ();
					}
				}
			});
	}
	return bIsCategoryExist;
}

