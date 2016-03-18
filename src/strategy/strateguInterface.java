package strategy;

import java.util.List;

import comm.AssetMgr;

public interface strateguInterface {
	
	public void init(float money);
	public void calc(List<String> inputData, List<String> date);

	public float getMoneyInc();
	public float getPriceInc();
	
	public void test();
}
