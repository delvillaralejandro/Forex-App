import java.math.BigDecimal;
import java.util.Observable;
import java.util.Observer;

public class ClienteFree implements Observer{
	
	private int clientID;
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
			
			/*
			 * evaluar(viejoValor - nuevoValor >= rangoPips){
			 * 		enviarPopup();
			 * }
			 */
			
			printParams();
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