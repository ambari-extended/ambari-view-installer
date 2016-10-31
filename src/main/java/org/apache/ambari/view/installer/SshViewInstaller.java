package org.apache.ambari.view.installer;

import com.google.common.collect.Lists;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * Install view over ssh
 */
public class SshViewInstaller extends RemoteViewInstaller {

  private UserInfo userInfo;
  JSch jsch = new JSch();
  private Session session;
  List<Future<?>> tasks = Lists.newArrayList();
  // List of commands to be issued over SSH


  public SshViewInstaller(RemoteHost remoteHost, String identityFile, Optional<String> passPhrase) throws JSchException {
    super(remoteHost);
    Objects.requireNonNull(identityFile);
    if (passPhrase.isPresent()) {
      jsch.addIdentity(identityFile, passPhrase.get());
    } else
      jsch.addIdentity(identityFile);
    jsch.setConfig("StrictHostKeyChecking", "no");
  }

  public SshViewInstaller(RemoteHost remoteHost, UserInfo userInfo) {
    super(remoteHost);
    Objects.requireNonNull(userInfo);
    this.remoteHost = remoteHost;
    this.userInfo = userInfo;
  }


  @Override
  public Status install(Package aPackage, Consumer<String> listener) throws InterruptedException {
    List<String> installCommands = aPackage.getInstallCommands();
    runCommands(listener, installCommands);
    return Status.SUCCESS;

  }

  private void runCommands(Consumer<String> listener, List<String> installCommands) throws InterruptedException {
    ExecutorService executorService = Executors.newSingleThreadExecutor();

    installCommands.stream().forEach(c -> {
        try {
          tasks.add(executorService.submit(getRunnable(c,listener)));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    );

    if (!executorService.isShutdown())
      executorService.shutdown();
  }

  private Runnable getRunnable(String c,Consumer<String> listener) {
    return () -> {
      try {
        Channel channel = session.openChannel("exec");
        ChannelExec channelExec = (ChannelExec) channel;
        channelExec.setCommand(c);
        channel.setInputStream(null);
        channel.connect(20 * 1000);

        InputStreamReader in = new InputStreamReader(
          new SequenceInputStream(channelExec.getInputStream(),channelExec.getErrStream()));
        BufferedReader bufferedReader = new BufferedReader(in);
        for (String line; (line = bufferedReader.readLine()) != null; ) {
          listener.accept(line);
        }
        bufferedReader.close();
        channel.disconnect();
      } catch (Exception e) {
        listener.accept(e.getCause().getMessage());
      }

    };
  }

  @Override
  public Status uninstall(Package aPackage, Consumer<String> listener) throws InterruptedException {
    List<String> unInstallCommands = aPackage.getUnInstallCommands();
    runCommands(listener, unInstallCommands);
    return Status.SUCCESS;
  }


  @Override
  public Status stop() {
    tasks.forEach( f -> f.cancel(true));
    return Status.SUCCESS;
  }


  @Override
  public Status openConnection() throws IOException {
    try {
      session = jsch.getSession(remoteHost.getUserName(), remoteHost.getHost(), remoteHost.getPort());
      session.connect();
      return Status.SUCCESS;

    } catch (JSchException e) {
      e.printStackTrace();
      return Status.FAIL;
    }
  }

  @Override
  public Status closeConnection() throws IOException {
    session.disconnect();
    return Status.SUCCESS;
  }
}
