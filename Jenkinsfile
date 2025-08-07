pipeline {
  agent any

  environment {
    MAVEN_HOME = tool 'Maven 3'
    SONARQUBE = 'SonarQube'
    NODEJS_HOME = tool name: 'Node 20.19.2', type: 'jenkins.plugins.nodejs.tools.NodeJSInstallation'

    // Variables de entorno para base de datos y puertos (se configuran en stage 'Set Env Vars')
    DB_HOST = ''
    DB_PORT = ''
    BACKEND_PORT = ''
    FRONTEND_PORT = ''
    DOCKER_TAG = ''
  }

  stages {

    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Set Env Vars') {
      steps {
        script {
          def branch = env.GIT_BRANCH?.replaceFirst(/^origin\//, '')
          switch (branch) {
            case 'dev':
              env.DB_HOST = 'db-dev'
              env.DB_PORT = '1521'
              env.BACKEND_PORT = '8080'
              env.FRONTEND_PORT = '5173'
              env.DOCKER_TAG = 'dev'
              break
            case 'uat':
              env.DB_HOST = 'db-uat'
              env.DB_PORT = '1522'
              env.BACKEND_PORT = '8081'
              env.FRONTEND_PORT = '5174'
              env.DOCKER_TAG = 'uat'
              break
            case 'main':
              env.DB_HOST = 'db-prod'
              env.DB_PORT = '1523'
              env.BACKEND_PORT = '8082'
              env.FRONTEND_PORT = '5175'
              env.DOCKER_TAG = 'prod'
              break
            default:
              error("Rama no reconocida: ${branch}")
          }
        }
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

    stage('Docker Build & Deploy') {
      steps {
        sh '''
        docker build -t proyectom2-backend:$DOCKER_TAG ./backend
        docker build -t proyectom2-frontend:$DOCKER_TAG ./frontend

        docker run -d --rm --name backend-$DOCKER_TAG -p $BACKEND_PORT:8080 -e DB_HOST=$DB_HOST -e DB_PORT=$DB_PORT proyectom2-backend:$DOCKER_TAG
        docker run -d --rm --name frontend-$DOCKER_TAG -p $FRONTEND_PORT:5173 proyectom2-frontend:$DOCKER_TAG
        '''
      }
    }
  }
}
