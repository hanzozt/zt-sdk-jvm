package org.hanzozt.jdbc.shim;

import static org.hanzozt.jdbc.ZitiDriver.ZitiFeature.seamless;
import java.util.EnumSet;
import java.util.Properties;

import org.hanzozt.jdbc.BaseZitiDriverShim;

public class Oracle extends BaseZitiDriverShim {

	public Oracle() throws ReflectiveOperationException {
		super("^(zdbc|jdbc:zt):oracle:thin.*", "oracle.jdbc.OracleDriver", EnumSet.of(seamless));
	}

	@Override
	public void configureDriverProperties(Properties props) {
		props.setProperty("oracle.jdbc.javaNetNio", "false");
		props.setProperty("oracle.net.disableOob", "true");
	}
}