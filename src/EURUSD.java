
import java.security.Timestamp;
import java.util.Observable;

public class EURUSD extends Observable{
	String Name;
	float Bid;
	float Offer;
	Timestamp timestamp;
	
	public void setParameters(String name, float bid,float offer, Timestamp timest) {
		this.Name = name;
		this.Bid = bid;
		this.Offer = offer;
		this.timestamp = timest;
		//measurementsChanged();
	}
	/*public float geBbid() {
		return Pressure;
	}
	public float getTemp() {
		return Temp;
	}
	public float getHumidity() {
		return Humidity;
	}
	
	public void measurementsChanged() {
		setChanged();
		//notifyObservers();
		notifyObservers(new Wrapper(Temp,Humidity,Pressure));
	}
	*/

}