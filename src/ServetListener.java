import java.util.Timer;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


/**
 * Application Lifecycle Listener implementation class ServetListener
 *
 */
@WebListener
public class ServetListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public ServetListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    	//Timer tim = new Timer();
    	//refreshThread rT = new refreshThread();
    	
    	//tim.scheduleAtFixedRate(rT, 0, 3600000);
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
    }
	
}
