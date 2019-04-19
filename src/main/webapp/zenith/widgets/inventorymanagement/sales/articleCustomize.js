var articleCustomize_includeDataObjects = 
[
	'widgets/inventorymanagement/item/itemData.js',
	'widgets/inventorymanagement/item/ItemCategoryData.js',
	'widgets/clientmanagement/SiteData.js',
	'widgets/clientmanagement/ClientData.js',
	'widgets/clientmanagement/DemographyData.js',
	'widgets/clientmanagement/ContactData.js',
	'widgets/clientmanagement/BusinessTypeData.js',
	'widgets/inventorymanagement/sales/CustomizedItemData.js',
	'widgets/inventorymanagement/tax/ApplicableTaxData.js',
	'widgets/usermanagement/userinfo/UserInformationData.js'
];



 includeDataObjects (articleCustomize_includeDataObjects, "articleCustomize_loaded()");

function articleCustomize_MemberData ()
{
	this.m_strArticleNumber = "";
	this.m_oCustomizedItemData = null;
	this.m_oKeyDownHandler = function (event)
    {
		if(event.keyCode == 13)
		{
			$('#articleCustomize_input_articleNumber').combobox('textbox').unbind('keydown', m_oArticleCustomizeMemberData.m_oKeyDownHandler);
			$('#articleCustomize_input_clientDescription').combobox('textbox').focus();
		}
    };
}

var m_oArticleCustomizeMemberData = new articleCustomize_MemberData ();

function articleCustomize_init()
{
	createPopup ("thirdDialog", "#articleCustomize_button_create", "#articleCustomize_button_cancel", true);
	articleCustomize_initArticleNumberCombobox ('#articleCustomize_input_articleNumber', m_oArticleCustomizeMemberData.m_oKeyDownHandler);
	initFormValidateBoxes ("articleCustomize_form_id");
}

function articleCustomize_newArticle ()
{   
	articleCustomize_init();
	initFormValidateBoxes ("articleCustomize_form_id");
}

function articleCustomize_new ()
{   
	articleCustomize_newArticle ()
	$("#articleCustomize_input_clientArticleNumber").val(m_oArticleCustomizeMemberData.m_strArticleNumber);
}

function articleCustomize_edit ()
{   
	articleCustomize_init();
	document.getElementById("articleCustomize_input_clientArticleNumber").readOnly = true;
	document.getElementById("articleCustomize_button_create").setAttribute('update', true);
	document.getElementById("articleCustomize_button_create").innerHTML = "Update";
	articleCustomize_setValues ();
}

function articleCustomize_setValues ()
{
	var oCustomizedItemData = m_oArticleCustomizeMemberData.m_oCustomizedItemData;
	$('#articleCustomize_input_articleNumber').combobox('select', oCustomizedItemData.m_oItemData.m_strArticleNumber);	
	$("#articleCustomize_input_description").val(oCustomizedItemData.m_oItemData.m_strItemName +" | "+ oCustomizedItemData.m_oItemData.m_strDetail);
	$("#articleCustomize_input_clientArticleNumber").val(oCustomizedItemData.m_strClientArticleNumber);
	$("#articleCustomize_input_clientDescription").val(oCustomizedItemData.m_strClientArticleDescription);
	initFormValidateBoxes ("articleCustomize_form_id");
}

function articleCustomize_initArticleNumberCombobox (strComboboxID, oMemberData)
{
	$(strComboboxID).combobox
	({
		valueField:'m_strArticleDetail',
	    textField:'m_strArticleNumber',
	    loader: getArticleNumberList,
		mode: 'remote',
		selectOnNavigation: false,
		formatter:function(row)
    	{
        	var opts = $(this).combobox('options');
        	return decodeURIComponent(row[opts.valueField]);
    	},
    	onSelect:function(row)
        {
    		var opts = $(this).combobox('options');
    		var strDecodeDetails = decodeURIComponent (row[opts.valueField]);
    		var arrArticleDetails = strDecodeDetails.split(" | ");
    		$("#articleCustomize_input_description").val(arrArticleDetails[1] +" | "+ arrArticleDetails[2]);
    		$("#articleCustomize_input_clientDescription").val(arrArticleDetails[1] +" | "+ arrArticleDetails[2]);
    		initFormValidateBoxes ("articleCustomize_form_id");
    		
        }
	});
	var ArticleTextBox = $(strComboboxID).combobox('textbox');
	ArticleTextBox[0].placeholder = "Article Number";
}

var getArticleNumberList = function(param, success, error)
{
	if(param.q != undefined && param.q.trim() != "")
	{
		var strQuery = param.q.trim();
		var oItemData = new ItemData ();
		oItemData.m_strArticleNumber = strQuery;
		oItemData.m_strItemName = strQuery;
		oItemData.m_oUserCredentialsData.m_nUserId = m_oTrademustMemberData.m_nUserId;
		oItemData.m_oUserCredentialsData.m_nUID = m_oTrademustMemberData.m_nUID;
		ItemDataProcessor.getArticleSuggesstions(oItemData, "", "", function(oItemDataResponse)
				{
					var arrItemData = new Array();
					for(var nIndex=0; nIndex< oItemDataResponse.m_arrItems.length; nIndex++)
				    {
						arrItemData.push(oItemDataResponse.m_arrItems[nIndex]);
						arrItemData[nIndex].m_strArticleDetail = encodeURIComponent(arrItemData[nIndex].m_strArticleNumber + " | " +
						arrItemData[nIndex].m_strItemName + " | " +
						arrItemData[nIndex].m_strDetail);
				    }
					success(arrItemData);
				});
	}
	else
		success(new Array ());
}

function articleCustomize_cancel ()
{
	HideDialog ("thirdDialog");
}

function articleCustomize_submit ()
{
	if (articleCustomize_validate())
	{
		//disable ("articleCustomize_button_create");
		var oCustomizedItemData = articleCustomize_getFormData ();
		if(document.getElementById("articleCustomize_button_create").getAttribute('update') == "false")
			CustomizedItemDataProcessor.create (oCustomizedItemData, articleCustomize_created);
		else
			CustomizedItemDataProcessor.update(oCustomizedItemData, articleCustomize_updated);
	}
}

function articleCustomize_validate ()
{
	return validateForm("articleCustomize_form_id");
}


function articleCustomize_getFormData ()
{
	var oCustomizedItemData = new CustomizedItemData ();
	oCustomizedItemData.m_strItemArticleNumber = $('#articleCustomize_input_articleNumber').combobox('getText');
	oCustomizedItemData.m_strClientArticleNumber = $("#articleCustomize_input_clientArticleNumber").val();
	oCustomizedItemData.m_strClientArticleDescription = $("#articleCustomize_input_clientDescription").val();
	oCustomizedItemData.m_oClientData.m_nClientId = m_oArticleCustomizeMemberData.m_nSelectedClientId;
	oCustomizedItemData.m_oCreatedBy.m_nUserId = m_oTrademustMemberData.m_nUserId;
	return oCustomizedItemData;
}

function articleCustomize_created (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("Article Customize Created Successfully", "kSuccess");
		HideDialog("thirdDialog");
		try
		{
			articleCustomize_handleAfterSave ();
		}catch(oException){}
	}
}

function articleCustomize_updated (oResponse)
{
	if(oResponse.m_bSuccess)
	{
		informUser("ArticleCustomize Updated Successfully", "kSuccess");
		HideDialog("thirdDialog");
		try
		{
			articleCustomize_handleAfterSave ();
		}catch(oException){}
	}
}
