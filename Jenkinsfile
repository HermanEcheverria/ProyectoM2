pipeline {
    agent any

    environment {
        PROJECT_NAME   = 'proyecto-m2'
        SONARQUBE_ENV  = 'SonarQubeServer' 
    }

    tools {
        maven 'Maven'
        jdk   'java-17'
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

        stage('SonarQube Analysis (Backend)') {
            steps {
                dir('backend') {
                    withSonarQubeEnv("${SONARQUBE_ENV}") {
                        withCredentials([string(credentialsId: 'tokensonar', variable: 'SONAR_TOKEN')]) {
                            sh '''
                              test -f target/site/jacoco/jacoco.xml || { echo "No se encontró target/site/jacoco/jacoco.xml. Revisa JaCoCo."; exit 1; }
                              mvn -B sonar:sonar \
                                -Dsonar.token=$SONAR_TOKEN \
                                -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                            '''
                        }
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                    echo "SonarQube OK: Quality Gate aprobado"
                }
            }
        }
    }

    post {
        failure {
            mail to: 'magic@productowner.com, herman@unis.edu.gt',
                 subject: " Falló pipeline en rama ${env.BRANCH_NAME}",
                 body: "La ejecución del pipeline falló en la etapa ${env.STAGE_NAME}. Revisar Jenkins."
        }
    }
}
