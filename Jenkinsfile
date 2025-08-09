pipeline {
    agent any

    environment {
        PROJECT_NAME  = 'proyecto-m2'       // nombre base del proyecto
        SONARQUBE_ENV = 'SonarQubeServer'   // nombre del server Sonar configurado en Jenkins
    }

    tools {
        maven 'Maven'
        jdk   'java-17'
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Build & Unit Tests (Backend)') {
            steps {
                dir('backend') {
                    sh 'mvn clean verify -DskipTests=false'
                    // Sanity check: asegurarnos que JaCoCo generó el XML
                    sh '''
                      ls -lah target/site/jacoco || true
                      test -f target/site/jacoco/jacoco.xml || { 
                        echo "❌ No se encontró backend/target/site/jacoco/jacoco.xml. Revisa JaCoCo en el POM.";
                        exit 1;
                      }
                    '''
                }
            }
        }

       stage('SonarQube Analysis (repo completo)') {
  steps {
    script {
      def scannerHome = tool 'SonarScanner' 
      withSonarQubeEnv('SonarQubeServer') {
        withCredentials([string(credentialsId: 'tokensonar', variable: 'SONAR_TOKEN')]) {
          sh """
            export PATH="${scannerHome}/bin:\\$PATH"
            sonar-scanner -Dsonar.token=\\$SONAR_TOKEN
          """
        }
      }
    }
  }
}



        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                    echo "SonarQube OK (Quality Gate) para ${env.BRANCH_NAME}"
                }
            }
        }
    }

    post {
        failure {
            mail to: 'magic@productowner.com, herman@unis.edu.gt',
                 subject: " Falló pipeline en rama ${env.BRANCH_NAME}",
                 body: "El pipeline falló en la etapa ${env.STAGE_NAME}. Revisar Jenkins."
        }
    }
}
