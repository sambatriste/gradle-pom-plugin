package com.example

import java.util.regex.Pattern

/**
 * Created by kawasaki on 2015/06/21.
 */
class Dependency {
  final String groupId
  final String artifactId
  final String version
  final String scope

  Dependency(String groupId, String artifactId, String version, String scope) {
    this.groupId = groupId
    this.artifactId = artifactId
    this.version = version
    this.scope = scope
  }


  String toString() {
    return "${scope} ${gradleNotation}"
  }

  def getGradleNotation() {
    //return [group: groupId, name: artifactId, version: version]
    return "$groupId:$artifactId:$version"

  }


  static class DependencyBuilder {
    private final Map mvnProperties
    DependencyBuilder(Map mvnProperties) {
      this.mvnProperties = mvnProperties
    }

    Dependency build(String groupId, String artifactId, String version, String scope) {
      return new Dependency(
              evaluateValue(groupId),
              evaluateValue(artifactId),
              evaluateValue(version),
              evaluateScope(scope)
      )
    }

    private static String evaluateScope(String scope) {
      switch (scope) {
        case null:
        case "":
          return "compile"
        case "test":
          return "testCompile"
        default:
          return scope
      }
    }

    String evaluateValue(String value) {
      def m = Pattern.compile(/\$\{(.+)\}/).matcher(value)
      if (!m.matches()) {
        return value
      }
      def keyOfProps = m.group(1)
      assert mvnProperties.containsKey(keyOfProps)
      return mvnProperties.get(keyOfProps)
    }

  }
}


