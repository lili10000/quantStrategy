package comm;

import java.util.ArrayList;
import java.util.List;

public class StockChoise {
	private List<String> StockChoise = new ArrayList<String>();

	public List<String> getStockChoise() {
		return StockChoise;
	}
	
	public void init(){
		//bankStock();  //银行
		//alcoholStock(); //酒
		//assuranceStock(); //保险
		myselfStock();
		//StockChoise.add("k_d_603288");
		//StockChoise.add("k_d_002039");
		//StockChoise.add("k_d_600674");
		//StockChoise.add("k_d_600519");
		
	}
	private void myselfStock(){
		//自持有
		StockChoise.add("k_d_600309");
		StockChoise.add("k_d_300124");
		StockChoise.add("k_d_002169");
		
		StockChoise.add("k_d_600196");
		StockChoise.add("k_d_300146");
		StockChoise.add("k_d_600535");		
		StockChoise.add("k_d_600519");

		StockChoise.add("k_d_600674");
		StockChoise.add("k_d_002053");
		StockChoise.add("k_d_000970");

	}
	
	
	private void assuranceStock(){
		//StockChoise.add("k_d_601318");
		StockChoise.add("k_d_601336");
		StockChoise.add("k_d_601601");
		StockChoise.add("k_d_601628");
	}
	
	private void alcoholStock(){
		StockChoise.add("k_d_600519");
		StockChoise.add("k_d_000858");
		StockChoise.add("k_d_002304");
		StockChoise.add("k_d_600600");
		StockChoise.add("k_d_000596");
		StockChoise.add("k_d_600809");
	}
	
	
	private void bankStock(){
		StockChoise.add("k_d_000001");
		StockChoise.add("k_d_002142");
		StockChoise.add("k_d_600000");
		StockChoise.add("k_d_600015");
		StockChoise.add("k_d_600016");
		StockChoise.add("k_d_600036");
		StockChoise.add("k_d_601009");
		StockChoise.add("k_d_601166");
		StockChoise.add("k_d_601169");
		StockChoise.add("k_d_601288");
		StockChoise.add("k_d_601328");
		StockChoise.add("k_d_601398");
		StockChoise.add("k_d_601818");
		StockChoise.add("k_d_601939");
		StockChoise.add("k_d_601988");
		StockChoise.add("k_d_601998");
	}
	
	
}
