# Introduction

This Java project, called 'Grep App', aims to replicate the functionality of the 'grep' command from Linux, removing the constraint of only being usable on a Linux platform. By implementing it in Java, the 'grep' functionality becomes cross-platform, as Java is platform-independent. The project was developed using Core Java 8, utilizing lambda functions introduced in Java 8 and Stream APIs to enhance performance. Maven was used to manage dependencies, project structure, and build lifecycle. Additionally, the project was deployed using Docker.

# Quick Start

There are two methods to starting this file.

### 1. Jar file

```bash
mvn clean package
java -jar ./target/grep-1.0-SNAPSHOT.jar [arg1] [arg2] [arg3]
```

### 2. Docker Image

```bash
docker pull bleuchair/grep
docker run --rm \
-v `pwd`/data:/data -v `pwd`/log:/log \
bleuchair/grep [arg1] [arg2] [arg3]
```

# Implemenation

## Pseudocode

```
matchedLines = []
for file in listFiles
    for line in readLines
        if containsPattern
writeToFile
```

## Performance Issue

When processing a large data file that exceeded the amount of heap memory provided to the JVM, it ran into a outOfMemory error. After some diagnosing I discovered where exactly the exception occurred. It occurred when reading the lines and storing it into a list hence the memory issue. To resolve this instead of storing the lines into a list, I changed it to return a stream instead so that it no longer had to store the element causing the memory issue.

# Test

The testing of this application was performed manually. I created a sample data file, and ran the application and then I compared the results against what I was expecting.

# Deployment

Created a docker file

```bash
cat > Dockerfile << EOF
FROM openjdk:8-alpine
COPY target/grep*.jar /usr/local/app/grep/lib/grep.jar
ENTRYPOINT ["java","-jar","/usr/local/app/grep/lib/grep.jar"]
EOF
```

Built the image, and uploaded it to dockerhub with the image name 'bleuchair/grep'

# Improvement

- Automated Testing
- User Interface (UI)
- Optimization and Profiling
