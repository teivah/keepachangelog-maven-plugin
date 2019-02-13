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

import static io.teivah.keepachangelog.MojoUtils.LINE_UNRELEASED;
import static io.teivah.keepachangelog.MojoUtils.UNRELEASED;
import static io.teivah.keepachangelog.MojoUtils.getChangelogFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import net.peachjean.slf4j.mojo.AbstractLoggingMojo;

@Mojo(name = "check")
public class CheckMojo extends AbstractLoggingMojo {

  @Parameter(readonly = true, defaultValue = "${project}")
  private MavenProject project;

  @Override
  protected void executeWithLogging() throws MojoExecutionException {
    final File f = getChangelogFile(project);
    if (f == null) {
      return;
    }

    getLog().info("Found one changelog file in " + f);

    try {
      List<String> fileContent = new ArrayList<>(Files.readAllLines(f.toPath(), StandardCharsets.UTF_8));

      final String line = fileContent.get(LINE_UNRELEASED);
      if (UNRELEASED.equals(line)) {
        return;
      } else {
        getLog().error("Changelog file does not respect the 1.0.0 format.");
        throw new MojoExecutionException("Changelog file does not respect the 1.0.0 format.");
      }
    } catch (FileNotFoundException e) {
      getLog().error("Unable to read file " + f.getAbsolutePath(), e);
      throw new MojoExecutionException(e.getMessage());
    } catch (IOException e) {
      getLog().error("Unable to read file " + f.getAbsolutePath(), e);
      throw new MojoExecutionException(e.getMessage());
    }
  }
}
