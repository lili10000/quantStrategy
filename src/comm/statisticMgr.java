package comm;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class statisticMgr {
	private List<Float> moneyInc = new ArrayList<Float>();
	private List<Float> priceInc = new ArrayList<Float>();
	//private int dataCount = 0;
	
	private String headInfo;
	
	public void setHeadInfo(String headInfo) {
		this.headInfo = headInfo;
	}


	public final int printAndWriteFile = 1;
	//public final int onlyPrint    	   = 2;
	public final int onlyWriteFile     = 3;
	
	private int myMode = onlyWriteFile;
	
	File file = new File("C:/Users/lenovo/Desktop/quantReport.txt");
	
	public statisticMgr(){
        try {
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
			FileWriter fileWriter =new FileWriter(file);
	        fileWriter.write("");
	        fileWriter.flush();
	        fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	

	public void setMyMode(int myMode) {
		this.myMode = myMode;
	}

	public void addData(float moneyInc, float priceInc){

		this.moneyInc.add(moneyInc);
		this.priceInc.add(priceInc);			
	}
	
	public void getReport(){

		float moneyIncReport = getMoneyIncResult();
		float priceIncReport = getPriceIncResult();
		int size = moneyInc.size();
		if(myMode == printAndWriteFile){
			printReport(moneyIncReport, priceIncReport, size);
		}	
		writeReport(moneyIncReport, priceIncReport, size);

        moneyInc.clear();
        priceInc.clear();
	}


	private float getMoneyIncResult() {

		int dataCount = moneyInc.size();
		float sum = 0;
		for (int index = 0; index < dataCount; index++){
			sum += moneyInc.get(index);
		}
		float moneyIncReport = sum/dataCount;
		return moneyIncReport;
	}

	private float getPriceIncResult() {

		int dataCount = priceInc.size();
		float sum = 0;
		for (int index = 0; index < dataCount; index++){
			sum += priceInc.get(index);
		}
		float priceIncReport = sum/dataCount;
		return priceIncReport;
	}

	private void writeReport(float moneyInc, float priceInc, int dataCount) {
		FileWriter writer = null;
        try{      	
   
	        String writeData = headInfo;
	        writeData += "price increate:		" + priceInc + 
	        		     "	moneyInc increate:	" + moneyInc; 
	        //writeData += "---------------------------------------\r\n";
	        //writeData += "report:	" + moneyInc/priceInc + "%\r\n";
	        if(moneyInc/priceInc > 1){
	        	//writeData += "strategy win !!! \r\n";
	        }
	        else{
	        	//writeData += "strategy lose !!! \r\n";
	        }
	        writeData += "\r\n";
	        
	        
	        writer = new FileWriter(file, true);     
            writer.write(writeData); 
            writer.close(); 
        }
    	catch (Exception e) {   
            e.printStackTrace();  
        }

	}


	private void printReport(float moneyInc, float priceInc, int dataCount) {
		System.out.println("################  report #################");
		System.out.println("we calc data num:	" + dataCount);
		System.out.println("------------------------------------------");
		System.out.println("price increate:		" + priceInc + "%" );
		System.out.println("moneyInc increate:	" + moneyInc + "%" );
		System.out.println("##########################################");
		if (moneyInc > priceInc){
			System.out.println("	strategy win !!!	" );
		}
		else{
			System.out.println("	strategy lose !!!	" );
		}
	}
}
