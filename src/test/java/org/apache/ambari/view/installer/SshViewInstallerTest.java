package org.apache.ambari.view.installer;

import com.jcraft.jsch.JSchException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class SshViewInstallerTest {
    @Before
    public void setUp() throws Exception {



    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testInstall() throws Exception {
        RemoteHost remoteHost = new RemoteHost("192.168.64.101","vagrant",22);
        SshViewInstaller sshViewInstaller = new SshViewInstaller(remoteHost,
                "/Users/arajeev/IdeaProjects/ambari-vagrant/centos6.4/insecure_private_key",
                Optional.empty());

        RpmPackage aPackage = new RpmPackage("ftp://195.220.108.108/linux/fedora/linux/updates/24/x86_64/c/cloc-1.70-1.fc24.noarch.rpm");

        sshViewInstaller.openConnection();
        sshViewInstaller.install(aPackage,(a) -> System.out.println(a));
        sshViewInstaller.uninstall(aPackage,(a) -> System.out.println(a));
        sshViewInstaller.closeConnection();
    }


    @Test
    public void testCancel() throws JSchException, IOException, InterruptedException {
        RemoteHost remoteHost = new RemoteHost("192.168.64.101","vagrant",22);
        SshViewInstaller sshViewInstaller = new SshViewInstaller(remoteHost,
          "/Users/arajeev/IdeaProjects/ambari-vagrant/centos6.4/insecure_private_key",
          Optional.empty());

        Package aPackage = new Package() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getLocation() {
                return null;
            }

            @Override
            public String getVersion() {
                return null;
            }

            @Override
            public ArtifactType getArtifactType() {
                return null;
            }

            @Override
            public String getArtifactName() {
                return null;
            }

            @Override
            public List<String> getInstallCommands() {
                return Arrays.asList("ping www.google.com");
            }

            @Override
            public List<String> getUnInstallCommands() {
                return null;
            }
        };

        sshViewInstaller.openConnection();
        sshViewInstaller.install(aPackage,(a) -> System.out.println(a));
        Thread.sleep(10000);
        sshViewInstaller.stop();
        Thread.sleep(10000);


    }


}