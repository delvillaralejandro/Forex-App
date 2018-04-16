
import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.Observable;

public abstract class Quote extends Observable{
	String Name;
	long timestamp;
	BigDecimal bidBig;
	int bidPoints;
	BigDecimal offerBig;
	int offerPoints;
	BigDecimal High;
	BigDecimal Low;
	BigDecimal Open;
	
	public void setParameters(String[] data) {
		this.Name = data[0];
		this.timestamp = Long.parseLong(data[1]);
		this.bidBig = new BigDecimal(data[2]);
		this.bidPoints = Integer.parseInt(data[3]);
		this.offerBig = new BigDecimal(data[4]);
		this.offerPoints = Integer.parseInt(data[5]);
		this.High = new BigDecimal(data[6]);
		this.Low = new BigDecimal(data[7]);
		this.Open = new BigDecimal(data[8]);
		
		measurementsChanged();
	}
	
	public String getName() {
		return this.Name;
	}
	public long getTimestamp() {
		return this.timestamp;
	}
	public BigDecimal getBidBig() {
		return this.bidBig;
	}
	public int getBidPoints() {
		return this.bidPoints;
	}
	public BigDecimal getOfferBig() {
		return this.offerBig;
	}
	public int getOfferPoints() {
		return this.offerPoints;
	}
	public BigDecimal getHigh() {
		return this.High;
	}
	public BigDecimal getLow() {
		return this.Low;
	}
	public BigDecimal getOpen() {
		return this.Open;
	}
	
	public void printParams() {
		System.out.println(getName() + " " 
				+ getTimestamp() + " "
				+ getBidBig().toString()
				+ getBidPoints() + " "
				+ getOfferBig().toString()
				+ getOfferPoints() + " "
				+ getHigh() + " "
				+ getLow() + " "
				+ getOpen());
	}
	
	public void measurementsChanged() {
		setChanged();
		notifyObservers();
		//notifyObservers(new Wrapper(Temp,Humidity,Pressure));
	}

}