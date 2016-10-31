package org.apache.ambari.view.installer;

import com.jcraft.jsch.JSchException;

import java.util.function.Consumer;

public abstract class RemoteViewInstaller implements RemoteConnector {

  protected RemoteHost remoteHost;

  public RemoteViewInstaller(RemoteHost remoteHost) {
    this.remoteHost = remoteHost;
  }

  public RemoteHost getRemoteHost() {
    return remoteHost;
  }


  /**
   *
   * @param aPackage the package to be installed
   * @param listener Consumer which is used to get logs and messages
   * @return Status
   * @throws InterruptedException
   */
  public abstract Status install(Package aPackage, Consumer<String> listener) throws InterruptedException, JSchException;


  /**
   *
   * @param aPackage the package to be removed
   * @param listener Consumer which is used to get logs and messages
   * @return Status
   * @throws InterruptedException
   */
  public abstract Status uninstall(Package aPackage, Consumer<String> listener) throws InterruptedException;

  /**
   * Stop execution of an install/uninstall task
   * @return
   */
  public abstract Status stop();


}
