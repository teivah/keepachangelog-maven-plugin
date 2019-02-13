package io.teivah.keepachangelog;

import java.io.File;

import org.apache.maven.project.MavenProject;

public interface MojoUtils {

  static final String CHANGELOG = "CHANGELOG.md";
  static final String SNAPSHOT = "-SNAPSHOT";
  static final String UNRELEASED = "## [Unreleased]";
  static final int LINE_UNRELEASED = 6;

  static File getChangelogFile(MavenProject project) {
    final String absolutePath = project.getBasedir().getAbsolutePath();
    File f = new File(absolutePath + File.separator + CHANGELOG);
    if (!f.exists() || f.isDirectory()) {
      return null;
    }
    return f;
  }
}
