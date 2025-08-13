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
        // Hora local (Guatemala)
        def tz  = TimeZone.getTimeZone('America/Guatemala')
        def now = new Date().format("yyyy-MM-dd HH:mm:ss z", tz)

        // Rama -> entorno (main/master se mapean a prod)
        def raw       = env.BRANCH_NAME ?: 'prod'
        def targetEnv = (['main','master'].contains(raw)) ? 'prod' : raw

        // Datos de build
        def jobName  = env.JOB_NAME
        def buildNum = env.BUILD_NUMBER
        def buildURL = (env.RUN_DISPLAY_URL ?: env.BUILD_URL ?: '')  // BlueOcean o clásico
        def duration = (currentBuild.durationString ?: '').replace(' and counting','')

        // Datos Git (no rompas si falla)
        def commit   = sh(script: 'git rev-parse --short HEAD || true', returnStdout: true).trim()
        def author   = sh(script: "git --no-pager show -s --format='%an <%ae>' HEAD || true", returnStdout: true).trim()
        def message  = sh(script: "git --no-pager show -s --format='%s' HEAD || true", returnStdout: true).trim()

        // PR info (si viene de PR)
        def prURL = env.CHANGE_URL ?: '-'
        def prID  = env.CHANGE_ID  ?: '-'

        def subject = "❌ Falló pipeline | ${jobName} #${buildNum} | rama ${raw} (${targetEnv}) | etapa ${env.STAGE_NAME}"
        def body = """\
Fecha/Hora:  ${now}
Resultado:   FAILURE
Job:         ${jobName}
Build:       #${buildNum}
Rama:        ${raw}   (entorno: ${targetEnv})
Etapa:       ${env.STAGE_NAME}
Duración:    ${duration}
Nodo:        ${env.NODE_NAME}

Commit:      ${commit}
Autor:       ${author}
Mensaje:     ${message}

Pull Request: ${prURL} (ID: ${prID})
Consola:     ${buildURL}console
Artefactos:  ${buildURL}artifact/

-- Jenkins auto-notificación
"""
        mail to: 'hecheverria@unis.edu.gt,jflores@unis.edu.gt',
             subject: subject,
             body: body
      } catch (e) {
        echo "No se pudo enviar correo (failure): ${e}"
      }
    }
  }

  unstable {
    script {
      try {
        def tz  = TimeZone.getTimeZone('America/Guatemala')
        def now = new Date().format("yyyy-MM-dd HH:mm:ss z", tz)

        def raw       = env.BRANCH_NAME ?: 'prod'
        def targetEnv = (['main','master'].contains(raw)) ? 'prod' : raw

        def jobName  = env.JOB_NAME
        def buildNum = env.BUILD_NUMBER
        def buildURL = (env.RUN_DISPLAY_URL ?: env.BUILD_URL ?: '')
        def duration = (currentBuild.durationString ?: '').replace(' and counting','')

        def commit   = sh(script: 'git rev-parse --short HEAD || true', returnStdout: true).trim()
        def author   = sh(script: "git --no-pager show -s --format='%an <%ae>' HEAD || true", returnStdout: true).trim()
        def message  = sh(script: "git --no-pager show -s --format='%s' HEAD || true", returnStdout: true).trim()

        def prURL = env.CHANGE_URL ?: '-'
        def prID  = env.CHANGE_ID  ?: '-'

        def subject = "⚠️ Pipeline UNSTABLE | ${jobName} #${buildNum} | rama ${raw} (${targetEnv}) | etapa ${env.STAGE_NAME}"
        def body = """\
Fecha/Hora:  ${now}
Resultado:   UNSTABLE
Job:         ${jobName}
Build:       #${buildNum}
Rama:        ${raw}   (entorno: ${targetEnv})
Etapa:       ${env.STAGE_NAME}
Duración:    ${duration}
Nodo:        ${env.NODE_NAME}

Commit:      ${commit}
Autor:       ${author}
Mensaje:     ${message}

Pull Request: ${prURL} (ID: ${prID})
Consola:     ${buildURL}console
Artefactos:  ${buildURL}artifact/

-- Jenkins auto-notificación
"""
        mail to: 'hecheverria@unis.edu.gt,jflores@unis.edu.gt',
             subject: subject,
             body: body
      } catch (e) {
        echo "No se pudo enviar correo (unstable): ${e}"
      }
    }
  }
}

}
