package org.apache.ambari.view.installer;

import java.util.Arrays;
import java.util.List;


public class RpmPackage implements Package {

  private final String location;
  private String name ;
  private String version;
  private String artifactName;
  private String executableName;

  public void setName(String name) {
    this.name = name;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public void setArtifactName(String artifactName) {
    this.artifactName = artifactName;
  }

  public void setExecutableName(String executableName) {
    this.executableName = executableName;
  }

  public RpmPackage(String location) {
    this.location = location;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getLocation() {
    return location;
  }

  @Override
  public String getExecutableName() {
    return executableName;
  }

  @Override
  public String getVersion() {
    return version;
  }

  @Override
  public ArtifactType getArtifactType() {
    return ArtifactType.RPM;
  }

  @Override
  public String getArtifactName() {
    return artifactName;
  }

  @Override
  public List<String> getInstallCommands() {
    return Arrays.asList(
      "wget -c "+ getLocation(),
      "sudo rpm -i "+ getArtifactName(),
      "rm "+ getArtifactName()
    );

  }

  @Override
  public List<String> getUnInstallCommands() {
    return Arrays.asList("yum remove "+getExecutableName());
  }
}
