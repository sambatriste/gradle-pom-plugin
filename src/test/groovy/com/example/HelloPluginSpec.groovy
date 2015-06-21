package com.example

import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class HelloPluginSpec extends Specification {

  def "apply() should load the plugin"() {
    given:
    def project = ProjectBuilder.builder().build()
    def pomFile = new File(project.projectDir, 'pom.xml')
    def input = getClass().getResourceAsStream("/test-pom.xml");
    pomFile.text = input.newReader().text

    when:
    project.with {
      apply plugin: 'com.example.hello'
    }

    then:
    project.plugins.hasPlugin(HelloPlugin)
    def testCompile = project.configurations.getByName('testCompile')
    testCompile.dependencies.size() == 1
    println testCompile.dependencies.getAt(0)
    project.group == "com.example"
    project.version == "1.2.3"

    project.description == "This is test."

    println project.extensions.getByName('mvnProperties')

  }



}
