package org.apache.ambari.view.installer;

public class RemoteHost {

  private String host;
  private int port;
  private String userName;

  public RemoteHost(String host, String userName, int port) {
    this.host = host;
    this.userName = userName;
    this.port = port;
  }


  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public String getUserName() {
    return userName;
  }


  @Override
  public String toString() {
    return "RemoteHost{" +
      "ip='" + host + '\'' +
      ", port=" + port +
      ", userName='" + userName + '\'' +
      '}';
  }
}
