package strategy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import comm.AssetMgr;

public class T_T2 implements strateguInterface {
	private int T_day = 5;
	
	private float moneyInc = 0;
	private float priceInc = 0;
	private AssetMgr asset = new AssetMgr(); 
		
	private Vector<Float> price = new Vector<Float>();	
	private Vector<String> myDate = new Vector<String>();
	private Vector<Float> T_nDay = new Vector<Float>();
	private Vector<Float> T2_nDay = new Vector<Float>();
	
	private File file;
	
	private boolean have_buy = false;
	private boolean have_sell = false;
	public T_T2(float money){
		asset.setMoney(money);
		asset.setMode(asset.noOutput);
		file = new File("C:/Users/Administrator/Desktop/开发/trade_Log.txt"); 
		if(!file.exists())    
		{    
		    try {    
		        file.createNewFile();    
		    } 
		    catch (Exception e) {    
		        // TODO Auto-generated catch block    
		        e.printStackTrace();    
		    }    
		}
		try{
			FileWriter fileWriter = new FileWriter(file);
	        fileWriter.write("");
	        fileWriter.flush();
	        fileWriter.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	public void setDay(int day){
		T_day = day;
	}
	
	@Override
	public void init(float money) {
		// TODO Auto-generated method stub
		asset.setMoney(money);
		moneyInc = 0;
		priceInc = 0;
		price.clear();
		myDate.clear();
		T_nDay.clear();
		T2_nDay.clear();
	}

	@Override
	public void calc(List<String> inputData, List<String> date) {
		// TODO Auto-generated method stub
		
		if(inputData.size() < 2){
			return;
		}
		
		int size = inputData.size();
		initDataInput(inputData, date, size);
		

		for (int index = 0 ; index < size; index++){
			float T_dayTmp = getMeanPrice(size, index, T_day);
			float T2_dayTmp = getMeanPrice(size, index, T_day+5);
			T_nDay.add(T_dayTmp);
			T2_nDay.add(T2_dayTmp);
		}
		check();
	}
	
//	public void calcAllToday(List<String> inputData, List<String> date, String tableName) {
//		// TODO Auto-generated method stub
//		if(inputData.size() < 2){
//			return;
//		}
//		
//		int size = inputData.size();
//		initDataInput(inputData, date, size);
//		
//		for (int index = 0 ; index < size; index++){
//			float T_dayTmp = getMeanPrice(size, index, T_day);
//			float T2_dayTmp = getMeanPrice(size, index, 2*T_day);
//			T_nDay.add(T_dayTmp);
//			T2_nDay.add(T2_dayTmp);
//		}
//		size = price.size() - 1;
//		try{
//			FileWriter fileWriter = new FileWriter(file, true);
//			fileWriter.write("\r\nstart ["+ tableName + "] \r\n\r\n");
//			for(int index = size - 5; index <= size ; index ++){
//				strategyResult(date, tableName, fileWriter, index);
//			}
//			
//			//tomorrowSuggest(size, fileWriter);
//			
//			fileWriter.close(); 
//	    }
//		catch (Exception e) {   
//	        e.printStackTrace();  
//	    }
//	}
	
	

	@Override
	public float getMoneyInc() {
		// TODO Auto-generated method stub
		return moneyInc;
	}

	@Override
	public float getPriceInc() {
		// TODO Auto-generated method stub
		return priceInc;
	}

	@Override
	public void test() {
		// TODO Auto-generated method stub
		
	}

	private void initDataInput(List<String> inputData, List<String> date, 
			int size) {
		myDate.addAll(date);
		for(int index = 0 ; index < size; index ++){
			String tmpStr = inputData.get(index);
			float tmpInt =Float.parseFloat(tmpStr);
			price.add(tmpInt);	
		}
	}
	
	private float getMeanPrice(int size, int index, int nDay) {
		int indexStart = 0;
		int indexEnd = index;
		
		if (index >= nDay){
			indexStart = index - nDay + 1;
		}
		
		float meanPriceTmp = price.get(index);
		for (int tmp = indexStart; tmp < indexEnd; tmp++){
			meanPriceTmp += price.get(tmp);
		}
		meanPriceTmp /= (indexEnd - indexStart + 1); 
		
		return meanPriceTmp;
	}
	
	private void strategyResult(List<String> date, String tableName,
			FileWriter fileWriter, int index) throws IOException {
		float priceToday = price.get(index);
		boolean buySingle = getBuySingle(index) && !have_buy;		
		boolean sellSingle = getSellSingle(index) && !have_sell;
		if(buySingle){
			have_sell = false;
			have_buy = true;
			String LogInfo = date.get(index) + ":	[buy]	'" + tableName + "'| price = " + priceToday + 
					"	T1 = "+ T_nDay.get(index) + 
					"	T2 = "+ T2_nDay.get(index) + "\r\n";
			System.out.println (LogInfo);
			fileWriter.write(LogInfo);
		}
		else if(sellSingle){
			have_sell = true;
			have_buy = false;
			String LogInfo = date.get(index) + ":	<sell>	'"+ tableName + "'| price = " + priceToday + 
					"	T1 = "+ T_nDay.get(index) + 
					"	T2 = "+ T2_nDay.get(index) + "\r\n";
			System.out.println (LogInfo);
			fileWriter.write(LogInfo);
		}
		else{
			String LogInfo = date.get(index) + ":	nothing	'"+ tableName + "'| price = " + priceToday + 
					"	T1 = "+ T_nDay.get(index) + 
					"	T2 = "+ T2_nDay.get(index) + "\r\n";
			System.out.println (LogInfo);
			fileWriter.write(LogInfo);
		}
	}

	private boolean getBuySingle(int index) {
		
		//System.out.println (LogInfo);
		return ( T_nDay.get(index) > T2_nDay.get(index));
	}
	
	private boolean getSellSingle(int index) {	
		return ( T_nDay.get(index) < T2_nDay.get(index));
	}
	

	public void check(){
		int size = price.size();
		if(size < 2){
			return;
		}
		
		for(int index = 1; index < size; index ++){

			boolean buySingle = getBuySingle(index) && !have_buy;		
			boolean sellSingle = getSellSingle(index) && !have_sell;
			if(buySingle){
				have_sell = false;
				have_buy = true;

				float  priceNow = price.get(index);
				String dateNow = myDate.get(index);
				asset.buy(priceNow, dateNow);
			}
			else if(sellSingle){
				have_sell = true;
				have_buy = false;
				float  priceNow = price.get(index);
				String dateNow = myDate.get(index);
				asset.sell(priceNow, dateNow);
			}			
		}
		
		float startPrice = price.get(0);
		float endPrice = price.get(size - 1);
		priceInc = (endPrice/startPrice -1)*100;
		
		asset.result(endPrice);
		moneyInc = asset.getMoneyInc();
	}
}
