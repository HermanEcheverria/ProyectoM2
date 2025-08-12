pipeline {
  agent any

  options {
    timestamps()
    ansiColor('xterm')
    disableConcurrentBuilds()
  }

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
                  def targetEnv = (raw in ['main','master']) ? 'prod' : raw
                  def key   = "${PROJECT_NAME}-backend-${targetEnv}"
                  def pname = "${PROJECT_NAME} :: Backend [${targetEnv}]"
                  withEnv(["SONAR_PROJECT_KEY=${key}", "SONAR_PROJECT_NAME=${pname}", "TARGET_ENV=${targetEnv}"]) {
                    sh '''
                      # asegurar tags para versionado en prod
                      git fetch --tags --force || true

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
                    git fetch --tags --force || true

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

    stage('Deploy DEV') {
      when { branch 'dev' }
      steps {
        sh '''
          set -euxo pipefail
          cd "$WORKSPACE/deploy"

          # Detecta compose v2/v1
          if docker compose version >/dev/null 2>&1; then CMD="docker compose"; else CMD="docker-compose"; fi

          docker network inspect m2-dev-net >/dev/null 2>&1 || docker network create m2-dev-net

          # Tira el stack previo y limpia huérfanos (sin borrar volúmenes)
          $CMD -p m2dev -f docker-compose.dev.yml down --remove-orphans || true

          # Construye y recrea contenedores
          $CMD -p m2dev -f docker-compose.dev.yml build --pull
          $CMD -p m2dev -f docker-compose.dev.yml up -d --force-recreate

          $CMD -p m2dev ps
        '''
      }
    }

    stage('Deploy UAT') {
      when { branch 'uat' }
      steps {
        sh '''
          set -euxo pipefail
          cd "$WORKSPACE/deploy"

          if docker compose version >/dev/null 2>&1; then CMD="docker compose"; else CMD="docker-compose"; fi

          docker network inspect m2-uat-net >/dev/null 2>&1 || docker network create m2-uat-net

          $CMD -p m2uat -f docker-compose.uat.yml down --remove-orphans || true
          $CMD -p m2uat -f docker-compose.uat.yml build --pull
          $CMD -p m2uat -f docker-compose.uat.yml up -d --force-recreate

          $CMD -p m2uat ps
        '''
      }
    }

    stage('Deploy PROD') {
      when { anyOf { branch 'prod'; branch 'main'; branch 'master' } }
      steps {
        sh '''
          set -euxo pipefail
          cd "$WORKSPACE/deploy"

          if docker compose version >/dev/null 2>&1; then CMD="docker compose"; else CMD="docker-compose"; fi

          docker network inspect m2-prod-net >/dev/null 2>&1 || docker network create m2-prod-net

          $CMD -p m2prod -f docker-compose.prod.yml down --remove-orphans || true
          $CMD -p m2prod -f docker-compose.prod.yml build --pull
          $CMD -p m2prod -f docker-compose.prod.yml up -d --force-recreate

          $CMD -p m2prod ps
        '''
      }
    }
  }

  post {
    failure {
      script {
        try {
          mail to: 'hecheverria@unis.edu.gt,jflores@unis.edu.gt',
               subject: "Falló pipeline en rama ${env.BRANCH_NAME}",
               body: "El pipeline falló en la etapa ${env.STAGE_NAME}. Revisar Jenkins."
        } catch (e) { echo "No se pudo enviar correo: ${e}" }
      }
    }
    unstable {
      script {
        try {
          mail to: 'hecheverria@unis.edu.gt,jflores@unis.edu.gt',
               subject: "Pipeline UNSTABLE en ${env.BRANCH_NAME}",
               body: "El pipeline quedó UNSTABLE en la etapa ${env.STAGE_NAME}. Revisar Jenkins."
        } catch (e) { echo "No se pudo enviar correo: ${e}" }
      }
    }
  }
}
