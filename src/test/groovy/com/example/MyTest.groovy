package com.example
import groovy.util.slurpersupport.GPathResult
import groovy.util.slurpersupport.NodeChild
import groovy.util.slurpersupport.NodeChildren
import org.junit.Test
/**
 * Created by kawasaki on 2015/06/21.
 */
class MyTest {

  @Test
  public void test() {


    GPathResult pom = new XmlSlurper().parse(getClass().getResourceAsStream('/test-pom.xml'))
    NodeChildren props = pom.getProperty('properties')
    props.children().each { NodeChild c ->
      println c.name()
      println c.text()
    }


  }

}
