package lille1.car3.durieux_gouzer.config;

/**
 * est le singloton permettant d'accéder aux configurations du projet
 * (registryPort et registryHost)
 * 
 * @author Durieux Thomas
 */
public interface RMIConfiguration extends PropertiesUtility {

	PropertiesUtility INSTANCE = new PropertiesUtilityImpl(
			RMIConfiguration.class.getResourceAsStream("rmi_config.ini"));
}
