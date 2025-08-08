pipeline {
    agent any

    environment {
        PROJECT_NAME = 'proyecto-m2'
        SONARQUBE_ENV = 'SonarQubeServer'
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
                dir('backend') {
                    sh 'mvn clean verify -DskipTests=false'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                dir('backend') {
                    withSonarQubeEnv("${SONARQUBE_ENV}") {
                        withCredentials([string(credentialsId: 'tokensonar', variable: 'SONAR_TOKEN')]) {
                            sh '''
                                mvn sonar:sonar \
                                    -Dsonar.token=$SONAR_TOKEN \
                                    -Dsonar.projectKey=${PROJECT_NAME}-${BRANCH_NAME} \
                                    -Dsonar.projectName=${PROJECT_NAME}-${BRANCH_NAME}
                            '''
                        }
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir('backend') {
                    sh "docker build -t ${DOCKER_IMAGE} ."
                }
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
            }
        }
    }

    post {
        failure {
            mail to: 'magic@productowner.com, herman@unis.edu.gt',
                 subject: "Falló pipeline en rama ${env.BRANCH_NAME}",
                 body: "La ejecución del pipeline falló en la etapa ${env.STAGE_NAME}. Requiere atención inmediata."
        }
    }
}
