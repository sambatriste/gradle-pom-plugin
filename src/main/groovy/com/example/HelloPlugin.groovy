package com.example
import groovy.util.slurpersupport.GPathResult
import groovy.util.slurpersupport.NodeChild
import org.gradle.api.Plugin
import org.gradle.api.Project

class HelloPlugin implements Plugin<Project> {
  @Override
  void apply(Project project) {
    project.apply plugin: 'java'
    def pomFile = new File(project.projectDir, 'pom.xml')
    GPathResult pom = new XmlSlurper().parse(pomFile)

    project.group = pom.groupId
    project.version = pom.version
    project.description = pom.description

    def mvnProperties = [:]
    pom.getProperty('properties').children().each { NodeChild c ->
      mvnProperties.put(c.name(), c.text())
    }
    project.extensions.add('mvnProperties', mvnProperties)
    def pomDependencies = getDependenciesFrom(pom, mvnProperties);
    pomDependencies.each { Dependency d ->
      project.dependencies {
        project.logger.lifecycle(d.toString())
        it.add(d.scope, d.gradleNotation)
      }
    }
  }



  static List<Dependency> getDependenciesFrom(GPathResult pom, Map mvnProperties) {
    pom.dependencies.dependency.collect {
      new Dependency.DependencyBuilder(mvnProperties).build(
                     it.groupId.text() as String,
                     it.artifactId.text() as String,
                     it.version.text() as String,
                     it.scope.text() as String)
    }
  }
}
