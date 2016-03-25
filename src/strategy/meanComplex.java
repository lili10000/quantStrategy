package strategy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

import comm.AssetMgr;

public class meanComplex implements strateguInterface {
	private int shortDay = 5;
	private int midDay = 30;
	private int longDay = 60;
	

	public void setnDay(int shortDay, int midDay,int longDay) {
		this.shortDay = shortDay;
		this.midDay = midDay;
		this.longDay = longDay;
	}


	List<Float> shortMean_nDay = new ArrayList<Float>();
	List<Float> midMean_nDay = new ArrayList<Float>();
	List<Float> longMean_nDay = new ArrayList<Float>();
	private List<Float> price = new ArrayList<Float>();	
	private List<String> myDate = new ArrayList<String>();
	private AssetMgr asset = new AssetMgr(); 

	
	
	private float moneyInc = 0;
	private float priceInc = 0;
	
	private File file;
	
	public meanComplex(float money){
		asset.setMoney(money);
		asset.setMode(asset.noOutput);
		file = new File("C:/Users/lenovo/Desktop/trade_Log.txt"); 
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
	

	
	@Override
	public void init(float money) {
		// TODO Auto-generated method stub
		asset.setMoney(money);
		moneyInc = 0;
		priceInc = 0;
		price.clear();
		myDate.clear();
		shortMean_nDay.clear();
		midMean_nDay.clear();
		longMean_nDay.clear();
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
			float shortMeanTmp = getMeanPrice(size, index, shortDay);
			float midMeanTmp = getMeanPrice(size, index, midDay);
			float longMeanTmp = getMeanPrice(size, index, longDay);
			shortMean_nDay.add(shortMeanTmp);
			midMean_nDay.add(midMeanTmp);
			longMean_nDay.add(longMeanTmp);
		}
		check();
	}
	
	public void calcToday(List<String> inputData, List<String> date, String tableName){
		if(inputData.size() < 2){
			return;
		}
		
		int size = inputData.size();
		initDataInput(inputData, date, size);
		
		for (int index = 0 ; index < size; index++){
			float shortMeanTmp = getMeanPrice(size, index, shortDay);
			float midMeanTmp = getMeanPrice(size, index, midDay);
			float longMeanTmp = getMeanPrice(size, index, longDay);
			shortMean_nDay.add(shortMeanTmp);
			midMean_nDay.add(midMeanTmp);
			longMean_nDay.add(longMeanTmp);
		}
		size = price.size() - 1;
		boolean buySingle = getBuySingle(size);		
		boolean sellSingle = getSellSingle(size);
		float priceToday = price.get(size);
		if(buySingle){
			System.out.println (date.get(size) + "	: [buy] " + tableName + " | price = " + priceToday);
		}
		else if(sellSingle){
			System.out.println (date.get(size) + "	: [sell] "+ tableName + " | price = " + priceToday);
		}
		else{
			//System.out.println (date.get(size) + " nothing ");
		}
	}

	public void calcAllToday(List<String> inputData, List<String> date, String tableName){
		if(inputData.size() < 2){
			return;
		}
		
		int size = inputData.size();
		initDataInput(inputData, date, size);
		
		for (int index = 0 ; index < size; index++){
			float shortMeanTmp = getMeanPrice(size, index, shortDay);
			float midMeanTmp = getMeanPrice(size, index, midDay);
			float longMeanTmp = getMeanPrice(size, index, longDay);
			shortMean_nDay.add(shortMeanTmp);
			midMean_nDay.add(midMeanTmp);
			longMean_nDay.add(longMeanTmp);
		}
		size = price.size() - 1;
		
		try{
			FileWriter fileWriter = new FileWriter(file, true);
			fileWriter.write("\r\n	start ["+ tableName + "] \r\n\r\n");
			for(int index = size - 5; index <= size ; index ++){
				strategyResult(date, tableName, fileWriter, index);
			}
			
			
			tomorrowSuggest(size, fileWriter);
			
			fileWriter.close(); 
	    }
		catch (Exception e) {   
	        e.printStackTrace();  
	    }
	}



	private void tomorrowSuggest(int size, FileWriter fileWriter)
			throws IOException {
		float shortMean =  shortMean_nDay.get(size);
		float midMean =  midMean_nDay.get(size);
		float longMean =  longMean_nDay.get(size);
		
		float buyPriceTmp = Math.min(midMean, longMean);
		float buyPrice = Math.max(shortMean, buyPriceTmp);
		
		float sellPriceTmp = Math.max(midMean, longMean);
		float sellPrice = Math.min(shortMean, sellPriceTmp);
		
		String LogInfo = "\r\n" + "[buy price] = " + buyPrice  + "\r\n" + 
		                          "[sell price] = " + sellPrice + "\r\n";
		System.out.println (LogInfo);
		fileWriter.write(LogInfo);
	}



