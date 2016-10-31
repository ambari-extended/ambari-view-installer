package org.apache.ambari.view.installer;

import java.util.List;

public interface Package {

  String getName();

  String getLocation();

  String getExecutableName();

  String getVersion();

  ArtifactType getArtifactType();

  String getArtifactName();

  List<String> getInstallCommands();

  List<String> getUnInstallCommands();
}
