package io.teivah.keepachangelog;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.junit.Assert.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.Test;

public class ReleaseMojoTest {

  @Test
  public void getVersionWithSnapshot() {
    final String version = new ReleaseMojo().getVersion("1.0.0-SNAPSHOT");
    assertEquals("1.0.0", version);
  }

  @Test
  public void getVersionWithoutSnapshot() {
    final String version = new ReleaseMojo().getVersion("1.0.0");
    assertEquals("1.0.0", version);
  }

  @Test
  public void getCurrentDate() {
    Clock clock = Clock.fixed(Instant.ofEpochSecond(0), ZoneId.systemDefault());
    final String currentDate = new ReleaseMojo().getCurrentDate(clock);
    assertEquals("1970-01-01", currentDate);
  }
}
