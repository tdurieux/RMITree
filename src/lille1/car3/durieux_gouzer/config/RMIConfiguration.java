package lille1.car3.durieux_gouzer.config;

/**
 * is a singleton used to access RMI configuration (registry port and registry
 * host)
 * 
 * @author Durieux Thomas
 */
public interface RMIConfiguration extends PropertiesUtility {

	PropertiesUtility INSTANCE = new PropertiesUtilityImpl(
			RMIConfiguration.class.getResourceAsStream("rmi_config.ini"));
}
