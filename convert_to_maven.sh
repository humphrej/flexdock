FLEXDOCK_VERSION=1.2.3-SNAPHOT

mkdir -p maven/flexdock/src/main/java maven/flexdock/src/test/java
mkdir -p maven/flexdock-demo/src/main/java maven/flexdock-demo/src/test/java

cp -R src/java/core/* maven/flexdock/src/main/java
cp -R src/java/dockbar/* maven/flexdock/src/main/java
cp -R src/java/drag/* maven/flexdock/src/main/java
cp -R src/java/perspective/* maven/flexdock/src/main/java
cp -R src/java/plaf/* maven/flexdock/src/main/java
cp -R src/java/view/* maven/flexdock/src/main/java

cp -R src/java/demo/* maven/flexdock-demo/src/main/java
cp -R src/java/test/* maven/flexdock-demo/src/test/java

cat > maven/flexdock/pom.xml <<_eof
<?xml version="1.0"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.scilab</groupId>
  <artifactId>flexdock</artifactId>
  <name>Flexdock</name>
  <packaging>jar</packaging>
  <version>${FLEXDOCK_VERSION}</version>
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>2.0.2</version>
        <configuration>
          <source>1.5</source>
          <target>1.5</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-source-plugin</artifactId>
        <version>2.2.1</version>
        <executions>
          <execution>
            <id>attach-sources</id>
            <goals>
              <goal>jar</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-javadoc-plugin</artifactId>
        <version>2.9</version>
        <executions>
          <execution>
            <id>attach-javadocs</id>
            <goals>
              <goal>jar</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
  <dependencies>
    <dependency>
      <groupId>skinlf</groupId>
      <artifactId>skinlf</artifactId>
      <version>1.2.3</version>
    </dependency>
    <dependency>
      <groupId>com.jgoodies</groupId>
      <artifactId>jgoodies-looks</artifactId>
      <version>2.5.2</version>
    </dependency>
  </dependencies>
</project>

_eof


