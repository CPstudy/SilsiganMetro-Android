package com.ganada.silsiganmetro.listitem;

public class ListTest {
	String strStation;
	int iNum;
	int iStainfo;
	int iExpress;
	String strSub;
	int iUpStatus;
	String strUpHead;
	String strUpNumber;
	String strUpStatus;
	String strUpType;
	String strUpStart;
	int iUpExpress;
	int iDnStatus;
	String strDnHead;
	String strDnNumber;
	String strDnStatus;
	String strDnType;
	String strDnStart;
	int iDnExpress;

	ListTest(String aStation,
			 int aNum,
			 int aStainfo,
			 int aExpress,
			 String aSub,
			 int aiUpStatus,
			 String aUpHead,
			 String aUpNumber,
			 String aUpStatus,
			 String aUpType,
			 String aUpStart,
			 int aiUpExpress,
			 int aiDnStatus,
			 String aDnHead,
			 String aDnNumber,
			 String aDnStatus,
			 String aDnType,
			 String aDnStart,
			 int aiDnExpress) {
		strStation = aStation;
		iNum = aNum;
		iStainfo = aStainfo;
		iExpress = aExpress;
		strSub = aSub;
		iUpStatus = aiUpStatus;
		strUpHead = aUpHead;
		strUpNumber = aUpNumber;
		strUpStatus = aUpStatus;
		strUpType = aUpType;
		strUpStart = aUpStart;
		iUpExpress = aiUpExpress;
		iDnStatus = aiDnStatus;
		strDnHead = aDnHead;
		strDnNumber = aDnNumber;
		strDnStatus = aDnStatus;
		strDnType = aDnType;
		strDnStart = aDnStart;
		iDnExpress = aiDnExpress;
	}
}
