import java.util.Observable;
import java.util.Observer;

public class ClienteFree implements Observer{

	
	public ClienteFree(Observable quote) {
		quote.addObserver(this);
	}
	public void show(float valor) {
		System.out.println("DisplayA muestra: " + valor + " grados de presion");
	}
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	/*@Override
	public void update(Observable observable, Object arg) {
		if(arg == null) {
			System.out.println("Null Arg A");
		}else 
		{
			lastPressure = currentPressure;
			currentPressure = wrap.Pressure;
			show(currentPressure);
			}
		}
	}*/

}