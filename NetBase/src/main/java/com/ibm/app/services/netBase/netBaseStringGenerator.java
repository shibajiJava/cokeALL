package com.ibm.app.services.netBase;

public class netBaseStringGenerator {
	
	public static void main(String args[])
	{
		String topicID = "68700"; 
		String[] topicIDArr = topicID.split(",");
		String theamID = "29449,29451";
		String[] theamIDArr = theamID.split(",");
		String otherVal = "TotalBuzz,TotalBuzzPost,Impressions,PositiveSentiment,NegativeSentiment,NetSentiment,Passion,StrongEmotion,WeakEmotion";
		String[] otherValArr = otherVal.split(",");
		String otherKey="metricSeries";
		
		
		StringBuffer optionStr = new StringBuffer();
		
		
		for(int i=0;i<topicIDArr.length;i++)
		{
			for(int j=0;j<theamIDArr.length;j++)
			{
				for(int k=0;k<otherValArr.length;k++)
				{
					String singleOptionValue= "topicIds:"+topicIDArr[i]+";themeIds:"+theamIDArr[j]+";"+otherKey+":"+otherValArr[k];
					optionStr = optionStr.append(singleOptionValue+"#");
				}
			}
		}
		
		System.out.println("Value is : "+optionStr.toString());
		
		
	}

}
