package strategy;

import java.util.ArrayList;
import java.util.List;

import comm.AssetMgr;

public class mean implements strateguInterface {
	private int nDay = 5;
	private boolean buySingle = false;
	private boolean sellSingle = false;	
	
	public boolean isBuySingle() {
		return buySingle;
	}
	
	public boolean isSellSingle() {
		return sellSingle;
	}


	public void setnDay(int nDay) {
		this.nDay = nDay;
	}


	List<Float> Mean_nDay = new ArrayList<Float>();
	private List<Float> price = new ArrayList<Float>();	
	private List<String> myDate = new ArrayList<String>();
	private AssetMgr asset = new AssetMgr(); 

	
	
	private float moneyInc = 0;
	private float priceInc = 0;
	
	public mean(float money){
		asset.setMoney(money);
		//asset.setMode(asset.noOutput);
	}
	
	
	@Override
	public void init(float money) {
		// TODO Auto-generated method stub
		asset.setMoney(money);
		moneyInc = 0;
		priceInc = 0;
		price.clear();
		myDate.clear();
		Mean_nDay.clear();
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
			float meanTmp = getMeanPrice(size, index);
			Mean_nDay.add(meanTmp);
		}
		check();
	}

	
	public void check(){
		int size = price.size();
		if(size < 2){
			return;
		}
		
		
		for(int index = 1; index < size; index ++){
			
			buySingle = (price.get(index) > Mean_nDay.get(index));
						//(price.get(index-1) < Mean_nDay.get(index-1));
			
			sellSingle = (price.get(index) < Mean_nDay.get(index)); 
						 //(price.get(index-1) > Mean_nDay.get(index-1));
			
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
	
	
	
	private float getMeanPrice(int size, int index) {
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
	
	public String getEndDate(){
		int size = myDate.size();
		if(size < 2){
			return "";
		}
		return myDate.get(size - 1);
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
			float meanTmp = getMeanPrice(size, index);
			Mean_nDay.add(meanTmp);
		}
		
		System.out.println(Mean_nDay.toString());
		
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
