pipeline {
  agent any

  environment {
    MAVEN_HOME = tool 'Maven 3'
    SONARQUBE = 'SonarQube'
    NODEJS_HOME = tool name: 'Node 18', type: 'jenkins.plugins.nodejs.tools.NodeJSInstallation'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build Backend (Quarkus)') {
      steps {
        dir('backend') {
          sh "${MAVEN_HOME}/bin/mvn clean package -DskipTests"
        }
      }
    }

    stage('Unit Tests Backend') {
      steps {
        dir('backend') {
          sh "${MAVEN_HOME}/bin/mvn test"
        }
      }
    }

    stage('SonarQube Analysis') {
      steps {
        dir('backend') {
          withSonarQubeEnv("${SONARQUBE}") {
            sh "${MAVEN_HOME}/bin/mvn sonar:sonar -Dsonar.projectKey=ProyectoM2 -Dsonar.projectName=ProyectoM2"
          }
        }
      }
    }

    stage('Build Frontend (Vue.js)') {
      steps {
        dir('frontend') {
          withEnv(["PATH+NODE=${NODEJS_HOME}/bin"]) {
            sh 'npm install'
            sh 'npm run build'
          }
        }
      }
    }

    stage('SonarQube Quality Gate') {
      steps {
        timeout(time: 1, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
        }
      }
    }
  }
}
