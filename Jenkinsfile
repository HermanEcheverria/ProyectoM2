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
                            sh 'mvn sonar:sonar -Dsonar.token=$SONAR_TOKEN'
                        }
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                    echo "Análisis SonarQube terminado correctamente y pasó el Quality Gate"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir('backend') {
                    sh "docker build -f Dockerfile.jvm -t ${DOCKER_IMAGE} ."
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
                script {
                    def containerName = "${PROJECT_NAME}-${env.BRANCH_NAME}"
                    def imageName = "${DOCKER_IMAGE}"
                    def port = ""

                    // Asignación de puertos por rama
                    if (env.BRANCH_NAME == "dev") {
                        port = "8082"
                    } else if (env.BRANCH_NAME == "uat") {
                        port = "8083"
                    } else if (env.BRANCH_NAME == "main") {
                        port = "8084"
                    } else {
                        error " Rama no reconocida para despliegue: ${env.BRANCH_NAME}"
                    }

                    echo "Desplegando ambiente ${env.BRANCH_NAME} en el contenedor ${containerName} en el puerto ${port}..."

                    // Elimina contenedor anterior si existe
                    sh "docker rm -f ${containerName} || true"

                    // Ejecuta nuevo contenedor
                    sh """
                        docker run -d \
                            --name ${containerName} \
                            --restart=unless-stopped \
                            -p ${port}:8080 \
                            --network red-proyecto \
                            ${imageName}
                    """
                }
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
