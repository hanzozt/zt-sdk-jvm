package org.hanzozt.jdbc.shim;

import java.util.EnumSet;
import java.util.Properties;
import org.hanzozt.jdbc.BaseZitiDriverShim;
import org.hanzozt.jdbc.ZitiDriver.ZitiFeature;

public class Postgresql extends BaseZitiDriverShim {
  public Postgresql() throws ReflectiveOperationException {
    super("^(zdbc|jdbc:ziti):postgresql.*", "org.postgresql.Driver", EnumSet.noneOf(ZitiFeature.class));
  }

  @Override
  public void configureDriverProperties(Properties props) {
    props.setProperty("socketFactory", "org.hanzozt.net.ZitiSocketFactory");
  }
}
