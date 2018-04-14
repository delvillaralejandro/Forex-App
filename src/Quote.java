
import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.Observable;

public abstract class Quote extends Observable{
	String Name;
	Timestamp timestamp;
	BigDecimal bidBig;
	BigDecimal bidPoints;
	BigDecimal offerBig;
	BigDecimal offerPoints;
	BigDecimal High;
	BigDecimal Low;
	BigDecimal Open;
	
	public void setParameters(String[] data) {
		this.Name = data[0];
		//this.timestamp = data[1]; //Falta convertir el String a Timestamp
		this.bidBig = new BigDecimal(data[2]);
		this.bidPoints = new BigDecimal(data[3]);
		this.offerBig = new BigDecimal(data[4]);
		this.offerPoints = new BigDecimal(data[5]);
		this.High = new BigDecimal(data[6]);
		this.Low = new BigDecimal(data[7]);
		this.Open = new BigDecimal(data[8]);
		
		measurementsChanged();
	}
	/*public float geBbid() {
		return Pressure;
	}
	public float getTemp() {
		return Temp;
	}
	public float getHumidity() {
		return Humidity;
	}*/
	
	public void measurementsChanged() {
		setChanged();
		notifyObservers();
		//notifyObservers(new Wrapper(Temp,Humidity,Pressure));
	}

}