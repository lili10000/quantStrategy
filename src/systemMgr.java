import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




import com.mysql.jdbc.ResultSetMetaData;

import comm.SQLCommand;
import comm.StockChoise;
import comm.statisticMgr;
import strategy.classicKDJ;
import strategy.classicMacd;
import strategy.mean;

import strategy.meanComplex;
import strategy.strateguInterface;

public class systemMgr {
	
	private final int allDate = 1;
	private final String startDate = "2009-07-31";
	private final int allCode = 0;
	private final String codeType  = "k_d_%";
	
	private final int moneyInit = 100000;
		
	private statisticMgr report = new statisticMgr();
	private SQLCommand comm;
	
	private class priceAndDate{
		public List<String> inputData = new ArrayList<String>();
		public List<String> date = new ArrayList<String>();
	}
	private HashMap<String, priceAndDate> code_price_map = new HashMap<String, priceAndDate>();
	

	private StockChoise stockPool = new StockChoise();
	//private strateguInterface strategy = new classicMacd(moneyInit);
	//private strateguInterface strategy = new classicKDJ(moneyInit);
	//private strateguInterface strategy = new mean(moneyInit);
	//private mean strategy = new mean(moneyInit);
	private meanComplex strategy = new meanComplex(moneyInit);

	private List<String> stockName = new ArrayList<String>();
	
	public void run() {
		
		comm = new SQLCommand();
		comm.connect();
		
		//List<String> tableName = new ArrayList<String>();
		
		stockPool.init();
		List<String> tableName;
		if(allCode == 1){
			tableName = new ArrayList<String>();
			queryTableName(tableName);
		}
		else{
			tableName = stockPool.getStockChoise();
		}
		getStockName_CN(tableName);
		//queryTableName(tableName);
		
		initMemoryData(tableName);

		//Mean_combination(tableName);
		//Mean_calcToday(tableName, 4);
		//Mean_calc(tableName);		
		//MACD_calc(tableName);
		//MeanComplex_calc(tableName);
		//MeanComplex_calcToday(tableName);
		MeanComplex_calcAllToday(tableName);
		
		comm.disConnect();
		System.out.println ("end work !! "); 
	}

	private void initMemoryData(List<String> tableName) {
		int size = tableName.size();
		for(int index = 0 ; index < size; index ++){
			String tableNameTmp = tableName.get(index);
			getDataAndSaveInMap(tableNameTmp);
		}
		System.out.println ("read data in DB ......ok "); 
	}

	private void getDataAndSaveInMap(String tableNameTmp) {
		String sql;
		if(allDate == 1){
			sql = "select * from " + tableNameTmp ;
		}
		else{
			sql = "select * from " + tableNameTmp
				+ " where ( 日期 > '" + startDate +"') ";
		}

		
		
		
		ResultSet retn = comm.query(sql);
		try{
			priceAndDate tmp = new priceAndDate();
			while(retn.next()){		
				tmp.inputData.add(retn.getString("前复权价"));
				tmp.date.add(retn.getString("日期"));	
			}
			code_price_map.put(tableNameTmp, tmp);
			//System.out.println ("recv data size = " + tmp.inputData.size()); 
		}
		catch(SQLException e) {   
			e.printStackTrace();   
		} catch(Exception e) {   
			e.printStackTrace();   
		}
	}

	private void MACD_calc(List<String> tableName) {
		int size = tableName.size();

		report.setHeadInfo("[MACD] anaylze data: we calc data num:	"+ size + "	");
		for(int index = 0 ; index < size; index ++){
 			String tableNameTmp = tableName.get(index);
			analyseData(tableNameTmp);
		}
		report.getReport();
	}
	
	private void MeanComplex_calc(List<String> tableName) {
		int size = tableName.size();

		report.setHeadInfo("[MeanComplex] anaylze data: we calc data num:	"+ size + "	");
		for(int index = 0 ; index < size; index ++){
 			String tableNameTmp = tableName.get(index);
			analyseData(tableNameTmp);
		}
		report.getReport();
	}
	
	private void MeanComplex_calcToday(List<String> tableName) {
		int size = tableName.size();

		//report.setHeadInfo("[MeanComplex] anaylze data: we calc data num:	"+ size + "	");
		for(int index = 0 ; index < size; index ++){
 			String tableNameTmp = tableName.get(index);
			strategy.init(moneyInit);
			priceAndDate obj = code_price_map.get(tableNameTmp);	
			strategy.calcToday(obj.inputData, obj.date, tableNameTmp);
		}
	}
	private void MeanComplex_calcAllToday(List<String> tableName) {
		int size = tableName.size();

		//report.setHeadInfo("[MeanComplex] anaylze data: we calc data num:	"+ size + "	");
		for(int index = 0 ; index < size; index ++){
 			String tableNameTmp = tableName.get(index);
 			String stockNameTmp = stockName.get(index);
			strategy.init(moneyInit);
			priceAndDate obj = code_price_map.get(tableNameTmp);	
			strategy.calcAllToday(obj.inputData, obj.date, stockNameTmp);
		}
	}

