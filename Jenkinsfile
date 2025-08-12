pipeline {
  agent any

  environment {
    PROJECT_NAME  = 'proyecto-m2'
    SONARQUBE_ENV = 'SonarQubeServer'
  }

  tools {
    maven 'Maven'
    jdk   'java-17'
  }

  stages {
    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Install Frontend deps (root)') {
      steps {
        script {
          nodejs('Node 20') {
            sh '''
              if [ -f package.json ]; then
                echo "Instalando dependencias del front en la raíz..."
                npm ci --no-audit --no-fund
              else
                echo "No hay package.json en la raíz."
              fi
            '''
          }
        }
      }
    }

    stage('Build & Unit Tests (Backend)') {
      steps {
        dir('backend') {
          sh 'mvn -q clean verify -DskipTests=false'
          sh 'mvn -q jacoco:report'
          sh '''
            test -f target/site/jacoco/jacoco.xml || {
              echo "No se encontró backend/target/site/jacoco/jacoco.xml";
              exit 1;
            }
          '''
        }
      }
    }

    stage('SonarQube Analysis - Backend (con cobertura)') {
      when {
        expression { ['dev','uat','prod','main','master'].contains(env.BRANCH_NAME ?: 'prod') }
      }
      steps {
        script {
          def scannerHome = tool 'SonarScanner'
          withSonarQubeEnv("${SONARQUBE_ENV}") {
            withCredentials([string(credentialsId: 'tokensonar', variable: 'SONAR_TOKEN')]) {
              withEnv(["PATH+SONAR=${scannerHome}/bin"]) {
                sh 'rm -rf .scannerwork backend/.scannerwork || true'
                dir('backend') {
                  def raw = env.BRANCH_NAME ?: 'prod'
                  def targetEnv = (raw in ['main','master']) ? 'prod' : raw   // mapear main/master → prod
                  def key   = "${PROJECT_NAME}-backend-${targetEnv}"
                  def pname = "${PROJECT_NAME} :: Backend [${targetEnv}]"

                  withEnv(["SONAR_PROJECT_KEY=${key}", "SONAR_PROJECT_NAME=${pname}", "TARGET_ENV=${targetEnv}"]) {
                    sh '''
                      EXTRA=""
                      if [ "$TARGET_ENV" = "prod" ]; then
                        VER=$(git describe --tags --always 2>/dev/null || echo "$BUILD_NUMBER")
                        EXTRA="-Dsonar.projectVersion=$VER"
                      fi
                      sonar-scanner -Dsonar.token=$SONAR_TOKEN \
                                    -Dsonar.projectKey=$SONAR_PROJECT_KEY \
                                    -Dsonar.projectName="$SONAR_PROJECT_NAME" $EXTRA
                    '''
                  }
                }
              }
            }
          }
        }
      }
    }

    stage('Quality Gate - Backend') {
      when {
        expression { ['dev','uat','prod','main','master'].contains(env.BRANCH_NAME ?: 'prod') }
      }
      steps {
        dir('backend') {
          timeout(time: 10, unit: 'MINUTES') {
            waitForQualityGate abortPipeline: true
            echo "Quality Gate BACKEND OK"
          }
        }
      }
    }

    stage('SonarQube Analysis - Frontend (sin cobertura)') {
      when {
        allOf {
          expression { fileExists('sonar-project.properties') && fileExists('package.json') }
          expression { ['dev','uat','prod','main','master'].contains(env.BRANCH_NAME ?: 'prod') }
        }
      }
      steps {
        script {
          def scannerHome = tool 'SonarScanner'
          withSonarQubeEnv("${SONARQUBE_ENV}") {
            withCredentials([string(credentialsId: 'tokensonar', variable: 'SONAR_TOKEN')]) {
              withEnv(["PATH+SONAR=${scannerHome}/bin"]) {
                sh 'rm -rf .scannerwork backend/.scannerwork || true'
                def raw = env.BRANCH_NAME ?: 'prod'
                def targetEnv = (raw in ['main','master']) ? 'prod' : raw
                def key   = "${PROJECT_NAME}-frontend-${targetEnv}"
                def pname = "${PROJECT_NAME} :: Frontend [${targetEnv}]"

                withEnv(["SONAR_PROJECT_KEY=${key}", "SONAR_PROJECT_NAME=${pname}", "TARGET_ENV=${targetEnv}"]) {
                  sh '''
                    EXTRA=""
                    if [ "$TARGET_ENV" = "prod" ]; then
                      VER=$(git describe --tags --always 2>/dev/null || echo "$BUILD_NUMBER")
                      EXTRA="-Dsonar.projectVersion=$VER"
                    fi
                    sonar-scanner -Dsonar.token=$SONAR_TOKEN \
                                  -Dsonar.projectKey=$SONAR_PROJECT_KEY \
                                  -Dsonar.projectName="$SONAR_PROJECT_NAME" $EXTRA
                  '''
                }
              }
            }
          }
        }
      }
    }

    stage('Quality Gate - Frontend') {
      when {
        allOf {
          expression { fileExists('sonar-project.properties') && fileExists('package.json') }
          expression { ['dev','uat','prod','main','master'].contains(env.BRANCH_NAME ?: 'prod') }
        }
      }
      steps {
        timeout(time: 10, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
          echo "Quality Gate FRONTEND OK"
        }
      }
    }
  }

  post {
    failure {
      script {
        try {
          mail to: 'hecheverria@unis.edu.gt',
               subject: "Falló pipeline en rama ${env.BRANCH_NAME}",
               body: "El pipeline falló en la etapa ${env.STAGE_NAME}. Revisar Jenkins."
        } catch (e) { echo "No se pudo enviar correo: ${e}" }
      }
    }
    unstable {
      script {
        try {
          mail to: 'hecheverria@unis.edu.gt',
               subject: "Pipeline UNSTABLE en ${env.BRANCH_NAME}",
               body: "El pipeline quedó UNSTABLE en la etapa ${env.STAGE_NAME}. Revisar Jenkins."
        } catch (e) { echo "No se pudo enviar correo: ${e}" }
      }
    }
  }
}
