pipeline {
    agent any

    environment {
        PROJECT_NAME = 'proyecto-m2'
        SONARQUBE_ENV = 'SonarQubeServer'
    }

    tools {
        maven 'Maven'
        jdk 'java-17'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Unit Tests') {
            steps {
                dir('backend') {
                    sh 'mvn clean verify -DskipTests=false'
                }
            }
        }

        stage('SonarQube Analysis') {
    steps {
        withSonarQubeEnv("${SONARQUBE_ENV}") {
            withCredentials([string(credentialsId: 'tokensonar', variable: 'SONAR_TOKEN')]) {
                sh "sonar-scanner -Dsonar.token=$SONAR_TOKEN"
            }
        }
    }
}


        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                    echo "✅ Análisis SonarQube terminado correctamente y pasó el Quality Gate"
                }
            }
        }
    }

    post {
        failure {
            mail to: 'magic@productowner.com, herman@unis.edu.gt',
                 subject: "❌ Falló pipeline en rama ${env.BRANCH_NAME}",
                 body: "La ejecución del pipeline falló en la etapa ${env.STAGE_NAME}. Por favor revisarlo."
        }
    }
}
