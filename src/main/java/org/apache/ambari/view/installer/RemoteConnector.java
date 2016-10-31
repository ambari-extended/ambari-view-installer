package org.apache.ambari.view.installer;

import java.io.IOException;

public interface RemoteConnector {

  Status openConnection() throws IOException;

  Status closeConnection() throws IOException;


}
