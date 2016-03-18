package strategy;

import java.util.ArrayList;
import java.util.List;

import comm.AssetMgr;

public class classicKDJ implements strateguInterface {
	private final int nDay = 9;

	private float moneyInc = 0;
	private float priceInc = 0;
	
	private AssetMgr asset = new AssetMgr(); 
	private List<String> myDate = new ArrayList<String>();
	private List<Float> price = new ArrayList<Float>();	
	private List<Float> K_value = new ArrayList<Float>();	
	private List<Float> D_value = new ArrayList<Float>();	
	private List<Float> J_value = new ArrayList<Float>();

	
	public classicKDJ(float money){
		asset.setMoney(money);
		asset.setMode(asset.noOutput);		
	}
	
	public void init(float money){
		asset.setMoney(money);
		moneyInc = 0;
		priceInc = 0;
		price.clear();
		myDate.clear();
		
		K_value.clear();
		D_value.clear();
		J_value.clear();
	}
	
	public void calc(List<String> inputData, List<String> date){
		if(inputData.size() < 2){
			return;
		}	
	
		int size = inputData.size();
		initDataInput(inputData, date, size);
	
		List<Float> RSV = new ArrayList<Float>();
		
		K_value.add((float) 50);
		D_value.add((float) 50);
		J_value.add((float) 50);
		for(int index = 1 ; index < size; index ++){
			
			float Ln = getLowestPrice(size, index);
			float Hn = getHighestPrice(size, index);
			float Cn = price.get(index);	
			float RSV_tmp = 100;
			if(Hn != Cn){
				RSV_tmp = (Cn - Ln)/(Hn - Ln) * 100;
			}
			
			float K_tmp = (float)2/3 * K_value.get(index - 1) + (float)1/3 * RSV_tmp; 	
			K_value.add(K_tmp);
			
			float D_tmp = (float)2/3 * D_value.get(index - 1) + (float)1/3 * K_tmp;
			D_value.add(D_tmp);
			
			float J_tmp = (float)3*D_tmp - (float)2*K_tmp;
			J_value.add(J_tmp);	
		}
		
		check();
	}

	public void check(){
		int size = price.size();
		if(size < 2){
			return;
		}

		for(int index = 1; index < size; index ++){
			float K_tmp =  K_value.get(index);
			float D_tmp =  J_value.get(index);
			float J_tmp =  J_value.get(index);
			
			//System.out.println("K_tmp = " + K_tmp);
			//System.out.println("D_tmp = " + D_tmp);
			//boolean buySingle = (K_tmp < 20 && D_tmp < 20 && J_tmp < 20);
			boolean buySingle = (K_tmp < 50 && D_tmp < 50 && J_tmp < 50) &&
								(K_tmp > K_value.get(index - 1)) &&
								(D_tmp < D_value.get(index - 1)) &&
								(J_tmp > J_value.get(index - 1));
			
			boolean sellSingle = (K_tmp > 50 && D_tmp > 50 && J_tmp > 50) &&
					(K_tmp < K_value.get(index - 1)) &&
					(D_tmp > D_value.get(index - 1)) &&
					(J_tmp < J_value.get(index - 1));
			
			//boolean sellSingle = (K_tmp > 80 && D_tmp > 80 && J_tmp > 80);
			//boolean sellSingle = (K_tmp < 40 && D_tmp < 50);
			
			
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
	
	
	
	
	
	
	
	private float getLowestPrice(int size, int index) {
		int indexStart = 0;
		int indexEnd = index;
		
		if (index >= nDay){
			indexStart = index - nDay + 1;
		}

		
		float lowestPriceTmp = price.get(index);
		for (int tmp = indexStart; tmp < indexEnd; tmp++){
			lowestPriceTmp = min(lowestPriceTmp, price.get(tmp));
		}
		return lowestPriceTmp;
	}
	
	private float getHighestPrice(int size, int index) {
		int indexStart = 0;
		int indexEnd = index;
		
		if (index >= nDay){
			indexStart = index - nDay + 1;
		}
		
		
		float highestPriceTmp = price.get(index);
		for (int tmp = indexStart; tmp < indexEnd; tmp++){
			highestPriceTmp = max(highestPriceTmp, price.get(tmp));
		}
		return highestPriceTmp;
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

	
	public float getMoneyInc(){
		return moneyInc;
	}
	
	public float getPriceInc(){
		return priceInc;
	}
	
	public float min(float param_A, float Param_B){
		if(param_A < Param_B){
			return param_A;
		}	
		return Param_B;
	}
	
	public float max(float param_A, float param_B){
		if(param_A < param_B){
			return param_B;
		}	
		return param_A;
	}
	
	
	
	
	
	
	public void test(){
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
	
		
		K_value.add((float) 50);
		D_value.add((float) 50);
		J_value.add((float) 50);
		
		for(int index = 1 ; index < size; index ++){
			
			float Ln = getLowestPrice(size, index);
			float Hn = getHighestPrice(size, index);
			float Cn = price.get(index);
			float RSV_tmp = 100;
			if(Hn != Cn){
				RSV_tmp = (Cn - Ln)/(Hn - Ln) * 100;
			}
			if(index == 9 )
				System.out.println("index = "+ index +" Ln = " + Ln + " Hn = " + Hn + " Cn = " + Cn);
			else
				System.out.println("index = "+ index +" Ln = " + Ln + " Hn = " + Hn + " Cn = " + Cn);
			
			float K_tmp =  (float)2/3 * K_value.get(index - 1) + (float)1/3 * RSV_tmp; 	
			K_value.add(K_tmp);
			
			float D_tmp = (float)2/3 * D_value.get(index - 1) + (float)1/3 * K_tmp;
			D_value.add(D_tmp);
			
			float J_tmp = (float)3*D_tmp - (float)2*K_tmp;
			J_value.add(J_tmp);	
		}
		
		System.out.println(K_value.toString());
		System.out.println(D_value.toString());
		System.out.println(J_value.toString());
	}
	
	
	
}
