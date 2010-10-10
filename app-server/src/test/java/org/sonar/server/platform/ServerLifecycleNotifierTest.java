/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2009 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.server.platform;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.platform.ServerStartHandler;
import org.sonar.api.platform.ServerStopHandler;
import org.sonar.api.platform.Server;

import java.util.Date;

import static org.mockito.Mockito.*;

public class ServerLifecycleNotifierTest {

  private Server server;
  private ServerStartHandler start1;
  private ServerStartHandler start2;
  private ServerStopHandler stop1;
  private ServerStopHandler stop2;

  @Before
  public void before() {
    server = new FakeServer();
    start1 = mock(ServerStartHandler.class);
    start2 = mock(ServerStartHandler.class);
    stop1 = mock(ServerStopHandler.class);
    stop2 = mock(ServerStopHandler.class);
  }

  /**
   * see the explanation in the method ServerLifecycleNotifier.start()
   */
  @Test
  public void doNotNotifyWithTheStartMethod() {
    ServerLifecycleNotifier notifier = new ServerLifecycleNotifier(server, new ServerStartHandler[]{start1, start2}, new ServerStopHandler[]{stop2});
    notifier.start();

    verify(start1, never()).onServerStart(server);
    verify(start2, never()).onServerStart(server);
    verify(stop1, never()).onServerStop(server);
  }

  @Test
  public void notifyOnStart() {
    ServerLifecycleNotifier notifier = new ServerLifecycleNotifier(server, new ServerStartHandler[]{start1, start2}, new ServerStopHandler[]{stop2});
    notifier.notifyStart();

    verify(start1).onServerStart(server);
    verify(start2).onServerStart(server);
    verify(stop1, never()).onServerStop(server);
  }


  @Test
  public void notifyOnStop() {
    ServerLifecycleNotifier notifier = new ServerLifecycleNotifier(server, new ServerStartHandler[]{start1, start2}, new ServerStopHandler[]{stop1, stop2});
    notifier.stop();

    verify(start1, never()).onServerStart(server);
    verify(start2, never()).onServerStart(server);
    verify(stop1).onServerStop(server);
    verify(stop2).onServerStop(server);
  }
}

class FakeServer extends Server {

  public String getId() {
    return null;
  }

  public String getVersion() {
    return null;
  }

  public Date getStartedAt() {
    return null;
  }
}