	private void Mean_calc(List<String> tableName) {
//		int size = tableName.size();
//		for (int tmp = 2; tmp < 120; tmp ++){
//
//			report.setHeadInfo("[Mean_" + tmp +"] anaylze data: we calc data num:	"
//								+ size + "	");
//			for(int index = 0 ; index < size; index ++){
//
//				String tableNameTmp = tableName.get(index);
//				//getDataFromDb(tableNameTmp);
//				strategy.setnDay(tmp);
//				
//				analyseData(tableNameTmp);
//			}
//			report.getReport();
//		}
	}
	
	
//	private void Mean_combination(List<String> tableName) {
//		int size = tableName.size();
//		
//		for (int tmp = 5; tmp < 6; tmp ++){
//
//			report.setHeadInfo("[Mean_combination " + tmp +"] anaylze data: we calc data num:	"
//								+ size + "	");
//			
//			List<String> date = new ArrayList<String>();
//			
//			List<HashMap<String, String>> inputData = new ArrayList<HashMap<String, String>>();
//			
//			for(int index = 0 ; index < size; index ++){
//				HashMap<String, String> MapTmp = new HashMap<String, String>();
//				String tableNameTmp = tableName.get(index);			
//				priceAndDate obj = code_price_map.get(tableNameTmp);
//				date = obj.date;
//				List<String> inputDataTmp = obj.inputData;
//				
//				for(int dateIndex = 0; dateIndex < date.size(); dateIndex++){
//					MapTmp.put(date.get(dateIndex) ,inputDataTmp.get(dateIndex));
//				}
//				inputData.add(MapTmp);
//			}
//			strategy.init(moneyInit);
//			strategy.setnDay(tmp);
//			
//			strategy.combinationCalc(inputData);
//			float moneyInc = strategy.getMoneyInc();
//			float priceInc = strategy.getPriceInc();
//			report.addData(moneyInc, priceInc);
//			report.getReport();
//		}
//	}
	
	
	private void Mean_calcToday(List<String> tableName, int tmp) {
//		int size = tableName.size();
//
//		report.setHeadInfo("[Mean_" + tmp +"] anaylze data: we calc data num:	"
//							+ size + "	");
//		for(int index = 0 ; index < size; index ++){
//
//			String tableNameTmp = tableName.get(index);
//			//getDataFromDb(tableNameTmp);
//			strategy.setnDay(tmp);
//			analyseData(tableNameTmp);
//			
//			String today = strategy.getEndDate();
//			if(strategy.isBuySingle()){
//				System.out.println (today + "	: [buy] " + tableNameTmp);
//			}
//			
//			if(strategy.isSellSingle()){
//				System.out.println (today + "	: [sell] " + tableNameTmp);
//			}
//			
//		}
//		report.getReport();

	}
	
	

	private void queryTableName(List<String> tableName) {
		SQLCommand connectHandler = new SQLCommand();
		connectHandler.setUrl("jdbc:mysql://localhost:3306/information_schema");
		connectHandler.connect();
		String SQL = " select * from TABLES "
				+ "where ( TABLE_SCHEMA = 'stockdb' and TABLE_NAME like '" + codeType +"' )";
		ResultSet retn = connectHandler.query(SQL);
		
		try{
			while(retn.next()){
				tableName.add(retn.getString("TABLE_NAME"));
			}
			System.out.println ("recv tableName size = " + tableName.size()); 
		}
		catch(SQLException e) {   
			e.printStackTrace();   
		} catch(Exception e) {   
			e.printStackTrace();   
		}
		connectHandler.disConnect();
	}
private void getStockName_CN(List<String> tableName) {
		
		int count = tableName.size();
		
		for(int index = 0; index < count; index ++){
			String tmp[] = tableName.get(index).split("k_d_"); 
			String name = "名称";
			String code = "代码";
			String codeInput = tmp[1];

			String SQL ="select 名称  from basics where  ( 代码 like '" + codeInput + "');";

			
			ResultSet retn = comm.query(SQL);
			try{
				while(retn.next()){
					stockName.add(retn.getString("名称"));
				}
			}
			catch(SQLException e) {   
				e.printStackTrace();   
			} catch(Exception e) {   
				e.printStackTrace();   
			}
		}	
	}
	private void analyseData(String tableNameTmp) {
		strategy.init(moneyInit);
		
		priceAndDate obj = code_price_map.get(tableNameTmp);	
		strategy.calc(obj.inputData, obj.date);
		float moneyInc = strategy.getMoneyInc();
		float priceInc = strategy.getPriceInc();
		report.addData(moneyInc, priceInc);
	}


	

//	private void getDataFromDb(String tableName) {
//		date.clear();
//		inputData.clear();
//		
////		String sql = "select * from " + tableName +
////					 " where (����  > '2007-10-16') ";
//		
//		String sql = "select * from " + tableName;
//		
//		ResultSet retn = comm.query(sql);
//		
//		try{
//			while(retn.next()){
//				inputData.add(retn.getString("ǰ��Ȩ��"));
//				date.add(retn.getString("����"));
//			}
//			//System.out.println ("recv data size = " + inputData.size()); 
//		}
//		catch(SQLException e) {   
//			e.printStackTrace();   
//		} catch(Exception e) {   
//			e.printStackTrace();   
//		}
//	}
}