	private void strategyResult(List<String> date, String tableName,
			FileWriter fileWriter, int index) throws IOException {
		float priceToday = price.get(index);
		boolean buySingle = getBuySingle(index);		
		boolean sellSingle = getSellSingle(index);
		if(buySingle){
			String LogInfo = date.get(index) + ":	[buy]	'" + tableName + "'| price = " + priceToday + 
					"	short = "+ shortMean_nDay.get(index) + 
					"	mid = "+ midMean_nDay.get(index) + 
					"	long = "+ longMean_nDay.get(index) + "\r\n";
			System.out.println (LogInfo);
			fileWriter.write(LogInfo);
		}
		else if(sellSingle){
			String LogInfo = date.get(index) + ":	<sell>	'"+ tableName + "'| price = " + priceToday + 
					"	short = "+ shortMean_nDay.get(index) + 
					"	mid = "+ midMean_nDay.get(index) + 
					"	long = "+ longMean_nDay.get(index) + "\r\n";
			System.out.println (LogInfo);
			fileWriter.write(LogInfo);
		}
		else{
			String LogInfo = date.get(index) + ":	nothing	'"+ tableName + "'| price = " + priceToday + 
					"	short = "+ shortMean_nDay.get(index) + 
					"	mid = "+ midMean_nDay.get(index) + 
					"	long = "+ longMean_nDay.get(index) + "\r\n";
			System.out.println (LogInfo);
			fileWriter.write(LogInfo);
		}
	}
	
	
	public void check(){
		int size = price.size();
		if(size < 2){
			return;
		}
		
		for(int index = 1; index < size; index ++){
			boolean buySingle = getBuySingle(index);		
			boolean sellSingle = getSellSingle(index);
		
			if(buySingle){
				float  priceNow = price.get(index);
				String dateNow = myDate.get(index);
				asset.buy(priceNow, dateNow);
			}
			else if(sellSingle){
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


	private boolean getSellSingle(int index) {
		float now = price.get(index);
		float shortMean =  shortMean_nDay.get(index);
		float midMean =  midMean_nDay.get(index);
		float longMean =  longMean_nDay.get(index);
		
		boolean single = false;
		boolean singleTmp;
		
		//000
		singleTmp = (now < shortMean) && (now < midMean) && (now < longMean);
		single = single || singleTmp;
		//010
		singleTmp = (now < shortMean) && (now > midMean) && (now < longMean);
		single = single || singleTmp;
		//001
		singleTmp = (now < shortMean) && (now < midMean) && (now > longMean);
		single = single || singleTmp;
		

		
		return single;
	}


	private boolean getBuySingle(int index) {
		float now = price.get(index);
		float shortMean =  shortMean_nDay.get(index);
		float midMean =  midMean_nDay.get(index);
		float longMean =  longMean_nDay.get(index);
		
		boolean single = false;
		boolean singleTmp;
		//111
		singleTmp = (now > shortMean) && (now > midMean) && 
					(now > longMean);
		single = single || singleTmp;
		
		//110
		singleTmp = (now > shortMean) && (now > midMean) && 
					(now < longMean);
		single = single || singleTmp;
		
		//101
		singleTmp = (now > shortMean) && (now < midMean) && 
					(now > longMean);
		single = single || singleTmp;
		

		
		return single;
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
	
	
	public float getMoneyInc(){
		return moneyInc;
	}
	
	public float getPriceInc(){
		return priceInc;
	}

	@Override
	public void test() {
		// TODO Auto-generated method stub
		price.add((float)10);
		price.add((float)11);
		price.add((float)12);
		price.add((float)13);
		price.add((float)14);
		price.add((float)15);
		price.add((float)16);
		price.add((float)15);
		price.add((float)14);
		price.add((float)15);
		price.add((float)16);
		price.add((float)13);
		price.add((float)12);
		price.add((float)11);
		price.add((float)10);
		price.add((float)11);
		price.add((float)10);
		price.add((float)11);
		price.add((float)10);	
		
		int size = price.size();
		
		for (int index = 0 ; index < size; index++){
			float shortMeanTmp = getMeanPrice(size, index, shortDay);
			float longMeanTmp = getMeanPrice(size, index, longDay);
			shortMean_nDay.add(shortMeanTmp);
			longMean_nDay.add(longMeanTmp);
		}
		
		System.out.println(shortMean_nDay.toString());
		System.out.println(longMean_nDay.toString());
		
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
}
