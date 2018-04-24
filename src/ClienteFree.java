import java.math.BigDecimal;
import java.util.Observable;
import java.util.Observer;

public class ClienteFree implements Observer{
	
	private int clientID;
	private String Name;
	private String LastName;
	private String Email;
	private String Password;
	private int pipChange = 50;
	
	private String oldQuoteName;
	private BigDecimal oldbidBig;
	private int oldbidPoints;
	private BigDecimal oldofferBig;
	private int oldofferPoints;
	
	private String quoteName;
	private long timestamp;
	private BigDecimal bidBig;
	private int bidPoints;
	private BigDecimal offerBig;
	private int offerPoints;
	private BigDecimal High;
	private BigDecimal Low;
	private BigDecimal Open;
	
	
	public ClienteFree(Observable quote) {
		this.clientID = (int) Math.ceil(Math.random()*1000);
		quote.addObserver(this);
	}
	
	public ClienteFree(String name, String last, String email,String pass) {
		this.clientID = (int) Math.ceil(Math.random()*1000);
		this.Name = name;
		this.LastName = last;
		this.Email = email;
		this.Password = pass;
	}
	
	public ClienteFree() {
		this.clientID = (int) Math.ceil(Math.random()*1000);
	}

	public void update(Observable observable, Object arg) {
		if(arg == null) {
			System.out.println("Null Argument");
		}else 
		{
			Wrapper wrap = (Wrapper) arg;
			
			this.quoteName = wrap.Name;
			this.timestamp = wrap.timestamp;
			this.bidBig = wrap.bidBig;
			this.bidPoints = wrap.bidPoints;
			this.offerBig = wrap.offerBig;
			this.offerPoints = wrap.offerPoints;
			this.High = wrap.High;
			this.Low = wrap.Low;
			this.Open = wrap.Open;
			
			
			 if(evaluateChange()){
				 this.oldQuoteName = this.quoteName;
				 //this.oldtimestamp = this.timestamp;
				 this.oldbidBig = this.bidBig;
				 this.oldbidPoints = this.bidPoints;
				 this.oldofferBig = this.offerBig;
				 this.oldofferPoints = this.offerPoints;
				 System.out.println("Old values set");
				 printParams();
			 }
			 
			
			//printParams();
		}
	}
	
	public boolean evaluateChange() {
		if( Math.abs(oldbidPoints - bidPoints) >= pipChange) {
			return true;
		}
		else {
			//if((this.oldbidBig.doubleValue() != this.bidBig.doubleValue())) {
			//	return true;
			//}
			return false;
		}
	}
	
	public void setPipChange(int pip) {
		this.pipChange = pip;
	}
	
	public void setOldValues(Object arg) {
		if(arg == null) {
			System.out.println("Null Argument");
		}else {
			Quote wrap = (Quote) arg;
			
			this.oldQuoteName = wrap.getName();
			//this.timestamp = wrap.getTimestamp();
			this.oldbidBig = wrap.getBidBig();
			this.oldbidPoints = wrap.getBidPoints();
			this.oldofferBig = wrap.getOfferBig();
			this.oldofferPoints = wrap.getOfferPoints();
			System.out.println("Old values set");
		}
	}
	
	public void printParams() {
		System.out.println(
				"ID de Cliente: " 
				+ this.clientID + " "
				+ this.quoteName + " " 
				+ this.timestamp + " "
				+ this.bidBig.toString()
				+ this.bidPoints + " "
				+ this.offerBig.toString()
				+ this.offerPoints + " "
				+ this.High + " "
				+ this.Low + " "
				+ this.Open);
	}
}