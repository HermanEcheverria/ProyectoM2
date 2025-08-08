pipeline {
    agent any

    environment {
        PROJECT_NAME = 'proyecto-m2'
        SONARQUBE_ENV = 'SonarQubeServer' // nombre definido en Jenkins para SonarQube
        DOCKER_IMAGE = "hermanecheverria/${PROJECT_NAME}:${env.BRANCH_NAME}"
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

        stage('Build & Test') {
            steps {
                sh 'mvn clean verify -DskipTests=false'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv("${SONARQUBE_ENV}") {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Push Docker Image') {
            when {
                anyOf {
                    branch 'main'
                    branch 'uat'
                    branch 'dev'
                }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                        echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
                        docker push ${DOCKER_IMAGE}
                    """
                }
            }
        }

        stage('Deploy') {
            steps {
                echo "Desplegando ambiente: ${env.BRANCH_NAME}"
                // Aquí puedes usar ssh, docker-compose o kubectl
            }
        }
    }

    post {
        failure {
            mail to: 'magic@productowner.com, herman@unis.edu.gt',
                 subject: "Falló pipeline en ${env.BRANCH_NAME}",
                 body: "El pipeline falló en la etapa ${env.STAGE_NAME}. Revisión necesaria."
        }
    }
}
