package com.techmust.traderp.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberToWords 
{
	public static String convertNumberToWords(BigDecimal nNumber, boolean bPrefix, boolean bSuffix) 
	{
	    String strNumberInWords = convertNumberToWords(nNumber);
	    if (bPrefix) 
	    {
	        if (nNumber.compareTo(BigDecimal.ONE) == 0) 
	            strNumberInWords = "Indian Rupee " + strNumberInWords;
	        else 
	            strNumberInWords = "Indian Rupees " + strNumberInWords;
	    }
	    if (bSuffix) 
	        strNumberInWords += " Only";
	    return strNumberInWords;
	}

	public static String convertNumberToWords(BigDecimal nNumber) 
	{
	
	    boolean bNegativeAmount = false;
	    if (nNumber.signum() == -1) 
	    {
	        bNegativeAmount = true;
	        nNumber = nNumber.abs();
	    }
	    StringBuilder oStringBuilder = new StringBuilder();
	    String numberString = nNumber.setScale(2,RoundingMode.HALF_UP).toPlainString();
	    double nParsedNumber = Double.parseDouble(numberString);
	    
	    int nQuotient = (int) (nParsedNumber / 10000000);
	    if (nQuotient > 0) 
	    	oStringBuilder.append(convertNumberToWords(new BigDecimal(nQuotient))+ " Crore ");
	
	    nParsedNumber = nParsedNumber % 10000000;
	    nQuotient = (int) (nParsedNumber / 100000);
	    if (nQuotient > 0) 
	    	oStringBuilder.append(numberToWords(nQuotient) + " Lakh ");
	   
	    nParsedNumber = nParsedNumber % 100000;
	    nQuotient = (int) (nParsedNumber / 1000);
	    if (nQuotient > 0) 
	    	oStringBuilder.append(numberToWords(nQuotient) + " Thousand ");
	
	    nParsedNumber = nParsedNumber % 1000;
	    nQuotient = (int) (nParsedNumber / 100);
	    if (nQuotient > 0) 
	    	oStringBuilder.append(numberToWords(nQuotient) + " Hundred ");
	
	    nParsedNumber = nParsedNumber % 100;
	    if (nParsedNumber != 0) 
	    	oStringBuilder.append(numberToWords((int) nParsedNumber) + " ");
	
	    int nDecimal = 0;
	    String val;
	    if (nParsedNumber % 1 != 0) 
	    {
	        String strDecimalInWords = Double.toString(nParsedNumber);
	        int nIndex = strDecimalInWords.indexOf(".");
	        strDecimalInWords = strDecimalInWords.substring(nIndex + 1);
	        if (strDecimalInWords.length() > 2) 
	        {
	            val = strDecimalInWords.substring(0, 2);
	            nDecimal = Integer.parseInt(val);
	            if (Integer.parseInt(strDecimalInWords.substring(2, 3)) > 5) 
	            	nDecimal++;
	        } 
	        else 
	        	nDecimal = Integer.parseInt(strDecimalInWords);
	        if (strDecimalInWords.length() == 1) 
	        	nDecimal *= 10;
	        if (oStringBuilder.toString().trim().length() > 0) 
	        	oStringBuilder.append(("and "));
	        
	        oStringBuilder.append(numberToWords(nDecimal));
	        if (nDecimal > 1) 
	        	oStringBuilder.append(" Paise");
	    	else 
	        	oStringBuilder.append(" Paisa");
	    }
	    if (oStringBuilder.toString().trim().length() == 0) 
	        return "Zero";
	    
	    String strResult = oStringBuilder.toString().trim();
	    if (bNegativeAmount)
	    	strResult = "Minus " + strResult;
	    return strResult;
	}
	
	private static String numberToWords(int nNumber) 
	{
	    int nQuotient = (int) (nNumber / 10);
	    StringBuilder oStringBuilder = new StringBuilder();
	
	    if (nQuotient > 0) 
	    {
	        if (nQuotient == 1 && (nNumber % 10) > 0) 
	        {
	        	oStringBuilder.append(wordInTens(nNumber % 10));
	            return oStringBuilder.toString();
	        }
	        oStringBuilder.append(wordInTenMultiples(nQuotient));
	    }
	    int nRemainder = nNumber % 10;
	    if (nRemainder > 0) 
	    {
	        if (oStringBuilder.length() > 0) {
	        	oStringBuilder.append(" ");
	        }
	        oStringBuilder.append(numberInWords(nRemainder));
	    }
	    return oStringBuilder.toString();
	}

    private static String wordInTenMultiples(int nPos) 
    {
        String [] arrTens = {"Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};
        return arrTens[nPos - 1];
    }

    private static String wordInTens(int nPos) 
    {
        String [] arrTeen = {"Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Ninteen"};
        return arrTeen[nPos - 1];
    }

    private static String numberInWords(int nPos) 
    {
        String [] arrUnits = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
        return arrUnits[nPos - 1];
    }
} 