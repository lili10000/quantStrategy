package strategy;
import java.util.ArrayList;
import java.util.List;

import comm.AssetMgr;

public class classicMacd implements strateguInterface {

	private float moneyInc = 0;
	private float priceInc = 0;
	
	private List<String> myDate = new ArrayList<String>();
	private List<Float> price = new ArrayList<Float>();	
	private AssetMgr asset = new AssetMgr(); 
	
	public classicMacd(float money){
		asset.setMoney(money);
		asset.setMode(asset.noOutput);

	}
	
	public void init(float money){
		asset.setMoney(money);
		moneyInc = 0;
		priceInc = 0;
		price.clear();
		myDate.clear();
	}
	
	
	public void calc(List<String> inputData, List<String> date){
		
		if(inputData.size() < 2){
			return;
		}
		
		int size = inputData.size();	
			
		initDataInput(inputData, date, size);
		
		List<Float> EMA_12 = new ArrayList<Float>();
		List<Float> EMA_26 = new ArrayList<Float>();
		List<Float> DIF = new ArrayList<Float>();
		List<Float> DEA = new ArrayList<Float>();
		List<Float> BAR = new ArrayList<Float>();			
		
		EMA_12.add((float)0);
		EMA_26.add((float)0);
		DIF.add((float)0);
		DEA.add((float)0);
		BAR.add((float)0);
		
		for(int index = 1 ; index < size; index ++){
			float yesterday_EMA_12 = EMA_12.get(index -1);
			float yesterday_EMA_26 = EMA_26.get(index -1);
			float today = price.get(index);
			
			float EMA_12_today = today*2/13 + yesterday_EMA_12*11/13;
			float EMA_26_today = today*2/27 + yesterday_EMA_26*25/27;					
			
			EMA_12.add(EMA_12_today);
			EMA_26.add(EMA_26_today);
			
			float difTmp = EMA_12_today - EMA_26_today;
			DIF.add(difTmp);
			
			float DEA_yesterday = DEA.get(index - 1);
			float deaTmp = difTmp*2/10 + DEA_yesterday*8/10;
			DEA.add(deaTmp);
			
			float barTmp = (difTmp - deaTmp)*2;
			BAR.add(barTmp);		
		}
		check(BAR);
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

	
	private void check(List<Float> inputData){
		
		int size = inputData.size();
		if(size < 2){
			return;
		}
			
		//System.out.println("---------------- start check MACD ------------------");
		
		for(int index = 1; index < size; index ++){
			inputData.get(index);
			if(inputData.get(index) > 0 && inputData.get(index - 1) < 0){
				float  priceNow = price.get(index);
				String dateNow = myDate.get(index);
				asset.buy(priceNow, dateNow);
			}
			else if(inputData.get(index) < 0 && inputData.get(index -1) > 0){
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
	
	
	public float getMoneyInc(){
		return moneyInc;
	}
	
	public float getPriceInc(){
		return priceInc;
	}
	
	public void test(){
		
	}
}
