package comm;

public class AssetMgr {
	private float myMoney = 0;
	private float myMoneyInit = 0;
	private int stock = 0;
	private final int stockMin = 100;
	
	// 输出模式
	public final int outputAll    = 1;
	public final int outputBuy    = 2;
	public final int outputSell   = 2;
	public final int outputResult = 3;
	public final int noOutput     = 4;
	
	private int myMode = noOutput;
	
	// 交易模式
	public final int dealMean = 5;
	public final int dealMean_weighted  = 2;
	public final int deal_all  = 1;
	private int dealMode = deal_all;
	private int dealTime = dealMode;
	
	
	
	public void setDealTime(int dealTime) {
		this.dealTime = dealTime;
	}

	private float moneyInc = 0;
	
	public int getMode() {
		return myMode;
	}


	public void setMode(int myMode) {
		this.myMode = myMode;
	}


	public void setMoney(float money){
		myMoney = money;
		myMoneyInit = money;
		moneyInc = 0;
		stock = 0;
	}
	
	
	public void buy(float price, String dateNow){
		float assetNow = myMoney + stock * price;		
		float buyMoneyMax = calcBuyMoney(assetNow);
				
		int buyStock = (int)(buyMoneyMax/(price*stockMin))*100; 
		float buyInUse = buyStock * price;
		
		stock += buyStock;
		myMoney -= buyInUse;
		
		assetNow = myMoney + stock * price;
		
		if(myMode <= outputBuy){
			System.out.println("buy	" + stock + "	in price =	" + price + 
					"	date is	["+ dateNow + "]	assetNow = " + assetNow + 
					" rest stock = " + stock + " rest money = " + myMoney);
		}
	}


	private float calcBuyMoney(float assetNow) {
		float buyMoneyMax = 0;
		if(dealMode == dealMean){
			buyMoneyMax = assetNow / dealTime;
			if(buyMoneyMax > myMoney){
				buyMoneyMax = myMoney;
			}
		}
		else if(dealMode == dealMean_weighted){
			buyMoneyMax = myMoney / dealTime;
		}
		else if(dealMode == deal_all){
			buyMoneyMax = myMoney;
		}
		
		
		
		return buyMoneyMax;
	}
	
	public void sell(float price, String dateNow){
		if(stock == 0){
			return;
		}

		
		float assetNow = myMoney + stock * price;
		
		float sellMoneyMax = calcSellMoney(price, assetNow);
		
		int sellStock = (int)(sellMoneyMax/price); 
		float getMoney = sellStock * price;
		
		stock -= sellStock;
		myMoney += getMoney;
		
		assetNow = myMoney + stock * price;
		//myMoney += stock * price;
		//stock = 0;
		if(myMode <= outputSell){
			System.out.println("sell	" + stock + "	in price =	" + price + 
					"	date is	["+ dateNow + "]	assetNow = " + assetNow + 
					" rest stock = " + stock + " rest money = " + myMoney);
		}
	}


	private float calcSellMoney(float price, float assetNow) {
		float sellMoneyMax = 0;
		if(dealMode == dealMean){
			sellMoneyMax = assetNow / dealTime;
			if(sellMoneyMax > stock * price){
				sellMoneyMax = stock * price;
			}
		}
		else if(dealMode == dealMean_weighted){
			sellMoneyMax = stock * price / dealTime;
		}
		else if(dealMode == deal_all){
			sellMoneyMax = stock * price;
		}
		
		
		return sellMoneyMax;
	}
	
	public void result(float endPrice){
		
		float assetNow = myMoney + stock * endPrice;
		moneyInc = (assetNow/myMoneyInit -1)*100;
		if(myMode <= outputResult){
			System.out.println("############### AssetMgr #################");
			System.out.println("start money	=	" + myMoneyInit);
			System.out.println("end money	=	" + assetNow);
			System.out.println("------------------------------------------");
			System.out.println("money increate:	" + moneyInc + "%" );
			System.out.println("##########################################");
		}
	}
	
	public float getMoneyInc(){
		return moneyInc;
	}
}
