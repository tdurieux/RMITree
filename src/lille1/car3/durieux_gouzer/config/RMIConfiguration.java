package lille1.car3.durieux_gouzer.config;

/**
 * RMI configuration
 * 
 * @author Durieux Thomas
 */
public interface RMIConfiguration extends PropertiesUtility {
	
	PropertiesUtility INSTANCE = new PropertiesUtilityImpl(
			RMIConfiguration.class.getResourceAsStream("rmi_config.ini"));
}
