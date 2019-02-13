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

import static io.teivah.keepachangelog.MojoUtils.SNAPSHOT;
import static io.teivah.keepachangelog.MojoUtils.getChangelogFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Clock;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import net.peachjean.slf4j.mojo.AbstractLoggingMojo;

@Mojo(name = "release")
public class ReleaseMojo extends AbstractLoggingMojo {

  private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
      .withZone(ZoneOffset.UTC);
  private static final int LINE_UNRELEASED = 6;

  @Parameter(readonly = true, defaultValue = "${project}")
  private MavenProject project;

  @Override
  protected void executeWithLogging() throws MojoExecutionException, MojoFailureException {
    final File f = getChangelogFile(project);
    if (f == null) {
      return;
    }

    getLog().info("Found one changelog file in " + f);

    try {
      List<String> fileContent = new ArrayList<>(Files.readAllLines(f.toPath(), StandardCharsets.UTF_8));

      final String projectVersion = getVersion(project.getVersion());
      final String currentDate = getCurrentDate(Clock.systemUTC());
      final String l = String.format("## [%s] - %s", projectVersion, currentDate);
      getLog().info("Replacing by version " + l);

      fileContent.set(LINE_UNRELEASED, l);
      Files.write(f.toPath(), fileContent, StandardCharsets.UTF_8);
    } catch (FileNotFoundException e) {
      getLog().error("Unable to read file " + f.getAbsolutePath(), e);
      throw new MojoExecutionException(e.getMessage());
    } catch (IOException e) {
      getLog().error("Unable to read file " + f.getAbsolutePath(), e);
      throw new MojoExecutionException(e.getMessage());
    }
  }

  String getVersion(String projectVersion) {
    if (projectVersion.endsWith(SNAPSHOT)) {
      return projectVersion.substring(0, projectVersion.length() - SNAPSHOT.length());
    } else {
      return projectVersion;
    }
  }

  String getCurrentDate(Clock clock) {
    return DATE_TIME_FORMATTER.format(clock.instant());
  }
}
