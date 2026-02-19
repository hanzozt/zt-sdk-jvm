package org.hanzozt.jdbc.shim;

import java.util.EnumSet;
import org.hanzozt.jdbc.BaseZitiDriverShim;
import org.hanzozt.jdbc.ZitiDriver.ZitiFeature;

public class Mysql extends BaseZitiDriverShim {

  public Mysql() throws ReflectiveOperationException {
    super("^(zdbc|jdbc:zt):mysql.*", "com.mysql.cj.jdbc.Driver", EnumSet.of(ZitiFeature.seamless));
  }
}
